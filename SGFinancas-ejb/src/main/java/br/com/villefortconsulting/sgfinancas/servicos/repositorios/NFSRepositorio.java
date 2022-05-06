package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import java.util.List;
import javax.persistence.EntityManager;

public class NFSRepositorio extends BasicRepository<NFS> {

    public NFSRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public NFSRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public NFS buscarNFSPorNumeroNotaFiscal(String numeroNotaFiscal) {
        return getPurePojoTop1("select nfs from NFS nfs where nfs.numeroNotaFiscal = ?1", numeroNotaFiscal);
    }

    public List<NFS> listar() {
        String jpql = "select n from NFS n order by n.descricao";
        return getPureList(jpql);
    }

    public Integer obterProximoNumeroRPS() {

        // verifica se o tem algum numero ja informado no parametro e é a primeira emissão
        if (existeAlgumaNFSemitida()) {
            Integer numero = getPurePojo(Integer.class, "select max(n.numeroRPS) from NFS n ");
            return numero + 1;
        } else {
            return 0;
        }

    }

    public boolean existeAlgumaNFSemitida() {
        return getPurePojoTop1(Integer.class, "select n.id from NFS n where n.numeroRPS <> null") != null;
    }

    public List<NFS> buscarNFSPorContrato(Contrato contrato) {
        StringBuilder sql = new StringBuilder();

        sql.append(" select cp.idNFS ");
        sql.append(" from ContaParcela cp ");
        sql.append(" join cp.idConta c ");
        sql.append(" join c.idContrato co ");
        sql.append(" where co = ?1 ");

        return getPureList(sql.toString(), contrato);
    }

}
