package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.servicos.EmailService;
import br.com.villefortconsulting.sgfinancas.util.EmailUtil;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
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
public class ContatoSiteControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmailService emailService;

    private EmailDTO emailDTO;

    private String nome;

    private String email;

    private String assunto;

    private String mensagem;

    public String doFinishEnviaEmailContato() {
        if (!EmailUtil.validarEmail(email, true)) {
            return "contato.xhtml";
        }
        try {
            List<Usuario> usuarioList = new LinkedList<>();

            usuarioList.add(preencheUsuarioEmail());

            emailDTO = new EmailDTO();

            emailDTO.setAssunto(assunto);
            emailDTO.setMensagem(mensagem);
            emailDTO.setRemetente(email);
            emailDTO.setTituloMensagem("FORMULÁRIO ENVIADO EM WWW.SGFINANCAS.COM");
            emailDTO.setDestinatarios(usuarioList);

            emailService.enviarEmailMS(emailDTO, "S");

            cleanCampos();

            createFacesSuccessMessage("E-mail enviado com sucesso.");

        } catch (Exception e) {
            createFacesErrorMessage("Não foi possível enviar o e-mail.");
        }
        return "contato.xhtml";
    }

    public void cleanCampos() {
        assunto = null;
        mensagem = null;
        email = null;
        nome = null;
    }

    public Usuario preencheUsuarioEmail() {

        Usuario usuario = new Usuario();

        usuario.setEmail("comercial@villefortconsulting.com");
        usuario.setRecebeEmailComunicacao("S");

        return usuario;
    }

}
