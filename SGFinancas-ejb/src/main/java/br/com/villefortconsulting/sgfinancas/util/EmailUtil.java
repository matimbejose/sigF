package br.com.villefortconsulting.sgfinancas.util;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroGeral;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoGetnet;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroGeralService;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmailUtil extends EmailDTO {

    @EJB
    private ParametroGeralService parametroGeralService;

    private static final long serialVersionUID = 1L;

    private static final String REMETENTE_VILLEFORT = "Equipe Villefort Consulting";

    public static EmailDTO getEmailBoletoPagamentoSistema(TransacaoGetnet tg, Empresa empresa) {
        List<Usuario> destinatarios = new LinkedList<>();
        destinatarios.add(Usuario.builder().nome(empresa.getDescricao()).email(empresa.getEmail()).build());

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Renovação do sistema SG Finanças");
        emailDTO.setTituloMensagem("Foi realizado um pedido de renovação do  SG Finanças com pagamento via boleto bancário");
        emailDTO.setMensagem("<a href = \"" + tg.getLinkBoleto() + "\">Clique aqui</a> para baixar o boleto.");
        emailDTO.setDestinatarios(destinatarios);
        return emailDTO;
    }

    public static EmailDTO getSolicitacaoDados(Empresa empresa) {
        List<Usuario> destinatarios = new LinkedList<>();
        destinatarios.add(Usuario.builder().nome("Suporte").email("suporte@villefortconsulting.com").build());

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Solicitação de exportação dos dados");
        emailDTO.setTituloMensagem("A empresa " + empresa.getNomeFantasia() + " solicita a exportação dos dados contidos no SG Finanças");
        emailDTO.setMensagem("");
        emailDTO.setDestinatarios(destinatarios);
        return emailDTO;
    }

    public static EmailDTO getEmailRecuperacaoSenha(Usuario usuarioRecuperacao, String senha) {
        List<Usuario> destinatarios = new LinkedList<>();
        destinatarios.add(usuarioRecuperacao);

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Solicitação de nova senha atendida, acesse o SGFinanças com o login e senha abaixo. <br/><br/>")
                .append("Login: ").append(usuarioRecuperacao.getLogin()).append(" <br/>")
                .append("Senha: ").append(senha);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Recuperação de senha");
        emailDTO.setTituloMensagem("Recuperação de senha");
        emailDTO.setMensagem(mensagem.toString());
        emailDTO.setDestinatarios(destinatarios);

        return emailDTO;
    }

    public static EmailDTO getEmailContaPendente(Usuario usuario, Empresa empresa, String mensagem) {
        List<Usuario> lista = new ArrayList<>();
        lista.add(usuario);
        return getEmailContaPendente(lista, empresa, mensagem);
    }

    public static EmailDTO getEmailContaPendente(List<Usuario> usuarios, Empresa empresa, String mensagem) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Pendências SGFinanças");
        emailDTO.setTituloMensagem("Contas pendentes da empresa " + empresa.getDescricao());
        emailDTO.setMensagem(mensagem);
        emailDTO.setDestinatarios(usuarios);

        return emailDTO;
    }

    public static EmailDTO getEmailVencimentoSistema(PagamentoMensalidade pm) {
        List<Usuario> usuarios = new ArrayList<>();
        Usuario user = new Usuario();
        user.setNome(pm.getIdEmpresa().getDescricao());
        user.setEmail(pm.getIdEmpresa().getEmail());
        usuarios.add(user);

        StringBuilder sb = new StringBuilder();
        sb.append("Seu plano expira no dia ")
                .append(DataUtil.dateToString(pm.getDataValidade()))
                .append(" Acesse o SG Finanças, menu Meu plano e renove-o por mais 30 dias. <br/><br/>Se houver alguma dúvida, entre em contato com o nosso suporte através do botão \"Suporte\" no SG Finanças, ou pelos números: <br/><br/>Whatsapp -> (31)9-8239-7105<br/><br/>Comercial -> (31)2531-9416<br/><br/>Att.<br/><br/>Equipe SG Finanças");

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Vencimento do contrato do SG Finanças");
        emailDTO.setTituloMensagem("Seu plano vai expirar em breve");
        emailDTO.setMensagem(sb.toString());
        emailDTO.setDestinatarios(usuarios);

        return emailDTO;
    }

    public static EmailDTO getEmailClienteFaturasIuguFaltantes(List<Cliente> clientes, List<Usuario> destinatarios) {
        Date referencia = DataUtil.subtrairMeses(DataUtil.getHoje(), 1);
        List<Usuario> usuarios = destinatarios.stream().map(destinatario -> {
            Usuario user = new Usuario();
            user.setNome(destinatario.getNome());
            user.setEmail(destinatario.getEmail());
            return user;
        }).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        StringBuilder c = new StringBuilder();
        sb.append("No mês ").append(DataUtil.dateToString(referencia, "MM/yyyy")).append(" ")
                .append("receberam notas porem não receberam faturas do IUGU");
        clientes.forEach(cliente -> c.append("<br/>").append(cliente.getNome()).append(" ")
                .append(cliente.getCpfCNPJ()));

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Faturas IUGU faltantes");
        emailDTO.setTituloMensagem("Faturas IUGU faltantes");
        emailDTO.setMensagem(sb.toString() + c.toString());
        emailDTO.setDestinatarios(usuarios);

        return emailDTO;
    }

    public static EmailDTO getEmailSenhaFuncionario(Usuario usuarioFuncionario, String senha) {
        List<Usuario> destinatarios = new LinkedList<>();
        destinatarios.add(usuarioFuncionario);

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Caro funcionário(a), favor utilizar o login e senha abaixo para logar no SGFinanças. <br/><br/>")
                .append("Login: ").append(usuarioFuncionario.getLogin()).append(" <br/>")
                .append("Senha: ").append(senha);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Senha de acesso do funcionário");
        emailDTO.setTituloMensagem("Senha de acesso do funcionário");
        emailDTO.setMensagem(mensagem.toString());
        emailDTO.setDestinatarios(destinatarios);

        return emailDTO;
    }

    public EmailDTO getEmailSenhaAcesso(Usuario usuarioCredenciamento, Empresa empresa) {
        List<Usuario> destinatarios = new LinkedList<>();
        destinatarios.add(usuarioCredenciamento);

        ParametroGeral pg = parametroGeralService.getParametro();

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Bem-vindo ao SGFinanças");
        emailDTO.setTituloMensagem("Seja bem-vindo, " + usuarioCredenciamento.getNome() + "!");
        emailDTO.setDestinatarios(destinatarios);
        if (pg.getIdDocumentoAnexoEmailCadastro() != null) {
            pg.getIdDocumentoAnexoEmailCadastro().getDocumentoAnexoList().stream()
                    .forEach(da -> emailDTO.attachFile(da.getFile(), da.getNomeArquivo()));
        }
        String corpoEmail = pg.getCorpoEmailCadastro();
        if (corpoEmail == null) {
            corpoEmail = "";
        }
        corpoEmail = corpoEmail
                .replace("{{NOME_EMPRESA}}", empresa.getDescricao())
                .replace("{{EMAIL_EMPRESA}}", empresa.getEmail())
                .replace("{{DATA_CADASTRO}}", DataUtil.dataHoraToString(empresa.getDataCredenciamento()))
                .replace("{{USUARIO_CADASTRO}}", usuarioCredenciamento.getNome());
        emailDTO.setMensagem(corpoEmail);

        return emailDTO;
    }

    public static EmailDTO getEmailAvisoCadastro(Usuario usuarioCredenciamento, Empresa empresa) {
        List<Usuario> destinatarios = new LinkedList<>();
        destinatarios.add(Usuario.builder().nome("Suporte").email("suporte@sgfinancas.com").build());
        destinatarios.add(Usuario.builder().nome("Suporte").email("suporte@villefortconsulting.com").build());
        destinatarios.add(Usuario.builder().nome("Comercial").email("comercial@sgfinancas.com").build());

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("A empresa ")
                .append(empresa.getDescricao())
                .append(", cnpj: ")
                .append(empresa.getCnpj())
                .append(" cadastrou-se no SG Finanças.<br/><br/>Usuário responsável: ")
                .append(usuarioCredenciamento.getNome())
                .append(", e-mail: ")
                .append(usuarioCredenciamento.getEmail());
        if (empresa.getFone() != null) {
            mensagem.append(", telefone: ")
                    .append(empresa.getFone());
        }

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Novo cadastro no SGFinanças");
        emailDTO.setTituloMensagem("Foi realizado um cadastro no SGFinanças");
        emailDTO.setMensagem(mensagem.toString());
        emailDTO.setDestinatarios(destinatarios);

        return emailDTO;
    }

    public static EmailDTO getEmailNotaXml(NF nf) throws IOException {

        Usuario usuario = new Usuario();
        usuario.setEmail(nf.getObjTNFe().getInfNFe().getDest().getEmail());

        List<Usuario> destinatarios = new LinkedList<>();
        destinatarios.add(usuario);

        File file = FileUtil.convertByteToFile(nf.getXmlArmazenamento().getBytes(StandardCharsets.UTF_8), "nfe_" + nf.getNumeroNotaFiscal() + ".xml");
        List<File> anexos = new LinkedList<>();
        anexos.add(file);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Envio de xml da nota fiscal");
        emailDTO.setTituloMensagem(nf.getChaveFormatada() + "<br/><br/><br/>Chave de acesso");
        emailDTO.setDestinatarios(destinatarios);
        emailDTO.setAnexos(anexos);

        return emailDTO;
    }

    public static EmailDTO getEmailContratoVencendo(List<Contrato> contratos, Empresa empresa) {
        Usuario usuario = new Usuario();
        usuario.setEmail(empresa.getEmail());

        List<Usuario> destinatarios = new LinkedList<>();
        destinatarios.add(usuario);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Alerta de contrato próximo da data de vencimento");
        emailDTO.setTituloMensagem(contratos.size() + " contratos próximos da data de vencimento");
        emailDTO.setDestinatarios(destinatarios);

        return emailDTO;
    }

    public static EmailDTO getEmailNotaXml(NFS nfs) throws IOException {

        Usuario usuario = new Usuario();
        usuario.setEmail(nfs.getEmailCliente());

        List<Usuario> destinatarios = new LinkedList<>();
        destinatarios.add(usuario);

        File file = FileUtil.convertByteToFile(nfs.getXmlArmazenamento().getBytes(StandardCharsets.UTF_8), "nfse_" + nfs.getNumeroNotaFiscal() + ".xml");
        List<File> anexos = new LinkedList<>();
        anexos.add(file);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Envio de xml da nota fiscal serviço");
        emailDTO.setTituloMensagem("NFS");
        emailDTO.setTituloMensagem(nfs.getNumeroNotaFiscalFormatada() + "<br/><br/><br/>Número da nota fiscal serviço");
        emailDTO.setDestinatarios(destinatarios);
        emailDTO.setAnexos(anexos);

        return emailDTO;
    }

    public static boolean validarEmail(String email, boolean required) {
        if (email == null) {
            return !required;
        }

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = pattern.matcher(email);
        return m.matches();
    }

    public static EmailDTO getEmailAcessoEmpresa(byte[] xls, List<Usuario> usuarios) throws IOException {
        Date date = DataUtil.subtrairMeses(new Date(), 1);
        List<File> anexos = new LinkedList<>();
        anexos.add(FileUtil.convertByteToFile(xls, "acessosPorEmpresa.xls"));

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRemetente(REMETENTE_VILLEFORT);
        emailDTO.setAssunto("Acessos por mês no SG Finanças");
        emailDTO.setTituloMensagem("Acessos por mês no SG Finanças");
        emailDTO.setMensagem("Em anexo a listagem de acessos à plataforma SG Finanças da competência " + DataUtil.dateToString(date, "MM/yyyy"));
        if (SystemPreferencesUtil.getProp("ambiente", "homologacao").equalsIgnoreCase("producao")) {
            Usuario financeiro = new Usuario();
            financeiro.setNome("Departamento financeiro");
            financeiro.setEmail("financeiro@villefortconsulting.com");
            emailDTO.setDestinatarios(Collections.singletonList(financeiro));
        }
        emailDTO.setDestinatariosOcultos(usuarios);
        emailDTO.setAnexos(anexos);

        return emailDTO;
    }

}
