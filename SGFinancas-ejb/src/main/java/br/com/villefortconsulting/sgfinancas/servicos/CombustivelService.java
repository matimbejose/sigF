package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.Combustivel;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VeiculoCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CombustivelFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CombustivelRepositorio;
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
public class CombustivelService extends BasicService<Combustivel, CombustivelRepositorio, CombustivelFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new CombustivelRepositorio(em);
    }

    @Override
    public Combustivel salvar(Combustivel combustivel) {
        boolean combustivelCadastrado = listar().stream()
                .anyMatch(cor -> cor.getDescricao().equals(combustivel.getDescricao()));
        if (combustivelCadastrado) {
            throw new IllegalArgumentException("Combustível já cadastrado.");
        }
        return super.salvar(combustivel);
    }

    public Combustivel importDto(VeiculoCadastroDTO cadastro) {
        Combustivel combustivel = new Combustivel();
        combustivel.setDescricao(cadastro.getDescricao());
        return salvar(combustivel);
    }

}
