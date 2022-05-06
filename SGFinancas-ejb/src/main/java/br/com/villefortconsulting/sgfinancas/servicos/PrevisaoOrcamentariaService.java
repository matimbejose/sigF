package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.PrevisaoOrcamentaria;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PrevisaoOrcamentariaRepositorio;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class PrevisaoOrcamentariaService extends BasicService<PrevisaoOrcamentaria, PrevisaoOrcamentariaRepositorio, BasicFilter<PrevisaoOrcamentaria>> {

    private static final long serialVersionUID = 1L;

    @EJB
    private PlanoContaService planoContaService;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new PrevisaoOrcamentariaRepositorio(em, adHocTenant);
    }

    @Override
    public PrevisaoOrcamentaria salvar(PrevisaoOrcamentaria previsao) {
        if (previsao.getId() == null) {
            previsao.setTenatID(adHocTenant.getTenantID());
            return adicionar(previsao);
        }
        return alterar(previsao);
    }

    public void salvarLancamentos(List<PrevisaoOrcamentaria> listPrevisao) {
        listPrevisao.forEach(previsaoOrcamentaria -> {
            if (NumeroUtil.formatarCasasDecimais(previsaoOrcamentaria.getPrevisao(), 2).compareTo(0d) != 0) {
                salvar(previsaoOrcamentaria);
            } else if (previsaoOrcamentaria.getId() != null) {
                remover(previsaoOrcamentaria);
            }
        });
    }

    public List<PrevisaoOrcamentaria> obterPrevisaoOrcamentaria(Date data, CentroCusto centroCusto) {
        final Date competencia = DataUtil.getPrimeiroDiaDoMes(data);
        List<PrevisaoOrcamentaria> listaComLancamentos = listarPorPeriodo(competencia, centroCusto);

        return planoContaService.listarPlanoContaOrderCodigo().stream()
                .map(planoConta
                        -> listaComLancamentos.stream()
                        .filter(p -> p.getIdPlanoConta().equals(planoConta))
                        .findFirst().orElseGet(() -> {
                            PrevisaoOrcamentaria po = new PrevisaoOrcamentaria();
                            po.setData(competencia);
                            po.setIdPlanoConta(planoConta);
                            po.setTenatID(adHocTenant.getTenantID());
                            po.setPrevisao(0d);
                            return po;
                        }))
                .sorted((p1, p2) -> p1.getIdPlanoConta().getCodigo().compareTo(p2.getIdPlanoConta().getCodigo()))
                .collect(Collectors.toList());
    }

    public List<PrevisaoOrcamentaria> listarPorPeriodo(Date data, CentroCusto centroCusto) {
        if (centroCusto != null) {
            return repositorio.listarPorPeriodo(data, centroCusto);
        }
        return repositorio.listarPorPeriodo(data);
    }

    public PrevisaoOrcamentaria getPrevisaoPorPlanoConta(Date data, PlanoConta planoConta) {
        return repositorio.listarPorIDPlanoConta(data, planoConta);
    }

    public List<PrevisaoOrcamentaria> obterlistaPrevisaoOrcamentaria(Date dataInicio, Date dataFim) {
        return repositorio.obterlistaPrevisaoOrcamentaria(dataInicio, dataFim);
    }

}
