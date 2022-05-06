package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.TransferenciaContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TransferenciaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TransferenciaContaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.TransferenciaService;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private TransferenciaService transferenciaService;

    @EJB
    private ContaBancariaService contaBancariaService;

    private List<ContaBancaria> listContaOrigem;

    private List<ContaBancaria> listContaDestino;

    private ContaBancaria idContaBancariaOrigem;

    private ContaBancaria idContaBancariaDestino;

    private TransferenciaDTO transferenciaSelecionada;

    private TransferenciaContaBancaria objetoSelecionado;

    private LazyDataModel<TransferenciaContaBancaria> model;

    private TransferenciaContaFiltro filtro = new TransferenciaContaFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, transferenciaService::quantidadeRegistrosFiltrados, transferenciaService::getListaFiltrada);
    }

    public String doStartAdd() {
        transferenciaSelecionada = new TransferenciaDTO();
        idContaBancariaDestino = null;
        idContaBancariaOrigem = null;

        listContaOrigem = contaBancariaService.listarAtivas();

        return "cadastroTransferencia.xhtml";
    }

    public String doFinishAdd() {
        try {
            if (idContaBancariaOrigem != null && idContaBancariaDestino != null) {
                transferenciaSelecionada.setIdContaDestino(idContaBancariaDestino);
                transferenciaSelecionada.setIdContaOrigem(idContaBancariaOrigem);

                transferenciaService.transfereConta(transferenciaSelecionada);
                createFacesSuccessMessage("Transferência realizada com sucesso!");
            }

            return "listaTransferenciaConta.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroTransferencia.xhtml";
        }
    }

    public void cancelarTransferencia() {
        if (transferenciaService.cancelarTransferencia(objetoSelecionado)) {
            createFacesSuccessMessage("Transferência cancelada com sucesso");
        } else {
            createFacesErrorMessage("Não foi possível cancelar a transferência, entre em contato com o suporte.");
        }
    }

    public List<ContaBancaria> getListOrigemPreenchida() {
        listContaOrigem = contaBancariaService.listarAtivas();

        if (idContaBancariaDestino != null) {
            listContaOrigem.remove(idContaBancariaOrigem);
        }

        if (idContaBancariaOrigem != null && !idContaBancariaOrigem.equals(idContaBancariaDestino)) {
            listContaDestino.add(idContaBancariaOrigem);
        }

        return listContaOrigem;
    }

    public List<ContaBancaria> getListDestinoPreenchida() {
        listContaDestino = contaBancariaService.listarAtivas();

        if (idContaBancariaOrigem != null) {
            listContaDestino.remove(idContaBancariaOrigem);
        }

        if (idContaBancariaDestino != null && !idContaBancariaDestino.equals(idContaBancariaOrigem)) {
            listContaOrigem.add(idContaBancariaDestino);
        }

        return listContaDestino;
    }

    public String getDataHoje() {
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
        return out.format(DataUtil.getHoje());
    }

}
