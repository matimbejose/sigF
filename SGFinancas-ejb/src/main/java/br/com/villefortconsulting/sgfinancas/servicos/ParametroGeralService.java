package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroGeral;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ParametroGeralRepositorio;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
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
public class ParametroGeralService extends BasicService<ParametroGeral, ParametroGeralRepositorio, BasicFilter<ParametroGeral>> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ParametroGeralRepositorio(em);
    }

    public ParametroGeral getParametro() {
        return listar().get(0);
    }

    public boolean podeAcessar(Empresa empresa) {
        if (!empresa.getTipoConta().equalsIgnoreCase("G") || empresa.getPrazoUsarSemComprar() == -1) {
            return true;
        }
        if (empresa.getDataCredenciamento() != null) {
            int diffInDays = DataUtil.diferencaEntreDias(empresa.getDataCredenciamento(), new Date());
            return diffInDays <= empresa.getPrazoUsarSemComprar();
        }
        return false;
    }

}
