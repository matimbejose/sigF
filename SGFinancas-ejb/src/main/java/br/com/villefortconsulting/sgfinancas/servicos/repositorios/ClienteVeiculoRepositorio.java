package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import java.util.List;
import javax.persistence.EntityManager;

public class ClienteVeiculoRepositorio extends BasicRepository<ClienteVeiculo> {

    public ClienteVeiculoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ClienteVeiculoRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

    public List<ClienteVeiculo> listaTodosVeiculosPorCliente(Cliente cliente) {
        return getPureList("select cv from ClienteVeiculo cv where cv.idCliente = ?1", cliente);
    }

    public List<ClienteVeiculo> listaVeiculosPor(Cliente cliente) {
        return getPureList("select cv from ClienteVeiculo cv where cv.idCliente = ?1 and cv.ativo = 'S'", cliente);
    }

    public List<ClienteVeiculo> listaVeiculosPor(Modelo modelo) {
        return getPureList("select cv from ClienteVeiculo cv where cv.idModelo = ?1", modelo);
    }

}
