package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.LoginDuplicadoException;
import br.com.villefortconsulting.exception.SenhaIncorretaException;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.WizardTelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FornecedorService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncionarioService;
import br.com.villefortconsulting.sgfinancas.servicos.LoginAcessoService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.ServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.UsuarioException;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.jrimum.bopepo.BancosSuportados;
import org.primefaces.event.FileUploadEvent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WizardControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ClienteService clienteService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private FuncionarioService funcionarioService;

    @EJB
    private FornecedorService fornecedorService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private ServicoService servicoService;

    @EJB
    private ContaBancariaService contaBancariaService;

    private List<WizardTelaDTO> passos;

    private Integer step;

    private boolean done;

    // Tela 1
    private byte[] tela1Logomarca;

    private Endereco tela1Endereco;

    private String tela1Fone;

    private String tela1Email;

    private UF tela1UfSelecionado;

    private String tela1RamoAtividade;

    // Tela 2
    private String tela2TipoCertificadoDigital;

    private String tela2InscricaoEstadual;

    private String tela2InscricaoMunicipal;

    // Tela 3
    private ContaBancaria tela3ContaBancaria;

    // Tela 4
    private Funcionario tela4Funcionario;

    // Tela 5
    private Cliente tela5Cliente;

    // Tela 7
    private Fornecedor tela7Fornecedor;

    // Tela 9
    private Produto tela9Produto;

    // Tela 10
    private Servico tela10Servico;

    public String getStep() {
        init();
        if (step > passos.size()) {
            return "done";
        }
        return passos.get(step).getStep();
    }

    public void init() {
        if (step != null) {
            return;
        }
        step = 0;
        done = false;
        tela1Logomarca = null;
        tela1Endereco = new Endereco();

        Empresa empresa = empresaService.getEmpresa();

        int i = 0;
        passos = new ArrayList<>();
        passos.add(new WizardTelaDTO("0", "Wizard tela " + ++i + " - Apresentação do wizard", true, () -> false));// Bloqueado o wizard para não sair da primeira tela
        passos.add(new WizardTelaDTO("1", "Wizard tela " + ++i + " - Completar cadastro da empresa 1", true, this::saveTela1));
        if (CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj()).length() == 14) {
            passos.add(new WizardTelaDTO("2", "Wizard tela " + ++i + " - Completar cadastro da empresa 2", false, this::saveTela2));
        }
        passos.add(new WizardTelaDTO("3", "Wizard tela " + ++i + " - Cadastro da conta bancária", true, this::saveTela3));
        passos.add(new WizardTelaDTO("4", "Wizard tela " + ++i + " - Cadastro de funcionário", false, this::saveTela4));
        passos.add(new WizardTelaDTO("5", "Wizard tela " + ++i + " - Cadastro de cliente", false, this::saveTela5));
        passos.add(new WizardTelaDTO("6", "Wizard tela " + ++i + " - Passo a passo 1", false));
        passos.add(new WizardTelaDTO("7", "Wizard tela " + ++i + " - Cadastro de fornecedor", false, this::saveTela7));
        passos.add(new WizardTelaDTO("8", "Wizard tela " + ++i + " - Passo a passo 2", false));
        passos.add(new WizardTelaDTO("9", "Wizard tela " + ++i + " - Cadastro de produtos", false, this::saveTela9));
        passos.add(new WizardTelaDTO("10", "Wizard tela " + ++i + " - Cadastro de serviços", false, this::saveTela10));
        passos.add(new WizardTelaDTO("11", "Wizard tela " + ++i + " - Conclusão do cadastro", true, this::saveFinal));

        tela1Logomarca = empresa.getLogo();
        tela1Endereco = empresa.getEndereco();
        tela1Fone = empresa.getFone();
        tela1Email = empresa.getEmail();
        if (empresa.getEndereco() != null && empresa.getEndereco().getIdCidade() != null) {
            tela1UfSelecionado = empresa.getEndereco().getIdCidade().getIdUF();
        }
        tela1RamoAtividade = empresa.getRamo();
        tela2TipoCertificadoDigital = empresa.getTipoCertificadoDigital();
        tela2InscricaoEstadual = empresa.getInscricaoEstadual();
        tela2InscricaoMunicipal = empresa.getInscricaoMunicipal();
        tela3ContaBancaria = new ContaBancaria();
        tela4Funcionario = new Funcionario();
        tela4Funcionario.setTemHorarioEspecial("N");
        tela5Cliente = new Cliente();
        tela7Fornecedor = new Fornecedor();
        tela9Produto = new Produto();
        tela10Servico = new Servico();
    }

    public String getStepName() {
        return passos.get(step).getNome();
    }

    public String next() {
        if (!done) {
            try {
                if (passos.get(step).getSaveFnc() == null || passos.get(step).getSaveFnc().call()) {
                    step += 1;
                } else {
                    createFacesErrorMessage("Preencha todos os campos obrigatórios e tente novamente.");
                }
            } catch (Exception ex) {
                createFacesErrorMessage(ex.getMessage());
            }
        }
        if (step == passos.size()) {
            done = true;
            return "/restrito/dashboard.xhtml";
        }
        return "index.xhtml";
    }

    public String getLabelNext() {
        if (step + 1 >= passos.size()) {
            return "Finalizar";
        }
        return passos.get(step).getSaveFnc() != null ? "Salvar e avançar" : "Avançar";
    }

    public String getIconNext() {
        return step + 1 < passos.size() ? "fa fa-arrow-right" : null;
    }

    public Boolean getNextBtnRendered() {
        return !done;
    }

    public void previous() {
        if (!done) {
            step -= 1;
        }
    }

    public Boolean getPreviousBtnRendered() {
        return !done && step > 0;
    }

    public Boolean getSkipBtnRendered() {
        return (!passos.get(step).getObrigatorio() && passos.get(step).getSaveFnc() != null) || "dev".equals(getUsuarioLogado().getMenuMode());
    }

    public void skip() {
        if (Boolean.TRUE.equals(getSkipBtnRendered())) {
            step += 1;
        }
    }

    public void buscarEnderecoPorCep() {
        CepDTO cepDTO = cidadeService.getEnderecoPorCep(tela1Endereco.getCep());
        tela1Endereco.setBairro(cepDTO.getBairro());
        tela1Endereco.setEndereco(cepDTO.getEndereco());
        tela1Endereco.setIdCidade(cepDTO.getCidade());
        tela1UfSelecionado = cepDTO.getUf();
    }

    public List<Cidade> getCidades() {
        if (tela1UfSelecionado != null) {
            return cidadeService.listar(tela1UfSelecionado);
        }
        return new LinkedList<>();
    }

    public void setPart(FileUploadEvent event) {
        try {
            tela1Logomarca = IOUtils.toByteArray(event.getFile().getInputstream());
        } catch (IOException ex) {
            Logger.getLogger(WizardControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao processar a logomarca.");
            tela1Logomarca = new byte[]{};
        }
    }

    public String getLogo64() {
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(tela1Logomarca);
    }

    public boolean ehBancoCaixa() {
        if (tela3ContaBancaria.getIdBanco() != null) {
            return BancosSuportados.CAIXA_ECONOMICA_FEDERAL.getCodigoDeCompensacao().equals(tela3ContaBancaria.getIdBanco().getNumero());
        }
        return false;
    }

    public Boolean saveTela1() {
        Empresa empresa = empresaService.getEmpresa();
        empresa.setEndereco(tela1Endereco);
        empresa.setLogo(tela1Logomarca);
        empresa.setFone(tela1Fone);
        empresa.setEmail(tela1Email);
        empresa.setRamo(tela1RamoAtividade);
        empresaService.salvar(empresa);
        return true;
    }

    public Boolean saveTela2() {
        Empresa empresa = empresaService.getEmpresa();
        empresa.setTipoCertificadoDigital(tela2TipoCertificadoDigital);
        empresa.setInscricaoEstadual(tela2InscricaoEstadual);
        empresa.setInscricaoMunicipal(tela2InscricaoMunicipal);
        empresaService.salvar(empresa);
        return true;
    }

    public Boolean saveTela3() {
        tela3ContaBancaria = contaBancariaService.salvar(tela3ContaBancaria);
        return true;
    }

    public Boolean saveTela4() throws SenhaIncorretaException, LoginDuplicadoException, UsuarioException, EmailException {
        tela4Funcionario = funcionarioService.salvarFuncionario(tela4Funcionario);
        return true;
    }

    public Boolean saveTela5() {
        if (tela5Cliente.getCpfCNPJ() == null) {
            tela5Cliente.setCpfCNPJ("");
        }
        tela5Cliente = clienteService.salvar(tela5Cliente);
        return true;
    }

    public Boolean saveTela7() {
        tela7Fornecedor = fornecedorService.salvar(tela7Fornecedor);
        return true;
    }

    public Boolean saveTela9() {
        tela9Produto = produtoService.salvar(tela9Produto);
        return true;
    }

    public Boolean saveTela10() {
        tela10Servico = servicoService.salvar(tela10Servico);
        return true;
    }

    public Boolean saveFinal() {
        // Marcar que o wizard foi finalizado
        return false;
    }

    //################################################################################################################################################
    //################################################################################################################################################
    //################################################################################################################################################
    @EJB
    private LoginAcessoService loginAcessoService;

    private EmpresaCadastroDTO cadastroSimplificado = new EmpresaCadastroDTO();

    public String experimentarGratuitamente() {
        try {
            cadastroSimplificado.setAceitouTermo(true);
            empresaService.cadastrarEmpresa(cadastroSimplificado);
            loginAcessoService.autenticaUsuario(getUsuarioLogado(), "");
            return "/restrito/dashboard.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "";
        }
    }

}
