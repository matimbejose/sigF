package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.NFCorrecao;
import br.com.villefortconsulting.sgfinancas.entidades.NfInutilizacao;
import br.com.villefortconsulting.sgfinancas.entidades.Transportadora;
import java.util.List;
import javax.persistence.EntityManager;

public class NFRepositorio extends BasicRepository<NF> {

    public NFRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public NFRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<NF> listar() {
        String jpql = "select n from NF n order by n.descricao";
        return getPureList(jpql);
    }

    public boolean existeCodigoNumero(Integer codigoNumerico) {
        return getPurePojo(Integer.class, "select n from NF n where n.codigoNumerico = ?1", codigoNumerico) == null;
    }

    public boolean existeNotaComNumero(NF nf) {
        return getPurePojoTop1("select n from NF n where n.numeroNotaFiscal = ?1", nf.getNumeroNotaFiscal()) != null;
    }

    public Integer obterProximoNumeroNF() {
        // verifica se o tem algum numero ja informado no parametro e é a primeira emissão
        Integer idNf = getPurePojoTop1(" select nf.id from NF nf where tenatID = ?1", adHocTenant.getTenantID());

        if (idNf != null) {
            Integer numero = getPurePojo(Integer.class, "select max(n.numeroNotaFiscal) from NF n ");
            if (numero == null) {
                numero = 0;
            }
            return 1 + numero;
        }
        return 1;
    }

    public List<NF> notasPorTransportadora(Transportadora transportadora) {
        String jpql = "select c from NF c where c.idTransportadora =?1 and c.tenatID = ?2";
        return getPureList(jpql, transportadora, adHocTenant.getTenantID());
    }

    public List<NFCorrecao> obterLitaCorrecoes(NF nf) {
        String jpql = "select c from NFCorrecao c where c.idNf = ?1";
        return getPureList(jpql, nf);
    }

    public NfInutilizacao salvarInutilizacao(NfInutilizacao nfInutilizacao) {
        addEntity(nfInutilizacao);
        return nfInutilizacao;
    }

    public List<NfInutilizacao> buscarInutilizacoes(Integer id) {
        String jpql = "select ni from NfInutilizacao ni where ni.id = ?1";
        return getPureList(jpql, id);
    }

    public boolean notaDevolvida(NF nf) {
        String jpql = " select nf from NF nf where nf.idNfReferencia = ?1 ";
        return !getPureList(jpql, nf).isEmpty();
    }

    public List<Object> getDadosAuditoriaByID(Class<NFCorrecao> aClass, Integer id) {
        return getDadosAuditoria(aClass, id);
    }

    public List<NF> listarNFProblemaRetorno() {
        String jpql = "select n from NF n where xmlRetorno is not null and situacao = 'J' ";
        return getPureList(jpql);
    }

}
