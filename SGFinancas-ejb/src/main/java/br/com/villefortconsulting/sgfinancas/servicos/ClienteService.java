package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteContato;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteHdDDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.HubDoDesenvolvedorWrapperPessoa;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ImpostosDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ClienteMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ClienteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ClienteRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoCadastro;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.MicroServiceUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.sql.JoinType;
import org.springframework.http.ResponseEntity;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClienteService extends BasicService<Cliente, ClienteRepositorio, ClienteFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private PlanoContaService planoContaPadraoService;

    @EJB
    private ContaService contaService;

    @EJB
    private EmailService emailService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private PerfilService perfilService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @Inject
    private ClienteMapper clienteMapper;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ClienteRepositorio(em, adHocTenant);
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        if (cliente.getCpfCNPJ() == null && cliente.getCpfCNPJ().trim().isEmpty()) {
            cliente.setCpfCNPJ("");
        }
        boolean cpfRepetido = cliente.getAtivo().equals("S") && !cliente.getCpfCNPJ().isEmpty() && listar().stream()
                .anyMatch(aux -> aux.getCpfCNPJ() != null && aux.getCpfCNPJ().equals(cliente.getCpfCNPJ()) && "S".equals(aux.getAtivo()) && !aux.getId().equals(cliente.getId()));
        if (cpfRepetido) {
            throw new CadastroException(cliente.getTipo().equals("PF") ? "Existe um cliente cadastrado para o CPF informado." : "Existe um cliente cadastrado para o CNPJ informado.", null);
        }
        cliente.getClienteContatoList().forEach(cc -> cc.setTenatID(adHocTenant.getTenantID()));
        cliente.getClienteVeiculoList().forEach(cv -> {
            cv.setTenatID(adHocTenant.getTenantID());
            cv.setIdCliente(cliente);
        });
        PlanoConta planoConta;

        if (cliente.getIdPlanoConta() == null) {
            planoConta = planoContaPadraoService.criaPlanoContaCadastroBasico(cliente.getNome(), EnumTipoCadastro.CLIENTE.getTipo());
            cliente.setIdPlanoConta(planoConta);
        } else {
            cliente.getIdPlanoConta().setDescricao(cliente.getNome());
            planoContaPadraoService.alterar(cliente.getIdPlanoConta());
        }

        boolean enviarEmail = false;
        if ("S".equals(parametroSistemaService.getParametro().getCriarUsuarioParaClienteCadastrado()) && cliente.getIdUsuario() == null) {
            String cpfCnpj = CpfCnpjUtil.removerMascaraCnpj(cliente.getCpfCNPJ());
            enviarEmail = true;
            if (StringUtils.isNotEmpty(cpfCnpj)) {
                Usuario user = new Usuario();
                user.setLogin(cpfCnpj);
                user.setNome(cliente.getNome());
                user.setSenha(cpfCnpj);
                user.setEmail(cliente.getEmail());
                user.setContaExpirada("N");
                user.setContaBloqueada("N");
                user.setCadCredenciamento("N");
                user.setIdPerfil(perfilService.getPerfilCliente());
                user.setTelefone(cliente.getTelefoneCelular());
                cliente.setIdUsuario(user);
            }
        }

        if (cliente.getClienteMovimentacaoList() != null) {
            cliente.getClienteMovimentacaoList().forEach(cm -> cm.setTenatID(adHocTenant.getTenantID()));
            Double saldo = cliente.getClienteMovimentacaoList().stream()
                    .mapToDouble(ClienteMovimentacao::getValor).sum();

            cliente.setSaldoAtual(saldo);
        }

        Cliente retorno;
        try {
            retorno = super.salvar(cliente);
            if (enviarEmail) {
                Usuario dest = new Usuario();
                dest.setNome(retorno.getNome());
                dest.setEmail(retorno.getEmail());
                EmailDTO email = new EmailDTO();
                email.getDestinatarios().add(dest);
                email.setAssunto("Seu usuário foi criado");
                email.setTituloMensagem("Criação de conta APP ABAPAV");
                email.setMensagem("Seu usuário do APP ABAPAV foi criado,<br/>Login: " + retorno.getEmail() + "<br/>Senha: " + CpfCnpjUtil.removerMascaraCnpj(retorno.getCpfCNPJ()));
                emailService.enviarEmailMS(email, "S");
            }
        } catch (Exception ignored) {
            retorno = null;
        }
        return retorno;
    }

    @Override
    public void remover(Cliente cliente) {
        //Lista se tem alguma conta(COMPRA, VENDA, CONTRATO GERA UMA CONTA) com aquele cliente.
        List<Conta> listConta = contaService.contasPorCliente(cliente);

        //Caso não tenha, ele remove o item do plano de conta e em seguida o fornecedor.
        if (listConta.isEmpty()) {
            PlanoConta planoConta = planoContaPadraoService.buscar(cliente.getIdPlanoConta().getId());

            if (planoConta != null) {
                planoContaPadraoService.remover(planoConta);
            }

            remover(cliente);
        } else {
            // Caso tenha, lança uma exception.
            throw new CadastroException("Cliente associado a outros cadastros. Favor desassociar o cliente antes de remove-lo.", null);
        }

    }

    public Cliente buscarPorCpfCnpj(String cpfCnpj) {
        return repositorio.buscarPorCpfCnpj(cpfCnpj);
    }

    public List<Cliente> buscarPor(Usuario usuario) {
        return repositorio.buscarPor(usuario);
    }

    public Cliente registraClientePor(Usuario usuario) {

        Cliente cliente = new Cliente();
        cliente.setNome(usuario.getNome());
        cliente.setEmail(usuario.getEmail());
        cliente.setCpfCNPJ("");
        cliente.setTipo("PJ");
        cliente.setIdUsuario(usuario);
        cliente.setTelefoneCelular(usuario.getTelefone());

        return salvar(cliente);

    }

    public Cliente findClienteByNomeAndCreate(String nome) {
        Cliente cliente = repositorio.buscarCliente(nome);
        if (cliente == null) {
            cliente = new Cliente();
            cliente.setAtivo("S");
            cliente.setNome(nome);
            cliente.setTipo("PF");
            cliente.setCpfCNPJ("");
            return salvar(cliente);
        }
        return cliente;
    }

    public List<Cliente> listar(String nome) {
        return repositorio.listar(nome);
    }

    public List<Cliente> listarSeguradoras() {
        return repositorio.listarSeguradores();
    }

    public List<Cliente> listarPessoa(String nome) {
        return repositorio.listarPessoa(nome);
    }

    public List<ClienteContato> listarClienteContatoistarClienteContato() {
        return repositorio.listarClienteContato();
    }

    @Override
    public Criteria getModel(ClienteFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "nome", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "email", filter.getEmail(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "cpfCNPJ", filter.getCpfCnpj(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());
        addEqRestrictionTo(criteria, "seguradora", filter.getSeguradora());

        if (filter.getPlaca() != null && !filter.getPlaca().trim().isEmpty() && StringUtil.validarPlaca(filter.getPlaca())) {
            criteria.createAlias("clienteVeiculoList", "clienteVeiculoList", JoinType.LEFT_OUTER_JOIN);
            addEqRestrictionTo(criteria, "clienteVeiculoList.placa", filter.getPlaca());
        }

        return criteria;
    }

    public List<ClienteContato> listarClienteContato(Cliente cliente) {
        return repositorio.listarClienteContato(cliente);
    }

    public List<Cliente> listarClientePorEmpresa(Empresa empresa) {
        return repositorio.listarClienteAtivoPorEmpresa(empresa);
    }

    public List<Cliente> listarClienteAtivoPorEmpresa(Empresa empresa) {
        return repositorio.listarClienteAtivoPorEmpresa(empresa);
    }

    public ImpostosDTO calcularImpostosPorCliente(Double valor, Cliente cliente) {
        ImpostosDTO impostosDTO = new ImpostosDTO();
        if (cliente != null && valor != null) {
            impostosDTO.setIr(NumeroUtil.calcularValorPorcentual(valor, cliente.getValorIR()));
            impostosDTO.setPis(NumeroUtil.calcularValorPorcentual(valor, cliente.getValorPIS()));
            impostosDTO.setCsll(NumeroUtil.calcularValorPorcentual(valor, cliente.getValorCSLL()));
            impostosDTO.setInss(NumeroUtil.calcularValorPorcentual(valor, cliente.getValorINSS()));
            impostosDTO.setCofins(NumeroUtil.calcularValorPorcentual(valor, cliente.getValorCOFINS()));
            impostosDTO.setIss(NumeroUtil.calcularValorPorcentual(valor, cliente.getValorISS()));
        }

        return impostosDTO;
    }

    public Cliente preencherClienteResumido(String nome, String cpf, String tipo, String email, String telResidencial, String celular, String telefoneComercial, String cep,
            String logradouro, String numero, String complemento, String bairro) {

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpfCNPJ(cpf);
        cliente.setTipo(tipo);
        cliente.setEmail(email);
        cliente.setTelefoneResidencial(telResidencial);
        cliente.setTelefoneCelular(celular);
        cliente.setTelefoneComercial(telefoneComercial);
        cliente.getEndereco().setCep(cep);
        cliente.getEndereco().setEndereco(logradouro);
        cliente.getEndereco().setNumero(numero);
        cliente.getEndereco().setComplemento(complemento);
        cliente.getEndereco().setBairro(bairro);

        return cliente;
    }

    public Cliente preencherDadosPessoaJuridica(Cliente cliente, String razaoSocial, String inscricaoMunicipal, String temInscricaoEstadual, String inscricaoEstadual,
            String indicadorInscricaoEstadual, String optaSimples) {

        cliente.setRazaoSocial(razaoSocial);
        cliente.setInscricaoMunicipal(inscricaoMunicipal);
        cliente.setTemInscricaoEstadual(temInscricaoEstadual);
        cliente.setInscricaoEstadual(inscricaoEstadual);
        cliente.setIndicadorInscricaoEstadual(indicadorInscricaoEstadual);
        cliente.setOptaSimples(optaSimples);

        return cliente;
    }

    public void buscarDadosEmpresaPorCNPJ(Cliente cliente) {
        if (CpfCnpjUtil.validarCNPJ(cliente.getCpfCNPJ())) {
            EmpresaDTO dadosEmpresa = empresaService.buscarDadosEmpresaPorCNPJ(cliente.getCpfCNPJ());

            if (dadosEmpresa.getStatus().equals("OK")) {
                cliente.getEndereco().setComplemento(dadosEmpresa.getComplemento());
                cliente.setRazaoSocial(dadosEmpresa.getNome());
                cliente.setEmail(dadosEmpresa.getEmail());
                cliente.getEndereco().setNumero(dadosEmpresa.getNumero());
                cliente.getEndereco().setCep(dadosEmpresa.getCep());
                cliente.setTelefoneComercial(dadosEmpresa.getTelefone());
                cliente.getEndereco().setIdCidade(cidadeService.buscar(dadosEmpresa.getMunicipio()));
                cliente.getEndereco().setEndereco(dadosEmpresa.getLogradouro());
                cliente.getEndereco().setBairro(dadosEmpresa.getBairro());
            } else {
                throw new NotFoundException("Não foi possível buscar o CNPJ");
            }
        } else {
            throw new ValidationException("CNPJ inválido");
        }
    }

    public void buscarDadosEmpresaPorCpf(Cliente cliente) {
        if (CpfCnpjUtil.validarCPF(cliente.getCpfCNPJ())) {
            ClienteHdDDTO dadosCliente = buscarDadosEmpresaPorCpf(cliente.getCpfCNPJ(), cliente.getDataNascimento());

            if (dadosCliente.getStatus().equals("OK")) {
                cliente.setNome(dadosCliente.getNome());
            } else {
                throw new NotFoundException("Não foi possível buscar o CPF");
            }
        } else {
            throw new ValidationException("CPF inválido");
        }
    }

    public ClienteHdDDTO buscarDadosEmpresaPorCpf(String cpf, Date dataNascimento) {
        try {
            if (cpf == null || cpf.replaceAll("\\D", "").isEmpty()) {
                return null;
            }
            cpf = cpf.replaceAll("\\D", "");
            String data = dataNascimento == null ? "" : DataUtil.dateToString(dataNascimento, "yyyy-MM-dd");
            if (data.isEmpty()) {
                data = "2000-01-01";
            }
            ResponseEntity<String> status = MicroServiceUtil.doGet(MicroServiceUtil.MicroServicos.BUSCA_CPF_CNPJ, "cpf/" + cpf + "/" + data);

            ObjectMapper mapper = new ObjectMapper();

            if (status.getBody().contains("NOK")) {
                ClienteHdDDTO clienteToReturn = new ClienteHdDDTO();
                clienteToReturn.setCpfCnpj(cpf);
                clienteToReturn.setNome("");
                clienteToReturn.setStatus("OK");
                return clienteToReturn;

            } else {
                HubDoDesenvolvedorWrapperPessoa resp = mapper.readValue(status.getBody(), HubDoDesenvolvedorWrapperPessoa.class);
                if (resp == null || resp.getResult() == null) {
                    ClienteHdDDTO clienteToReturn = new ClienteHdDDTO();
                    clienteToReturn.setCpfCnpj(cpf);
                    clienteToReturn.setNome("");
                    clienteToReturn.setStatus("OK");
                    return clienteToReturn;
                }
                resp.getResult().setStatus(resp.getRetorno());
                return resp.getResult();
            }
        } catch (IOException e) {
            return null;
        }
    }

    public Cliente importDto(ClienteCadastroDTO clienteDTO, String tenat) {
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        cliente.setTenatID(tenat);
        if (clienteDTO.getId() != null) {
            Cliente fromBanco = buscar(clienteDTO.getId());
            cliente.setIdPlanoConta(fromBanco.getIdPlanoConta());
            cliente.setIdUsuario(fromBanco.getIdUsuario());
        }

        if (StringUtils.isNotEmpty(cliente.getCpfCNPJ())) {
            String cpfCnpj = cliente.getCpfCNPJ();
            if (StringUtil.removerMascara(cpfCnpj).length() <= 11) {
                String cpf = StringUtil.removerMascara(cpfCnpj);
                if (cpf.length() < 11) {
                    cpf = StringUtil.adicionarCaracterEsquerda(cpf, "0", 11);
                    cliente.setCpfCNPJ(CpfCnpjUtil.mascararCpf(cpf));
                }
                cliente.setTipo("PF");
                if (!CpfCnpjUtil.validarCPF(cpf)) {
                    cliente.setRazaoSocial(null);
                    throw new IllegalArgumentException("CPF inválido!");
                }
            } else {
                cliente.setTipo("PJ");
                if (!CpfCnpjUtil.validarCNPJ(cpfCnpj)) {
                    if (cliente.getRazaoSocial() == null || cliente.getRazaoSocial().equals("NULL")) {
                        cliente.setRazaoSocial(null);
                        cliente.setDataNascimento(null);
                    }
                    throw new IllegalArgumentException("CNPJ inválido!");
                }
            }
        } else {
            cliente.setTipo("PF");
        }
        if (!StringUtils.isEmpty(cliente.getInscricaoEstadual())) {
            cliente.setTemInscricaoEstadual("S");
        } else {
            cliente.setTemInscricaoEstadual("N");
        }
        return salvar(cliente);
    }

    public Cliente findByCpfCnpjNome(String cpfCNPJ, String nome) {
        List<Cliente> clientes = repositorio.findByCpfCnpjNome(cpfCNPJ, nome);
        boolean cpfCnpjENomeIgual = clientes.stream()
                .filter(cliente -> cliente.getCpfCNPJ().equals(cpfCNPJ) && cliente.getNome().equals(nome))
                .count() == 1;
        if (cpfCnpjENomeIgual) {
            return clientes.stream()
                    .filter(cliente -> cliente.getCpfCNPJ().equals(cpfCNPJ) && cliente.getNome().equals(nome))
                    .findAny()
                    .orElse(null);
        }
        boolean cpfCnpjIgual = clientes.stream()
                .filter(cliente -> cliente.getCpfCNPJ().equals(cpfCNPJ))
                .count() == 1;
        if (cpfCnpjIgual) {
            return clientes.stream()
                    .filter(cliente -> cliente.getCpfCNPJ().equals(cpfCNPJ))
                    .findAny()
                    .orElse(null);
        }

        return clientes.stream()
                .filter(cliente -> cliente.getNome().equals(nome))
                .findAny()
                .orElse(null);

    }

    public Cliente findByIdIntegracao(Long idIntegracao) {
        return repositorio.findByIdIntegracao(idIntegracao);
    }

    public List<Cliente> listarClienteComIdIntegracao() {
        return repositorio.listarComIdIntegracao();
    }

}
