package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.TokenApp;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.PagamentoMensalidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.TokenAppService;
import br.com.villefortconsulting.sgfinancas.servicos.UsuarioService;
import br.com.villefortconsulting.util.DataUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuPerfilControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private PagamentoMensalidadeService pagamentoMensalidadeService;

    @EJB
    private TokenAppService tokenAppService;

    private Usuario usuario;

    private String senhaAtual;

    private String senhaNova;

    private String confimacaoSenha;

    private transient UploadedFile part;

    private TokenApp tokenSelecionado;

    @Override
    public void cleanCache() {
        this.senhaAtual = null;
        this.senhaNova = null;
        this.confimacaoSenha = null;
    }

    public void doStartAlterar() {
        if (usuario == null) {
            usuario = getUsuarioLogado();
        }
    }

    public void salvarFoto(FileUploadEvent event) throws IOException {
        try (InputStream is = event.getFile().getInputstream()) {
            byte[] bytes = new byte[(int) event.getFile().getSize()];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            byte[] fotoConvertida = bytes;
            usuario.setFoto(fotoConvertida);
        } catch (Exception ex) {
            Logger.getLogger(MenuPerfilControle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void salvarFotoPadrao() {
        try {
            if (part != null) {
                usuario = usuarioService.salvarFotoPadrao(usuario, getUsuarioLogado(), part);
            }
        } catch (Exception ex) {
            Logger.getLogger(MenuPerfilControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public void removerFotoPadrao() {
        try {
            usuario = usuarioService.removerFotoPadrao(usuario, getUsuarioLogado());
        } catch (Exception ex) {
            Logger.getLogger(MenuPerfilControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public void alterarSenha() {
        try {
            if (senhaAtual == null) {
                createFacesErrorMessage("Digite a senha atual.");
            }
            if (senhaNova == null) {
                createFacesErrorMessage("Digite a nova senha.");
            }
            if (confimacaoSenha == null) {
                createFacesErrorMessage("Digite a confirmação da senha.");
            }
            usuarioService.alterarSenha(usuario, senhaAtual, senhaNova, confimacaoSenha);
            createFacesSuccessMessage("Senha alterada com sucesso! ");
        } catch (Exception ex) {
            Logger.getLogger(MenuPerfilControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            cleanCache();
        }
    }

    public void salvarDadosUsuario() {
        try {
            usuarioService.alterar(usuario);
            createFacesSuccessMessage("Usuario salvo com sucesso! ");
        } catch (Exception ex) {
            Logger.getLogger(MenuPerfilControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            cleanCache();
        }
    }

    public List<TokenApp> getListaTokenApp() {
        return tokenAppService.listar(getUsuarioLogado());
    }

    public void doStartRevokeToken() {
        try {
            tokenAppService.remover(tokenSelecionado);
            createFacesSuccessMessage("Acesso removido.");
        } catch (Exception ex) {
            Logger.getLogger(MenuPerfilControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível remover o acesso");
        }
    }

    public String getTipoDeConta() {
        Empresa empresa = empresaService.getEmpresa();
        switch (empresa.getTipoConta()) {
            case "G":
                return "Gratuita";
            case "E":
                return "Expirado";
            case "P":
                return "Paga";
            default:
                return "";
        }
    }

    public Integer getTempoDegustacao() {
        return DataUtil.diferencaEntreDias(new Date(), getDataVigencia());
    }

    public Date getDataVigencia() {
        return pagamentoMensalidadeService.getUltimoPagamentoEmpresaLogada().getDataValidade();
    }

    public Date getDataCadastro() {
        return empresaService.getEmpresa().getDataCredenciamento();
    }

    public boolean ehMaster() {
        Usuario user = getUsuarioLogado();
        return user.getIdPerfil().getEhUsuarioMaster();
    }

}
