package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.controle.apoio.ContratoControleBase;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContrato;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class ContratoClienteControle extends ContratoControleBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct();
    }

    public String doStartAddContratoAluno() {
        contratoSelecionado = new Contrato();
        contratoSelecionado.setIdCliente(solicitanteTurmaSelecionado.getIdSolicitante().getIdCliente());
        contratoSelecionado.setIdTurma(solicitanteTurmaSelecionado.getIdTurma());
        contratoSelecionado.setIdCentroCusto(solicitanteTurmaSelecionado.getIdTurma().getIdCentroCusto());
        contratoSelecionado.setDataInicio(solicitanteTurmaSelecionado.getIdTurma().getDataInicial());
        contratoSelecionado.setDataFim(solicitanteTurmaSelecionado.getIdTurma().getDataFinal());

        return "/restrito/contratoEntrada/cadastroContratoCliente.xhtml";
    }

    public String doStartAlterarContratoAluno() {
        contratoSelecionado.setListParcelaDTO(contaService.obterParcelaDTO(contratoSelecionado.getIdConta()));
        return "/restrito/contratoEntrada/cadastroContratoCliente.xhtml";
    }

    public List<ContaParcela> getListaParcelasPagas() {
        return contaService.listarContaParcela(contratoSelecionado.getIdConta()).stream()
                .filter(parcela -> parcela.getDataPagamento() != null)
                .collect(Collectors.toList());
    }

    public String doFinishExcluirContratoEParcelas() {
        try {
            contratoService.remover(contratoSelecionado);
            createFacesSuccessMessage("Contrato excluído com sucesso!");
            return "listaContratoCliente.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "listaContratoCliente.xhtml";
        }
    }

    public void calcularDescontoTotal() {
        Double descontoTotal = contratoService.calcularDescontoTotal(contratoSelecionado.getListParcelaDTO());
        contratoSelecionado.setDesconto(descontoTotal);
    }

    public Integer quantidadeMinimaDeParcelas() {
        if (!contratoSelecionado.getListParcelaDTO().isEmpty()) {
            return contratoSelecionado.obterNumeroUltimaParcelaPaga();
        }
        return 1;
    }

    public String buscarSituacao(String situacao) {
        return EnumSituacaoConta.getDescricao(situacao);
    }

    public Double totalizaValorPago() {
        return contratoSelecionado.getListParcelaDTO().stream()
                .mapToDouble(parcela -> parcela.getValorPago() == null ? 0d : parcela.getValorPago())
                .sum();
    }

    public List<NFS> buscarListNFS(Contrato contrato) {
        return nfsService.buscarNFSPorContrato(contrato);
    }

    public StreamedContent baixarXML(NFS nfs) {
        return nfsService.downloadXML(nfs);
    }

    public void atualizaStatus() {
        Double valorpago = contratoSelecionado.getListParcelaDTO().stream()
                .mapToDouble(ParcelaDTO::getValorPago)
                .sum();
        Double valorTotal = contratoSelecionado.getListParcelaDTO().stream()
                .mapToDouble(ParcelaDTO::getValor)
                .sum();
        if (valorpago.compareTo(valorTotal) == 0) {
            contratoSelecionado.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
        } else if (valorpago > 0) {
            contratoSelecionado.setSituacao(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
        } else {
            contratoSelecionado.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        }
    }

    @Override
    public EnumTipoContrato getTipoContrato() {
        return EnumTipoContrato.CLIENTE;
    }

}
