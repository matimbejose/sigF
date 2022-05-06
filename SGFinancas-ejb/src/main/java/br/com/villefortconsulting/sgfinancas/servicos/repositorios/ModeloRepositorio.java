package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Marca;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVeiculoFipe;
import java.util.List;
import javax.persistence.EntityManager;

public class ModeloRepositorio extends BasicRepository<Modelo> {

    public ModeloRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Modelo> listar(Marca marca) {
        return getPureList("select m from Modelo m where m.idMarca = ?1", marca);
    }

    public List<Modelo> listar(EnumTipoVeiculoFipe tipo, Marca marca) {
        return getPureList("select m from Modelo m where m.idMarca = ?1 and m.tipo = ?2", marca, tipo.name());
    }

    public List<Modelo> listarByMarcaFipeId(Integer id) {
        return getPureList("select m from Modelo m where m.idMarca.fipeId = ?1", id);
    }

    public Modelo buscarByModeloFipeId(Integer id) {
        return getPurePojoTop1("select m from Modelo m where m.fipeId = ?1", id);
    }

}
