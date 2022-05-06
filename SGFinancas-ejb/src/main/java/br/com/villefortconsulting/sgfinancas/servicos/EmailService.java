package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.entidades.StatusEnvio;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.DestinatarioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MSEmailDTO;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.util.EmailUtil;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusEnvio;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Dest;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.MicroServiceUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Stateless
@LocalBean
public class EmailService extends BasicService<EntityId, BasicRepository<EntityId>, BasicFilter<EntityId>> {

    private static final long serialVersionUID = 1L;

    private static String getSmtpHostName() {
        return SystemPreferencesUtil.getProp("sec.email.host");
    }

    private static String getSmtpAuthUser() {
        return SystemPreferencesUtil.getProp("sec.email.user");
    }

    private static String getSmtpAuthPwd() {
        return SystemPreferencesUtil.getProp("sec.email.pass");
    }

    private static int getPort() {
        return Integer.parseInt(SystemPreferencesUtil.getProp("sec.email.port"));
    }

    public static final String ENVIO_OBRIGATORIO_SIM = "S";

    private static final boolean SSL = false;

    private static final boolean TLS = true;

    /**
     * Envia um email pelo microserviço
     * <br/>
     * Para adicionar uma imagem inline, ao invés de utilizar <code>htmlEmail.embed(arquivo)</code>, utilizar
     * <code>emailDTO.attachFile(arquivo, htmlMail)</code>
     *
     * @param emailDTO
     * @param envioObrigatorio
     * @param empresa
     * @throws EmailException
     */
    public void enviarEmailMS(EmailDTO emailDTO, String envioObrigatorio) throws EmailException {
        HtmlEmail htmlMail = new HtmlEmail();
        enviarEmailMS(emailDTO, envioObrigatorio, getTemplate(htmlMail, emailDTO.getTituloMensagem(), emailDTO.getMensagem()));
    }

    public void enviarEmailMS(EmailDTO emailDTO, String envioObrigatorio, String mensagem) throws EmailException {
        MSEmailDTO email;
        try {
            email = new MSEmailDTO();
            email.setAssunto(emailDTO.getAssunto());
            email.setAuthUsername(getSmtpAuthUser());
            email.setCc(emailDTO.getDestinatarios().stream()
                    .map(usuario -> new DestinatarioDTO(usuario.getNome(), usuario.getEmail()))
                    .collect(Collectors.toList()));
            email.setCco(emailDTO.getDestinatariosOcultos().stream()
                    .map(usuario -> new DestinatarioDTO(usuario.getNome(), usuario.getEmail()))
                    .collect(Collectors.toList()));
            email.setForcarEnvio("S".equals(envioObrigatorio));
            email.setMensagem(mensagem);
            email.setRemetente("nao.responda@villefortconsulting.com");
        } catch (Exception ex) {
            throw new EmailException(ex.getMessage(), ex);
        }
        ResponseEntity<String> response;
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            emailDTO.getAnexos().forEach(value -> body.add("anexos", new FileSystemResource(value)));
            emailDTO.getEmbededFiles().forEach((nome, anexo) -> body.add("anexosInline", new FileSystemResource(anexo)));
            body.add("email", email);

            response = MicroServiceUtil.doFormDataPost(MicroServiceUtil.MicroServicos.EMAIL, "email/sendWithAttachment/", body, new HashMap<>());
        } catch (Exception ex) {
            throw new EmailException("Não foi possível enviar o e-mail.", ex);
        }
        try {
            StatusEnvio status = new ObjectMapper().readValue(response.getBody(), StatusEnvio.class);
            if (status.getStatus() == EnumStatusEnvio.NAO_ENVIADO) {
                throw new EmailException("Não foi possível enviar o email.", null);
            }
            if (status.getError() != null) {
                throw new EmailException("Não foi possível enviar o email, motivo: " + status.getMessage(), null);
            }
        } catch (IOException ex) {
            throw new EmailException("Não foi possível obter o status de envio do e-mail.", ex);
        }
    }

    public void enviarEmailNotaFiscalServico(EmailDTO emailDTO, NFS nfs, Empresa empresa, String envioObrigatorio) throws EmailException {
        try {
            HtmlEmail htmlMail = buscarParamentrosHtmlMail();

            htmlMail.setHtmlMsg(getTemplateNotaFiscalServico(htmlMail, emailDTO, nfs, empresa));
            enviarEmail(htmlMail, emailDTO, envioObrigatorio);
        } catch (Exception ex) {
            throw new EmailException(ex.getMessage(), ex);
        }
    }

    public void enviarEmailNotaFiscal(EmailDTO emailDTO, NF nf, Empresa empresa, List<VendaProduto> produtos, String envioObrigatorio) throws EmailException {
        try {
            HtmlEmail htmlMail = buscarParamentrosHtmlMail();

            htmlMail.setHtmlMsg(getTemplateNotaFiscal(htmlMail, emailDTO, nf, empresa, produtos));
            enviarEmail(htmlMail, emailDTO, envioObrigatorio);
        } catch (Exception ex) {
            throw new EmailException(ex.getMessage(), ex);
        }
    }

    private static HtmlEmail buscarParamentrosHtmlMail() {
        HtmlEmail htmlMail = new HtmlEmail();
        htmlMail.setHostName(getSmtpHostName());
        htmlMail.setAuthentication(getSmtpAuthUser(), getSmtpAuthPwd());
        return htmlMail;
    }

    public String getImagem(HtmlEmail htmlEmail, String fileName) throws IOException, org.apache.commons.mail.EmailException {
        String[] ext = fileName.split("\\.");
        InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName);
        File file = FileUtil.convertInputStreamToFile(resourceAsStream, ext[0], ext[1]);
        return htmlEmail.embed(file);
    }

    public String getTemplate(HtmlEmail htmlEmail, String titulo, String texto) {
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("<html>")
                .append("<body style=\"padding: 0px; margin: 0px;\">")
                .append("<div id=\"mailsub\" class=\"notification\" align=\"center\">")
                .append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" ><tr><td align=\"center\" bgcolor=\"#eff3f8\">")
                .append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" >")
                .append("<tr>")
                .append("<td></td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td align=\"center\" bgcolor=\"#ffffff\">")
                .append("<table width=\"90%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">")
                .append("<tr>")
                .append("<td>")
                .append("<img src=\"https://sgfinancas.com/resources/images/logo_SG_Financas_menor_invertido.png\" style=\"width:200px\" />")
                .append("</td>")
                .append("<td align=\"left\">")
                .append("<div class=\"mob_center_bl\" style=\"float: left; display: inline-block;\">")
                .append("<table class=\"mob_center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\" style=\"border-collapse: collapse;\">")
                .append("<tr>")
                .append("<td align=\"left\" valign=\"middle\"><div style=\"height: 20px; line-height: 20px; font-size: 10px;\">&nbsp;</div>")
                .append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >")
                .append("<tr>")
                .append("<td align=\"left\" valign=\"top\" class=\"mob_center\"></td>")
                .append("</tr>")
                .append("</table>")
                .append("</td>")
                .append("</tr>")
                .append("</table>")
                .append("</div>")
                .append("<div style=\"height: 50px; line-height: 50px; font-size: 10px;\">&nbsp;</div>")
                .append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td align=\"center\" bgcolor=\"#fbfcfd\">")
                .append("<table width=\"90%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">")
                .append("<tr>")
                .append("<td>")
                .append("<div style=\"height: 60px; font-size: 10px;\">&nbsp;</div>")
                .append("<div>")
                .append("<font face=\"Arial, Helvetica, sans-serif\" size=\"5\" color=\"#57697e\" style=\"font-size: 15px;\">")
                .append("<span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 15px; align=right color: #57697e; \">")
                .append("<b>")
                .append(titulo)
                .append("</b>")
                .append("</span>")
                .append("</font>")
                .append("</div>")
                .append("<br />")
                .append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td>")
                .append("<div>")
                .append("<font face=\"Arial, Helvetica, sans-serif\" size=\"4\" color=\"#57697e\" style=\"font-size: 15px;\">")
                .append("<span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 13px; color: #57697e;\">")
                .append(texto)
                .append("</span>")
                .append("</font>")
                .append("</div>")
                .append("<div style=\"height: 60px; font-size: 10px;\">&nbsp;</div>										")
                .append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td align=\"center\"><div style=\"line-height: 24px;\"></div></td>")
                .append("</tr>")
                .append("</table>")
                .append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td class=\"iage_footer\" align=\"center\" bgcolor=\"#ffffff\">")
                .append("<div style=\"height: 80px; line-height: 80px; font-size: 10px;\">&nbsp;</div>")
                .append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">")
                .append("<tr>")
                .append("<td align=\"right\">")
                .append("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"#96a5b5\" style=\"font-size: 13px;\">")
                .append("<span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 13px; color: #96a5b5;\">")
                .append("© 2015-2017 Villefortconsulting.com | Todos os direitos reservados.")
                .append("</span>")
                .append("</font>")
                .append("</td>")
                .append("</tr>")
                .append("</table>")
                .append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td></td>")
                .append("</tr>")
                .append("</table>")
                .append("</td>")
                .append("</tr>")
                .append("</table>")
                .append("</div>")
                .append("</body>")
                .append("</html>");

        return mensagem.toString();
    }

    public String getTemplateNotaFiscalServico(HtmlEmail htmlEmail, EmailDTO emailDTO, NFS nfs, Empresa empresa) throws IOException, org.apache.commons.mail.EmailException {
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("<table >")
                .append(" <td style='font-size:12px;' align='left' valign='top'>")
                .append("<div style='margin-top: -150px'; width='37%;' font-size: '11px !important;' font-family: 'Arial, Helvetica, sans-serif;'>");
        File fileLogo = FileUtil.convertByteToFile(empresa.getLogo(), "logo.jpg");
        mensagem.append("<img src=\"cid:").append(htmlEmail.embed(fileLogo)).append("\" style=\"width:37%;\" />")
                .append("</div>")
                .append("</td >")
                .append("</table >")
                .append(" <table width='100%'>")
                .append(" <td style='font-size: 11px;' align='justify'>")
                .append(" <strong>CLIENTE:</strong> <br/><br/>")
                .append(" <strong>Nome:</strong> ").append(nfs.getNomeCliente() == null ? " " : nfs.getNomeCliente()).append(" <br/>")
                .append(" <strong>CPF/CNPJ:</strong> ").append(nfs.getCpfCnpjCliente() == null ? " " : nfs.getCpfCnpjCliente()).append(" <br/>")
                .append(" <strong>Endereço:</strong> ").append(nfs.getEnderecoCliente() == null ? " " : nfs.getEnderecoCliente()).append(", ").append(nfs.getNumeroCliente() == null ? " " : nfs.getNumeroCliente()).append(" <br/>")
                .append(" <strong>Bairro:</strong> ").append(nfs.getBairroCliente() == null ? " " : nfs.getBairroCliente()).append(" <br/>")
                .append(" <strong>Cidade:</strong> ").append(nfs.getCidadeCliente() == null ? " " : nfs.getCidadeCliente()).append(" <br/>")
                .append("</td>")
                .append(" <td style='font-size: 11px;' align='justify'>")
                .append(" <strong>DADOS DA NOTA:</strong> <br/><br/>")
                .append(" <strong>Número cliente:</strong> ").append(nfs.getNumeroCliente() == null ? " " : nfs.getNumeroCliente()).append(" <br/>")
                .append(" <strong>Situação:</strong> ").append(nfs.getSituacao() == null ? " " : EnumSituacaoNF.getDescricao(nfs.getSituacao())).append(" <br/>")
                .append(" <strong>Emissão:</strong> ").append(nfs.getDataEmissao() == null ? " " : new SimpleDateFormat("dd/MM/yyyy HH:mm").format(nfs.getDataEmissao())).append(" <br/>")
                .append(" <strong>Competência:</strong> ").append(nfs.getCompetencia() == null ? " " : new SimpleDateFormat("dd/MM").format(nfs.getCompetencia())).append(" <br/>")
                .append(" <strong>Protocolo:</strong> ").append(nfs.getProtocolo() == null ? " " : nfs.getProtocolo()).append(" <br/>")
                .append("</td>")
                .append(" <td style='font-size:11px;' align='justify'>")
                .append("<strong>IMPOSTOS NFS: </strong> <br/><br/>")
                .append(" <strong>Valor dos serviços:</strong> ").append(nfs.getValorTotal() == null ? " " : NumeroUtil.formatarCasasDecimais(nfs.getValorTotal(), 2, false)).append(" <br/>")
                .append(" <strong>Valor do ISS retido:</strong> ").append(nfs.getValorIssRetido() == null ? " " : NumeroUtil.formatarCasasDecimais(nfs.getValorIssRetido(), 2, false)).append(" <br/>")
                .append(" <strong>Valor do PIS:</strong> ").append(nfs.getValorPis() == null ? " " : NumeroUtil.formatarCasasDecimais(nfs.getValorPis(), 2, false)).append(" <br/>")
                .append(" <strong>Valor do INSS:</strong> ").append(nfs.getValorInss() == null ? " " : NumeroUtil.formatarCasasDecimais(nfs.getValorInss(), 2, false)).append(" <br/>")
                .append(" <strong>Valor do COFINS:</strong> ").append(nfs.getValorCofins() == null ? " " : NumeroUtil.formatarCasasDecimais(nfs.getValorCofins(), 2, false)).append(" <br/>")
                .append("</td>")
                .append("</table>")
                .append("<br/><br/>")
                .append(" <table style=\"width:100%\">")
                .append(" <tr>")
                .append(" <th align=\"left\" style=\"background:#f4f4f6;border:2px solid white;color:#333333;font-family:'trebuchet ms',sans-serif;font-size:14px;padding:10px 15px;text-align:left\" bgcolor=\"#f4f4f6\">Descrição</th>")
                .append(" </tr>")
                .append(" <tr>")
                .append(" <td valign=\"middle\" class=\"m_3739803928145709693hide-mobile\" style=\"background:#f4f4f6;border-style:solid;border-collapse:collapse;border-width:2px;padding:10px 15px;border-color:#f4f4f6 white white;color:#333333;font-family:'trebuchet ms',sans-serif;font-size:14px;vertical-align:middle\">").append(nfs.getDescricaoServico() == null ? " " : nfs.getDescricaoServico()).append(" </td>")
                .append(" </tr>")
                .append("</table>")
                .append("<br/><br/>")
                .append("<table>")
                .append(" <td style='font-size:13px;' align='left'>")
                .append("<div style=\"font-size: 13px !important; font-family: Arial, Helvetica, sans-serif; color: #596167;\">")
                .append(nfs.getIdCliente().getRazaoSocial()).append(" <br/>")
                .append(nfs.getIdCliente().getNome()).append(" <br/>")
                .append(nfs.getIdCliente().getEndereco().getEndereco()).append(" - ").append(nfs.getIdCliente().getEndereco().getNumero()).append(", ").append(nfs.getIdCliente().getEndereco().getBairro()).append(" <br/>")
                .append(nfs.getIdCidadeCliente().getDescricao()).append(" / ").append(nfs.getIdCidadeCliente().getIdUF().getDescricao()).append(" <br/>")
                .append("Telefone: ").append(nfs.getTelefoneCliente()).append(" <br/>")
                .append("Email: ").append(nfs.getEmailCliente())
                .append("</div>")
                .append("</td>")
                .append("</table >");

        return getTemplate(htmlEmail, emailDTO.getTituloMensagem(), mensagem.toString());
    }

    public String getTemplateNotaFiscal(HtmlEmail htmlEmail, EmailDTO emailDTO, NF nf, Empresa empresa, List<VendaProduto> produtos) throws IOException, org.apache.commons.mail.EmailException {
        StringBuilder mensagem = new StringBuilder();
        Dest dest = nf.getObjTNFe().getInfNFe().getDest();

        mensagem.append("<table >")
                .append(" <td style='font-size:12px;' align='left' valign='top'>")
                .append("<div style='margin-top: -150px'; width='37%;' font-size: '11px !important;' font-family: 'Arial, Helvetica, sans-serif;'>");
        if (empresa.getLogo() != null) {
            File fileLogo = FileUtil.convertByteToFile(empresa.getLogo(), "logo.jpg");
            mensagem.append("<img src=\"cid:").append(htmlEmail.embed(fileLogo)).append("\" style=\"width:37%;\" />");
        }
        mensagem.append("</div>")
                .append("</td >")
                .append("</table >")
                .append(" <table width='100%'>")
                .append(" <td style='font-size: 12px;' align='justify'>")
                .append(" <strong>CLIENTE:</strong> <br/><br/>")
                .append(" <strong>Nome:</strong> ").append(dest.getXNome() == null ? " " : dest.getXNome()).append(" <br/>")
                .append(" <strong>CPF/CNPJ:</strong> ").append(dest.getCpfCnpj() == null ? " " : dest.getCpfCnpj()).append(" <br/>")
                .append(" <strong>Endereço:</strong> ").append(dest.getEnderDest().getXLgr() == null ? " " : dest.getEnderDest().getXLgr()).append(", ")
                .append(dest.getEnderDest().getNro() == null ? " " : dest.getEnderDest().getNro()).append(" <br/>")
                .append(" <strong>Bairro:</strong> ").append(dest.getEnderDest().getXBairro() == null ? " " : dest.getEnderDest().getXBairro()).append(" <br/>")
                .append(" <strong>Cidade:</strong> ").append(dest.getEnderDest().getXMun() == null ? " " : dest.getEnderDest().getXMun()).append(" <br/>")
                .append("</td>")
                .append(" <td style='font-size: 12px;' align='justify'>")
                .append(" <strong>DADOS DA NOTA:</strong> <br/><br/>")
                .append(" <strong>Número:</strong> ").append(nf.getNumeroNotaFiscal() == null ? " " : nf.getNumeroNotaFiscal().toString()).append(" <br/>")
                .append(" <strong>Protocolo:</strong> ").append(nf.getProtocolo() == null ? " " : nf.getProtocolo()).append(" <br/>")
                .append(" <strong>Emissão:</strong> ").append(nf.getDataEmissao() == null ? " " : nf.getDataEmissao()).append(" <br/>")
                .append(" <strong>Autorização:</strong> ").append(nf.getDataRetornoProcessamento() == null ? " " : nf.getDataRetornoProcessamento()).append(" <br/>")
                .append(" <strong>Inscrição Estadual:</strong> ").append(nf.getInscricaoEstadual() == null ? " " : nf.getInscricaoEstadual()).append(" <br/>")
                .append("</td>")
                .append(" <td style='font-size:12px;' align='justify'>")
                .append("<strong>IMPOSTOS NF: </strong> <br/><br/>")
                .append(" <strong>Valor do COFINS:</strong> ").append(nf.getValorConfins() == null ? " " : NumeroUtil.formatarCasasDecimais(nf.getValorConfins(), 2, false)).append(" <br/>")
                .append(" <strong>Valor do ISS:</strong> ").append(nf.getValorIss() == null ? " " : NumeroUtil.formatarCasasDecimais(nf.getValorIss(), 2, false)).append(" <br/>")
                .append(" <strong>Valor do PIS:</strong> ").append(nf.getValorPis() == null ? " " : NumeroUtil.formatarCasasDecimais(nf.getValorPis(), 2, false)).append(" <br/>")
                .append(" <strong>Valor do Frete:</strong> ").append(nf.getValorFrete() == null ? " " : NumeroUtil.formatarCasasDecimais(nf.getValorFrete(), 2, false)).append(" <br/>")
                .append(" <strong>Valor do Seguro:</strong> ").append(nf.getValorSeguro() == null ? " " : NumeroUtil.formatarCasasDecimais(nf.getValorSeguro(), 2, false)).append(" <br/>")
                .append("</td>")
                .append("</table>")
                .append("<br/><br/>")
                .append(" <table style=\"width:100%\">")
                .append(" <tr>")
                .append(" <th align=\"left\" style=\"background:#f4f4f6;border:2px solid white;color:#333333;font-family:'trebuchet ms',sans-serif;font-size:14px;padding:10px 15px;text-align:left\" bgcolor=\"#f4f4f6\">Descrição</th>")
                .append(" <th align=\"left\" style=\"background:#f4f4f6;border:2px solid white;color:#333333;font-family:'trebuchet ms',sans-serif;font-size:14px;padding:10px 15px;text-align:left\" bgcolor=\"#f4f4f6\">Quantidade</th>")
                .append(" <th align=\"left\" style=\"background:#f4f4f6;border:2px solid white;color:#333333;font-family:'trebuchet ms',sans-serif;font-size:14px;padding:10px 15px;text-align:left\" bgcolor=\"#f4f4f6\">Valor venda</th>")
                .append(" <th align=\"left\" style=\"background:#f4f4f6;border:2px solid white;color:#333333;font-family:'trebuchet ms',sans-serif;font-size:14px;padding:10px 15px;text-align:left\" bgcolor=\"#f4f4f6\">Valor total</th>")
                .append(" </tr>");

        for (VendaProduto vendaProduto : produtos) {
            mensagem.append(" <tr>")
                    .append(" <td valign=\"middle\" class=\"m_3739803928145709693hide-mobile\" style=\"background:#f4f4f6;border-style:solid;border-collapse:collapse;border-width:2px;padding:10px 15px;border-color:#f4f4f6 white white;color:#333333;font-family:'trebuchet ms',sans-serif;font-size:14px;vertical-align:middle\">").append(vendaProduto.getDadosProduto().getIdProduto().getDescricao() == null ? " " : vendaProduto.getDadosProduto().getIdProduto().getDescricao()).append(" </td>")
                    .append(" <td valign=\"middle\" class=\"m_3739803928145709693hide-mobile\" style=\"background:#f4f4f6;border-style:solid;border-collapse:collapse;border-width:2px;padding:10px 15px;border-color:#f4f4f6 white white;color:#333333;font-family:'trebuchet ms',sans-serif;font-size:14px;vertical-align:middle\">").append(vendaProduto.getDadosProduto().getQuantidade() == null ? " " : NumeroUtil.formatarCasasDecimais(vendaProduto.getDadosProduto().getQuantidade(), 2, false)).append("</td>")
                    .append(" <td valign=\"middle\" class=\"m_3739803928145709693hide-mobile\" style=\"background:#f4f4f6;border-style:solid;border-collapse:collapse;border-width:2px;padding:10px 15px;border-color:#f4f4f6 white white;color:#333333;font-family:'trebuchet ms',sans-serif;font-size:14px;vertical-align:middle\">").append(vendaProduto.getDadosProduto().getValor() == null ? " " : NumeroUtil.formatarCasasDecimais(vendaProduto.getDadosProduto().getValor(), 2, false)).append("</td>")
                    .append(" <td valign=\"middle\" class=\"m_3739803928145709693hide-mobile\" style=\"background:#f4f4f6;border-style:solid;border-collapse:collapse;border-width:2px;padding:10px 15px;border-color:#f4f4f6 white white;color:#333333;font-family:'trebuchet ms',sans-serif;font-size:14px;vertical-align:middle\">").append(vendaProduto.getDadosProduto().getValorTotal() == null ? " " : NumeroUtil.formatarCasasDecimais(vendaProduto.getDadosProduto().getValorTotal(), 2, false)).append(" </td>")
                    .append(" </tr>");
        }

        mensagem.append("</table>")
                .append("<br/><br/>")
                .append("<table>")
                .append(" <td style='font-size:13px;' align='left'>")
                .append("<div style=\"font-size: 13px !important; font-family: Arial, Helvetica, sans-serif; color: #596167;\">")
                .append(empresa.getNomeFantasia()).append(" <br/>")
                .append(empresa.getEndereco().getEnderecoCompleto()).append(" <br/>")
                .append("Phone: ").append(empresa.getFone())
                .append("</div>")
                .append("</td>")
                .append("</table >");

        return getTemplate(htmlEmail, emailDTO.getTituloMensagem(), mensagem.toString());
    }

    private static void enviarEmail(HtmlEmail htmlMail, EmailDTO emailDTO, String envioObrigatorio) throws EmailException {
        try {
            boolean possuiDestinatarios = false;
            htmlMail.setCharset("utf-8");
            htmlMail.setSmtpPort(getPort());
            htmlMail.setSSLOnConnect(SSL);
            htmlMail.setStartTLSEnabled(TLS);
            htmlMail.setFrom(getSmtpAuthUser(), emailDTO.getRemetente());
            htmlMail.setSubject(emailDTO.getAssunto());

            if (emailDTO.getDestinatarios() != null) {
                for (Usuario usuario : emailDTO.getDestinatarios()) {
                    if (usuario.getEmail() != null && EmailUtil.validarEmail(usuario.getEmail(), true)
                            && "S".equals(envioObrigatorio) || "S".equals(usuario.getRecebeEmailComunicacao())) {
                        htmlMail.addBcc(usuario.getEmail());
                        possuiDestinatarios = true;
                    }
                }
            }

            for (File anexo : emailDTO.getAnexos()) {
                EmailAttachment attachment = new EmailAttachment();
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setPath(anexo.getAbsolutePath());
                attachment.setDescription(anexo.getName());
                attachment.setName(anexo.getName());
                htmlMail.attach(attachment);
            }

            if (possuiDestinatarios) {
                htmlMail.send();
            }
        } catch (Exception ex) {
            throw new EmailException("Erro ao enviar email: " + ex.getMessage(), ex);
        }
    }

}
