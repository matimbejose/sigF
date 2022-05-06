package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.CategoriaEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CategoriaEmpresaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CategoriaEmpresaRepositorio;
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
public class CategoriaEmpresaService extends BasicService<CategoriaEmpresa, CategoriaEmpresaRepositorio, CategoriaEmpresaFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new CategoriaEmpresaRepositorio(em);
    }

    public CategoriaEmpresa buscarPorNome(String nome) {
        return repositorio.buscarPorNome(nome);
    }

}
