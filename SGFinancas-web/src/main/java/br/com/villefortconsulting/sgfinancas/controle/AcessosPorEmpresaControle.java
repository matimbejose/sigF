package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.dto.AcessoPorEmpresaDTO;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.operator.In;
import br.com.villefortconsulting.util.operator.MinMax;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcessosPorEmpresaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private RelatorioService relatorioService;

    private Boolean showSuporte;

    private Boolean showContabilidade;

    private In<String> tipoPagamento;

    private MinMax<Date> periodo;

    private List<AcessoPorEmpresaDTO> listaAcesso;

    @PostConstruct
    public void postContruct() {
        showSuporte = false;
        showContabilidade = false;
        tipoPagamento = new In<>(new ArrayList<>());
        periodo = new MinMax<>();
        periodo.setMin(DataUtil.getPrimeiroDiaDoAnoCorrente());
        periodo.setMax(DataUtil.getHoje());
    }

    public List<AcessoPorEmpresaDTO> getListaAcesso() {
        if (showSuporte == null || showContabilidade == null || tipoPagamento == null || periodo == null) {
            postContruct();
        }
        if (listaAcesso == null) {
            listaAcesso = empresaService.listaAcessosPorEmpresa(showSuporte, showContabilidade, tipoPagamento, periodo);
        }
        return listaAcesso;
    }

    public void refresh() {
        listaAcesso = null;
    }

    public void baixarExcel() {
        try {
            gerarExcel(relatorioService.acessosPorEmpresa(showSuporte, showContabilidade, false, periodo), "Acessos por empresa");
        } catch (IOException ex) {
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public Long getTotalEmpresas() {
        return listaAcesso.stream()
                .map(AcessoPorEmpresaDTO::getEmpresa)
                .distinct()
                .count();
    }

    public Long getTotalUsuarios(String empresa) {
        return listaAcesso.stream()
                .filter(dto -> empresa.isEmpty() || dto.getEmpresa().equals(empresa))
                .map(AcessoPorEmpresaDTO::getUsuario)
                .distinct()
                .count();
    }

    public Double getTotalValor(String empresa) {
        return listaAcesso.stream()
                .filter(dto -> empresa.isEmpty() || dto.getEmpresa().equals(empresa))
                .mapToDouble(AcessoPorEmpresaDTO::getValor)
                .sum();
    }

    public Long getTotalAcessos(String empresa) {
        return listaAcesso.stream()
                .filter(dto -> empresa.isEmpty() || dto.getEmpresa().equals(empresa))
                .mapToLong(AcessoPorEmpresaDTO::getQuantidade)
                .sum();
    }

}
