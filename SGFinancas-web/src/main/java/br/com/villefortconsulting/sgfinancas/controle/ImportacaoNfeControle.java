package br.com.villefortconsulting.sgfinancas.controle;

import br.com.swconsultoria.nfe.schema.cce.TProcEvento;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Dest;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNfeProc;
import br.com.villefortconsulting.sgfinancas.controle.apoio.ImportacaoNFBase;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ImportacaoMovimentoDTO;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumFinalidadeNF;
import br.com.villefortconsulting.sgfinancas.util.nf.cfe.CFe;
import br.com.villefortconsulting.sgfinancas.util.nf.cfe_canc.CFeCanc;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class ImportacaoNfeControle extends ImportacaoNFBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String getPage() {
        return "nfe.xhtml";
    }

    @Override
    public ImportacaoMovimentoDTO convertNFe(String arquivo) throws JAXBException {
        String start = arquivo.substring(0, 100);
        if (start.contains("<nfeProc ")) {// NFe autorizada, NFCe autorizada
            return convertTNfeProc(arquivo);
        } else if (start.contains("<procEventoNFe ")) {// NFCe cancelada
            return convertProcEventoNFe(arquivo);
        } else if (start.contains("<CFe>")) {// CFe autorizada
            return convertCFe(arquivo);
        } else if (start.contains("<CFeCanc>")) {// CFe cancelada
            return convertCFeCanc(arquivo);
        }
        return null;
    }

    private static ImportacaoMovimentoDTO convertCFeCanc(String arquivo) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(CFeCanc.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        CFeCanc nota = (CFeCanc) unmarshaller.unmarshal(new ByteArrayInputStream(arquivo.getBytes(StandardCharsets.UTF_8)));
        if (nota == null || nota.getInfCFe() == null) {
            return null;
        }
        CFeCanc.InfCFe inf = nota.getInfCFe();
        return ImportacaoMovimentoDTO.builder()
                .nomeDestinatario("Cancelamento CFe" + inf.getId().replaceAll("\\D", ""))
                .dataMovimentacao(DataUtil.converterStringParaDate(nota.getInfCFe().getIde().getDEmi(), "yyyyMMdd"))
                .origem(EnumTipoClienteMovimentacao.CUPOM_FISCAL_ELETRONICO_CANCELADO)
                .finalidade(EnumFinalidadeNF.NF_NORMAL)
                .idIntegracao(inf.getId().replaceAll("\\D", ""))
                .build();
    }

    private ImportacaoMovimentoDTO convertCFe(String arquivo) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(CFe.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        CFe nota = (CFe) unmarshaller.unmarshal(new ByteArrayInputStream(arquivo.getBytes(StandardCharsets.UTF_8)));
        if (nota == null || nota.getInfCFe() == null) {
            return null;
        }
        CFe.InfCFe inf = nota.getInfCFe();
        String cpfCnpj = null;
        String nome = null;
        for (String parte : inf.getInfAdic().getInfCpl().split("  +")) {
            if (parte.startsWith("Cliente:")) {
                nome = parte.replaceAll(".*\\d+-", "");
            } else if (parte.startsWith("C.P.F.:")) {
                cpfCnpj = parte.replaceAll(".*: ", "");
            }
        }
        return ImportacaoMovimentoDTO.builder()
                .nomeDestinatario(nome)
                .cpfCnpjDestinatario(CpfCnpjUtil.mascararCpfOuCnpj(CpfCnpjUtil.removerMascaraCnpj(cpfCnpj)))
                .dataMovimentacao(DataUtil.converterStringParaDate(nota.getInfCFe().getIde().getDEmi(), "yyyyMMdd"))
                .valor(Double.parseDouble(inf.getTotal().getIcmsTot().getVProd()))
                .valorDesconto(Double.parseDouble(inf.getTotal().getIcmsTot().getVDesc()))
                .origem(EnumTipoClienteMovimentacao.CUPOM_FISCAL_ELETRONICO)
                .finalidade(EnumFinalidadeNF.NF_NORMAL)
                .idIntegracao(inf.getId().replaceAll("\\D", ""))
                .centroCusto(centroCustoService.buscaCentroCustoByCNPJ(inf.getEmit().getCNPJ()))
                .observacao(inf.getInfAdic().getInfCpl())
                .build();
    }

    private static ImportacaoMovimentoDTO convertProcEventoNFe(String arquivo) throws JAXBException {
        TProcEvento nota = XmlUtil.xmlToObject(arquivo, TProcEvento.class);
        if (nota == null || nota.getEvento() == null || nota.getEvento().getInfEvento() == null || nota.getEvento().getInfEvento().getChNFe() == null) {
            return null;
        }
        String chave = nota.getEvento().getInfEvento().getChNFe();
        return ImportacaoMovimentoDTO.builder()
                .nomeDestinatario("Cancelamento NFe " + chave)
                .dataMovimentacao(DataUtil.retornaDataNFeConvertida(nota.getEvento().getInfEvento().getDhEvento()))
                .origem(EnumTipoClienteMovimentacao.NOTA_FISCAL_PRODUTO_CONSUMIDOR_CANCELADA)
                .finalidade(EnumFinalidadeNF.NF_NORMAL)
                .idIntegracao(chave)
                .build();
    }

    private ImportacaoMovimentoDTO convertTNfeProc(String arquivo) throws JAXBException {
        EnumTipoClienteMovimentacao origem;
        EnumFinalidadeNF finalidade;
        TNfeProc nota = XmlUtil.xmlToObject(arquivo, TNfeProc.class);
        if (nota == null || nota.getNFe() == null || nota.getNFe().getInfNFe() == null) {
            return null;
        }
        String cpfCnpj = null;
        String nome = null;
        String obs = "";
        String referencia = null;
        if (nota.getNFe().getInfNFe().getDest() != null) {
            Dest dest = nota.getNFe().getInfNFe().getDest();
            cpfCnpj = dest.getCNPJ() != null ? dest.getCNPJ() : dest.getCPF();
            nome = dest.getXNome();
            origem = EnumTipoClienteMovimentacao.NOTA_FISCAL_PRODUTO;
        } else {
            for (String inf : nota.getNFe().getInfNFe().getInfAdic().getInfCpl().split("#xD#xA")) {
                if (inf.startsWith("Cliente:")) {
                    nome = inf.split("-")[1];
                } else if (inf.startsWith("C.P.F.: ")) {
                    cpfCnpj = inf.split(" ")[1];
                }
            }
            origem = EnumTipoClienteMovimentacao.NOTA_FISCAL_PRODUTO_CONSUMIDOR;
        }
        finalidade = EnumFinalidadeNF.retornaEnumSelecionado(nota.getNFe().getInfNFe().getIde().getFinNFe());

        boolean naoImportar = nota.getNFe().getInfNFe().getDet().stream()
                .anyMatch(produto -> produto.getProd().getCFOP().equals("5929"));

        if (nota.getNFe().getInfNFe().getInfAdic() != null && nota.getNFe().getInfNFe().getInfAdic().getInfCpl() != null) {
            obs = nota.getNFe().getInfNFe().getInfAdic().getInfCpl();
        }
        if (nota.getNFe().getInfNFe().getIde().getNFref() != null && !nota.getNFe().getInfNFe().getIde().getNFref().isEmpty()) {
            referencia = nota.getNFe().getInfNFe().getIde().getNFref().get(0).getRefNFe();
        }
        return ImportacaoMovimentoDTO.builder()
                .nomeDestinatario(nome)
                .cpfCnpjDestinatario(CpfCnpjUtil.mascararCpfOuCnpj(CpfCnpjUtil.removerMascaraCnpj(cpfCnpj)))
                .dataMovimentacao(DataUtil.retornaDataNFeConvertida(nota.getNFe().getInfNFe().getIde().getDhEmi()))
                .valor(Double.parseDouble(nota.getNFe().getInfNFe().getTotal().getICMSTot().getVProd()))
                .valorDesconto(Double.parseDouble(nota.getNFe().getInfNFe().getTotal().getICMSTot().getVDesc()))
                .origem(origem)
                .finalidade(finalidade)
                .importar(!naoImportar)
                .notaReferencia(referencia)
                .idIntegracao(nota.getNFe().getInfNFe().getId().replaceAll("\\D", ""))
                .centroCusto(centroCustoService.buscaCentroCustoByCNPJ(nota.getNFe().getInfNFe().getEmit().getCNPJ()))
                .observacao(obs)
                .build();
    }

}
