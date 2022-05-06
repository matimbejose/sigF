package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Classificacao;
import br.com.villefortconsulting.sgfinancas.entidades.Cnae;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaCnae;
import java.util.List;
import javax.persistence.EntityManager;

public class CnaeRepositorio extends BasicRepository<Cnae> {

    public CnaeRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public EmpresaCnae addEmpresaCnae(EmpresaCnae obj) {
        addEntity(obj);
        return obj;
    }

    public void removeEmpresaCnae(EmpresaCnae obj) {
        removeEntity(obj);
    }

    public List<Cnae> listar() {
        String jpql = "select c from Cnae c order by c.codigo";
        return getPureList(jpql);
    }

    public List<Cnae> listar(String descricao) {
        String jpql = "select c from Cnae c where c.descCnae like ?1 order by c.descricao ";
        return getPureList(jpql, "%" + descricao + "%");
    }

    public List<Cnae> listar(Integer codigo) {
        String jpql = "select c from Cnae c where c.codigoCnae = ?1 order by c.codigo ";
        return getPureList(jpql, codigo);
    }

    public List<Cnae> listarCnaePorClassificacao(Classificacao classificacao) {
        String jpql = "select c from Cnae c where c.idClassificacao = ?1 ";
        return getPureList(jpql, classificacao);
    }

    public List<Cnae> listarCnaePorClassificacao(List<Classificacao> classificacoes) {
        String jpql = "select c from Cnae c where c.idClassificacao in(?1)";
        return getPureList(jpql, classificacoes);
    }

    public List<Cnae> listarCnae(Empresa empresa) {
        String jpql = "select c.idCnae from EmpresaCnae  c where c.idEmpresa =?1 order by c.idCnae.descricao";
        return getPureList(jpql, empresa);
    }

    public List<EmpresaCnae> listarEmpresaCnae(Empresa empresa) {
        String jpql = "select c from EmpresaCnae c where c.idEmpresa = ?1 order by c.idEmpresa.descricao ";
        return getPureList(jpql, empresa);
    }

    public Cnae buscarPorCodigo(String codigo) {
        String jpql = " select c from Cnae c where c.codigo = ?1 ";
        return getPurePojo(jpql, codigo);
    }

}
