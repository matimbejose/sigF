package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.PontoObservacao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PontoEntradaSaidaDTO;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.PontoService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.util.DataUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class RegistroPontoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private PontoService pontoService;

    @EJB
    private RelatorioService relatorioService;

    private List<PontoEntradaSaidaDTO> lista = new LinkedList<>();

    private PontoObservacao pontoObservacao;

    private Funcionario funcionario;

    private Date competencia;

    public Integer getQuantidadeDias() {
        if (competencia != null) {
            return DataUtil.getQuantidadeDiasNoMes(competencia);
        }
        return 0;
    }

    public void doGeraRelatorioPonto() {
        try {
            gerarPdf(relatorioService.gerarRelatorioPonto(funcionario, empresaService.getEmpresa(), competencia), "relatórioPonto");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Erro ao gerar relatório de ponto", e);
            createFacesErrorMessage(e.getMessage());
        }
    }

    public void buscarRegistros() {
        try {
            lista = pontoService.listarRegistroEntradaSaidaFuncionario(funcionario, competencia);
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
        }
    }

}
