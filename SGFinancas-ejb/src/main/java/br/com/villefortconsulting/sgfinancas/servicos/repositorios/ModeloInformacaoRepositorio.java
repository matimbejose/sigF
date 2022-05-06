package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import br.com.villefortconsulting.sgfinancas.entidades.ModeloInformacao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVeiculoFipe;
import java.util.List;
import javax.persistence.EntityManager;

public class ModeloInformacaoRepositorio extends BasicRepository<ModeloInformacao> {

    public ModeloInformacaoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<ModeloInformacao> listar(Modelo modelo) {
        return getPureList("select mi from ModeloInformacao mi where mi.idModelo = ?1", modelo);
    }

    public List<ModeloInformacao> listar(EnumTipoVeiculoFipe tipo, Modelo modelo) {
        return getPureList("select mi from ModeloInformacao mi where mi.idModelo = ?1 and mi.tipo = ?2", modelo, tipo.name());
    }

    public ModeloInformacao buscarPorFipeCodigo(Integer fipeCodigo, Integer ano) {
        return getPurePojo("select mi from ModeloInformacao mi where mi.fipeId = ?1 and mi.ano = ?2", fipeCodigo.toString(), ano);
    }

    public ModeloInformacao buscarPorFipeCodigo(String fipeCodigo, Modelo modelo) {
        return getPurePojoTop1("select mi from ModeloInformacao mi where mi.fipeCodigo = ?1 and mi.idModelo = ?2", fipeCodigo, modelo);
    }

}
