package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.FuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FuncaoAjudaFiltro;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.criterion.Criterion;

public class FuncaoAjudaRepositorio extends BasicRepository<FuncaoAjuda> {

    public FuncaoAjudaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public FuncaoAjuda getFuncaoAjuda(String chave) {
        return getPurePojo("select ts from FuncaoAjuda ts where ts.chave = ?1", chave);
    }

    public List<FuncaoAjuda> getFuncaoAjudas() {
        return getPureList("select ts from FuncaoAjuda ts order by ts.descricao");
    }

    public List<FuncaoAjuda> getListaFiltrada(List<Criterion> criterions, FuncaoAjudaFiltro filtro) {
        return getListaFiltrada(FuncaoAjuda.class, criterions, filtro);
    }

}
