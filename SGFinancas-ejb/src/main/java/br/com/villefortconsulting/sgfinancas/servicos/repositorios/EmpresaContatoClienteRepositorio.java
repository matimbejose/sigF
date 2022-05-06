package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaContatoCliente;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EmpresaContatoClienteFiltro;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.criterion.Criterion;

public class EmpresaContatoClienteRepositorio extends BasicRepository<EmpresaContatoCliente> {

    public static final Boolean NAO_USA_TENANT = false;

    public EmpresaContatoClienteRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public EmpresaContatoClienteRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<EmpresaContatoCliente> getListaFiltradaEmpresaContatoCliente(List<Criterion> criterions, EmpresaContatoClienteFiltro filtro, Boolean usaTenant) {
        return getListaFiltrada(EmpresaContatoCliente.class, criterions, filtro, usaTenant);
    }

    public int getQuantidadeRegistrosFiltradosEmpresaContatoCliente(List<Criterion> criterions, Boolean usaTenant) {
        return getQuantidadeRegistrosFiltrados(EmpresaContatoCliente.class, criterions, usaTenant);
    }

}
