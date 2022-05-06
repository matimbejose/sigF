package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EmpresaFiltro;
import br.com.villefortconsulting.sgfinancas.integratorclient.IntegratorClient;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.AccountDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.AccountResponseDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.exception.CriarContaDigitalException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.EmpresaRepositorio;
import java.io.IOException;
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
public class ContaDigitalService extends BasicService<Empresa, EmpresaRepositorio, EmpresaFiltro> {

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
        repositorio = new EmpresaRepositorio(em, adHocTenant);
    }

    private transient IntegratorClient client = new IntegratorClient();

    public AccountResponseDTO criarContaDigital(AccountDTO accountDTO) throws CriarContaDigitalException, IOException {
        return client.createAccount(accountDTO);
    }

}
