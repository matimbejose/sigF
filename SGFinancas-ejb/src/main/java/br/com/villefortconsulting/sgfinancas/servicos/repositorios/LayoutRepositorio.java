package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Layout;
import br.com.villefortconsulting.sgfinancas.entidades.LayoutCampo;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.LayoutFiltro;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoLayout;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.criterion.Criterion;

public class LayoutRepositorio extends BasicRepository<Layout> {

    public LayoutRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public LayoutRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Layout getLayoutPorTipo(String tipo) {
        return getPurePojoTop1("select lay from Layout lay where lay.tipo = ?1 ", tipo);
    }

    public Layout getLayoutFetchLayoutCampos(int id) {
        return getPurePojo("select lay from Layout lay left join fetch lay.layoutCampoList where lay.id = ?1 ", id);
    }

    public List<Layout> getLayouts() {
        return getPureList("select lay from Layout lay");
    }

    public List<Layout> getLayouts(String descricao) {
        return getPureList("select lay from Layout lay where lay.descricao like ?1", "%" + descricao + "%");
    }

    public List<Layout> getLayoutsExportacao() {
        return getPureList("select lay from Layout lay where lay.tipo = ?1", EnumTipoLayout.EXPORTACAO.getTipo());
    }

    public List<LayoutCampo> getLayoutCampos(Layout obj) {
        return getPureList("select campos from LayoutCampo campos where campos.idLayout = ?1", obj);
    }

    public List<Layout> getListaFiltrada(List<Criterion> criterions, LayoutFiltro filtro) {
        return getListaFiltrada(Layout.class, criterions, filtro);
    }

}
