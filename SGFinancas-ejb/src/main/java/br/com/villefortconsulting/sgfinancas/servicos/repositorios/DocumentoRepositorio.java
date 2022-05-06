package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import java.util.List;
import javax.persistence.EntityManager;

public class DocumentoRepositorio extends BasicRepository<Documento> {

    public DocumentoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public DocumentoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Documento> listar() {
        String jpql = "select d from Documento d order by d.descricao";
        return getPureList(jpql);
    }

}
