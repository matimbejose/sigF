package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.Layout;
import br.com.villefortconsulting.sgfinancas.entidades.LayoutCampo;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.LayoutFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.LayoutRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoLayout;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoLayoutCampo;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LayoutService extends BasicService<Layout, LayoutRepositorio, LayoutFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new LayoutRepositorio(em);
    }

    public Layout getLayoutPorTipo(String tipo) {
        return repositorio.getLayoutPorTipo(tipo);
    }

    public Layout getLayoutFetchLayoutCampos(int id) {
        return repositorio.getLayoutFetchLayoutCampos(id);
    }

    public Layout addLayout(Layout obj) {

        if (obj.getTipo().equals(EnumTipoLayout.EXPORTACAO.getTipo())) {
            // IMPORTANTE: QUANDO ALTERAR A DESCRICAO DE ALGUM CAMPO ALTERAR IMPORTACAO NA CLASSE ArquivoExportacaoService.java

            //CABEÇALHO
            obj.addLayoutCampo(new LayoutCampo("Data", "N", "D", EnumTipoLayoutCampo.CABECALHO.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Número de linhas", "N", "I", EnumTipoLayoutCampo.CABECALHO.getTipo()));

            //DETALHE
            obj.addLayoutCampo(new LayoutCampo("Cliente", "N", "T", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("CPF/CNPJ do cliente", "N", "I", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Fornecedor", "N", "T", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("CPF/CNPJ do fornecedor", "N", "I", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Código do plano de contas", "N", "T", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Descrição do plano de contas", "N", "T", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Tipo de conta", "N", "T", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Situação", "N", "T", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Data de vencimento", "N", "D", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Data de pagamento", "N", "D", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Número da parcela", "N", "I", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Quantidade de parcelas", "N", "I", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Valor da parcela", "N", "I", EnumTipoLayoutCampo.DETALHE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Valor do contrato", "N", "I", EnumTipoLayoutCampo.DETALHE.getTipo()));

            //RODAPÉ
            obj.addLayoutCampo(new LayoutCampo("Data", "N", "D", EnumTipoLayoutCampo.RODAPE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Número de linhas", "N", "I", EnumTipoLayoutCampo.RODAPE.getTipo()));
            obj.addLayoutCampo(new LayoutCampo("Valor total das parcelas", "N", "I", EnumTipoLayoutCampo.RODAPE.getTipo()));

        }
        return adicionar(obj);
    }

    public List<Layout> getLayouts() {
        return repositorio.getLayouts();
    }

    public List<Layout> getLayouts(String descricao) {
        return repositorio.getLayouts(descricao);
    }

    public List<Layout> getLayoutsExportacao() {
        return repositorio.getLayoutsExportacao();
    }

    public List<LayoutCampo> getLayoutCampos(Layout layout) {
        return repositorio.getLayoutCampos(layout);
    }

    @Override
    public Criteria getModel(LayoutFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "tipo", EnumTipoLayout.EXPORTACAO.getTipo());

        return criteria;
    }

}
