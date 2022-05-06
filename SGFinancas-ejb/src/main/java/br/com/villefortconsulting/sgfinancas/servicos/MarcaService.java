package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.Marca;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.MarcaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.MarcaRepositorio;
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
public class MarcaService extends BasicService<Marca, MarcaRepositorio, MarcaFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new MarcaRepositorio(em);
    }

    public Marca buscarByFipeId(Integer fipeId) {
        return repositorio.buscarByFipeId(fipeId);
    }
    
    public List<Marca> listarMarcasByTipo(String tipo){
        return repositorio.listarMarcasByTipo(tipo);
    }

}
