package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.VersaoSistema;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.VersaoSistemaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.VersaoSistemaRepositorio;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class VersaoSistemaService extends BasicService<VersaoSistema, VersaoSistemaRepositorio, VersaoSistemaFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public VersaoSistema salvar(VersaoSistema vs) {
        if (repositorio.findByVersao(vs.getVersao()) != null) {
            throw new CadastroException("Já exite uma versão do sistema com esse número.", null);
        }
        return super.salvar(vs);
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new VersaoSistemaRepositorio(em);
    }

    public VersaoSistema findLast() {
        return repositorio.findLast();
    }

}
