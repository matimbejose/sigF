package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Ncm;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoNcm;
import java.util.List;
import javax.persistence.EntityManager;

public class NcmRepositorio extends BasicRepository<Ncm> {

    public NcmRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public Ncm buscarPorCodigo(String codigo) {
        String jpql = "select n from Ncm n where n.codigo = ?1 ";
        return getPurePojoTop1(jpql, codigo);
    }

    public Ncm buscarPorCest(String cest) {
        String jpql = "select n from Ncm n where n.cest = ?1 ";
        return getPurePojoTop1(jpql, cest);
    }

    public List<Ncm> listar() {
        String jpql = "select n from Ncm n where n.tipo = ?1 order by n.codigo";
        return getPureList(jpql, EnumTipoNcm.NCM.getTipo());
    }

    public List<Ncm> listarCapitulos() {
        String jpql = "select n from Ncm n where n.tipo = ?1 ";
        return getPureList(jpql, EnumTipoNcm.CAPITULO.getTipo());
    }

    public List<Ncm> listarNcmPorCodigoDoPai(String codigo, String tipo) {
        String jpql = "select n from Ncm n where n.tipo = ?1 and n.codigoPai = ?2 ";
        return getPureList(jpql, tipo, codigo);
    }

    public List<Ncm> listar(String descricao) {
        String jpql = "select n from Ncm n where n.descricao like ?1 order by n.descricao ";
        return getPureList(jpql, "%" + descricao + "%");
    }

    public List<Ncm> listar(Integer codigo) {
        String jpql = "select n from Ncm n where n.codigo = ?1 order by n.codigo ";
        return getPureList(jpql, codigo);
    }

    public List<Ncm> listarPorCodigo(String codigo) {
        String jpql = "select n from Ncm n where n.codigo like ?1 order by n.codigo ";
        return getPureList(10, jpql, codigo + "%");
    }

}
