package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.PontoObservacao;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PontoObservacaoRepositorio;
import java.util.Date;
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
public class PontoObservacaoService extends BasicService<PontoObservacao, PontoObservacaoRepositorio, BasicFilter<PontoObservacao>> {

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
        repositorio = new PontoObservacaoRepositorio(em, adHocTenant);
    }

    public PontoObservacao salvar(PontoObservacao pontoObservacao, Date competencia, Funcionario funcionario) {
        if (pontoObservacao.getId() == null) {
            pontoObservacao = preencherPontoObservacao(pontoObservacao, competencia, funcionario);
            return adicionar(pontoObservacao);
        }
        return alterar(pontoObservacao);

    }

    public PontoObservacao preencherPontoObservacao(PontoObservacao ponto, Date competencia, Funcionario funcionario) {
        ponto.setIdFuncionario(funcionario);
        ponto.setReferencia(competencia);
        ponto.setTenatID(adHocTenant.getTenantID());

        return ponto;
    }

    public PontoObservacao pegaObservacao(Date competencia, Funcionario funcionario) {
        return repositorio.pegaObservacao(competencia, funcionario);
    }

    public String buscarObservacao(Date competencia, Funcionario funcionario) {
        return repositorio.buscarObservacao(competencia, funcionario);
    }

    public List<PontoObservacao> listaPorPeriodo(Date competencia, Funcionario funcionario) {
        return repositorio.listaPorPeriodo(competencia, funcionario);
    }

}
