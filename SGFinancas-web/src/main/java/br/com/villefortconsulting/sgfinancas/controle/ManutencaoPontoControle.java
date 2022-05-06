package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.Ponto;
import br.com.villefortconsulting.sgfinancas.entidades.PontoObservacao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PontoEntradaSaidaDTO;
import br.com.villefortconsulting.sgfinancas.servicos.PontoObservacaoService;
import br.com.villefortconsulting.sgfinancas.servicos.PontoService;
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
import org.apache.commons.lang3.StringUtils;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManutencaoPontoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PontoService pontoService;

    @EJB
    private PontoObservacaoService pontoObservacaoService;

    private List<PontoEntradaSaidaDTO> lista = new LinkedList<>();

    private Ponto ponto;

    private Funcionario funcionario;

    private Date competencia;

    private String observacao = null;

    private PontoObservacao pontoObservacaoSelecionado;

    public String doFinishSalvarManutencaoPonto() {
        try {
            if (pontoObservacaoSelecionado == null) {
                pontoObservacaoSelecionado = new PontoObservacao();
            }

            if (lista != null) {
                pontoService.atualizarRegistroPonto(funcionario, competencia, lista);
            }

            if (competencia != null && funcionario != null && !StringUtils.isEmpty(observacao)) {

                pontoObservacaoSelecionado.setObservacao(observacao);

                pontoObservacaoService.salvar(pontoObservacaoSelecionado, competencia, funcionario);
            }

            createFacesSuccessMessage("Registros do ponto atualizados com sucesso!");
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
        }
        return "listaManutencaoPonto";
    }

    public void buscarRegistros() {
        try {
            lista = pontoService.listarRegistroEntradaSaidaFuncionario(funcionario, competencia);

            pontoObservacaoSelecionado = pontoObservacaoService.pegaObservacao(competencia, funcionario);

            if (pontoObservacaoSelecionado != null) {
                observacao = pontoObservacaoSelecionado.getObservacao();
            } else {
                observacao = null;
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

}
