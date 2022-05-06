package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Marca;
import java.util.List;
import javax.persistence.EntityManager;

public class MarcaRepositorio extends BasicRepository<Marca> {

    public MarcaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public Marca buscarByFipeId(Integer fipeId) {
        return getPurePojoTop1("select m from Marca m where m.fipeId = ?1 and m.fipeOrder like '__%'", fipeId);
    }
    
    public List<Marca> listarMarcasByTipo (String tipo){
         return getPureList("select m from Marca m where m.fipeOrder = ?1", tipo);
    }

}
