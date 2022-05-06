package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VeiculoCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ClienteVeiculoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ClienteVeiculoRepositorio;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClienteVeiculoService extends BasicService<ClienteVeiculo, ClienteVeiculoRepositorio, ClienteVeiculoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ClienteService clienteService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new ClienteVeiculoRepositorio(em, adHocTenant);
    }

    public List<ClienteVeiculo> listaVeiculosPor(Cliente cliente) {
        return repositorio.listaVeiculosPor(cliente);
    }

    public List<ClienteVeiculo> listaTodosVeiculosPor(Cliente cliente) {
        return repositorio.listaTodosVeiculosPorCliente(cliente);
    }

    public ClienteVeiculo importDto(VeiculoCadastroDTO cadastro) {
        ClienteVeiculo cv = new ClienteVeiculo();
        cv.setAnoFabricacao(cadastro.getDadosVeiculo().getAnoFabricacao());
        cv.setAnoModelo(cadastro.getDadosVeiculo().getAnoModelo());
        cv.setAtivo("S");
        cv.setCambio(cadastro.getDadosVeiculo().getCambio());
        cv.setChassi(cadastro.getDadosVeiculo().getChassi());
        cv.setIdCliente(clienteService.buscar(cadastro.getDadosVeiculo().getCliente().getId()));
        cv.setIdCombustivel(cadastro.getDadosVeiculo().getCombustivel());
        cv.setIdCorVeiculo(cadastro.getDadosVeiculo().getCorVeiculo());
        cv.setIdModelo(cadastro.getDadosVeiculo().getModelo());
        cv.setNumeroPassageiros(cadastro.getDadosVeiculo().getNumeroPassageiros());
        cv.setPlaca(cadastro.getDadosVeiculo().getPlaca());
        cv.setPortas(cadastro.getDadosVeiculo().getPortas());
        cv.setRenavam(cadastro.getDadosVeiculo().getRenavam());
        cv.setValorProtegido(cadastro.getDadosVeiculo().getValorProtegido());
        cv.setTenatID(cadastro.getDadosVeiculo().getCliente().getTenatID());
        return salvar(cv);
    }

    public List<ClienteVeiculo> listaVeiculosPor(Modelo modelo) {
        return repositorio.listaVeiculosPor(modelo);
    }

}
