package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaPadrao;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PlanoContaPadraoRepositorio;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PlanoContaPadraoService extends BasicService<PlanoContaPadrao, PlanoContaPadraoRepositorio, BasicFilter<PlanoContaPadrao>> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new PlanoContaPadraoRepositorio(em);
    }

    public List<PlanoContaPadrao> obterTodosItensPlanoConta() {
        return repositorio.obterTodosItensPlanoConta();
    }

    public PlanoContaPadrao buscarPlanoContaPadrao(String codigo) {
        return repositorio.buscarPlanoContaPadrao(codigo);
    }

    public void aplicarPlanoContaAEmpresa(String tenatID) {
        repositorio.aplicarPlanoContaAEmpresa(tenatID);
    }

}
