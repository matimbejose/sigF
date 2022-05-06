package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.FornecedorContato;
import br.com.villefortconsulting.sgfinancas.entidades.FornecedorProduto;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FornecedorDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.FornecedorMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FornecedorFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.FornecedorRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoCadastro;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.StringUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FornecedorService extends BasicService<Fornecedor, FornecedorRepositorio, FornecedorFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private PlanoContaService planoContaPadraoService;

    @EJB
    private ContaService contaService;

    @Inject
    FornecedorMapper fornecedorMapper;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new FornecedorRepositorio(em, adHocTenant);
    }

    @Override
    public List<Fornecedor> listar() {
        return repositorio.listar();
    }

    public List<FornecedorContato> aadTenatContato(List<FornecedorContato> lista) {
        lista.forEach(fc -> fc.setTenatID(adHocTenant.getTenantID()));
        return lista;
    }

    @Override
    public Fornecedor salvar(Fornecedor fornecedor) {
        PlanoConta planoConta;
        if (fornecedor.getCpfCnpj() == null || fornecedor.getCpfCnpj().trim().isEmpty()) {
            throw new CadastroException("Informe o CNPJ.", null);
        }
        Fornecedor aux = buscarPorCpfCnpj(fornecedor.getCpfCnpj());
        if (aux != null && !aux.getId().equals(fornecedor.getId())) {
            throw new CadastroException("Fornecedor já cadastrado.", null);
        }

        if (fornecedor.getId() == null) {
            planoConta = planoContaPadraoService.criaPlanoContaCadastroBasico(fornecedor.getRazaoSocial(), EnumTipoCadastro.FORNECEDOR.getTipo());
            fornecedor.setIdPlanoConta(planoConta);
            return adicionar(fornecedor);
        } else {

            if (fornecedor.getIdPlanoConta() != null) {
                fornecedor.getIdPlanoConta().setDescricao(fornecedor.getRazaoSocial());
                planoContaPadraoService.alterar(fornecedor.getIdPlanoConta());
            } else {
                planoConta = planoContaPadraoService.criaPlanoContaCadastroBasico(fornecedor.getRazaoSocial(), EnumTipoCadastro.FORNECEDOR.getTipo());
                fornecedor.setIdPlanoConta(planoConta);
            }

            return alterar(fornecedor);
        }
    }

    @Override
    public void remover(Fornecedor fornecedor) {
        //Lista se tem alguma conta(COMPRA, VENDA, CONTRATO GERA UMA CONTA) com aquele fornecedor.
        List<Conta> listConta = contaService.contasPorFornecedor(fornecedor);

        //Caso não tenha, ele remove o item do plano de conta e em seguida o fornecedor.
        if (listConta.isEmpty()) {
            PlanoConta planoConta = planoContaPadraoService.buscar(fornecedor.getIdPlanoConta().getId());

            if (planoConta != null) {
                planoContaPadraoService.remover(planoConta);
            }

            super.remover(fornecedor);
        } else { // Caso tenha, lança uma exception.
            throw new CadastroException("Fornecedor associado a outros cadastros. Favor desassociar o fornecedor antes de remove-lo.", null);
        }

    }

    public Fornecedor buscarPorCpfCnpj(String cpfCnpj) {
        return repositorio.buscarPorCpfCnpj(cpfCnpj);
    }

    public List<Fornecedor> listar(String descricao) {
        return repositorio.listar(descricao);
    }

    public List<FornecedorContato> listarFornecedorContatoistarFornecedorContato() {
        return repositorio.listarFornecedorContato();
    }

    @Override
    public Criteria getModel(FornecedorFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "razaoSocial", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "tipoPessoa", filter.getTipoPessoa(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "cpfCnpj", filter.getCpfCnpj(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());

        return criteria;
    }

    public List<FornecedorContato> listarFornecedorContato(Fornecedor fornecedor) {
        return repositorio.listarFornecedorContato(fornecedor);
    }

    public List<FornecedorProduto> listarFornecedorProduto() {
        return repositorio.listarFornecedorProduto();
    }

    public List<Fornecedor> listarFornecedor() {
        return repositorio.listarFornecedor();
    }

    public FornecedorProduto buscarFornecedorProduto(int id) {
        return repositorio.buscarFornecedorProduto(id);
    }

    public List<FornecedorProduto> listarFornecedorProduto(Produto produto) {
        return repositorio.listarFornecedorProduto(produto);
    }

    public List<Fornecedor> listarFornecedorPorEmpresa(Empresa empresa) {
        return repositorio.listarFornecedorPorEmpresa(empresa);
    }

    public Fornecedor importDto(FornecedorDTO fornecedorDTO, String tenat) {
        Fornecedor fornecedor = fornecedorMapper.toEntity(fornecedorDTO);
        fornecedor.setAtivo("S");
        if (StringUtils.isNotEmpty(fornecedor.getCpfCnpj())) {
            String cpfCnpj = fornecedor.getCpfCnpj();
            if (StringUtil.removerMascara(cpfCnpj).length() <= 11) {
                String cpf = StringUtil.removerMascara(cpfCnpj);
                if (cpf.length() < 11) {
                    cpf = StringUtil.adicionarCaracterEsquerda(cpf, "0", 11);
                    fornecedor.setCpfCnpj(CpfCnpjUtil.mascararCpf(cpf));
                }
                fornecedor.setTipoPessoa("PF");
                if (!CpfCnpjUtil.validarCPF(cpf)) {
                    throw new CadastroException("O CPF " + cpfCnpj + " é inválido!", null);
                }
            } else {
                fornecedor.setTipoPessoa("PJ");
                if (!CpfCnpjUtil.validarCNPJ(cpfCnpj)) {
                    throw new CadastroException("O CNPJ " + cpfCnpj + " é inválido!", null);
                }
            }
        }
        if (!StringUtils.isEmpty(fornecedor.getInscricaoEstadual())) {
            fornecedor.setUsaInscricaoEstadual("S");
        } else {
            fornecedor.setUsaInscricaoEstadual("N");
        }
        try {
            return salvar(fornecedor);
        } catch (CadastroException ex) {
            if (ex.getMessage().equals("Fornecedor já cadastrado.")) {
                return null;
            }
            throw ex;
        } catch (ConstraintViolationException ex) {
            throw new CadastroException(ex.getMessage(), ex);
        }
    }

    public Fornecedor findByRazaoSocialCpfCnpj(String razaoSocial, String cpfCnpj) {
        return repositorio.findByRazaoSocialCpfCnpj(razaoSocial, cpfCnpj);
    }

    public Fornecedor findByRazaoSocial(String razaoSocial) {
        return repositorio.findByRazaoSocial(razaoSocial);
    }

}
