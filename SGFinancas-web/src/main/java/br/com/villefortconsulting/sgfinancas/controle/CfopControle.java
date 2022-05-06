package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cfop;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CfopFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CfopService;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
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
public class CfopControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CfopService cfopService;

    private Cfop cfopSelecionado;

    private LazyDataModel<Cfop> model;

    private CfopFiltro filtro = new CfopFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, cfopService::quantidadeRegistrosFiltrados, cfopService::getListaFiltrada);
    }

    public List<Cfop> getCfops(NF nf) {
        switch (nf.getEnumModelo()) {
            case DEVOLUCAO:// Devolução a partir de uma PJ
            case ENTRADA_DEVOLUCAO:// Devolução a partir de uma PF
                return cfopService.listarCfopDeDevolucao();
            case TRANSFERENCIA:
                return cfopService.listarCfopDeTransferencia();
            case VENDA:
                return cfopService.listarCfopDeVenda();
            case SERVICO:
                return cfopService.listarCfopDeServico();
            case ENTRADA_DA_COMPRA:
                return cfopService.listarCfopDeCompra();
            case COMPLEMENTAR:
            case OUTRA:
                return cfopService.listar();
            default:
                return new ArrayList<>();
        }
    }

    public List<Cfop> getCfops() {
        return cfopService.listar();
    }

}
