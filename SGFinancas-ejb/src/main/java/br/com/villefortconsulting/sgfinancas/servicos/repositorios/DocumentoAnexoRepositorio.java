package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import java.util.List;
import javax.persistence.EntityManager;

public class DocumentoAnexoRepositorio extends BasicRepository<DocumentoAnexo> {

    public DocumentoAnexoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public DocumentoAnexoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    @Override
    public List<DocumentoAnexo> list() {
        String jpql = "select da from DocumentoAnexo da where da.ativo = 'S' order by da.nomeArquivo";
        return getPureList(jpql);
    }

    public DocumentoAnexo buscarUltimoAnexoDocumento(Documento documento) {
        String jpql = "select da from DocumentoAnexo da where da.idDocumento = ?1 and da.ativo = 'S' order by da.dataEnvio desc ";
        return getPurePojoTop1(jpql, documento);
    }

    public List<DocumentoAnexo> listByDocumento(Documento documento) {
        String jpql = "select da from DocumentoAnexo da where da.idDocumento = ?1 and da.ativo = 'S'";
        return getPureList(jpql, documento);
    }

}
