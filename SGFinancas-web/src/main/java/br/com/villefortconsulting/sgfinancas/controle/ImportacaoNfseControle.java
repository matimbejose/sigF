package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.controle.apoio.ImportacaoNFBase;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ImportacaoMovimentoDTO;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import br.com.villefortconsulting.sgfinancas.nfs.sp.sp.NFSeSPSP;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumFinalidadeNF;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.gov.pbh.schema.nfse.TcCompNfse;
import br.gov.pbh.schema.nfse.TcCpfCnpj;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class ImportacaoNfseControle extends ImportacaoNFBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String getPage() {
        return "nfse.xhtml";
    }

    @Override
    public ImportacaoMovimentoDTO convertNFe(String arquivo) throws JAXBException {
        if (modelo == null) {
            throw new CadastroException("Informe o modelo.", null);
        }
        switch (modelo) {
            case "abrasf":
                return convertNFSeAbrasf(arquivo);
            case "paulistana":
                return convertNFSePaulistana(arquivo);
            default:
                return null;
        }
    }

    public ImportacaoMovimentoDTO convertNFSePaulistana(String arquivo) throws JAXBException {
        NFSeSPSP.NFe nota = XmlUtil.xmlToObject(arquivo, NFSeSPSP.NFe.class);
        if (nota == null || nota.RazaoSocialTomador == null || nota.CPFCNPJTomador == null || nota.ChaveNFe == null || nota.CPFCNPJPrestador == null) {
            return null;
        }
        return ImportacaoMovimentoDTO.builder()
                .nomeDestinatario(nota.RazaoSocialTomador)
                .cpfCnpjDestinatario(CpfCnpjUtil.mascararCpfOuCnpj(CpfCnpjUtil.removerMascaraCnpj(nota.CPFCNPJTomador.CPF)))
                .dataMovimentacao(nota.DataEmissaoNFe)
                .valor(nota.ValorServicos * 1d)
                .valorDesconto(0d)
                .finalidade(EnumFinalidadeNF.NF_NORMAL)
                .origem(nota.StatusNFe.equalsIgnoreCase("C") ? EnumTipoClienteMovimentacao.NOTA_FISCAL_SERVICO_CANCELADA : EnumTipoClienteMovimentacao.NOTA_FISCAL_SERVICO)
                .idIntegracao(nota.ChaveNFe.NumeroNFe)
                .centroCusto(centroCustoService.buscaCentroCustoByCNPJ(CpfCnpjUtil.mascararCpfOuCnpj(nota.CPFCNPJPrestador.CNPJ + "")))
                .build();
    }

    public ImportacaoMovimentoDTO convertNFSeAbrasf(String arquivo) throws JAXBException {
        TcCompNfse nota = XmlUtil.xmlToObject(arquivo, TcCompNfse.class);
        if (nota == null || nota.getNfse() == null || nota.getNfse().getInfNfse() == null) {
            return null;
        }
        TcCpfCnpj aux = nota.getNfse().getInfNfse().getTomadorServico().getIdentificacaoTomador().getCpfCnpj();
        String cpfCnpj = aux.getCnpj() != null ? aux.getCnpj() : aux.getCpf();
        return ImportacaoMovimentoDTO.builder()
                .nomeDestinatario(nota.getNfse().getInfNfse().getTomadorServico().getRazaoSocial())
                .cpfCnpjDestinatario(CpfCnpjUtil.mascararCpfOuCnpj(cpfCnpj))
                .dataMovimentacao(nota.getNfse().getInfNfse().getDataEmissao().toGregorianCalendar().getTime())
                .valor(nota.getNfse().getInfNfse().getServico().getValores().getValorLiquidoNfse().doubleValue())
                .valorDesconto(0d)
                .finalidade(EnumFinalidadeNF.NF_NORMAL)
                .origem(EnumTipoClienteMovimentacao.NOTA_FISCAL_SERVICO)
                .idIntegracao(nota.getNfse().getInfNfse().getNumero().toString())
                .centroCusto(centroCustoService.buscaCentroCustoByCNPJ(CpfCnpjUtil.mascararCpfOuCnpj(nota.getNfse().getInfNfse().getPrestadorServico().getIdentificacaoPrestador().getCnpj())))
                .build();
    }

}
