package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.CorVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VeiculoCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CorVeiculoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CorVeiculoRepositorio;
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
public class CorVeiculoService extends BasicService<CorVeiculo, CorVeiculoRepositorio, CorVeiculoFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new CorVeiculoRepositorio(em);
    }

    @Override
    public CorVeiculo salvar(CorVeiculo corVeiculo) {
        boolean corCadastrada = listar().stream()
                .anyMatch(cor -> cor.getDescricao().equals(corVeiculo.getDescricao()));
        if (corCadastrada) {
            throw new IllegalArgumentException("Cor já cadastrada.");
        }
        return super.salvar(corVeiculo);
    }

    public CorVeiculo importDto(VeiculoCadastroDTO cadastro) {
        CorVeiculo cor = new CorVeiculo();
        cor.setDescricao(cadastro.getDescricao());
        return salvar(cor);
    }

}
