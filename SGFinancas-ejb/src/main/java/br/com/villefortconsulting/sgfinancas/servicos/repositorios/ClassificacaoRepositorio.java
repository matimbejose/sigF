package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Classificacao;
import br.com.villefortconsulting.sgfinancas.entidades.ClassificacaoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ClassificacaoFiltro;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.criterion.Criterion;

public class ClassificacaoRepositorio extends BasicRepository<Classificacao> {

    public ClassificacaoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Classificacao> getListaFiltrada(List<Criterion> criterions, ClassificacaoFiltro filtro) {
        return getListaFiltrada(Classificacao.class, criterions, filtro);
    }

    public ClassificacaoEmpresa getClassificaoEmpresa(int id) {
        return getEntity(ClassificacaoEmpresa.class, id);
    }

    public List<ClassificacaoEmpresa> getClassificaoEmpresas() {
        return getPureList("select ce from ClassificacaoEmpresa");
    }

    public List<Classificacao> getClassificoes() {
        return getPureList("select c from Classificacao c order by c.secao");
    }

    public Classificacao getClassificaoByNumero(int id) {
        List<Classificacao> listaClassificacoes = list();
        for (Classificacao c : listaClassificacoes) {
            int s = Integer.parseInt(c.getDivisao().split("\\.\\.")[0]);
            int e = Integer.parseInt(c.getDivisao().split("\\.\\.")[1]);
            if (id >= s && id <= e) {
                return c;
            }
        }
        return null;
    }

    public ClassificacaoEmpresa setClassificaoEmpresa(ClassificacaoEmpresa obj) {
        return setEntity(obj);
    }

    public void removeClassificacaoEmpresa(ClassificacaoEmpresa obj) {
        removeEntity(obj);
    }

    public ClassificacaoEmpresa addClassificaoEmpresa(ClassificacaoEmpresa obj) {
        addEntity(obj);
        return obj;
    }

    public List<Classificacao> listarClassificacao(Empresa empresa) {
        String jpql = "select c.idClassificacao from ClassificacaoEmpresa  c where c.idEmpresa =?1 order by c.idClassificacao.descricao";
        return getPureList(jpql, empresa);
    }

    public List<ClassificacaoEmpresa> listarClassificacaoEmpresa(Empresa empresa) {
        String jpql = "select c from ClassificacaoEmpresa  c where c.idEmpresa =?1 order by c.idClassificacao.descricao";
        return getPureList(jpql, empresa);
    }

}
