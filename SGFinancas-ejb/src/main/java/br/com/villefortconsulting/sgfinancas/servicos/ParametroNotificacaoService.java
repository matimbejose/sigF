package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroNotificacao;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ParametroNotificacaoRepositorio;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ParametroNotificacaoService extends BasicService<ParametroNotificacao, ParametroNotificacaoRepositorio, BasicFilter<ParametroNotificacao>> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ParametroNotificacaoRepositorio(em, adHocTenant);
    }

    public void atualizarEmailsNotificacao(List<String> emails, String tipo) {

        // remover emails
        List<ParametroNotificacao> removerContatos;
        if (emails == null) {
            removerContatos = repositorio.listarParametroNotificacaoNaoEncontrado(tipo);
        } else {
            removerContatos = repositorio.listarParametroNotificacaoNaoEncontrado(tipo, emails);
        }
        removerContatos.stream().forEach(this::remover);

        // adicionar emails
        if (emails != null) {
            emails.stream().forEach(email -> {
                boolean emailNaoCadastrado = true;
                for (ParametroNotificacao c : repositorio.listar()) {
                    if (c.getEmail().equals(email) && c.getTipo().equals(tipo)) {
                        emailNaoCadastrado = false;
                    }
                }
                if (emailNaoCadastrado) {
                    ParametroNotificacao parametroNotificacao = new ParametroNotificacao();
                    parametroNotificacao.setEmail(email);
                    parametroNotificacao.setTipo(tipo);
                    parametroNotificacao.setTenatID(adHocTenant.getTenantID());

                    adicionar(parametroNotificacao);
                }
            });
        }
    }

    public List<String> listarEmails(String tipo) {
        return repositorio.listarEmails(tipo);
    }

    public List<String> listarEmails(String tipo, String tenatID) {
        return repositorio.listarEmails(tipo, tenatID);
    }

}
