package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import br.com.villefortconsulting.sgfinancas.entidades.ModeloInformacao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ModeloInformacaoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ModeloInformacaoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVeiculoFipe;
import java.util.List;
import javax.ejb.LocalBean;
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
public class ModeloInformacaoService extends BasicService<ModeloInformacao, ModeloInformacaoRepositorio, ModeloInformacaoFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new ModeloInformacaoRepositorio(em);
    }

    public List<ModeloInformacao> listar(Modelo modelo) {
        return repositorio.listar(modelo);
    }

    public List<ModeloInformacao> listar(EnumTipoVeiculoFipe tipo, Modelo modelo) {
        return repositorio.listar(tipo, modelo);
    }

    public ModeloInformacao buscarPorFipeCodigo(Integer fipeCodigo, Integer ano) {
        return repositorio.buscarPorFipeCodigo(fipeCodigo, ano);
    }

    public ModeloInformacao buscarPorFipeCodigo(String fipeCodigo, Modelo modelo) {
        return repositorio.buscarPorFipeCodigo(fipeCodigo, modelo);
    }

}
