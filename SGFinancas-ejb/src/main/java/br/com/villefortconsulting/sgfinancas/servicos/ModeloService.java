package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.Marca;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ModeloFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ModeloRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVeiculoFipe;
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
public class ModeloService extends BasicService<Modelo, ModeloRepositorio, ModeloFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new ModeloRepositorio(em);
    }

    public List<Modelo> listar(Marca marca) {
        return repositorio.listar(marca);
    }

    public List<Modelo> listar(EnumTipoVeiculoFipe tipo, Marca marca) {
        return repositorio.listar(tipo, marca);
    }

    public List<Modelo> listarByMarcaFipeId(Integer id) {
        return repositorio.listarByMarcaFipeId(id);
    }

    public Modelo buscarByModeloFipeId(Integer id) {
        return repositorio.buscarByModeloFipeId(id);
    }

}
