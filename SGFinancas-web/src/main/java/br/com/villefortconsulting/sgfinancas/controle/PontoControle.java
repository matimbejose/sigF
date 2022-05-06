package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PontoEntradaSaidaDTO;
import br.com.villefortconsulting.sgfinancas.servicos.FuncionarioService;
import br.com.villefortconsulting.sgfinancas.servicos.PontoService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContratacao;
import br.com.villefortconsulting.util.StringUtil;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PontoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PontoService pontoService;

    @EJB
    private FuncionarioService funcionarioService;

    private Funcionario funcionarioSelecionado;

    private transient Part partFoto;

    private transient Part part;

    private String matricula;

    private String senha;

    public String buscarSituacao() {
        if (funcionarioSelecionado != null && funcionarioSelecionado.getTipoContratacao() != null) {
            return EnumTipoContratacao.getDescricao(funcionarioSelecionado.getTipoContratacao());
        }
        return null;
    }

    public List<PontoEntradaSaidaDTO> getListaEntradaSaida() {
        return pontoService.buscarRegistroEntradaSaidaFuncionario(funcionarioSelecionado);
    }

    public void cleanFuncionario() {
        senha = "";
        matricula = "";
        funcionarioSelecionado = null;
    }

    public String doFinishAdd() {
        try {
            pontoService.salvar(funcionarioSelecionado);
            senha = "";
            matricula = "";
            funcionarioSelecionado = null;

            createFacesSuccessMessage("Ponto registrado com sucesso!");

        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
        }

        return "ponto.xhtml";
    }

    public boolean podeBaterPonto() {
        return pontoService.podeBaterPonto(funcionarioSelecionado);
    }

    public String buscarFuncionario() {
        // Verificar matricula funcionário
        if (StringUtils.isEmpty(matricula)) {
            return "ponto.xhtml";
        }

        // Buscar funcionário
        funcionarioSelecionado = funcionarioService.buscarMatricula(matricula);
        if (funcionarioSelecionado == null) {
            createFacesErrorMessage("Funcionário não encontrado para matrícula informada");
            return "ponto.xhtml";
        }

        if (funcionarioSelecionado.getSenha() == null) {
            cleanFuncionario();
            createFacesErrorMessage("Funcionário ainda não possui senha.");
            return "ponto.xhtml";
        }

        if (!funcionarioSelecionado.getSenha().equals(StringUtil.criptografarMD5(senha))) {
            cleanFuncionario();
            createFacesErrorMessage("Senha incorreta.");
            return "ponto.xhtml";
        }

        if (!podeBaterPonto()) {
            createFacesErrorMessage("Prezado colaborador, faltam " + pontoService.tempoRestanteRetornoAlmoco(funcionarioSelecionado) + " minutos para você poder registrar o retorno do almoço.");
        }

        return "ponto.xhtml";
    }

    public StreamedContent getFoto() {
        if (FacesContext.getCurrentInstance().getRenderResponse()) {
            return new DefaultStreamedContent();
        } else if (funcionarioSelecionado != null && funcionarioSelecionado.getFoto() != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(funcionarioSelecionado.getFoto()), "image/jpeg");
        }
        return null;
    }

}
