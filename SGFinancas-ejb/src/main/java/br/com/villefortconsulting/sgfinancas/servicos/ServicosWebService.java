package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.annotations.Logado;
import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.entity.Erro;
import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.entity.Time;
import br.com.villefortconsulting.exception.AcessoBloqueadoException;
import br.com.villefortconsulting.exception.ContaBloqueadaException;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.exception.LoginDuplicadoException;
import br.com.villefortconsulting.exception.LoginNaoEncontratoException;
import br.com.villefortconsulting.exception.MessageListException;
import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.exception.SenhaIncorretaException;
import br.com.villefortconsulting.exception.UsuarioOuSenhaInvalidaException;
import br.com.villefortconsulting.servicos.rest.request.UsuarioRequest;
import br.com.villefortconsulting.sgfinancas.entidades.Avaria;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Compra;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Estoque;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Formulario;
import br.com.villefortconsulting.sgfinancas.entidades.FormularioResposta;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioAtendimento;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioServico;
import br.com.villefortconsulting.sgfinancas.entidades.Marca;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.entidades.OrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModulo;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroGeral;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitacaoCadastroCliente;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitacaoCadastroClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.TipoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.TokenApp;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoGetnet;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioLeituraTermo;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaFormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AgendamentoCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AtualizaOrcamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AvariaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.BaixaContaParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CadastroMovimentacaoEstoqueDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CategoriaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CidadeDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CompraDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CompraImportacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaBancariaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContratacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.DescricaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.DocumentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaAtualizacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EnvioEmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FluxoCaixaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormaPagamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormularioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormularioRespostaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FornecedorDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FuncionarioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.HorarioDisponivelDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModeloInformacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModuloDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MovimentacaoEstoqueDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.NfsDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.OrdemServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.OsCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.OsRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroSistemaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PermissaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ReagendamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.RequestRetornoMicroServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ResumoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.RetornoAgendamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.RetornoCompraDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.RetornoContratacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SmsDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitacaoCadastroClienteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitacaoCadastroClienteVeiculoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TipoPagamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioAppCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValidacaoNFeDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VeiculoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaFormaPagamentoCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaServicoCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.BancoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.CidadeMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ClienteMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.CompraMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ContaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.EmpresaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.FipeMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.FormaPagamentoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.FornecedorMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.FuncionarioMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ModuloMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.NFSeMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.NFeMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.OrdemServicoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.PagamentoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ParametroMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.PlanoContaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ProdutoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ServicoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.TransportadoraMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.UsuarioMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.VeiculoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.VendaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.VistoriaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.AvariaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.BancoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CategoriaEmpresaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CidadeFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ClienteFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CompraFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaBancariaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaParcelaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EmpresaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EstoqueFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FormaPagamentoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FormularioFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FormularioRespostaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FornecedorFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FuncionarioFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.LoginAcessoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ModuloFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.NotaFiscalServicoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.OrdemDeServicoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PagamentoMensalidadeFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PlanoContaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ProdutoCategoriaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ProdutoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ServicoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TipoPagamentoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TransportadoraFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.VendaFiltro;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.UsuarioException;
import br.com.villefortconsulting.sgfinancas.servicos.nfe.NfeProdutoService;
import br.com.villefortconsulting.sgfinancas.util.EnumCategoriaCNH;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoCompraVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoTransacao;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOS;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusSolicitacaoCadastroCliente;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoAtualizacao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoComposicaoProduto;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoItemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsoSistema;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVeiculoFipe;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.ListUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import br.com.villefortconsulting.util.fitpag.FitPagUtil;
import br.com.villefortconsulting.util.fitpag.response.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServicosWebService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    //<editor-fold defaultstate="collapsed" desc="Mappers">
    @Inject
    transient BancoMapper bancoMapper;

    @Inject
    transient ClienteMapper clienteMapper;

    @Inject
    transient ModuloMapper moduloMapper;

    @Inject
    transient CidadeMapper cidadeMapper;

    @Inject
    transient ContaMapper contaMapper;

    @Inject
    transient EmpresaMapper empresaMapper;

    @Inject
    transient FipeMapper fipeMapper;

    @Inject
    transient FormaPagamentoMapper formaPagamentoMapper;

    @Inject
    transient FuncionarioMapper funcionarioMapper;

    @Inject
    transient FornecedorMapper fornecedorMapper;

    @Inject
    transient PlanoContaMapper planoContaMapper;

    @Inject
    transient ProdutoMapper produtoMapper;

    @Inject
    transient ServicoMapper servicoMapper;

    @Inject
    transient UsuarioMapper usuarioMapper;

    @Inject
    transient VendaMapper vendaMapper;

    @Inject
    transient ParametroMapper parametroMapper;

    @Inject
    transient PagamentoMapper pagamentoMapper;

    @Inject
    transient OrdemServicoMapper ordemServicoMapper;

    @Inject
    transient CompraMapper compraMapper;

    @Inject
    transient NFSeMapper nfsMapper;

    @Inject
    transient NFeMapper nfMapper;

    @Inject
    transient TransportadoraMapper transportadoraMapper;

    @Inject
    transient VeiculoMapper veiculoMapper;

    @Inject
    transient VistoriaMapper vistoriaMapper;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    private AnalisesService analisesService;

    @EJB
    private AvariaService avariaService;

    @EJB
    private FormularioRespostaService formularioRespostaService;

    @EJB
    private EmailService emailService;

    @EJB
    private ModuloService moduloService;

    @EJB
    private FormularioService formularioService;

    @EJB
    private CategoriaEmpresaService categoriaEmpresaService;

    @EJB
    private ClienteVeiculoService clienteVeiculoService;

    @EJB
    private CombustivelService combustivelService;

    @EJB
    private CorVeiculoService corVeiculoService;

    @EJB
    private ModeloService modeloService;

    @EJB
    private FipeService fipeService;

    @EJB
    private DanfeService danfeService;

    @EJB
    private EstoqueService estoqueService;

    @EJB
    protected ExtratoContaCorrenteService extratoContaCorrenteService;

    @EJB
    private LoginAcessoService acessoService;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private PagamentoMensalidadeService pagamentoMensalidadeService;

    @EJB
    private ServicoService servicoService;

    @EJB
    private FuncionarioService funcionarioService;

    @EJB
    private ContaService contaService;

    @EJB
    private PlanoContaService planoContaService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private SolicitacaoCadastroClienteService solicitacaoCadastroClienteService;

    @EJB
    private SolicitacaoCadastroClienteVeiculoService solicitacaoCadastroClienteVeiculoService;

    @EJB
    private FornecedorService fornecedorService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private UnidadeMedidaService unidadeMedidaService;

    @EJB
    private TokenAppService tokenAppService;

    @EJB
    private PermissaoService permissaoService;

    @EJB
    private VendaService vendaService;

    @EJB
    private FormaPagamentoService formaPagamentoService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private OrdemDeServicoService ordemDeServicoService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private DocumentoAnexoService documentoAnexoService;

    @EJB
    private ParametroGeralService parametroGeralService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private BancoService bancoService;

    @EJB
    private TransacaoGetnetService transacaoGetnetService;

    @EJB
    private TransportadoraService transportadoraService;

    @EJB
    private SmsService smsService;

    @EJB
    private TipoPagamentoService tipoPagamentoService;

    @EJB
    private TransacaoGetnetService transacaoService;

    @EJB
    private CompraService compraService;

    @EJB
    private NFSService nfsService;

    @EJB
    private CtissService ctissService;

    @EJB
    private NotaServicoService notaServicoService;

    @EJB
    private NfeProdutoService nfeProdutoService;

    @EJB
    private NFService nfService;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constantes">
    private static final String OK = "\"OK\"";
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Util">
    @AroundInvoke
    public Object interceptOrder(InvocationContext ctx) throws Exception {
        Logado ant = ctx.getMethod().getAnnotation(Logado.class);
        if (ant == null) {
            return ctx.proceed();
        }
        try {
            Usuario logado = getUsuarioLogado();
//            if (usuarioService.precisaAceitarTermo(logado) && !ctx.getMethod().getName().equals("aceitarTermo")) {
//                return gerarErro(Response.Status.UNAUTHORIZED, "É necessário aceitar o termo de uso para usar o SO Finanças.");
//            }
            if (ant.requerEmpresa() && logado.getTenat() == null) {
                return gerarErro(Response.Status.BAD_REQUEST, "Selecione uma empresa para continuar.");
            }
            if (ant.requerDadosCompletos()) {
                String cnpj = getEmpresa().getCnpj();
                if (cnpj == null || cnpj.trim().isEmpty()) {
                    return gerarErro(Response.Status.PRECONDITION_FAILED, "Complete o cadastro da empresa para continuar.");
                }
            }
            if (ant.permissoes().length > 0) {
                final List<String> listaPermissoes = Arrays.asList(ant.permissoes());
                boolean semPermissao = acessoService.getPermissoesUsuario(logado).stream()
                        .noneMatch(permissao -> listaPermissoes.contains(permissao.getDescricao()));
                if (semPermissao) {
                    return gerarErro(Response.Status.FORBIDDEN, "Usuário sem permissão.");
                }
            }
        } catch (NotAuthorizedException ex) {
            return gerarErro(Response.Status.UNAUTHORIZED, "Usuário não logado.");
        }

        return ctx.proceed();
    }

    private Authentication getUserAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new NotAuthorizedException("Bearer");
        } else if (auth.getPrincipal() instanceof String) {
            String[] tk = ((String) auth.getPrincipal()).split(":");
            if (tk.length == 1 && tk[0].trim().isEmpty()) {
                throw new NotAuthorizedException("Bearer");
            }
            Empresa empresa = null;
            if (!tk[1].equals("null")) {
                empresa = empresaService.getEmpresPorTenatID(tk[1]);
            }
            TokenApp token = tokenAppService.buscar(usuarioService.buscar(Integer.parseInt(tk[0])), empresa, tk[2]);
            if (token == null || token.getIdUsuario() == null) {
                throw new NotAuthorizedException("Bearer");
            }
            Usuario usuarioNovo = token.getIdUsuario();
            if (token.getIdEmpresa() != null) {
                usuarioNovo.setTenat(token.getIdEmpresa().getTenatID());
                adHocTenant.setTenantID(usuarioNovo.getTenat());
            }
            usuarioNovo.setUuid(token.getDeviceUuid());

            auth = new UsernamePasswordAuthenticationToken(usuarioNovo, "", null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else if (!(auth.getPrincipal() instanceof Usuario)) {
            throw new NotAuthorizedException("Bearer");
        }
        return auth;
    }

    private Usuario getUsuarioLogado() {
        return (Usuario) getUserAuth().getPrincipal();
    }

    private static Object gerarErro(String... descricao) {
        return gerarErro(Response.Status.INTERNAL_SERVER_ERROR, descricao);
    }

    private static Object gerarErro(Response.Status code, String... descricao) {
        RetornoWs.Erros erros = new RetornoWs.Erros();
        for (String erro : descricao) {
            erros.addErro(new Erro(erro == null ? "" : erro));
        }
        return gerarErro(code, erros.getErro());
    }

    private static Object gerarErro(Response.Status code, List<Erro> erroList) {
        RetornoWs.Erros erros = new RetornoWs.Erros();
        if (!SystemPreferencesUtil.getProp("ambiente", "PRODUCAO").equalsIgnoreCase("PRODUCAO")) {
            erroList.forEach(err -> err.setMotivo("API: " + err.getMotivo()));
        }
        erros.setErro(erroList);
        return Response.status(code).entity(erros).build();
    }

    private String getTenatId() {
        return getUsuarioLogado().getTenat();
    }

    private Empresa getEmpresa() {
        return empresaService.getEmpresPorTenatID(getTenatId());
    }

    private static <T extends BasicFilter<? extends EntityId>> T getFilter(T filtro, MultivaluedMap<String, String> urlInfo) {
        filtro.applyUrlInfo(urlInfo);
        return filtro;
    }

    private Documento setDocumento(Documento documento) throws FileException {
        if (documento == null) {
            return null;
        }
        final Usuario user = getUsuarioLogado();
        final String tenatID = user.getTenat();
        int i = 0;
        documento.setTenatID(tenatID);
        for (DocumentoAnexo doc : documento.getDocumentoAnexoList()) {
            doc.setIdDocumento(documento);
            doc.setIdUsuarioEnvio(user);
            doc.setTenatID(tenatID);
            if (doc.getNomeArquivo() == null) {
                doc.setNomeArquivo("foto_" + (++i) + ".png");
            }
            if (doc.getDataEnvio() == null) {
                doc.setDataEnvio(new Date());
            }
        }
        return documentoService.alterar(documento);
    }

    private Object fromJsonNode(JsonNode jn) throws JsonProcessingException {
        return fromJsonNode(jn, false);
    }

    private Object fromJsonNode(JsonNode jn, boolean full) throws JsonProcessingException {
        if (jn.isArray()) {
            List<Object> aux = new ArrayList<>();
            for (JsonNode jni : jn) {
                aux.add(fromJsonNode(jni, true));
            }
            return aux;
        } else if (jn.isBoolean()) {
            return jn.asBoolean();
        } else if (jn.isNull()) {
            return null;
        } else if (jn.isNumber()) {
            return jn.asLong();
        } else if (jn.isDouble() || jn.isFloat()) {
            return jn.asDouble();
        } else if (jn.isObject()) {
            if (!full && jn.get("id") != null) {
                return jn.get("id").asInt();
            } else if (full) {
                Map<String, Object> aux = new HashMap<>();
                jn.fields()
                        .forEachRemaining(entry -> {
                            try {
                                aux.put(entry.getKey(), fromJsonNode(entry.getValue()));
                            } catch (JsonProcessingException ex) {
                                aux.put(entry.getKey(), null);
                            }
                        });
                return aux;
            }
        }
        return jn.asText();
    }

    private Funcionario logarComIdFuncionario(Integer id) {
        Funcionario employee = funcionarioService.buscar(id);
        logarComTenant(employee.getTenatID());
        return employee;
    }

    private void logarComTenant(String tenant) {
        usuarioService.logarComTenant(tenant);
        adHocTenant.setTenantID(tenant);
    }

    protected Set<ConstraintViolation<EntityId>> getViolations(EntityId entidade) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(entidade);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Util para venda e orçamento">
    private VendaRestDTO vendaToDto(Venda obj) {
        return vendaToDto(obj, null);
    }

    private VendaRestDTO vendaToDto(Venda obj, MultivaluedMap<String, String> urlInfo) {
        VendaRestDTO aux = vendaMapper.toDTOFull(obj);
        aux.setTipo(EnumTipoVenda.retornaEnumSelecionado(obj.getStatusNegociacao()));
        aux.setSituacaoDescricao(EnumSituacaoCompraVenda.retornaEnumSelecionado(aux.getSituacaoSigla()).getDescricao());
        if (obj.getIdConta() != null) {
            aux.setSituacaoPagamento(EnumSituacaoConta.retornaDescricaoPorSituacao(obj.getIdConta().getSituacao()));
        }
        if (obj.getIdUsuarioVendedor().getIdFuncionario() != null) {
            aux.getUsuarioVendedor().setNome(obj.getIdUsuarioVendedor().getIdFuncionario().getNome());
            aux.getUsuarioVendedor().setCargo(obj.getIdUsuarioVendedor().getIdFuncionario().getCargo());
        }
        if (urlInfo != null && urlInfo.get("no-photo") != null) {
            aux.getProdutos().forEach(prod -> prod.getProduto().setFotos(null));
            aux.getServicos().forEach(serv -> serv.getServico().setFotos(null));
            aux.getUsuarioVendedor().setFoto64(null);
        } else if (obj.getIdUsuarioVendedor().getIdFuncionario() != null) {
            aux.getUsuarioVendedor().setFoto64(obj.getIdUsuarioVendedor().getIdFuncionario().getFoto64());
        }
        return aux;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Usuários">
    public Object logarUsuario(UsuarioRequest request) {
        try {
            Usuario usuario = acessoService.autenticarUsuario(request.getLogin(), StringUtil.criptografarMD5(request.getSenha()), true, null, true, true);
            UsuarioDTO usuarioDTO = usuarioService.converterDTO(usuario);
            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, "", null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            usuarioDTO.setToken(tokenAppService.generateToken(usuario, null, request.getDeviceId()));
            List<EmpresaDTO> listaEmpresas = (List<EmpresaDTO>) listarEmpresas();

            if (listaEmpresas.size() == 1) {
                StringBuilder name = new StringBuilder();
                for (String s : tokenAppService.getTokenData(usuarioDTO.getToken())) {
                    name.append(":").append(s);
                }
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(name.substring(1), "", new ArrayList<>()));
                return updateToken("" + listaEmpresas.get(0).getId());
            }

            usuarioDTO.setAceitouTermo(!usuarioService.precisaAceitarTermo(usuario));
            usuarioDTO.setEmpresaSelecionada(false);
            return usuarioDTO;
        } catch (LoginNaoEncontratoException | UsuarioOuSenhaInvalidaException | AcessoBloqueadoException | ContaBloqueadaException | CadastroException ex) {
            return gerarErro(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possivel realizar a autenticação");
        }
    }

    @Logado(requerEmpresa = false)
    public Object updateToken(String idEmpresa) {
        try {
            Usuario user = getUsuarioLogado();
            Empresa empresa = empresaService.getEmpresPorTenatID(idEmpresa);
            acessoService.autenticarUsuario(user.getLogin(), user.getSenha(), true, empresa.getTenatID(), true, true);
            String token = tokenAppService.updateToken(user, empresa);
            UsuarioDTO usuarioDTO = usuarioService.converterDTO(user);
            usuarioDTO.setToken(token);
            usuarioDTO.setEmpresaSelecionada(true);
            usuarioDTO.setDadosEmpresa(empresaMapper.toDTO(empresa));
            usuarioDTO.setAceitouTermo(!usuarioService.precisaAceitarTermo(user));
            if (empresa != null) {
                usuarioDTO.getDadosEmpresa().setDataValidadePagamento(pagamentoMensalidadeService.getUltimoPagamentoPor(empresa).getDataValidade());
            }
            return usuarioDTO;
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro(ex.getMessage());
        }
    }

    @Logado(requerEmpresa = false)
    public Object buscarUsuarioLogado() {
        return usuarioService.converterDTO(getUsuarioLogado());
    }

    @Logado
    public Object aceitarTermo(String ip) {
        UsuarioLeituraTermo ult = new UsuarioLeituraTermo();
        ult.setIdUsuario(getUsuarioLogado());
        ult.setVersaoTermo(Integer.parseInt(SystemPreferencesUtil.getPropOrThrow("defaults.versaoTermoUso", () -> new IllegalStateException("Termo de uso não configurado"))));
        ult.setDataAceite(new Date());
        ult.setIp(ip);
        usuarioService.salvarLeituraTermo(ult);
        return OK;
    }

    @Logado
    public Object recusarTermo() {
        if (!SystemPreferencesUtil.getProp("ambiente", "producao").equalsIgnoreCase("producao")) {
            usuarioService.removerLeituraTermo(getUsuarioLogado());
            return OK;
        }
        return "";
    }

    @Logado(requerEmpresa = false)
    public Object deleteToken() {
        Usuario user = getUsuarioLogado();
        tokenAppService.remover(tokenAppService.buscar(user, user.getUuid()));
        return OK;
    }

    @Logado
    public Object listarPermissoes() {
        return permissaoService.getPermissoes(getUsuarioLogado().getIdPerfil()).stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object buscarPermissao(String nome) {
        List<PermissaoDTO> list = permissaoService.getPermissoes(getUsuarioLogado().getIdPerfil()).stream()
                .filter(permissao -> permissao.getDescricao().equals(nome))
                .map(permissao -> {
                    PermissaoDTO permissaoDTO = new PermissaoDTO();

                    permissaoDTO.setId(permissao.getId());
                    permissaoDTO.setNome(permissao.getDescricao());
                    permissaoDTO.setDescricao(permissao.getDescricaoDetalhada());
                    permissaoDTO.setPossuiPermissao(true);

                    return permissaoDTO;
                })
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            Permissao permissao = permissaoService.buscarPorNome(nome);
            if (permissao == null) {
                throw new NotFoundException("Permissão não encontrada.");
            }
            PermissaoDTO permissaoDTO = new PermissaoDTO();
            permissaoDTO.setId(permissao.getId());
            permissaoDTO.setNome(permissao.getDescricao());
            permissaoDTO.setDescricao(permissao.getDescricaoDetalhada());
            permissaoDTO.setPossuiPermissao(false);
            list.add(permissaoDTO);
        }
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Object recuperarSenha(String login) {
        EnvioEmailDTO envio = new EnvioEmailDTO();
        try {
            Usuario user = usuarioService.getUserByLogin(login);
            if (user == null) {
                throw new NotFoundException("Não foi possível encontrar o login informado.");
            }
            if (user.getEmail() == null) {
                throw new NotAcceptableException("O usuário não possui email.");
            }
            envio.setEmail(user.getEmail());
            usuarioService.enviarSenha(user);
            envio.setEnviado(true);
            envio.setMensagem("Senha alterada com sucesso! Email de envio: " + user.getEmail());
        } catch (EmailException ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            envio.setEnviado(false);
            envio.setMensagem(ex.getMessage());
        } catch (NotAcceptableException | NotFoundException ex) {
            envio.setEnviado(false);
            envio.setMensagem(ex.getMessage());
        }
        return envio;
    }

    @Logado(requerEmpresa = false)
    public Object alterarSenha(String senha) {
        Usuario usuario = getUsuarioLogado();
        Empresa empresa = usuario.getTenat() != null ? empresaService.buscar(Integer.parseInt(usuario.getTenat())) : null;
        TokenApp ta = tokenAppService.buscar(usuario, empresa, usuario.getUuid());
        long agora = new Date().getTime();
        long token = ta.getData().getTime();
        long diferenca = agora - token;
        if (diferenca <= 1000 * 60 * 2) {// Dois minutos
            try {
                usuarioService.validarSenha(senha);
                usuario.setSenha(StringUtil.criptografarMD5(senha));
                usuarioService.alterar(usuario);
                return "Senha alterada com sucesso";
            } catch (SenhaIncorretaException ex) {
                return gerarErro(ex.getMessage());
            }
        }
        return gerarErro(Status.REQUEST_TIMEOUT, "É necessário um login recente para alterar a senha.");
    }

    @Logado(requerEmpresa = false)
    public Object cadastrarUsuario(UsuarioCadastroDTO usuarioDTO) {
        Usuario fromDB = getUsuarioLogado();
        Usuario fromDTO = usuarioMapper.toEntity(usuarioDTO);
        if (usuarioDTO.getFoto() != null) {
            fromDTO.setFoto(Base64.getDecoder().decode(usuarioDTO.getFoto()));
        }
        if (fromDTO.getIdFuncionario().getId() == null) {
            fromDTO.setIdFuncionario(null);
        }
        if (fromDTO.getIdPerfil() != null && fromDTO.getIdPerfil().getId() == null) {
            fromDTO.setIdPerfil(null);
        }
        fromDTO.setUsuarioPermissaoList(null);
        fromDTO.setUsuarioGrupoEmpresaList(null);
        fromDTO.setUsuarioAcessoRapidoList(null);
        if (fromDB != null) {
            fromDB.merge(fromDTO);
            usuarioService.alterar(fromDB);
            return fromDB.getId();
        } else {
            try {
                usuarioService.salvarUsuario(fromDTO, usuarioDTO.getSenha(), getEmpresa());
            } catch (SenhaIncorretaException | LoginDuplicadoException ex) {
                return gerarErro(Status.BAD_REQUEST, ex.getMessage());
            }
            return fromDTO.getId();
        }
    }

    @Logado
    public Object removerUsuario(String id) {
        Usuario user = usuarioService.buscar(Integer.parseInt(id));
        final String tenat = getUsuarioLogado().getTenat();
        boolean estaNaEmpresaSelecionada = acessoService.getTenats(user.getLogin()).stream()
                .anyMatch(acesso -> acesso.getTenatID().equals(tenat) && user.getTenat().equals(tenat));
        if (estaNaEmpresaSelecionada) {
            user.setContaBloqueada("S");
            usuarioService.alterar(user);
            return null;
        }
        throw new NotFoundException();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Produtos">
    @Logado
    public Object listarProduto(MultivaluedMap<String, String> urlInfo) {
        return produtoService.getListaFiltrada(getFilter(new ProdutoFiltro(), urlInfo)).stream()
                .map(produtoMapper::toDTO)
                .map(prod -> {
                    if (urlInfo.getFirst("no-photo") != null) {
                        prod.setFotos(null);
                    }
                    return prod;
                })
                .collect(Collectors.toList());
    }

    @Logado
    public Object consultarProduto(Integer id) {
        Produto prod = produtoService.buscar(id);
        if (prod == null || !prod.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException("Não foi encontrado nenhum produto com o ID " + id);
        }
        return produtoMapper.toDTO(prod);
    }

    @Logado
    public Object consultarProduto(String codigoDeBarras) {
        Produto prod = produtoService.buscarPorCodigoBarras(codigoDeBarras);
        if (prod == null || !prod.getTenatID().equals(getUsuarioLogado().getTenat())) {
            return new ProdutoDTO();
        }
        return produtoMapper.toDTO(prod);
    }

    @Logado
    public Object cadastrarProduto(ProdutoDTO prodDTO) {
        Produto produto = produtoMapper.toEntity(prodDTO, getUsuarioLogado());
        if (produto.getAtivo() == null || !produto.getAtivo().equals("N")) {
            produto.setAtivo("S");
        }

        if (prodDTO.getCategoria() != null) {
            produto.setIdProdutoCategoria(produtoService.buscarCategoria(prodDTO.getCategoria()));
        }
        if (prodDTO.getUnidadeDeMedida() != null) {
            produto.setIdUnidadeMedida(unidadeMedidaService.buscar(prodDTO.getUnidadeDeMedida()));
        } else {
            produto.setIdUnidadeMedida(unidadeMedidaService.buscar("UNIDADE"));
        }

        String codBar = produto.getCodigoBarra();
        if (!StringUtils.isBlank(codBar) && !NumeroUtil.isCodigoBarraEANValido(codBar) && !codBar.equals("SEMGTIN")) {
            return gerarErro("O código de barras informado não é válido! Favor informar com o formato EAN8 ou EAN13.");
        }

        if (null == produto.getComposto()) {
            produto.setComposto(EnumTipoComposicaoProduto.SEM_COMPOSICAO.getTipo());
        }
        if (prodDTO.getFotos() != null) {
            Documento doc;
            if (prodDTO.getFotos().getId() != null) {
                doc = documentoService.buscar(prodDTO.getFotos().getId());
            } else {
                doc = documentoService.criarDocumento(getUsuarioLogado(), "Fotos do produto");
            }
            produto.setIdDocumento(doc);
            documentoAnexoService.persistirAnexoDTO(doc, getUsuarioLogado(), prodDTO.getFotos().getListaAnexo());
        }
        try {
            return produtoService.salvarProduto(produto).getId();
        } catch (CadastroException ex) {
            return gerarErro(ex.getMessage());
        }
    }

    @Logado
    public Object removerProduto(String id) {
        Produto prod = produtoService.buscar(Integer.parseInt(id));
        prod.setAtivo("N");
        produtoService.salvarProduto(prod);
        return OK;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serviços">
    @Logado
    public Object listarServico(MultivaluedMap<String, String> urlInfo) {
        return servicoService.getListaFiltrada(getFilter(new ServicoFiltro(), urlInfo)).stream()
                .map(servicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object buscarServico(String id) {
        Servico serv = servicoService.buscar(Integer.parseInt(id));
        if (serv == null || !serv.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException("Não foi encontrado nenhum serviço com o ID " + id);
        }
        return servicoMapper.toDTO(serv);
    }

    @Logado
    public Object consultarServico(String nome) {
        Servico serv = servicoService.buscarServico("%" + nome + "%");
        if (serv == null || !serv.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException();
        }
        return servicoMapper.toDTO(serv);
    }

    @Logado
    public Object cadastrarServico(ServicoDTO servico) {
        Servico service = servicoMapper.toEntity(servico);
        service.setTenatID(getUsuarioLogado().getTenat());

        if (servico.getPlanoDeContas() != null) {
            service.setIdPlanoConta(planoContaService.buscar(servico.getPlanoDeContas()));
            if (!service.getIdPlanoConta().getTenatID().equals(service.getTenatID())) {
                return gerarErro("O plano de contas selecionado não está cadastrado para esta empresa.");
            }
        }
        if (servico.getCategoria() != null) {
            service.setIdProdutoCategoria(produtoService.buscarCategoria(servico.getCategoria()));
            if (!service.getIdProdutoCategoria().getTenatID().equals(service.getTenatID())) {
                return gerarErro("A categoria selecionada não está cadastrada para esta empresa.");
            }
        }
        try {
            service.setIdDocumento(setDocumento(service.getIdDocumento()));
        } catch (FileException ex) {
            return gerarErro("Não foi possível processar um ou mais arquivos enviados.");
        }
        try {
            service = servicoService.salvar(service);
            servico.setId(service.getId());
            return servicoMapper.toDTO(service);
        } catch (CadastroException ex) {
            return gerarErro(ex.getMessage());
        }
    }

    @Logado
    public Object removerServico(String id) {
        Servico serv = servicoService.buscar(Integer.parseInt(id));
        serv.setAtivo("N");
        servicoService.salvar(serv);
        return OK;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Contas">
    @Logado
    public Object getResumoConta(MultivaluedMap<String, String> urlInfo) {
        ContaParcelaFiltro filtro = getFilter(new ContaParcelaFiltro(), urlInfo);
        if (filtro.getDataInicio() == null) {
            return gerarErro("Informe a data inícial");
        }
        if (filtro.getDataFim() == null) {
            return gerarErro("Informe a data final");
        }
        ResumoContaDTO resumo = new ResumoContaDTO();
        resumo.setPagar(contaService.buscarEstatistica(EnumTipoTransacao.PAGAR, filtro));
        resumo.setReceber(contaService.buscarEstatistica(EnumTipoTransacao.RECEBER, filtro));
        return resumo;
    }

    @Logado
    public Object getResumoIndex(String tipo, MultivaluedMap<String, String> urlInfo) {
        ContaParcelaFiltro filtro = getFilter(new ContaParcelaFiltro(), urlInfo);
        if (filtro.getDataInicio() == null) {
            return gerarErro("Informe a data inícial");
        }
        if (filtro.getDataFim() == null) {
            return gerarErro("Informe a data final");
        }
        if (!"pago".equals(tipo) && !"pagar".equals(tipo)) {
            return gerarErro("O tipo deve ser \"pago\" ou \"pagar\".");
        }
        return contaService.getTimeline(filtro.getDataInicio(), filtro.getDataFim(), "pago".equals(tipo));
    }

    @Logado
    public Object listarConta(MultivaluedMap<String, String> urlInfo) {
        if (urlInfo.getFirst("tipoTransacao") == null) {
            return gerarErro(Response.Status.BAD_REQUEST, "Informe o tipo da transação");
        }

        return contaService.getListaParcelaFiltrada(getFilter(new ContaParcelaFiltro(), urlInfo)).stream()
                .map(contaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object cancelarConta(Integer id) {
        return gerarErro("Função desabilidata no momento. ID: " + id);
    }

    @Logado
    public Object cancelarContaParcela(Integer id) {
        try {
            contaService.cancelarContaParcela(contaService.buscarContaParcela(id), new ArrayList<>());
            return OK;
        } catch (Exception ex) {
            return gerarErro("Não foi possível cancelar a parcela.");
        }
    }

    @Logado
    public Object prepararBaixaContaParcela(Integer id) {
        ContaParcela cp = contaService.buscarContaParcela(id);
        EnumSituacaoConta situaccao = EnumSituacaoConta.retornaEnumSelecionado(cp.getSituacao());
        if (EnumSituacaoConta.CANCELADO == situaccao || EnumSituacaoConta.INTERROMPIDO == situaccao) {
            return gerarErro("Não é possível baixar uma conta cancelada ou excluída.");
        }
        contaService.preencherTotalMaisTributosMaisRestanteParcela(cp, null);
        return contaMapper.toDTO(cp);
    }

    @Logado
    public Object baixarContaParcela(Integer id, BaixaContaParcelaDTO dados) {
        Conta conta = contaService.buscarContaPorIdDaParcela(id);
        ContaParcela cp = contaService.listarContaParcela(conta).stream()
                .filter(parcela -> parcela.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Parcela inexistente."));
        EnumSituacaoConta situaccao = EnumSituacaoConta.retornaEnumSelecionado(cp.getSituacao());
        if (EnumSituacaoConta.CANCELADO == situaccao || EnumSituacaoConta.INTERROMPIDO == situaccao) {
            return gerarErro("Não é possível baixar uma conta cancelada ou excluída.");
        }

        contaService.preencherTributosParcela(cp);

        cp.setPagamentoParcial(dados.isPagamentoIntegral() ? "N" : "S");
        cp.setSituacao(dados.isPagamentoIntegral() ? EnumSituacaoConta.PAGA.getSituacao() : EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
        cp.setIdFormaPagamento(formaPagamentoService.buscar(dados.getIdFormaPagamento()));
        if (dados.getContaBancaria() != null) {
            cp.setIdContaBancaria(contaBancariaService.buscarContaBancariaById(dados.getContaBancaria()));
        }
        cp.setDataPagamento(dados.getDataPagamento());
        cp.setJuros(dados.getValorJuros());
        cp.setMulta(dados.getValorMulta());
        cp.setEncargos(dados.getValorEncargos());
        cp.setValorIR(dados.getValorIR());
        cp.setValorPIS(dados.getValorPIS());
        cp.setValorCSLL(dados.getValorCSLL());
        cp.setValorINSS(dados.getValorINSS());
        cp.setValorCOFINS(dados.getValorCOFINS());
        cp.setValorISS(dados.getValorISS());
        cp.setValorPago(dados.getValorPagamento());
        cp.setOutrosCustos(dados.getValorOutrosCustos());
        cp.setDesconto(dados.getValorDescontos());
        cp.setNumNf(dados.getNumeroDocumento());

        try {
            contaService.verificarPagamentoParcelas(conta.getContaParcelaList());
            contaService.salvarConta(conta, null, null);
            return "\"Parcela baixada com sucesso!\"";
        } catch (CadastroException | ContaException ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível baixar a parcela.");
        }
    }

    @Logado
    public Object cancelarBaixaContaParcela(Integer id) {
        try {
            Conta conta = contaService.buscarContaParcela(id).getIdConta();
            List<ContaParcela> parcelas = contaService.listarContaParcela(conta).stream()
                    .filter(cp -> cp.getId().equals(id))
                    .map(cp -> {
                        cp.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
                        cp.setCanceladoAgora(true);
                        return cp;
                    })
                    .collect(Collectors.toList());
            conta.getContaParcelaList().clear();
            conta.getContaParcelaList().addAll(contaService.verificarPagamentoParcelas(parcelas));
            contaService.salvarConta(conta, null, null);
            extratoContaCorrenteService.verificarAtualizarExtratoParcelas(conta.getContaParcelaList());
            return "\"Baixa cancelada com sucesso!\"";
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }

    @Logado
    public Object baixarArquivoContaParcela(Integer id) {
        ContaParcela cp = contaService.buscarContaParcela(id);
        DocumentoAnexo anexo = documentoAnexoService.buscarUltimoAnexoDocumento(cp.getIdConta().getIdDocumento());
        return Base64.getEncoder().encodeToString(anexo.readFromFile());
    }

    @Logado
    public Object lancarParcela(ContaCadastroRestDTO dados) {
        try {
            Conta contaSelecionada = contaMapper.toEntity(dados);
            contaSelecionada.setTipoRepeticaoParcelas("API");
            if (contaSelecionada.getNumeroParcelas() == null) {
                if (dados.getDetalheParcela() != null) {
                    contaSelecionada.setNumeroParcelas(dados.getDetalheParcela().size());
                }
                if (contaSelecionada.getNumeroParcelas() == null || contaSelecionada.getNumeroParcelas() == 0) {
                    contaSelecionada.setNumeroParcelas(1);
                }
            }
            contaSelecionada.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
            if (dados.isInformarRecebimento()) {
                contaSelecionada.setSituacao(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
                if (dados.getDadosParcela().isPagamentoIntegral()) {
                    contaSelecionada.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
                }
            }
            ParametroSistema ps = parametroSistemaService.getParametro();
            if (contaSelecionada.getIdPlanoConta() == null || contaSelecionada.getIdPlanoConta().getId() == null) {
                if (ps.getAppPlanoContaPadrao() == null) {
                    return gerarErro("O plano de conta padrão não foi definido no sistema. Não será possível salvar a conta.");
                }
                contaSelecionada.setIdPlanoConta(ps.getAppPlanoContaPadrao());
            } else {
                contaSelecionada.setIdPlanoConta(planoContaService.buscar(contaSelecionada.getIdPlanoConta().getId()));
            }
            if (contaSelecionada.getIdContaBancaria() == null || contaSelecionada.getIdContaBancaria().getId() == null) {
                if (ps.getAppContaBancariaPadrao() == null) {
                    return gerarErro("A conta bancária padrão não foi definida no sistema. Não será possível salvar a conta.");
                }
                contaSelecionada.setIdContaBancaria(ps.getAppContaBancariaPadrao());
            } else {
                contaSelecionada.setIdContaBancaria(contaBancariaService.buscar(contaSelecionada.getIdContaBancaria().getId()));
            }
            processaAlteracaoParcela(contaSelecionada, dados);
            contaSelecionada = contaService.salvarConta(contaSelecionada, dados.getDadosParcela().getDataPagamento(), dados.getValorPago());
            if (dados.getId() == null && dados.getDadosParcela().getIdFormaPagamento() != null) {
                final FormaPagamento fp = formaPagamentoService.buscar(dados.getDadosParcela().getIdFormaPagamento());
                contaSelecionada.getContaParcelaList().stream()
                        .forEach(cp -> cp.setIdFormaPagamento(fp));
            }
            return contaMapper.toDTO(contaSelecionada);
        } catch (ContaException ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro(ex.getMessage());
        }
    }

    private void processaAlteracaoParcela(Conta contaSelecionada, ContaCadastroRestDTO dados) {
        if (contaSelecionada.getId() == null) {
            return;
        }
        Conta contaBd = contaService.buscar(contaSelecionada.getId());
        contaSelecionada.setValor(contaBd.getValor());
        contaSelecionada.setValorTotal(contaBd.getValorTotal());
        contaSelecionada.setNumeroParcelas(contaBd.getNumeroParcelas());
        if (dados.getIdPlanoConta() == null) {
            contaSelecionada.setIdPlanoConta(contaBd.getIdPlanoConta());
        }
        if (dados.getIdContaBancaria() == null) {
            contaSelecionada.setIdContaBancaria(contaBd.getIdContaBancaria());
        }
        contaSelecionada.setDataPagamento(dados.getDadosParcela().getDataPagamento());
        contaSelecionada.setDataVencimento(dados.getDataVencimento());
        List<ContaParcela> parcelas = contaService.listarContaParcela(contaSelecionada);
        contaSelecionada.setContaParcelaList(parcelas);
        ContaParcela cpSelecionada = contaSelecionada.getContaParcelaList().stream()
                .filter(parcela -> parcela.getId().equals(dados.getIdParcela()))
                .findFirst()
                .orElseThrow(NotFoundException::new);
        cpSelecionada.setValor(dados.getDadosParcela().getValor());
        cpSelecionada.setValorCOFINS(dados.getDadosParcela().getValorCOFINS());
        cpSelecionada.setValorCSLL(dados.getDadosParcela().getValorCSLL());
        cpSelecionada.setValorINSS(dados.getDadosParcela().getValorINSS());
        cpSelecionada.setValorIR(dados.getDadosParcela().getValorIR());
        cpSelecionada.setValorISS(dados.getDadosParcela().getValorISS());
        cpSelecionada.setValorPIS(dados.getDadosParcela().getValorPIS());
        cpSelecionada.setValorTotal(dados.getDadosParcela().getValor());
        cpSelecionada.setDesconto(dados.getDadosParcela().getValorDescontos());
        cpSelecionada.setDataPagamento(dados.getDadosParcela().getDataPagamento());
        cpSelecionada.setEncargos(dados.getDadosParcela().getValorEncargos());
        cpSelecionada.setJuros(dados.getDadosParcela().getValorJuros());
        cpSelecionada.setMulta(dados.getDadosParcela().getValorMulta());

        if (dados.getDadosParcela().getIdFormaPagamento() != null) {
            FormaPagamento fp = formaPagamentoService.buscar(dados.getDadosParcela().getIdFormaPagamento());
            cpSelecionada.setIdFormaPagamento(fp);
        }

        Conta valores = new Conta();
        contaSelecionada.getContaParcelaList().stream()
                .forEach(parcela -> {
                    if (parcela.getId().equals(dados.getIdParcela())) {
                        parcela.setDataVencimento(dados.getDataVencimento());
                    }
                    valores.setValor(NumeroUtil.somar(valores.getValor(), parcela.getValor()));
                    valores.setValorCOFINS(NumeroUtil.somar(valores.getValorCOFINS(), parcela.getValorCOFINS()));
                    valores.setValorCSLL(NumeroUtil.somar(valores.getValorCSLL(), parcela.getValorCSLL()));
                    valores.setValorINSS(NumeroUtil.somar(valores.getValorINSS(), parcela.getValorINSS()));
                    valores.setValorIR(NumeroUtil.somar(valores.getValorIR(), parcela.getValorIR()));
                    valores.setValorISS(NumeroUtil.somar(valores.getValorISS(), parcela.getValorISS()));
                    valores.setValorPIS(NumeroUtil.somar(valores.getValorPIS(), parcela.getValorPIS()));
                    valores.setValorPago(NumeroUtil.somar(valores.getValorPago(), parcela.getValorPago()));
                    valores.setValorTotal(NumeroUtil.somar(valores.getValorTotal(), parcela.getValorTotal()));
                });
        contaSelecionada.setValor(valores.getValor());
        contaSelecionada.setValorCOFINS(valores.getValorCOFINS());
        contaSelecionada.setValorCSLL(valores.getValorCSLL());
        contaSelecionada.setValorINSS(valores.getValorINSS());
        contaSelecionada.setValorIR(valores.getValorIR());
        contaSelecionada.setValorISS(valores.getValorISS());
        contaSelecionada.setValorPIS(valores.getValorPIS());
        contaSelecionada.setValorPago(valores.getValorPago());
        contaSelecionada.setValorTotal(valores.getValorTotal());
    }

    @Logado
    public Object listarPlanoConta(MultivaluedMap<String, String> urlInfo) {
        return planoContaService.getListaFiltrada(getFilter(new PlanoContaFiltro(), urlInfo)).stream()
                .map(planoContaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object buscarPlanoConta(Integer id) {
        List<PlanoContaDTO> planosConta = new ArrayList<>();
        planosConta.add(planoContaMapper.toDTO(planoContaService.buscar(id)));
        return planosConta;
    }

    @Logado
    public Object getFluxoCaixa(Integer ano) {
        if (ano == null) {
            return gerarErro("Informe o ano");
        }
        Date dataInicial = DataUtil.converterStringParaDate("01/01/" + ano);
        Date dataFinal = DataUtil.converterStringParaDate("31/12/" + ano);

        List<FluxoCaixaDTO> listaFluxoCaixa = analisesService.obterFluxoCaixa(dataInicial, dataFinal, false, null);
        return analisesService.getFluxoCaixa(ano, listaFluxoCaixa, null);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Categorias">
    @Logado
    public Object listarCategoria(MultivaluedMap<String, String> urlInfo) {
        return produtoService.getListaFiltrada(getFilter(new ProdutoCategoriaFiltro(), urlInfo)).stream()
                .map(cat -> new CategoriaDTO(cat.getId(), cat.getDescricao()))
                .collect(Collectors.toList());
    }

    @Logado
    public Object consultarCategoria(String id) {
        List<CategoriaDTO> categorias = new ArrayList<>();
        ProdutoCategoria cat = produtoService.buscarCategoria(Integer.parseInt(id));
        if (getTenatId().equals(cat.getTenatID())) {
            categorias.add(new CategoriaDTO(cat.getId(), cat.getDescricao()));
        }
        return categorias;
    }

    @Logado
    public Object cadastrarCategoria(CategoriaDTO categoria) {
        ProdutoCategoria fromDB = null;
        ProdutoCategoria fromDTO;
        fromDTO = produtoMapper.toEntity(categoria);
        if (fromDTO.getId() != null) {
            fromDB = produtoService.buscarCategoria(fromDTO.getId());
        }
        if (fromDTO.getListProdutoCategoriaSubcategoria() == null) {
            fromDTO.setListProdutoCategoriaSubcategoria(new ArrayList<>());
        }
        try {
            if (fromDB != null) {
                if (fromDB.getAtivo().equals("N")) {
                    return gerarErro("Categoria inativa.");
                }
                fromDB.merge(fromDTO);
                produtoService.salvarCategoria(fromDB);
                return fromDB.getId();
            } else {
                produtoService.salvarCategoria(fromDTO);
                return fromDTO.getId();
            }
        } catch (CadastroException ex) {
            return gerarErro(ex.getMessage());
        } catch (Exception ex) {
            return gerarErro();
        }
    }

    @Logado
    public Object removerCategoria(String id) {
        ProdutoCategoria categoria = produtoService.buscarCategoria(Integer.parseInt(id));
        categoria.setAtivo("N");
        produtoService.salvarCategoria(categoria);
        return OK;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Clientes">
    @Logado
    public Object cadastrarCliente(ClienteCadastroDTO cliente) {
        try {
            return clienteMapper.toDTO(clienteService.importDto(cliente, getTenatId()));
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }

    @Logado
    public Object listarCliente(MultivaluedMap<String, String> urlInfo) {
        ClienteFiltro filtro = getFilter(new ClienteFiltro(), urlInfo);
        filtro.setIdEmpresa(getEmpresa());
        return clienteService.getListaFiltrada(filtro).stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object consultarCliente(String id) {
        Cliente client = clienteService.buscar(Integer.parseInt(id));
        if (client == null || !client.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException("Não foi encontrado nenhum cliente com o ID " + id);
        }
        return clienteMapper.toDTO(client);
    }

    @Logado
    public Object removerCliente(String id) {
        Cliente clente = clienteService.buscar(Integer.parseInt(id));
        clente.setAtivo("N");
        clienteService.salvar(clente);
        return OK;
    }

    public Object solicitarCadastroCliente(SolicitacaoCadastroClienteDTO dto) {
        Empresa empresa = empresaService.getEmpresPorTenatID(dto.getTenantID());
        if (empresa == null) {
            return gerarErro(Status.NOT_FOUND);
        }
        logarComTenant(empresa.getTenatID());
        ParametroSistema ps = parametroSistemaService.getParametro();
        if ("N".equals(ps.getCriarUsuarioParaClienteCadastrado())) {
            return gerarErro("Empresa não habilitada para solicitações de cadsatro.");
        }
        if (!dto.isAnexosOk()) {
            return gerarErro("É necessário informar todos os documentos obrigatórios para continuar.");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty() || !dto.getEmail().matches("^[^@]+@([a-zA-Z\\d]+\\.)+[a-zA-Z\\d]+$")) {
            return gerarErro("É necessário informar um email válido para solicitar o cadastro.");
        }
        dto.getVeiculos().stream()
                .map(SolicitacaoCadastroClienteVeiculoDTO::getAnexos)
                .map(DocumentoDTO::getListaAnexo)
                .flatMap(Collection::stream)
                .filter(da -> !da.getNome().matches("\\.[a-z]+$"))
                .forEach(da -> da.setNome(da.getNome() + ".png"));
        SolicitacaoCadastroCliente scc;
        if (dto.getId() != null) {
            SolicitacaoCadastroCliente banco = solicitacaoCadastroClienteService.buscar(dto.getId());
            if (banco == null) {
                return gerarErro(Status.NOT_FOUND);
            }
            final String status = EnumStatusSolicitacaoCadastroCliente.REJEITADO.getSigla();
            banco.getSolicitacaoCadastroClienteVeiculoList().stream()
                    .filter(sccv -> sccv.getStatus().equals(status))
                    .forEach(sccv -> dto.getVeiculos().stream()
                    .filter(v -> sccv.getId().equals(v.getId()))
                    .forEach(veiculo -> {
                        Documento doc;
                        if (veiculo.getAnexos().getId() != null) {
                            doc = documentoService.buscar(veiculo.getAnexos().getId());
                        } else {
                            doc = documentoService.criarDocumento(getUsuarioLogado(), "Fotos do produto");
                        }
                        veiculo.setStatus(EnumStatusSolicitacaoCadastroCliente.AGUARDANDO.getSigla());
                        veiculo.setDataModificacao(new Date());
                        veiculo.setObservacao(null);
                        sccv.setIdDocumento(documentoAnexoService.persistirAnexoDTO(doc, getUsuarioLogado(), veiculo.getAnexos().getListaAnexo()));
                        solicitacaoCadastroClienteVeiculoService.salvar(clienteMapper.toEntity(veiculo, usuarioService.getUsuarioSuporte()));
                    }));
            scc = solicitacaoCadastroClienteService.buscar(dto.getId());
        } else {
            scc = clienteMapper.toEntity(dto, usuarioService.getUsuarioSuporte());
            scc.getEndereco().setIdCidade(cidadeService.buscarPeloCodigoIBGE(dto.getCodIbgeCidade().toString()));
            scc.setTenatID(dto.getTenantID());
            for (SolicitacaoCadastroClienteVeiculo sccv : scc.getSolicitacaoCadastroClienteVeiculoList()) {
                sccv.setTenatID(dto.getTenantID());
                sccv.setIdSolicitacaoCadastroCliente(scc);
            }
            scc = solicitacaoCadastroClienteService.salvar(scc);
        }

        return clienteMapper.toDTO(scc);
    }

    public Object buscarSolicitacao(String tenantID, Integer id) {
        SolicitacaoCadastroCliente scc = solicitacaoCadastroClienteService.buscar(id);
        if (scc == null || !scc.getTenatID().equals(tenantID)) {
            return gerarErro(Status.NOT_FOUND);
        }
        return clienteMapper.toDTO(scc);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Vendas">
    @Logado(requerDadosCompletos = true)
    public Object listarVendas(MultivaluedMap<String, String> urlInfo) {
        VendaFiltro vf = getFilter(new VendaFiltro(), urlInfo);
        vf.getOrigem().getValue().add(EnumOrigemVenda.SITE.getOrigem());
        vf.getOrigem().getValue().add(EnumOrigemVenda.APP.getOrigem());
        vf.getOrigem().getValue().add(EnumOrigemVenda.AGENDAMENTO_APROVADO.getOrigem());
        return vendaService.getListaFiltradaVenda(vf).stream()
                .map(venda -> {
                    VendaRestDTO vendaDTO = vendaToDto(venda, urlInfo);
                    if (venda.getTipoItemVenda().contains(EnumTipoItemVenda.PRODUTO.getTipo())) {
                        vendaDTO.setEmitiuNFe(vendaService.vendaPossuiNF(venda));
                    }
                    if (venda.getTipoItemVenda().contains(EnumTipoItemVenda.SERVICO.getTipo())) {
                        vendaDTO.setEmitiuNFSe(vendaService.vendaPossuiNFServico(venda));
                    }
                    return vendaDTO;
                })
                .collect(Collectors.toList());
    }

    @Logado(requerDadosCompletos = true)
    public Object consultarVenda(String id) {
        Venda venda = vendaService.buscar(Integer.parseInt(id));
        if (!venda.getStatusNegociacao().equals(EnumTipoVenda.VENDA.getSituacao())) {
            throw new NotFoundException();
        }
        if (!venda.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException();
        }

        return vendaToDto(venda);
    }

    @Logado(requerDadosCompletos = true)
    public Object cadastrarVenda(VendaCadastroRestDTO vendaDTO) {
        try {
            List<Produto> produtosVenda = vendaService.listarVendaProduto(vendaDTO.getId()).stream()
                    .map(VendaProduto::getDadosProduto)
                    .map(DadosProduto::getIdProduto)
                    .filter(prod -> vendaDTO.getProdutos().stream().noneMatch(aux -> prod.getId().equals(aux.getProduto())))
                    .collect(Collectors.toList());
            if (vendaDTO.getTipo() == null) {
                vendaDTO.setTipo(EnumTipoVenda.VENDA.getSituacao());
            }
            Venda venda = vendaMapper.toEntityFromDB(vendaDTO, adHocTenant.getTenantID());
            if (venda.getId() != null) {
                venda.setPodeVerificarEstoque(false);
            }
            venda.setIdUsuarioVendedor(getUsuarioLogado());
            venda.setNumParcela(1);

            if (!vendaDTO.getFormasPagamento().isEmpty()) {
                String condicao = vendaDTO.getFormasPagamento().get(0).getCondicaoPagamento();
                if (!condicao.equals("V")) {
                    venda.setNumParcela(Integer.parseInt(condicao.toLowerCase().replace("x", "")));
                }
                venda.setFormaPagamento(condicao);
                venda.setIdFormaPagamento(formaPagamentoService.buscar(vendaDTO.getFormasPagamento().get(0).getFormaPagamento()));
            }
            venda.setOrigem(EnumOrigemVenda.APP.getOrigem());
            venda.setIdOrigem(getUsuarioLogado().getUuid());
            venda = contaService.parcelarVenda(venda);
            venda = vendaService.salvar(venda);
            produtosVenda.forEach(produtoService::atualizarQuantidadeEstoque);
            if (vendaDTO.getId() == null && vendaDTO.isEnviarLinkPagamento()) {
                transacaoService.adicionarEnviarTransacaoPendente(venda);
            }
            return venda.getId();
        } catch (CadastroException | NumberFormatException | IllegalAccessException | InvocationTargetException ex) {
            return gerarErro(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            String msg = ex.getMessage();
            if (msg == null || msg.isEmpty()) {
                msg = "Ocorreu um erro ao salvar a venda.";
            }
            return gerarErro(msg);
        }
    }

    @Logado(requerDadosCompletos = true)
    public Object removerVenda(String id) {
        Venda venda = vendaService.buscar(Integer.parseInt(id));
        if (!venda.getStatusNegociacao().equals(EnumTipoVenda.VENDA.getSituacao())) {
            throw new NotFoundException();
        }
        if (!venda.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException();
        }

        try {
            if (contaService.existeParcelaPaga(venda)) {
                return gerarErro("Existem parcelas pagas para essa venda, para cancelar a venda é necessário antes cancelar essas parcelas.");
            }
            vendaService.cancelarVenda(venda);
            return vendaToDto(venda);
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível cancelar a venda");
        }
    }

    @Logado(requerDadosCompletos = true)
    public Object imprimirVenda(Integer idVenda) {
        try {
            byte[] pdf = relatorioService.gerarImpressaoVenda(vendaService.buscar(idVenda));
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdf);
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Orçamentos">
    private Object aprovarOrcamento(AtualizaOrcamentoDTO dados, Integer numero) {
        try {
            Venda venda = vendaService.buscar(numero);
            if (venda.getStatusNegociacao().equals(EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao())) {
                return gerarErro("Orçamento já aprovado.");
            }
            if (venda.getStatusNegociacao().equals(EnumTipoVenda.ORCAMENTO_REJEITADO.getSituacao())) {
                return gerarErro("Orçamento rejeitado.");
            }
            if (vendaService.temVendaPorIdOrcamentoOsOrigem(venda)) {
                return gerarErro("Não é possível alterar o agendamento pois o mesmo já foi confirmado.");
            }
            if (venda.getVendaFormaPagamentoList().isEmpty() && !EnumOrigemVenda.AGENDAMENTO.getOrigem().equals(venda.getOrigem())) {
                return gerarErro("Ordem de serviço sem forma de pagamento definida.");
            }

            FormaPagamento formaPagamento = null;
            if (dados.getTipoAtualizacao() == EnumTipoAtualizacao.APROVAR) {

                if (venda.getOrigem().equals(EnumOrigemVenda.AGENDAMENTO.getOrigem())) {
                    venda.setStatusNegociacao(EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao());
                    VendaFormaPagamento vfp = new VendaFormaPagamento();
                    if (venda.getVendaFormaPagamentoList().isEmpty()) {
                        List<FormaPagamento> listFP = formaPagamentoService.listar();
                        if (!listFP.isEmpty()) {
                            formaPagamento = listFP.get(0);
                        }
                        vfp.setCondicaoPagamento("1");
                        vfp.setDesconto(0d);
                        vfp.setIdFormaPagamento(formaPagamento);
                        vfp.setIdVenda(venda);
                        vfp.setTenatID(venda.getTenatID());
                        venda.getVendaFormaPagamentoList().add(vfp);
                        vendaService.salvar(venda);
                    } else {
                        vfp = venda.getVendaFormaPagamentoList().get(0);
                        formaPagamento = vfp.getIdFormaPagamento();
                    }
                    dados.setFormaPagamento(new VendaFormaPagamentoCadastroRestDTO());
                    dados.getFormaPagamento().setFormaPagamento(vfp.getIdFormaPagamento().getId());
                } else if (dados.getFormaPagamento().getId() != null) {// VendaFomaPagamento informada
                    List<FormaPagamento> listFP = venda.getVendaFormaPagamentoList().stream()
                            .filter(fp -> fp.getId().equals(dados.getFormaPagamento().getId()))
                            .map(VendaFormaPagamento::getIdFormaPagamento)
                            .collect(Collectors.toList());
                    if (listFP.isEmpty()) {
                        return gerarErro("Forma de pagamento não cadastrada no orçamento");
                    }
                    formaPagamento = listFP.get(0);
                } else {// Cadastrar VendaFomaPagamento e selecioná-la
                    VendaFormaPagamento vfp = vendaMapper.toEntity(dados.getFormaPagamento(), venda);
                    vfp.setIdVenda(venda);
                    vfp.setIdFormaPagamento(formaPagamentoService.buscar(vfp.getIdFormaPagamento().getId()));
                    vfp.setCondicaoPagamento(dados.getFormaPagamento().getCondicaoPagamento());
                    vfp.setDesconto(dados.getFormaPagamento().getDesconto());
                    vfp.setFormaEscolhida("S");
                    venda.getVendaFormaPagamentoList().clear();
                    venda.getVendaFormaPagamentoList().add(vfp);
                    formaPagamento = vfp.getIdFormaPagamento();
                }
            }
            if (!venda.getVendaFormaPagamentoList().isEmpty() && !venda.getStatusNegociacao().equals(EnumTipoVenda.ORDEM_SERVICO.getSituacao())) {
                VendaFormaPagamento vfp = venda.getVendaFormaPagamentoList().get(0);
                if (venda.getVendaFormaPagamentoList().size() > 1) {
                    vfp = venda.getVendaFormaPagamentoList().stream()
                            .filter(fp -> {
                                if (fp.getId() != null) {
                                    return fp.getId().equals(dados.getFormaPagamento().getId());
                                }
                                return fp.getIdFormaPagamento().getId().equals(dados.getFormaPagamento().getFormaPagamento());
                            })
                            .collect(Collectors.toList())
                            .get(0);
                }
                vfp.setFormaEscolhida("S");

                venda.setFormaPagamento(vfp.getCondicaoPagamento().replace("x", ""));
                venda.setValorDesconto(NumeroUtil.somar(0d, vfp.getDesconto()));
            }
            boolean vendaProdutoIsEmpty = vendaService.listarVendaProduto(venda).isEmpty();
            boolean vendaServicoIsEmpty = vendaService.listarVendaServico(venda).isEmpty();
            boolean vendaItemOrdemDeServicoIsEmpty = vendaService.listarVendaItensOrdemDeServico(venda).isEmpty();

            if (dados.getTipoAtualizacao() == EnumTipoAtualizacao.APROVAR) {

                venda = contaService.parcelarVenda(venda);
                if ((!vendaProdutoIsEmpty || !vendaServicoIsEmpty || !vendaItemOrdemDeServicoIsEmpty)
                        && !venda.getListParcelaDTO().isEmpty()) {
                    vendaService.aprovarOrcamento(venda, formaPagamento);
                    return "\"Orçamento salvo com sucesso!\"";
                }
            } else {
                if ((!vendaProdutoIsEmpty || !vendaServicoIsEmpty || !vendaItemOrdemDeServicoIsEmpty)) {
                    vendaService.aprovarOS(venda);
                    return "\"Orçamento salvo com sucesso!\"";
                }

            }
            return gerarErro("Informe ao menos um produto para efetuar o orçamento");
        } catch (CadastroException ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível aprovar o orçamento. " + ex.getMessage());
        }
    }

    @Logado
    public Object listarOrcamentos(MultivaluedMap<String, String> urlInfo) {
        VendaFiltro vf = getFilter(new VendaFiltro(), urlInfo);
        vf.getOrigem().getValue().add(EnumOrigemVenda.SITE.getOrigem());
        vf.getOrigem().getValue().add(EnumOrigemVenda.APP.getOrigem());
        return vendaService.getListaFiltradaOrcamento(vf).stream()
                .map(this::vendaToDto)
                .collect(Collectors.toList());
    }

    @Logado
    public Object consultarOrcamento(String id) {
        Venda venda = vendaService.buscar(Integer.parseInt(id));
        if (!(venda.getStatusNegociacao().equals(EnumTipoVenda.ORCAMENTO.getSituacao())
                || venda.getStatusNegociacao().equals(EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao())
                || venda.getStatusNegociacao().equals(EnumTipoVenda.ORCAMENTO_REJEITADO.getSituacao()))) {
            throw new NotFoundException();
        }
        if (!venda.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException();
        }

        return vendaToDto(venda);
    }

    @Logado
    public Object removerOrcamento(String id) {
        Venda venda = vendaService.buscar(Integer.parseInt(id));
        if (!venda.isOrcamento()) {
            throw new NotFoundException();
        }
        if (!venda.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException();
        }

        try {
            venda.setDataCancelamento(new Date());
            venda.setSituacao("C");
            vendaService.salvar(venda);
            return vendaToDto(venda);
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível cancelar o orçamento.");
        }
    }

    @Logado
    public Object cadastrarOrcamento(VendaCadastroRestDTO vendaDTO) {
        Venda venda;
        try {
            if (vendaDTO.getId() != null) {
                venda = vendaService.buscar(vendaDTO.getId());
                if (venda.getStatusNegociacao().equals(EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao())) {
                    return gerarErro("Orçamento já aprovado.");
                }
                if (venda.getStatusNegociacao().equals(EnumTipoVenda.ORCAMENTO_REJEITADO.getSituacao())) {
                    return gerarErro("Orçamento rejeitado.");
                }
            }
            if (vendaDTO.getTipo() == null) {
                vendaDTO.setTipo(EnumTipoVenda.ORCAMENTO.getSituacao());
            }
            venda = vendaMapper.toEntityFromDB(vendaDTO, adHocTenant.getTenantID());
            venda.getVendaServicoList().stream()
                    .filter(vs -> vs.getQuantidade() == null)
                    .forEach(vs -> vs.setQuantidade(1));
            venda.setIdUsuarioVendedor(getUsuarioLogado());
            venda.setNumParcela(1);
            venda.setOrigem(EnumOrigemVenda.APP.getOrigem());
            venda.setIdOrigem(getUsuarioLogado().getUuid());
        } catch (CadastroException ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível preparar para salvar a venda.");
        }
        try {
            vendaService.salvar(venda);
            return venda.getId();
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível salvar a venda.");
        }
    }

    @Logado(requerEmpresa = false)
    public Object atualizarOrcamento(AtualizaOrcamentoDTO dados, Integer numero) {
        switch (dados.getTipoAtualizacao()) {
            case APROVAR:
                return aprovarOrcamento(dados, numero);
            case GERAR_OS:
                return aprovarOrcamento(dados, numero);
            case REJEITAR:
                return rejeitarOrcamento(numero);
            default:
                return gerarErro("Tipo de atualização inválida.");
        }
    }

    private Object rejeitarOrcamento(Integer numero) {
        try {
            Venda orcamento = vendaService.buscar(numero);
            orcamento.setStatusNegociacao(EnumTipoVenda.ORCAMENTO_REJEITADO.getSituacao());
            orcamento.setSituacao(EnumSituacaoCompraVenda.CANCELADO.getSituacao());
            if (getUsuarioLogado().getTenat() == null) {
                logarComTenant(orcamento.getTenatID());
            }
            if (orcamento.getIdConta() != null) {
                contaService.remover(orcamento.getIdConta());
            }
            if (orcamento.getIdVendaDestino() != null) {
                vendaService.cancelarVenda(orcamento.getIdVendaDestino());
            }
            vendaService.salvar(orcamento);
            if (orcamento.getOrigem().equals(EnumOrigemVenda.AGENDAMENTO.getOrigem()) || orcamento.getOrigem().equals(EnumOrigemVenda.AGENDAMENTO_APROVADO.getOrigem())) {
                ParametroSistema parametroSistema = parametroSistemaService.getParametro();
                if ("S".equals(parametroSistema.getEnviaSmsClienteAlteracaoAgendamento())) {
                    smsService.send(new SmsDTO(getCorpoSms("Agendamento cancelado", orcamento), orcamento.getIdCliente().getTelefoneCelular()));
                }
            }
            return "\"Orçamento rejeitado com sucesso.\"";
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível rejeitar a venda.");
        }
    }

    @Logado
    public Object imprimirOrcamento(Integer idOrcamento, Integer idFormaPagamento) {
        try {
            Venda orcamento = vendaService.buscar(idOrcamento);
            FormaPagamento formaPagamento = formaPagamentoService.buscar(idFormaPagamento);
            byte[] pdf;
            if (getEmpresa().getIdCategoriaEmpresa() != null && getEmpresa().getIdCategoriaEmpresa().getId().equals(51)) {
                pdf = relatorioService.gerarImpressaoOrcamentoParaCliente(orcamento, formaPagamento);
            } else {
                pdf = relatorioService.gerarImpressaoOrcamento(orcamento, formaPagamento);
            }
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdf);
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }

    @Logado
    public Object enviarEmailOrcamento(Integer idOrcamento, Integer idFormaPagamento) {
        try {
            Venda orcamento = vendaService.buscar(idOrcamento);
            FormaPagamento formaPagamento = formaPagamentoService.buscar(idFormaPagamento);
            String n = "";

            if (!orcamento.getVendaProdutoList().isEmpty()) {
                n += "P";
            }
            if (!orcamento.getVendaServicoList().isEmpty()) {
                n += "S";
            }
            if (!orcamento.getVendaItemOrdemDeServicoList().isEmpty()) {
                n += "O";
            }

            vendaService.enviarOrcamentoPorEmail(orcamento, formaPagamento, n + orcamento.getId());
            EnvioEmailDTO status = new EnvioEmailDTO();
            status.setEmail(getUsuarioLogado().getEmail());
            status.setEnviado(true);
            status.setMensagem("E-mail enviado com sucesso!");
            return status;
        } catch (CadastroException | EmailException | RelatorioException | JRException | IOException e) {
            return gerarErro(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Formas de pagamento">
    @Logado
    public Object listarFormaPagamento(MultivaluedMap<String, String> urlInfo) {
        return formaPagamentoService.getListaFiltrada(getFilter(new FormaPagamentoFiltro(), urlInfo)).stream()
                .map(formaPagamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object consultarFormaPagamento(String id) {
        FormaPagamento fp = formaPagamentoService.buscar(Integer.parseInt(id));
        if (fp == null || !fp.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException("Não foi encontrado nenhuma forma de pagamento com o ID " + id);
        }

        return formaPagamentoMapper.toDTO(fp);
    }

    @Logado
    public Object cadastrarFormaPagamento(FormaPagamentoDTO formaPagamentoDTO) {
        try {
            FormaPagamento fp = formaPagamentoMapper.toEntity(formaPagamentoDTO);
            formaPagamentoService.salvar(fp);
            return formaPagamentoMapper.toDTO(fp);
        } catch (CadastroException ex) {
            return gerarErro(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível preparar para salvar a forma de pagamento.");
        }
    }

    @Logado
    public Object removerFormaPagamento(String id) {
        FormaPagamento forma = formaPagamentoService.buscar(Integer.parseInt(id));
        forma.setAtivo("N");
        formaPagamentoService.salvar(forma);
        return OK;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Fornecedores">
    @Logado
    public Object cadastrarFornecedor(FornecedorDTO fornecedorDTO) {
        try {
            return fornecedorMapper.toDTO(fornecedorService.importDto(fornecedorDTO, OK));
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }

    @Logado
    public Object listarFornecedor(MultivaluedMap<String, String> urlInfo) {
        FornecedorFiltro filtro = getFilter(new FornecedorFiltro(), urlInfo);
        return fornecedorService.getListaFiltrada(filtro).stream()
                .map(fornecedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object consultarFornecedor(String id) {
        Fornecedor fornecedor = fornecedorService.buscar(Integer.parseInt(id));
        if (fornecedor == null || !fornecedor.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException("Não foi encontrado nenhum fornecedor com o ID " + id);
        }

        return fornecedorMapper.toDTO(fornecedor);
    }

    @Logado
    public Object removerFornecedor(String id) {
        Fornecedor clente = fornecedorService.buscar(Integer.parseInt(id));
        clente.setAtivo("N");
        fornecedorService.salvar(clente);
        return OK;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Busca de dados">
    public Object buscaEmpresa(MultivaluedMap<String, String> queryParameters) {
        if (queryParameters.containsKey("cnpj")) {
            return empresaService.buscarDadosEmpresaPorCNPJ(queryParameters.getFirst("cnpj"));
        }
        return gerarErro("Informe o CNPJ.");
    }

    public Object buscaPessoa(MultivaluedMap<String, String> queryParameters) {
        if (queryParameters.containsKey("cpf")) {
            String dataStr = queryParameters.getFirst("data");
            Date data = null;
            if (dataStr != null && !dataStr.isEmpty()) {
                data = DataUtil.converterStringParaDate(dataStr, "yyyy-MM-dd");
            }
            return clienteService.buscarDadosEmpresaPorCpf(queryParameters.getFirst("cpf"), data);
        }
        return gerarErro("Informe o CPF.");
    }

    public Object buscaCEP(MultivaluedMap<String, String> queryParameters) {
        if (queryParameters.containsKey("cep")) {
            return cidadeService.getEnderecoPorCep(queryParameters.getFirst("cep"));
        }
        return gerarErro("Informe o cep.");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Empresas">
    public Object listaCategoriaEmpresa() {
        return categoriaEmpresaService.getListaFiltrada(new CategoriaEmpresaFiltro()).stream()
                .map(empresaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado(requerEmpresa = false)
    public Object listarEmpresas() {
        return acessoService.getListaFiltradaEmpresa(new LoginAcessoFiltro(getUsuarioLogado())).stream()
                .map(empresaMapper::toDTO)
                .map(dto -> {
                    Empresa empresa = empresaService.buscar(dto.getId());
                    if (empresa != null) {
                        PagamentoMensalidade pm = pagamentoMensalidadeService.getUltimoPagamentoPor(empresa);
                        if (pm != null) {
                            dto.setDataValidadePagamento(pm.getDataValidade());
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Object listarPagamentoMensalidade() {
        return pagamentoMensalidadeService.getListaFiltrada(new PagamentoMensalidadeFiltro()).stream()
                .map(empresaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object listarModulos(MultivaluedMap<String, String> urlInfo) {
        ModuloFiltro filtro = getFilter(new ModuloFiltro(), urlInfo);
        return moduloService.getListaFiltrada(filtro).stream()
                .map(moduloMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object modulosContratados() {
        Empresa empresa = empresaService.getEmpresa();
        return pagamentoMensalidadeService.getUltimoPagamentoPor(empresa).getPagamentoMensalidadeModuloList().stream()
                .map(PagamentoMensalidadeModulo::getIdModulo)
                .map(moduloMapper::toDTO)
                .collect(Collectors.toList());
    }

    public String buscarTermo() {
        File file = new File(SystemPreferencesUtil.getPropOrThrow("defaults.pathTermoUso", NotFoundException::new));
        try {
            byte[] conteudoArquivo = FileUtil.convertFileToBytes(file);
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(conteudoArquivo);
        } catch (FileException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
        public String buscarTermoAbapav() {
        File file = new File(SystemPreferencesUtil.getPropOrThrow("defaults.pathTermoAbapav", NotFoundException::new));
        try {
            byte[] conteudoArquivo = FileUtil.convertFileToBytes(file);
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(conteudoArquivo);
        } catch (FileException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String buscarPoliticaPrivacidade() {
        File file = new File(SystemPreferencesUtil.getPropOrThrow("defaults.pathPoliticaPrivacidade", NotFoundException::new));
        try {
            byte[] conteudoArquivo = FileUtil.convertFileToBytes(file);
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(conteudoArquivo);
        } catch (FileException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public Object cadastrarEmpresa(EmpresaCadastroDTO empresaCadastroDTO) {
        EmpresaDTO dto;
        try {
            empresaCadastroDTO.setTipoUso(EnumTipoUsoSistema.AMBOS.getTipo());
            dto = empresaMapper.toDTO(empresaService.cadastrarEmpresa(empresaCadastroDTO));
            List<PagamentoMensalidade> lpm = pagamentoMensalidadeService.listar();
            if (lpm.isEmpty() || lpm.get(0).getDataPagamento() == null) {
                return Response.status(Status.CREATED).entity(dto).build();
            }
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
        try {
            UsuarioRequest request = new UsuarioRequest();
            request.setLogin(empresaCadastroDTO.getEmailResponsavel());
            request.setSenha(empresaCadastroDTO.getSenhaResponsavel());
            request.setDeviceId("-");
            dto.setUsuarioLogado((UsuarioDTO) logarUsuario(request));
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Não foi possível logar o usuário após o cadastro no APP", ex);
        }
        return dto;
    }

    @Logado
    public Object atualizarEmpresa(EmpresaAtualizacaoDTO dto) {
        try {
            Empresa empresa = empresaService.getEmpresa();
            String cnpjEmpresa = CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj() != null ? empresa.getCnpj() : "");
            String cnpjDto = dto.getCpfCnpj() != null ? CpfCnpjUtil.removerMascaraCnpj(dto.getCpfCnpj()) : "";
            String nomeFantasia = empresa.getNomeFantasia();
            empresaMapper.update(empresa, dto);
            if (empresa.getCnpj() != null && cnpjDto.length() == 14 && !cnpjEmpresa.equals(cnpjDto)) {
                Empresa aux = new Empresa();
                empresaService.buscarDadosEmpresaPorCNPJ(empresa.getCnpj(), aux);
                empresa.setEndereco(aux.getEndereco());
                empresa.setDescricao(aux.getDescricao());
                empresa.setEmail(aux.getEmail());
                empresa.setAtivo(aux.getAtivo());
                empresa.setNomeFantasia(aux.getNomeFantasia());
                empresa.setFone(aux.getFone());
                empresa.setDataConstituicao(aux.getDataConstituicao());
                empresa.setPorte(aux.getPorte());
                empresa.setRegimeTributario(aux.getRegimeTributario());
                empresa.setResponsavel(aux.getResponsavel());
                empresa.setIdCnae(aux.getIdCnae());
                empresa.getListEmpresaCnae().clear();
                empresa.getListEmpresaCnae().addAll(aux.getListEmpresaCnae());
                empresa.getListEmpresaCnae().forEach(e -> e.setIdEmpresa(empresa));
            }
            empresa.setTipoEmpresa("EM");
            if (empresa.getNomeFantasia() == null || empresa.getNomeFantasia().isEmpty()) {
                if (nomeFantasia == null || nomeFantasia.isEmpty()) {
                    nomeFantasia = empresa.getDescricao();
                }
                empresa.setNomeFantasia(nomeFantasia);
            }
            if (!getViolations(empresa).isEmpty()) {
                List<Erro> erros = new ArrayList<>();
                erros.add(new Erro("Preencha todos os capos obrigatórios antes de continuar."));
                erros.addAll(getViolations(empresa).stream().map(ConstraintViolation::getMessage).map(Erro::new).collect(Collectors.toList()));
                return gerarErro(Status.INTERNAL_SERVER_ERROR, erros);
            }
            EmpresaDTO dtoRet = empresaMapper.toDTO(empresaService.salvar(empresa));
            dtoRet.setDataValidadePagamento(pagamentoMensalidadeService.getUltimoPagamentoPor(empresa).getDataValidade());
            return dtoRet;
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }

    public Object contratar(ContratacaoDTO contratacaoDto) {
        List<Modulo> selection = contratacaoDto.getModulos().stream()
                .map(moduloService::buscar)
                .collect(Collectors.toList());
        List<ModuloDTO> modulosContratados = (List<ModuloDTO>) modulosContratados();
        Double valor;
        if (empresaService.getEmpresa().getTipoConta().equals("G")) {// Compra
            valor = selection.stream()
                    .mapToDouble(mod -> mod.getValorAdesao() + mod.getValorMensalidade())
                    .sum();
        } else {// Renovação/Contratação de módulo
            valor = selection.stream()
                    .mapToDouble(mod -> {
                        Double valorModulo = mod.getValorMensalidade();
                        if (modulosContratados.stream()
                                .noneMatch(mc -> mc.getId().equals(mod.getId()))) {
                            valorModulo += mod.getValorAdesao();
                        }
                        return valorModulo;
                    })
                    .sum();
        }
        contratacaoDto.getDadosPagamento().setPreco(valor);
        PagamentoMensalidade pm = pagamentoMensalidadeService.gerarPagamentoMensalidade(selection, contratacaoDto.getTipoPagamentoSistema(), getUsuarioLogado());
        RetornoContratacaoDTO retorno = new RetornoContratacaoDTO();
        try {
            boolean ok = pagamentoMensalidadeService.sendRequestPagamento(pm, contratacaoDto.getDadosPagamento(), selection, 30d);
            if (!ok) {
                return gerarErro("Transação não autorizada");
            }
            if (pm.getDataPagamento() == null) {
                retorno.setPendente(true);
                if (pm.getIdTransacaoGetnet() != null) {
                    retorno.setLinkBoleto(pm.getIdTransacaoGetnet().getLinkBoleto());
                }
            }
            return retorno;
        } catch (MessageListException ex) {
            return gerarErro(Status.BAD_REQUEST, ex.getMessages().stream().map(Erro::new).collect(Collectors.toList()));
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Contas bancárias">
    @Logado
    public Object listarContaBancaria(MultivaluedMap<String, String> urlInfo) {
        return contaBancariaService.getListaFiltrada(getFilter(new ContaBancariaFiltro(), urlInfo)).stream()
                .map(contaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object buscarContaBancaria(Integer id) {
        List<ContaBancariaDTO> contasBancarias = new ArrayList<>();
        contasBancarias.add(contaMapper.toDTO(contaBancariaService.buscar(id)));
        return contasBancarias;
    }

    @Logado
    public Object cancelarContaBancaria(Integer id) {
        ContaBancaria cb = contaBancariaService.buscar(id);
        if (cb != null) {
            contaBancariaService.cancelarConta(cb);
        }
        return contaMapper.toDTO(cb);
    }

    @Logado
    public Object cadastrarContaBancaria(ContaBancariaDTO dados) {
        Banco banco = bancoService.buscar(dados.getIdBanco());
        if (banco == null) {
            return gerarErro("Informe um banco");
        }
        ContaBancaria cb = contaMapper.toEntity(dados);
        cb.setIdBanco(banco);
        if (dados.getIdPlanoConta() != null) {
            cb.setIdPlanoConta(planoContaService.buscar(dados.getIdPlanoConta()));
        }
        cb.setTenatID(getUsuarioLogado().getTenat());
        contaBancariaService.salvar(cb);
        return contaMapper.toDTO(cb);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Transações">
    public Object atulizarTransacao(RequestRetornoMicroServico request) {
        try {
            TransacaoGetnet transacao = transacaoGetnetService.buscar(request.getIdPagamento());

            transacao = transacaoGetnetService.atualizarSituacaoTransacaoPagamento(transacao, request.getStatus(), request.getMensagem());

            if (transacao.getSituacao().equals(EnumSituacaoTransacao.APROVADO.getSituacao())) {
                if (transacao.getIdVenda() != null) {
                    vendaService.darBaixa(transacao.getIdVenda());
                } else if (transacao.getIdPagamentoSistema() != null && !transacao.getPagamentoMensalidadeList().isEmpty()) {
                    PagamentoMensalidade pm = transacao.getPagamentoMensalidadeList().get(0);
                    pm.setDataPagamento(new Date());
                    pm.setValorPago(transacao.getIdPagamentoSistema().getValor());
                    pagamentoMensalidadeService.salvar(pm);
                }
            }

            return "\"OK\"";
        } catch (Exception e) {
            return gerarErro(e.getMessage());
        }
    }

    public Object retornoFitpag(String notificationCode) {
        try {
            Transaction transacao = FitPagUtil.getTransactionByNotification(notificationCode);
            byte status = transacao.getStatus();
            PagamentoMensalidade pm = pagamentoMensalidadeService.buscarPorReferencia(transacao.getReference());
            switch (status) {
                case 3:
                case 4:// Pago ou disponível
                    pm.setDataPagamento(new Date());
                    pm.setValorPago(transacao.getNetAmount());
                    break;
                case 6:
                case 7:// Devolvida ou cancelada
                    pm.setDataPagamento(null);
                    pm.setValorPago(0d);
                    Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, () -> "Pagamento de mensalidade da empresa " + pm.getIdEmpresa().getDescricao() + " retornou com o status " + status);
                    break;
                default:
                    return gerarErro("Não processado");
            }
            pagamentoMensalidadeService.salvar(pm);
            return OK;
        } catch (MessageListException ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Parâmetros">
    public String getCallbackProducao() {
        return parametroGeralService.getParametro().getCallbackProducao();
    }

    public String getCallbackHomologacao() {
        return parametroGeralService.getParametro().getCallbackHomologacao();
    }

    @Logado
    public List<ParametroDTO> listarParametros() {
        ParametroGeral pg = parametroGeralService.getParametro();
        JsonNode node = new ObjectMapper().valueToTree(pg);
        List<ParametroDTO> lista = new ArrayList<>();
        node.fields()
                .forEachRemaining(entry -> lista.add(new ParametroDTO(entry.getKey(), entry.getValue().asText(), "Geral")));
        ParametroSistema ps = parametroSistemaService.getParametro();
        ps.getTipoProdutoUsoList().forEach(tpu -> tpu.setIdParametroSistema(null));
        node = new ObjectMapper().valueToTree(ps);
        node.fields()
                .forEachRemaining(entry -> {
                    String nome = entry.getKey();
                    if (!nome.equals("id") && !nome.equals("tenatID")) {
                        try {
                            lista.add(new ParametroDTO(nome, fromJsonNode(entry.getValue()), "Empresa"));
                        } catch (JsonProcessingException ex) {
                            lista.add(new ParametroDTO(nome, "Não foi possível converter o parâmetro.", "Erro"));
                        }
                    }
                });

        ps.getTipoProdutoUsoList().forEach(tpu -> tpu.setIdParametroSistema(ps));
        return lista;
    }

    public Integer quantidadeDiasGratuitos() {
        ParametroGeral pg = parametroGeralService.getParametro();
        return pg.getPrazoUsarSemComprar();
    }

    public Double valorAdesaoMinimmo() {
        return moduloService.valorAdesaoObrigatorio();
    }

    public Double valorMensalidadeMinimmo() {
        return moduloService.valorMensalidadeObrigatorio();
    }

    @Logado
    public Object consultarParametro(String nome) {
        return listarParametros().stream()
                .filter(item -> item.getName().equals(nome))
                .collect(Collectors.toList());
    }

    @Logado
    public Object atualizarParametro(ParametroDTO paramDTO) {
        if (paramDTO.getType() != null && !paramDTO.getType().equals("Empresa")) {
            return gerarErro("Tipo de parâmetro inválido");
        }
        Optional<ParametroDTO> param = listarParametros().stream()
                .filter(item -> item.getName().equals(paramDTO.getName()) && item.getType().equals("Empresa"))
                .findAny();
        if (!param.isPresent() || !parametroSistemaService.updateParametro(paramDTO.getName(), paramDTO.getValue())) {
            return gerarErro("Não foi possível atualizar o parâmetro.");
        }
        return "OK";
    }

    @Logado
    public Object habilitarNFe() {
        final String erroHabilitacao = "Não foi possível habilitar o envio. Tente novamente mais tarde ou entre em contato com o suporte.";
        try {
            ParametroSistema ps = parametroSistemaService.getParametro();
            String token = parametroSistemaService.registrarParaEnvioDeNfe(ps);
            if (token != null) {
                ps.setNfeMsToken(token);
                parametroSistemaService.salvar(ps);

                return "OK";
            }
            return gerarErro(erroHabilitacao);
        } catch (Exception ex) {
            return gerarErro(erroHabilitacao + " (" + ex.getClass().getSimpleName() + ")");
        }
    }

    @Logado
    public Object buscaParametroSistema() {
        return parametroMapper.toDTO(parametroSistemaService.getParametro());
    }

    @Logado
    public Object alteraParametroSistema(ParametroSistemaDTO dto) {
        try {
            ParametroSistema ps = parametroSistemaService.getParametro();
            parametroMapper.toEntity(ps, dto);
            parametroSistemaService.setParametroSistema(ps);
            return "OK";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
            return gerarErro("Não foi possível salvar os parâmetros do sistema");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Bancos">
    @Logado
    public Object listarBancos(MultivaluedMap<String, String> urlInfo) {
        BancoFiltro filtro = getFilter(new BancoFiltro(), urlInfo);
        return bancoService.getListaFiltrada(filtro).stream()
                .map(bancoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object buscarBanco(Integer id) {
        return bancoMapper.toDTO(bancoService.buscar(id));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Agendamento">
    @Logado
    public Object buscaAgendamento(Integer id) {
        Venda venda = vendaService.buscar(id);
        if (venda == null
                || !(EnumOrigemVenda.AGENDAMENTO.getOrigem().equals(venda.getOrigem())
                || EnumOrigemVenda.AGENDAMENTO_APROVADO.getOrigem().equals(venda.getOrigem()))
                || !venda.getTenatID().equals(getUsuarioLogado().getTenat())) {
            return gerarErro(Status.NOT_FOUND, "Nenhum agendamento foi encontrado com o ID especificado.");
        }
        return vendaToDto(venda);
    }

    @Logado(requerEmpresa = false)
    public Object listaAgendamentos(MultivaluedMap<String, String> urlInfo) {
        final Usuario usuario = getUsuarioLogado();
        final String tenantID = usuario.getTenat();
        VendaFiltro vf = getFilter(new VendaFiltro(), urlInfo);
        if (tenantID == null) {
            // Se for o usuário pelo App agendamento
            vf.setUsuarioLogado(usuario);
        } else {
            // Se for o funcionário pelo App SG Finanças
            vf.setTenantID(tenantID);
            logarComTenant(tenantID);
        }
        if (vf.getOrigem().getValue().isEmpty()) {
            vf.getOrigem().getValue().add(EnumOrigemVenda.AGENDAMENTO.getOrigem());
            vf.getOrigem().getValue().add(EnumOrigemVenda.AGENDAMENTO_APROVADO.getOrigem());
        }
        return vendaService.getListaFiltradaSemTenant(vf).stream()
                .filter(venda -> EnumTipoVenda.ORCAMENTO.getSituacao().equals(venda.getStatusNegociacao())
                || EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao().equals(venda.getStatusNegociacao()))
                .map(venda -> RetornoAgendamentoDTO.builder()
                .agendamento(vendaToDto(venda))
                .empresa(empresaMapper.toDTO(empresaService.getEmpresaPorTenat(venda.getTenatID())))
                .build())
                .collect(Collectors.toList());
    }

    @Logado(requerEmpresa = false)
    public Object cadastrarAgendamento(AgendamentoCadastroDTO dados) {
        final boolean requestDoAppAgenda = dados.getOrigem() == null || dados.getOrigem().equals("agenda-app");
        if (requestDoAppAgenda) {
            // Cadastrar o cliente caso não exista
            List<Cliente> clientes = clienteService.buscarPor(getUsuarioLogado());
            Usuario user = getUsuarioLogado();
            logarComIdFuncionario(dados.getFuncionario());
            Cliente cliente = clientes.stream()
                    .filter(cli -> cli.getTenatID().equals(adHocTenant.getTenantID()))
                    .findAny()
                    .orElseGet(() -> clienteService.registraClientePor(user));
            dados.setCliente(cliente.getId());
        }

        Integer[] novo = new Integer[dados.getServicos().size()];
        for (int i = 0; i < dados.getServicos().size(); i++) {
            novo[i] = dados.getServicos().get(i);
        }
        if (dados.getVenda() != null) {
            Venda fromDb = vendaService.buscar(dados.getVenda());
            Integer[] original = new Integer[fromDb.getVendaServicoList().size()];
            for (int i = 0; i < fromDb.getVendaServicoList().size(); i++) {
                original[i] = fromDb.getVendaServicoList().get(i).getIdServico().getId();
            }
            if (!podeMudarServicos(dados.getHorario(), dados.getFuncionario(), original, novo, fromDb)) {
                return gerarErro("Não é possível alterar o agendamento pois o horário vai colidir com outros agendamentos existentes.");
            }
        } else {
            if (!podeMudarServicos(dados.getHorario(), dados.getFuncionario(), new Integer[]{}, novo, null)) {
                return gerarErro("O horário selecionado já não está mais disponível.");
            }
        }
        VendaCadastroRestDTO req = new VendaCadastroRestDTO();
        req.setId(dados.getVenda());
        req.setTipo(EnumTipoVenda.ORCAMENTO.getSituacao());
        req.setCliente(dados.getCliente());
        req.setDataVencimento(dados.getHorario());
        req.setDataVenda(new Date());
        try {
            req.setFormaPagamento(Integer.parseInt(dados.getFormaPagamento()));
        } catch (NumberFormatException ex) {
            req.setFormaPagamento(1);
        }
        req.setSituacaoSigla("A");
        req.setDesconto(dados.getDesconto());
        req.setCondicaoPagamento(dados.getFormaPagamento());// Campo forma_pagamento (V, 1x, 2x, 3x, ...)
        req.setObservacao(dados.getObservacao());
        req.setObservacaoCliente(dados.getObservacaoCliente());
        req.setContaBancaria(dados.getContaBancaria());
        req.setPlanoConta(dados.getPlanoConta());
        req.setServicos(dados.getServicos().stream()
                .map(servicoService::buscar)
                .map(VendaServicoCadastroRestDTO::new)
                .collect(Collectors.toList()));
        req.setValor(req.getServicos().stream()
                .mapToDouble(VendaServicoCadastroRestDTO::getValorVenda).sum());

        req.setItensOS(new ArrayList<>());
        req.setProdutos(new ArrayList<>());

        Venda venda;
        try {
            venda = vendaMapper.toEntityFromDB(req, adHocTenant.getTenantID());
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível cadastrar o agendamento.");
        }
        venda.setIdUsuarioVendedor(usuarioService.getUserByFuncionario(funcionarioService.buscar(dados.getFuncionario())));
        venda.setNumParcela(1);

        if (!venda.getVendaFormaPagamentoList().isEmpty()) {
            String condicao = venda.getVendaFormaPagamentoList().get(0).getCondicaoPagamento();
            if (!condicao.equals("V")) {
                venda.setNumParcela(Integer.parseInt(condicao.toLowerCase().replace("x", "")));
            }
            venda.setFormaPagamento(condicao);
        }
        venda.setOrigem(EnumOrigemVenda.AGENDAMENTO.getOrigem());
        venda.setIdOrigem(getUsuarioLogado().getUuid());
        if (venda.getIdCliente().getTelefoneCelular() == null) {
            String telefone = venda.getIdCliente().getTelefoneComercial().replaceAll(".* ", "").replace("-", "");
            if (telefone.length() == 9) {
                venda.setTelefoneCliente(venda.getIdCliente().getTelefoneComercial());
            }
        } else {
            venda.setTelefoneCliente(venda.getIdCliente().getTelefoneCelular());
        }
        venda = vendaService.salvar(contaService.parcelarVenda(venda));
        String tipoSms = dados.getVenda() == null ? "cadastro" : "alteração";
        if (!requestDoAppAgenda) {
            Object o = aprovarOrcamento(new AtualizaOrcamentoDTO(EnumTipoAtualizacao.APROVAR), venda.getId());
            if (!(o instanceof String)) {
                String msg = ((RetornoWs.Erros) ((Response) o).getEntity()).getErro().stream()
                        .map(Erro::getMotivo)
                        .reduce("", (a, b) -> a + ", " + b).substring(2);
                return gerarErro("O agendamento foi criado mas não foi possível aprova-lo. " + msg);
            }
            tipoSms = "aprovar";
        }
        try {
            enviarSmsNovaSolicitacaoAgendamento(venda, tipoSms);
        } catch (CadastroException e) {
            enviarEmailErroSMS(e.getMessage());
        }
        return venda.getId();
    }

    private void enviarSmsNovaSolicitacaoAgendamento(Venda venda, String tipo) {
        ParametroSistema parametroSistema = parametroSistemaService.getParametro();
        List<SmsDTO> listaEnvio = new ArrayList<>();
        String titulo;
        switch (tipo) {
            case "cadastro":
                titulo = "Nova solicitação de agendamento. ";
                if ("S".equals(parametroSistema.getEnviaSmsClienteAgendamento())) {
                    listaEnvio.add(new SmsDTO(getCorpoSms(titulo, venda), venda.getTelefoneCliente()));
                }
                break;
            case "alteração":
                titulo = "Alteração de agendamento. ";
                if ("S".equals(parametroSistema.getEnviaSmsClienteAlteracaoAgendamento())) {
                    listaEnvio.add(new SmsDTO(getCorpoSms(titulo, venda), venda.getTelefoneCliente()));
                }
                break;
            case "aprovar":
                titulo = "Aprovação de agendamento. ";
                break;
            case "reagendar":
                titulo = "Mudança de horário/data. ";
                if ("S".equals(parametroSistema.getEnviaSmsClienteAlteracaoAgendamento())) {
                    listaEnvio.add(new SmsDTO(getCorpoSms(titulo, venda), venda.getTelefoneCliente()));
                }
                break;
            default:
                return;
        }
        if ("S".equals(parametroSistema.getEnviaSmsEmpresaSolicitacao())) {
            if (parametroSistema.getCelularEnvioSms() == null) {
                throw new CadastroException("Falta o cadastro do celular para recebimento do SMS de agendamento. Realize o cadastro em PARAMETRO SISTEMA -> AGENDAMENTO", null);
            }
            listaEnvio.add(new SmsDTO(getCorpoSms(titulo, venda), parametroSistema.getCelularEnvioSms()));
        }
        if ("S".equals(parametroSistema.getEnviaSmsFuncionarioConfirmacao())) {
            if (venda.getIdUsuarioVendedor().getIdFuncionario().getTelefoneCelular() == null) {
                throw new CadastroException("Falta o cadastro do celular do funcionario para recebimento do SMS de agendamento. Realize o cadastro do telefone do funcionario para o envio do SMS.", null);
            }
            listaEnvio.add(new SmsDTO(getCorpoSms(titulo, venda), venda.getIdUsuarioVendedor().getIdFuncionario().getTelefoneCelular()));
        }

        listaEnvio.forEach(smsService::send);
    }

    public void enviarEmailErroSMS(String erro) {
        try {
            Empresa empresa = empresaService.getEmpresa();
            Usuario destinatario = new Usuario();
            destinatario.setEmail(empresa.getEmail());
            destinatario.setNome(empresa.getDescricao());
            EmailDTO email = new EmailDTO();
            email.getDestinatarios().add(destinatario);
            email.setAssunto("Erro ao enviar SMS agendamento");
            email.setMensagem(erro);
            emailService.enviarEmailMS(email, "S");
        } catch (EmailException e) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static String getCorpoSms(String titulo, Venda venda) {
        String nomeFuncionario = null;
        if (venda.getIdUsuarioVendedor().getIdFuncionario() != null) {
            nomeFuncionario = venda.getIdUsuarioVendedor().getIdFuncionario().getNome();
        }
        if (nomeFuncionario == null) {
            nomeFuncionario = venda.getIdUsuarioVendedor().getNome();
        }
        return new StringBuilder().append(titulo)
                .append(" Cliente: ").append(venda.getIdCliente().getNome()).append(".")
                .append(" Data: ").append(DataUtil.dateToString(venda.getDataVencimento(), "dd/MM/yyyy HH:mm:ss")).append(".")
                .append(" Atendente: ").append(nomeFuncionario).toString();
    }

    public List<HorarioDisponivelDTO> listaTempoLivre(String dataStr, Integer funcionario, Integer[] servico, Venda vendaReferencia, boolean validOnly) {
        final Funcionario employee = logarComIdFuncionario(funcionario);
        final Date data = DataUtil.converterStringParaDate(dataStr, "yyyy-MM-dd");
        final boolean previousDay = DataUtil.diferencaEntreDias(new Date(), data) < 0;
        final boolean sameDay = DataUtil.diferencaEntreDias(new Date(), data) == 0;
        final Time interval = parametroSistemaService.getIntervaloAgendamento();
        final Time horaAtual = new Time(new Date());
        final List<Venda> listVendasDoFuncionario = vendaService.getListaFiltradaSemTenant(new VendaFiltro(data, employee));
        final FuncionarioAtendimento fa = employee.getFuncionarioAtendimentoDoDia(DataUtil.getDiaDaSemana(data));
        if (fa == null) {
            return new ArrayList<>();
        }
        Time aux = new Time(fa.getHoraInicial());
        Time end = new Time(fa.getHoraFinal()).sub(1, Time.Unit.SECOND);
        List<HorarioDisponivelDTO> listDisponibilidade = new ArrayList<>();
        while (true) {
            //pula o tempo de um serviço sendo executado
            listVendasDoFuncionario.stream()
                    .filter(venda -> {
                        // Não contabiliza o tempo gasto da venda que está sendo editada
                        if (vendaReferencia != null && venda.getId().equals(vendaReferencia.getId())) {
                            return false;
                        }
                        return aux.compareTo(new Time(venda.getDataVencimento())) == 0;
                    })
                    .findAny()
                    .ifPresent(venda -> {
                        Time tempoGasto = venda.getVendaServicoList().stream()
                                .map(VendaServico::getIdServico)
                                .map(Servico::getTempoExecucao)
                                .filter(Objects::nonNull)
                                .map(Time::new)
                                .reduce(aux.clone(), Time::add);
                        while (tempoGasto.compareTo(aux) > 0) {
                            listDisponibilidade.add(new HorarioDisponivelDTO(aux.clone(), false));
                            aux.add(interval);
                        }
                    });
            if (aux.compareTo(end) >= 0) {
                break;
            }
            listDisponibilidade.add(new HorarioDisponivelDTO(aux.clone(), !previousDay && (!sameDay || horaAtual.compareTo(aux) < 0)));
            aux.add(interval);
        }
        final Time total = getTimeFromServices(servico);
        listDisponibilidade.stream()
                .filter(HorarioDisponivelDTO::getLivre)
                .forEachOrdered(hdDTO -> {
                    Time totalSomado = hdDTO.obterHorario().clone().add(total);
                    listDisponibilidade.stream()
                            .filter(subList -> subList.obterHorario().compareTo(hdDTO.obterHorario()) > 0 && subList.obterHorario().compareTo(totalSomado) < 0 && !subList.getLivre())
                            .forEachOrdered(subList -> subList.setLivre(false));
                });
        if (validOnly) {
            return listDisponibilidade.stream()
                    .filter(HorarioDisponivelDTO::getLivre)
                    .collect(Collectors.toList());
        }
        return listDisponibilidade;
    }

    public Object listarEmpresasAgendamento(MultivaluedMap<String, String> urlInfo) {
        EmpresaFiltro ef = getFilter(new EmpresaFiltro(), urlInfo);
        ef.setAgendaHabilitada("S");
        return empresaService.getListaFiltradaSemTenant(ef).stream()
                .map(empresaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Object listarFuncionariosAgendamento(MultivaluedMap<String, String> urlInfo) {
        if (urlInfo.getFirst("tenantID") == null) {
            return gerarErro(Status.BAD_REQUEST, "O id da empresa é obrigatório.");
        }
        EmpresaFiltro ef = new EmpresaFiltro();
        ef.setAgendaHabilitada("S");
        Integer id = Integer.parseInt(urlInfo.getFirst("tenantID"));
        boolean isValid = empresaService.getListaFiltradaSemTenant(ef).stream()
                .filter(empresa -> empresa.getId().equals(id))
                .count() > 0;
        if (!isValid) {
            return gerarErro("Empresa inválida.");
        }
        return funcionarioService.getListaFiltradaSemTenant(getFilter(new FuncionarioFiltro(), urlInfo)).stream()
                .map(funcionarioMapper::toMinDTO)
                .filter(funcionario -> !funcionario.getFuncionarioAtendimentoList().isEmpty() && !funcionario.getFuncionarioServicoList().isEmpty())
                .collect(Collectors.toList());
    }

    public Object cadastrarUsuarioAgendamento(UsuarioAppCadastroDTO dados) {
        try {
            Usuario user = usuarioMapper.toEntity(dados);
            if (dados.getFoto() != null && dados.getFoto().startsWith("data:image")) {
                user.setFoto(Base64.getDecoder().decode(dados.getFoto().split(",")[1]));
            }
            if (dados.getId() != null) {
                user.setSenha(usuarioService.buscar(dados.getId()).getSenha());
            }

            user = usuarioService.adicionarUsuario(user);

            return user.getId();

        } catch (LoginDuplicadoException ex) {
            return gerarErro("O e-mail informado já existe!");
        } catch (SenhaIncorretaException ex) {
            return gerarErro(ex.getMessage());
        }
    }

    private boolean podeMudarServicos(Date horaInicial, Integer funcionario, Integer[] servicoOriginal, Integer[] servicoNovo, Venda venda) {
        final Time totalOriginal = getTimeFromServices(servicoOriginal);
        final Time totalNovo = getTimeFromServices(servicoNovo);
        if (totalNovo.compareTo(totalOriginal) <= 0) {
            return true;
        }
        final Time horarioInicio = new Time(horaInicial);
        return listaTempoLivre(DataUtil.dateToString(horaInicial, "yyyy-MM-dd"), funcionario, servicoNovo, venda, true).stream()
                .filter(hd -> hd.obterHorario().compareTo(horarioInicio) == 0)
                .findAny()
                .orElse(new HorarioDisponivelDTO(horarioInicio, false))
                .getLivre();
    }

    private Time getTimeFromServices(Integer[] servicos) {
        return Arrays.asList(servicos).stream()
                .filter(Objects::nonNull)
                .map(servicoService::buscar)
                .filter(Objects::nonNull)
                .map(Servico::getTempoExecucao)
                .filter(Objects::nonNull)
                .map(Time::new)
                .reduce(new Time(), Time::add);
    }

    public Object reagendarAgendamento(ReagendamentoDTO dados) {
        Venda agendamento = vendaService.buscar(dados.getId());
        if (agendamento == null || !agendamento.getTenatID().equals(getTenatId())) {
            throw new NotFoundException();
        }
        String dia = DataUtil.dateToString(dados.getData(), "yyyy-MM-dd");
        Time horario = new Time(dados.getData());
        agendamento.setVendaServicoList(vendaService.listarVendaServico(agendamento));
        Integer[] servicos = new Integer[agendamento.getVendaServicoList().size()];
        for (int i = 0; i < agendamento.getVendaServicoList().size(); i++) {
            servicos[i] = agendamento.getVendaServicoList().get(i).getIdServico().getId();
        }
        List<HorarioDisponivelDTO> horariosDisponiveis = listaTempoLivre(dia, agendamento.getIdUsuarioVendedor().getIdFuncionario().getId(), servicos, null, true);
        if (horariosDisponiveis.stream().noneMatch(hd -> hd.obterHorario().equals(horario))) {
            return gerarErro("Não será possível fazer o reagendamento. Sem horários livres no dia selecionado.");
        }
        agendamento.setDataVencimento(dados.getData());
        vendaService.salvar(agendamento);
        enviarSmsNovaSolicitacaoAgendamento(agendamento, "reagendar");
        return OK;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funcionários">
    @Logado
    public Object listarFuncionario(MultivaluedMap<String, String> urlInfo) {
        FuncionarioFiltro filtro = getFilter(new FuncionarioFiltro(), urlInfo);
        return funcionarioService.getListaFiltrada(filtro).stream()
                .map(funcionarioMapper::toDTO)
                .map(func -> {
                    if (urlInfo != null && urlInfo.get("no-photo") != null) {
                        func.setFoto(null);
                    }
                    return func;
                })
                .collect(Collectors.toList());
    }

    @Logado
    public Object consultarFuncionario(String id, MultivaluedMap<String, String> urlInfo) {
        Funcionario employee = funcionarioService.buscar(Integer.parseInt(id));
        if (employee == null || !employee.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException("Não foi encontrado nenhum cliente com o ID " + id);
        }
        if (urlInfo != null && urlInfo.get("no-photo") != null) {
            employee.setFoto(null);
        }
        return funcionarioMapper.toDTO(employee);
    }

    @Logado
    public Object removerFuncionario(String id) {
        Funcionario employee = funcionarioService.buscar(Integer.parseInt(id));
        try {
            employee.setAtivo("N");
            funcionarioService.salvar(employee);
        } catch (Exception ex) {
            return gerarErro("Ocorreu um erro ao remover o funcionário.");
        }
        return OK;
    }

    @Logado
    public Object cadastrarFuncionario(FuncionarioDTO dto) {
        if (dto.getId() != null) {
            Funcionario func = funcionarioService.buscar(dto.getId());
            if (func.getAtivo().equals("N")) {
                throw new ForbiddenException("Funcionário inativo");
            }
            if (dto.getFoto() == null) {
                dto.setFoto(func.getFoto());
            }
        }
        dto.setAtivo("S");
        if (dto.getPlanoConta() != null && dto.getPlanoConta().getId() == null) {
            dto.setPlanoConta(null);
        }
        if (dto.getEhOrcamentista() == null) {
            dto.setEhOrcamentista("N");
        }
        Funcionario employee = funcionarioMapper.toEntity(dto);
        employee.setTenatID(getTenatId());
        Cidade city = cidadeService.buscarPeloCodigoIBGE(dto.getCodIBGE() + "");
        employee.setIdCidade(city);

        if (!CpfCnpjUtil.validarCPF(employee.getCpf())) {
            return gerarErro("CPF inválido!");
        }

        if (employee.getId() != null) {
            employee.setFuncionarioServicoList(funcionarioService.listarFuncionarioServicoByIdFuncionario(employee));
            employee.setFuncionarioAtendimentoList(funcionarioService.listarFuncionarioAtendimentoByIdFuncionario(employee));
        }

        String tenant = getUsuarioLogado().getTenat();
        try {
            if (dto.getFuncionarioServicoList() != null) {
                ListUtil.persist(employee.getFuncionarioServicoList(), dto.getFuncionarioServicoList().stream()
                        .map(fs -> funcionarioMapper.toEntity(fs, employee, tenant))
                        .collect(Collectors.toList()), FuncionarioServico::contains);
            }
            if (dto.getFuncionarioAtendimentoList() != null) {
                ListUtil.persist(employee.getFuncionarioAtendimentoList(), dto.getFuncionarioAtendimentoList().stream()
                        .map(fa -> funcionarioMapper.toEntity(fa, employee, tenant))
                        .collect(Collectors.toList()), FuncionarioAtendimento::contains);
            }
            return funcionarioMapper.toDTO(funcionarioService.salvarFuncionario(employee));
        } catch (SenhaIncorretaException | LoginDuplicadoException | UsuarioException | CadastroException | EmailException ex) {
            return gerarErro(ex.getMessage());
        } catch (Exception ex) {
            return gerarErro("Não foi possível salvar o funcionário!");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Pagamentos">
    @Logado
    public Object listarTipoPagamento(MultivaluedMap<String, String> urlInfo) {
        TipoPagamentoFiltro filtro = getFilter(new TipoPagamentoFiltro(), urlInfo);
        return tipoPagamentoService.getListaFiltrada(filtro).stream()
                .map(pagamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object buscarTipoPagamento(String id) {
        TipoPagamento tp = tipoPagamentoService.buscar(Integer.parseInt(id));
        if (tp == null || !tp.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException("Não foi encontrado nenhum tipo de pagamento com o ID " + id);
        }

        return pagamentoMapper.toDTO(tp);
    }

    @Logado
    public Object removerTipoPagamento(String id) {
        TipoPagamento tp = tipoPagamentoService.buscar(0);
        tp.setAtivo("N");
        tipoPagamentoService.salvar(tp);
        return OK;
    }

    @Logado
    public Object cadastrarTipoPagamento(TipoPagamentoDTO dto) {
        try {
            return pagamentoMapper.toDTO(tipoPagamentoService.importDto(dto));
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Ordem de serviço">
    @Logado
    public Object listarOS(MultivaluedMap<String, String> urlInfo) {
        OrdemDeServicoFiltro filtro = getFilter(new OrdemDeServicoFiltro(), urlInfo);
        return ordemDeServicoService.getListaFiltrada(filtro).stream()
                .map(ordemServicoMapper::toDTO)
                .map(dto -> {
                    if (dto.getFuncionarioEntrada() != null) {
                        dto.getFuncionarioEntrada().setFoto(null);
                    }
                    if (dto.getFuncionarioSaida() != null) {
                        dto.getFuncionarioSaida().setFoto(null);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Logado
    public Object consultarOS(Integer id) {
        return ordemServicoMapper.toDTO(ordemDeServicoService.buscar(id));
    }

    @Logado
    public Object cadastrarOS(OrdemServicoDTO dto) {
        try {
            OrdemDeServico osDb = null;
            if (dto.getId() != null) {
                osDb = ordemDeServicoService.buscar(dto.getId());
                if (osDb.getIdFuncionarioSaida() != null) {
                    return gerarErro("Esta ordem de serviço já está finalizada!");
                }
            }
            if (dto.getStatusOrdemDeServico() == null || dto.getStatusOrdemDeServico().isEmpty()
                    || dto.getStatusOrdemDeServico().get(0).getStatus() == null) {
                return gerarErro("Selecione um status");
            }
            OrdemDeServico os = ordemServicoMapper.toEntity(dto);
            os.setTenatID(getTenatId());
            if (dto.getId() == null) {
                Conta conta = new Conta();
                conta.setAdvemContrato("N");
                conta.setRepetirConta("N");
                conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
                conta.setTipoConta(EnumTipoConta.ORDEM_DE_SERVICO.getTipo());
                conta.setIdContaBancaria(contaBancariaService.getContaBancariaPadrao());
                conta.setIdPlanoConta(parametroSistemaService.getParametro().getIdPlanoContaVendaPadrao());
                os.setIdConta(conta);
                if (getUsuarioLogado().getIdFuncionario() != null) {
                    dto.setFuncionarioEntrada(new FuncionarioDTO(getUsuarioLogado().getIdFuncionario().getId()));
                }
            } else if (osDb != null) {
                os.setIdConta(osDb.getIdConta());
            } else {
                os.setIdConta(contaService.buscar(dto.getConta().getId()));
            }
            if (os.getIdConta().getIdPlanoConta() == null && dto.getConta() != null && dto.getConta().getPlanoConta() != null && dto.getConta().getPlanoConta().getId() != null) {
                os.getIdConta().setIdPlanoConta(planoContaService.buscar(dto.getConta().getPlanoConta().getId()));
            }
            String status = dto.getStatusOrdemDeServico().get(dto.getStatusOrdemDeServico().size() - 1).getStatus().getCodigo();
            return ordemServicoMapper.toDTO(ordemDeServicoService.salvar(os, status, getUsuarioLogado()));
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }

    @Logado
    public Object removerOS(Integer id) {
        OrdemDeServico os = ordemDeServicoService.buscar(id);
        if (os.getIdFuncionarioSaida() != null) {
            return gerarErro("Esta ordem de serviço já está finalizada!");
        }
        ordemDeServicoService.remover(os);
        return OK;
    }

    @Logado
    public Object imprimirOS(Integer id) {
        try {
            OrdemDeServico os = ordemDeServicoService.buscar(id);
            byte[] pdf = relatorioService.gerarOrdemServico(os);
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdf);
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }

    @Logado
    public Object finalizarOS(Integer id) {
        OrdemDeServico os = ordemDeServicoService.buscar(id);
        if (os.getIdFuncionarioSaida() != null) {
            return gerarErro("Esta ordem de serviço já está finalizada!");
        }
        os.setIdFuncionarioSaida(getUsuarioLogado().getIdFuncionario());
        ordemDeServicoService.salvar(os);
        return OK;
    }

    @Logado
    public Object atualizaStatusOS(Integer id, EnumStatusOrdemDeServico status) {
        OrdemDeServico os = ordemDeServicoService.buscar(id);
        if (os == null || !os.getTenatID().equals(getTenatId())) {
            throw new NotFoundException();
        }
        if (os.getIdFuncionarioSaida() != null) {
            return gerarErro("Esta ordem de serviço já está finalizada!");
        }
        if (status == EnumStatusOrdemDeServico.FINALIZADO) {
            os.setIdFuncionarioSaida(getUsuarioLogado().getIdFuncionario());
        }
        try {
            os.setItensOrdemDeServico(ordemDeServicoService.listarItens(os));
            os.setStatusOrdemDeServico(ordemDeServicoService.listarMudancasDeStatus(os));
            ordemDeServicoService.salvar(os, status.getCodigo(), getUsuarioLogado());
            return OK;
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Ordem de serviço nova">
    @Logado
    public Object listarOSnova(MultivaluedMap<String, String> urlInfo) {
        VendaFiltro vf = getFilter(new VendaFiltro(), urlInfo);
        vf.getOrigem().getValue().add(EnumOrigemVenda.SITE.getOrigem());
        vf.getOrigem().getValue().add(EnumOrigemVenda.APP.getOrigem());
        return vendaService.getListaFiltradaOS(vf).stream()
                .map(this::vendaToOsDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object consultarOSnova(Integer id) {
        Venda venda = vendaService.buscar(id);
        if (!(venda.getStatusNegociacao().equals(EnumTipoVenda.ORDEM_SERVICO.getSituacao()))) {
            throw new NotFoundException();
        }
        if (!venda.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException();
        }

        return vendaToOsDTO(venda);
    }

    private Object vendaToOsDTO(Venda venda) {
        OsRestDTO osDto = vendaMapper.toOsDTO(vendaToDto(venda));
        osDto.setPodeReabrir(vendaService.podeReabrirOS(venda));
        return osDto;
    }

    @Logado
    public Object cadastrarOSnova(OsCadastroRestDTO osDto) {
        Venda venda;
        try {
            if (osDto.getId() != null) {
                venda = vendaService.buscar(osDto.getId());
                if (venda.getStatusOS().equals("F")) {
                    return gerarErro("Ordem de Serviço já finalizada.");
                }
                if (venda.getStatusOS().equals("CC")) {
                    return gerarErro("Orçamento rejeitado.");
                }
            }
            if (osDto.getTipo() == null) {
                osDto.setTipo(EnumTipoVenda.ORDEM_SERVICO.getSituacao());
            }

            venda = vendaMapper.toEntityFromDB(osDto, adHocTenant.getTenantID());
            venda.getVendaServicoList().stream()
                    .filter(vs -> vs.getQuantidade() == null)
                    .forEach(vs -> vs.setQuantidade(1));
            venda.setIdUsuarioVendedor(getUsuarioLogado());
            venda.setNumParcela(1);
            venda.setOrigem(EnumOrigemVenda.APP.getOrigem());
            venda.setIdOrigem(getUsuarioLogado().getUuid());

            if (venda.getStatusOS() == null) {
                venda.setStatusOS("AE");
            }

        } catch (CadastroException ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível preparar para salvar a venda.");
        }
        try {
            vendaService.salvar(venda);
            return venda.getId();
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível salvar a venda.");
        }
    }

    @Logado
    public Object cancelarOS(Integer id) {
        Venda venda = vendaService.buscar(id);
        if (!venda.getSituacao().equals("A") && (venda.getStatusOS().equals("AE") || venda.getStatusOS().equals("EE"))) {
            throw new NotFoundException();
        }

        try {
            venda.setDataCancelamento(new Date());
            venda.setStatusOS("CC");
            vendaService.salvar(venda);
            return vendaToDto(venda);
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível cancelar a ordem de serviço.");
        }
    }

    @Logado
    public Object imprimirOSnova(Integer id) {
        try {
            Venda venda = vendaService.buscar(id);
            byte[] pdf;
            if (getEmpresa().getIdCategoriaEmpresa() != null && getEmpresa().getIdCategoriaEmpresa().getId().equals(51)) {
                pdf = relatorioService.gerarImpressaoOSVeiculo(venda);
            } else {
                pdf = relatorioService.gerarImpressaoOS(venda);
            }
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdf);
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }

    @Logado
    public Object imprimirGarantia(Integer id) {
        try {
            Venda venda = vendaService.buscar(id);
            byte[] pdf = relatorioService.gerarImpressaoGarantia(venda);

            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdf);
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }

    @Logado
    public Object imprimirPermanencia(Integer id) {
        try {
            Venda venda = vendaService.buscar(id);
            byte[] pdf = relatorioService.gerarImpressaoPermanencia(venda);

            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdf);
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }

    @Logado
    public Object executarOS(Integer id) {
        Venda venda = vendaService.buscar(id);
        venda.setStatusOS(EnumStatusOS.EM_EXECUCAO.getCodigo());
        venda.setDataInicioExecucao(new Date());
        vendaService.alterar(venda);
        return OK;
    }

    @Logado
    public Object reabrirOS(Integer id) {
        Venda venda = vendaService.buscar(id);
        venda.setStatusOS(EnumStatusOS.AGUARDANDO_EXECUCAO.getCodigo());
        vendaService.alterar(venda);
        return OK;
    }

    public Object finalizarOSnova(Integer id) {
        AtualizaOrcamentoDTO aoDto = new AtualizaOrcamentoDTO();
        aoDto.setTipoAtualizacao(EnumTipoAtualizacao.APROVAR);
        aoDto.setFormaPagamento(new VendaFormaPagamentoCadastroRestDTO());
        aprovarOrcamento(aoDto, id);
        return OK;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Compras">
    @Logado
    public Object listarCompras(MultivaluedMap<String, String> urlInfo) {
        CompraFiltro cf = getFilter(new CompraFiltro(), urlInfo);
        return compraService.getListaFiltrada(cf).stream()
                .map(compraMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object consultarCompra(String id) {
        return compraMapper.toDTO(compraService.buscar(Integer.parseInt(id)));
    }

    @Logado
    public Object cadastrarCompra(CompraDTO compraDTO) {
        try {
            ParametroSistema parametroSistemaSelecionado = parametroSistemaService.getParametro();
            Compra compra = compraMapper.toEntityFromDB(compraDTO, adHocTenant.getTenantID());
            compra.setValorTotal(compraService.calcularValorTotal(compra.getListCompraProduto(), compra.getListParcelaDTO()));
            compra = contaService.parcelarCompra(compra);
            compra.setIdPlanoConta(parametroSistemaSelecionado.getIdPlanoContaCompraPadrao());
            compra.setDataPedido(new Date());
            return compraService.salvar(compra).getId();
        } catch (NumberFormatException | IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro(ex.getMessage());
        }
    }

    @Logado
    public Object removerCompra(String id) {
        Compra compra = compraService.buscar(Integer.parseInt(id));
        if (!compra.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException();
        }

        try {
            if (contaService.existeParcelaPaga(compra)) {
                return gerarErro("Existem parcelas pagas para essa compra, para cancelar a compra é necessário antes cancelar essas parcelas.");
            }
            compraService.cancelarCompra(compra);
            return OK;
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível cancelar a compra");
        }
    }

    @Logado
    public Object importarCompra(CompraImportacaoDTO importacao) {
        try {
            Compra compra = compraService.processarXML(importacao.getXml().getBytes(StandardCharsets.UTF_8));
            List<CompraProduto> listCompraProduto = compra.getListCompraProduto();
            listCompraProduto.stream().forEach(cp -> cp.setControle(StringUtil.gerarStringAleatoria(8)));

            // verifica se existe algum produto que ainda nao esta cadastrado
            boolean temProdutoNaoCadastrado = listCompraProduto.stream()
                    .anyMatch(prod -> prod.getDadosProduto().getIdProduto() == null);
            if (temProdutoNaoCadastrado) {
                if (importacao.getProdutos() == null || importacao.getProdutos().isEmpty()) {
                    return Response
                            .status(Status.fromStatusCode(100))
                            .entity(new RetornoCompraDTO(RetornoCompraDTO.Status.PRODUTO_PENDENTE, null, listCompraProduto.stream()
                                    .map(compraMapper::toDTO)
                                    .collect(Collectors.toList())))
                            .build();
                } else {
                    if (compra.getListCompraProduto().size() != importacao.getProdutos().size()) {
                        throw new CadastroException("A lista de produtos possui um tamanho incorreto.", null);
                    }
                    List<CompraProduto> listaImportacao = importacao.getProdutos().stream()
                            .map(dto -> compraMapper.toEntity(dto, compra))
                            .collect(Collectors.toList());
                    for (int i = 0; i < compra.getListCompraProduto().size(); i++) {
                        CompraProduto cp1 = compra.getListCompraProduto().get(i);
                        CompraProduto cp2 = listaImportacao.get(i);
                        if (!cp1.comparaParaImportacao(cp2)) {
                            throw new CadastroException("O item " + i + " não foi encontrado.", null);
                        }
                        if (cp2.getDadosProduto().getIdProduto().getId() == null) {
                            throw new CadastroException("O item " + i + " não possui um produto definido.", null);
                        }
                        cp1.getDadosProduto().setIdProduto(produtoService.buscar(cp2.getDadosProduto().getIdProduto().getId()));
                    }
                }
            }
            return new RetornoCompraDTO(RetornoCompraDTO.Status.SALVO, compraService.salvar(compra).getId(), null);
        } catch (JAXBException ex) {
            return gerarErro("Não foi possível ler o xml");
        } catch (CadastroException ex) {
            return gerarErro(ex.getMessage());
        } catch (Exception ex) {
            return gerarErro("Ocorreu um erro ao importar o XML");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Transportadoras">
    @Logado
    public Object listarTransportadoras(MultivaluedMap<String, String> urlInfo) {
        TransportadoraFiltro tf = getFilter(new TransportadoraFiltro(), urlInfo);
        return transportadoraService.getListaFiltrada(tf).stream()
                .map(transportadoraMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object consultarTransportadora(String id) {
        return transportadoraMapper.toDTO(transportadoraService.buscar(Integer.parseInt(id)));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Estoque">
    @Logado
    public Object listarMovimentacaoEstoque(MultivaluedMap<String, String> urlInfo) {
        EstoqueFiltro cf = getFilter(new EstoqueFiltro(), urlInfo);
        return estoqueService.getListaFiltrada(cf).stream()
                .map(MovimentacaoEstoqueDTO::new)
                .collect(Collectors.toList());
    }

    @Logado
    public Object cadastrarMovimentacaoEstoque(CadastroMovimentacaoEstoqueDTO dados) {
        Estoque estoque = new Estoque();
        estoque.setIdProduto(produtoService.buscar(dados.getProduto()));
        estoque.setQuantidade(dados.getQuantidade());
        estoque.setTipoOperacao(dados.getTipo().getTipo());
        estoque.setData(new Date());
        estoque.setPrimeiroCadastro("N");
        return estoqueService.salvar(estoque).getId();
    }

    @Logado
    public Object imprimirGiroEstoque(Integer idProduto, Date dataInicial, Date dataFinal) {
        try {
            Produto produto = null;
            if (idProduto != null) {
                produto = produtoService.buscar(idProduto);
            }
            byte[] pdf = relatorioService.gerarRelatorioGiroEstoque(produto, empresaService.getEmpresa(), dataInicial, dataFinal);
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdf);
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }

    @Logado
    public Object imprimirPosicaoEstoque(Integer idProduto) {
        try {
            Produto produto = null;
            if (idProduto != null) {
                produto = produtoService.buscar(idProduto);
            }
            byte[] pdf = relatorioService.gerarRelatorioPosicaoEstoque(produto, empresaService.getEmpresa(), "comValores");
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdf);
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }

    @Logado
    public Object imprimirMovimentoEstoque(Integer idProduto, Date dataInicial, Date dataFinal) {
        try {
            Produto produto = null;
            if (idProduto != null) {
                produto = produtoService.buscar(idProduto);
            }
            byte[] pdf = relatorioService.gerarRelatorioMovimentoEstoque(produto, empresaService.getEmpresa(), dataInicial, dataFinal);
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdf);
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }

    @Logado
    public Object imprimirExtratoEstoque(Integer idProduto, Date dataInicial, Date dataFinal) {
        try {
            Produto produto = null;
            if (idProduto != null) {
                produto = produtoService.buscar(idProduto);
            }
            byte[] pdf = relatorioService.gerarRelatorioExtratoEstoque(produto, empresaService.getEmpresa(), dataInicial, dataFinal);
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdf);
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="NFS">
    @Logado
    public Object listarNfs(MultivaluedMap<String, String> urlInfo) {
        NotaFiscalServicoFiltro nf = getFilter(new NotaFiscalServicoFiltro(), urlInfo);
        return nfsService.getListaFiltrada(nf).stream()
                .map(nfsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Logado
    public Object listarCtiss(Integer municipioIssqn) {
        if (municipioIssqn != null) {
            return ctissService.listarCtissRelacionadoCnaeEmpresa(empresaService.getEmpresa(), cidadeService.buscar(municipioIssqn)).stream()
                    .map(nfsMapper::toDTO)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Logado
    public Object cadastrarNfs(NfsDTO nfs) {
        List<ContaParcela> listaCP = null;
        NFS nfse = nfsMapper.toEntity(nfs);
        if (nfs.getId() != null) {
            listaCP = nfsService.buscar(nfs.getId()).getContaParcelaList();
            nfse.setContaParcelaList(null);
        }
        nfse = nfsService.salvarNotaFiscalServico(nfse);
        final NFS nfsFinal = nfse;
        if (listaCP != null) {
            listaCP.forEach(cp -> {
                cp.setIdNFS(nfsFinal);
                contaService.salvarContaParcela(cp);
            });
        }

        return nfsMapper.toDTO(nfse);
    }

    @Logado
    public Object removerNFS(String id) {
        NFS nfs = nfsService.buscar(Integer.parseInt(id));
        if (!nfs.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException();
        }

        try {
            nfsService.remover(nfs);
            return OK;
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível cancelar a compra");
        }
    }

    @Logado
    public Object imprimirNFS(Integer id) {
        try {
            NFS nfs = nfsService.buscar(id);
            Empresa empresa = empresaService.getEmpresa();
            byte[] pdf = notaServicoService.gerarNotaFiscalServicoByte(nfs, empresa);
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdf);
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }

    @Logado
    public Object baixarXML(Integer id) {
        try {
            NFS nfs = nfsService.buscar(id);
            byte[] zip = nfsService.compactarXmlsNFS(nfs);
            return "data:application/x-rar-compressed;base64," + Base64.getEncoder().encodeToString(zip);
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível baixar o XML da Nfs-e.");
            }
        }
    }

    @Logado
    public Object enviarEmailNfs(Integer id) {
        try {
            NFS nfs = nfsService.buscar(id);
            Empresa empresa = empresaService.getEmpresa();
            if (StringUtils.isEmpty(nfs.getIdCliente().getEmail())) {
                return gerarErro("Email não informado para o cliente");
            }
            nfsService.enviarXmlPorEmail(nfs, empresa);
            return "Arquivo Xml enviado com sucesso! Email de envio: " + nfs.getIdCliente().getEmail();
        } catch (org.apache.commons.mail.EmailException ex) {
            return gerarErro(ex.getMessage());
        }
    }

    @Logado
    public Object consultarNfs(String id) {
        return nfsMapper.toDTO(nfsService.buscar(Integer.parseInt(id)));
    }

    @Logado
    public Object trasmitirNfs(Integer id) {
        try {
            Empresa empresa = empresaService.getEmpresa();
            NFS nfs = nfsService.buscar(id);
            String ambiente = parametroSistemaService.getAmbienteNotaFiscalProduto();

            String xmlEnvio = nfsService.gerarXmlEnvioNfse(nfs, ambiente);

            if ("A3".equals(empresa.getTipoCertificadoDigital())) {
                return gerarErro("Assinatura com certificado A3 não suportada!");
            }

            String xmlAssinado = nfsService.assinarXmlA1(empresa, ambiente, xmlEnvio);
            nfsService.transmitirNfse(nfs, xmlAssinado);

            return OK;
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }

    @Logado
    public Object cancelarNfs(Integer id) {
        try {
            Empresa empresa = empresaService.getEmpresa();
            NFS nfs = nfsService.buscar(id);
            String ambiente = parametroSistemaService.getAmbienteNotaFiscalProduto();

            String xmlEnvio = nfsService.obterXmlCancelamento(nfs, ambiente);

            if ("A3".equals(empresa.getTipoCertificadoDigital())) {
                return gerarErro("Assinatura com certificado A3 não suportada!");
            }

            xmlEnvio = nfsService.assinarXmlCancelamentoNfs(empresa, ambiente, xmlEnvio);
            nfsService.cancelarNfse(empresa, ambiente, nfs, xmlEnvio);
            if (!EnumSituacaoNF.CANCELADA.getSituacao().equals(nfs.getSituacao())) {
                return gerarErro("Erro ao cancelar nota!");
            }
            return OK;
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cidade">
    public Object listarCidades(MultivaluedMap<String, String> urlInfo) {
        return cidadeService.getListaFiltrada(getFilter(new CidadeFiltro(), urlInfo)).stream()
                .map(cidadeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Object buscarCidade(Integer id) {
        List<CidadeDTO> cidades = new ArrayList<>();
        cidades.add(cidadeMapper.toDTO(cidadeService.buscar(id)));
        return cidades;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="NF">
    @Logado
    public Object listarNf(MultivaluedMap<String, String> urlInfo) {
        return null;
    }

    @Logado
    public Object removerNF(String id) {
        NF nf = nfService.buscar(Integer.parseInt(id));
        if (!nf.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException();
        }

        try {
            nfService.remover(nf);
            return OK;
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            return gerarErro("Não foi possível cancelar a compra");
        }
    }

    @Logado
    public Object trasmitirNf(Integer id, String ambiente) {
        NF nf = nfService.buscar(id);

        // ValidaNF-e
        if (nf == null || nf.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException();
        }
        if (nf.getSituacao().equals(EnumSituacaoNF.ENVIADA.getSituacao())) {
            return gerarErro("Nota já emitida.");
        }
        String just = nf.getJustificativaDevolucao();
        if (nf.getIdNfReferencia() != null && (just == null || just.isEmpty())) {
            return gerarErro("Informe a justificativa para devolução.");
        }

        // Valida ambiente
        if (!ambiente.matches("[12]")) {
            return gerarErro("Ambiente inválido.");
        }

        //Valida servidor sefaz
        String situacaoAmbiente = nfeProdutoService.consulta();
        if (situacaoAmbiente != null && !situacaoAmbiente.isEmpty() && !situacaoAmbiente.endsWith(": Operacional.")) {
            return gerarErro(situacaoAmbiente + " A ação não pôde ser realizada.");
        }

        // Valida certificado
        Empresa empresa = empresaService.getEmpresa();
        if (empresa.getTipoCertificadoDigital() == null) {
            return gerarErro("Certificado digital não informado!");
        } else if ("A3".equals(empresa.getTipoCertificadoDigital())) {
            return gerarErro("Assinatura com certificado A3 não suportada!");
        }
        ValidacaoNFeDTO validacao = nfeProdutoService.empresaAptaEmitirNF(ambiente);
        if (!validacao.isTodosOK()) {
            return Response.status(Status.NOT_ACCEPTABLE).entity(validacao).build();
        }

        try {
            if ("2".equals(parametroSistemaService.getAmbienteNotaFiscalProduto())) {
                nf.getObjTNFe().getInfNFe().getDest().setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
            }

            String xml = XmlUtil.removeAcentos(nfeProdutoService.obterXmlNfe(nf, parametroSistemaService.getAmbienteNotaFiscalProduto())
                    .replaceAll("\\s+<", "<").replaceAll(">\\s+", ">").replace("&", "&amp;"));

            nf = nfeProdutoService.transmitirNFe(nf, ambiente, xml, "NFe");

            if (EnumSituacaoNF.ENVIADA.getSituacao().equals(nf.getSituacao())
                    || EnumSituacaoNF.ENVIADA_DEVOLUCAO.getSituacao().equals(nf.getSituacao())) {
                return OK;
            } else {
                return gerarErro(nf.getMensagemErroEnvio());
            }
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }

    @Logado
    public Object imprimirNf(Integer id) {
        try {
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(danfeService.gerarDanfeBtye(getEmpresa(), nfService.buscar(id)));
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage() != null) {
                return gerarErro(ex.getMessage());
            } else {
                return gerarErro("Não foi possível gerar o PDF.");
            }
        }
    }

    @Logado
    public Object enviarEmailNf(Integer id) {
        try {
            NF nf = nfService.buscar(id);
            if (StringUtils.isEmpty(nf.getIdCliente().getEmail())) {
                return gerarErro("Email não informado para o cliente");
            }
            nfService.enviarXmlPorEmail(nf, empresaService.getEmpresa(), vendaService.listarVendaProduto(nf.getIdVenda()));
            return "Arquivo Xml enviado com sucesso! Email de envio: " + nf.getIdCliente().getEmail();
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Veículo">
    public Object listarMarca(String tipo, MultivaluedMap<String, String> urlInfo) {
        List<Marca> marcas = new ArrayList<>();
        if (tipo != null && !tipo.isEmpty()) {
            marcas.addAll(fipeService.listaMarcasSistema(EnumTipoVeiculoFipe.valueOf(tipo.toUpperCase())));
        } else {
            marcas.addAll(fipeService.listaMarcasSistema());
        }
        return marcas.stream()
                .map(fipeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Object listarModelo(Integer id, MultivaluedMap<String, String> urlInfo) {
        Marca marca = fipeService.getMarca(id);
        if (marca == null) {
            throw new NotFoundException();
        }
        List<Modelo> modelos = new ArrayList<>();
        if (marca.getFipeOrder() != null) {
            modelos.addAll(fipeService.listaModelosSistema(EnumTipoVeiculoFipe.valueOf(marca.getFipeOrder().toUpperCase()), marca));
        } else {
            modelos.addAll(fipeService.listaModelosSistema(marca));
        }
        return modelos.stream()
                .map(fipeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Object listarCombustivel() {
        return combustivelService.listar().stream()
                .map(DescricaoDTO::new)
                .collect(Collectors.toList());
    }

    public List<String> listarCategoriaCNH() {
        return EnumCategoriaCNH.retornaTodasCategorias();
    }

    public List<String> listarTipoVeiculos() {
        return EnumTipoVeiculoFipe.retornaTodosTipos();
    }

    public Object listarCorVeiclo() {
        return corVeiculoService.listar().stream()
                .map(DescricaoDTO::new)
                .collect(Collectors.toList());
    }

    public List<ModeloInformacaoDTO> listarInformacaoModelo(Integer idMarca, Integer idModelo) {
        Marca marca = fipeService.getMarca(idMarca);
        if (marca == null) {
            throw new NotFoundException();
        }
        Modelo modelo = fipeService.getModelo(marca, idModelo);
        if (modelo == null) {
            throw new NotFoundException();
        }
        return fipeService.listaModeloInformacaosSistema(EnumTipoVeiculoFipe.valueOf(modelo.getTipo().toUpperCase()), modelo).stream()
                .map(fipeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Object listarAnosModelo(Integer idMarca, Integer idModelo) {
        return listarInformacaoModelo(idMarca, idModelo).stream()
                .map(ModeloInformacaoDTO::getAno)
                .collect(Collectors.toList());
    }

    public Object valorProtegidoFipe(Integer idMarca, Integer idModelo, Integer anoModelo) {
        ModeloInformacaoDTO mi = listarInformacaoModelo(idMarca, idModelo)
                .stream().filter(ano -> ano.getAno().equals(anoModelo))
                .findAny().orElse(null);

        return mi != null ? mi.getPreco() : 0d;
    }

    public Object adicionaVeiculoParaCliente(Integer id, VeiculoDTO dto) {
        Cliente cliente = clienteService.buscar(id);
        ClienteVeiculo cv = veiculoMapper.toEntity(dto);
        cliente.setClienteVeiculoList(clienteVeiculoService.listaVeiculosPor(cliente));
        cliente.getClienteVeiculoList().add(cv);
        clienteService.salvar(cliente);
        return OK;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Vistoria -> Avaria">
    public Object listarAvarias(MultivaluedMap<String, String> urlInfo) {
        AvariaFiltro filtro = getFilter(new AvariaFiltro(), urlInfo);
        return avariaService.getListaFiltrada(filtro).stream()
                .map(vistoriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Object buscarAvaria(Integer id) {
        Avaria avaria = avariaService.buscar(id);
        if (avaria == null || !avaria.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException("Não foi encontrado nenhuma avaria com o ID " + id);
        }
        return vistoriaMapper.toDTO(avaria);
    }

    public Object cadastrarAvaria(AvariaDTO avaria) {
        try {
            return vistoriaMapper.toDTO(avariaService.importDto(avaria, getTenatId()));
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }

    public Object removerAvaria(Integer id) {
        Avaria avaria = avariaService.buscar(id);
        avaria.setAtivo("N");
        avariaService.salvar(avaria);
        return OK;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Vistoria -> Formulário">
    public Object listarFormularios(MultivaluedMap<String, String> urlInfo) {
        FormularioFiltro filtro = getFilter(new FormularioFiltro(), urlInfo);
        return formularioService.getListaFiltrada(filtro).stream()
                .map(vistoriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Object buscarFormulario(Integer id) {
        Formulario formulario = formularioService.buscar(id);
        if (formulario == null || !formulario.getTenatID().equals(getUsuarioLogado().getTenat())) {
            throw new NotFoundException("Não foi encontrado nenhum formulário com o ID " + id);
        }
        return vistoriaMapper.toDTO(formulario);
    }

    @Logado
    public Object cadastrarFormulario(FormularioDTO formulario) {
        try {
            return vistoriaMapper.toDTO(formularioService.importDto(formulario, getTenatId()));
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }

    @Logado(requerEmpresa = false)
    public Object cadastrarFormularioResposta(FormularioRespostaDTO formularioResposta) {
        final Usuario usuario = getUsuarioLogado();
        String tenantID = usuario.getTenat();
        if (tenantID == null) {
            // Se for o usuário pelo App ABAPAV
            List<Cliente> clientes = clienteService.buscarPor(usuario);
            if (!clientes.isEmpty()) {
                tenantID = clientes.get(0).getTenatID();
            }
        }
        logarComTenant(tenantID);
        try {
            return vistoriaMapper.toDTO(formularioRespostaService.importDto(formularioResposta, tenantID));
        } catch (Exception ex) {
            return gerarErro(ex.getMessage());
        }
    }

    public Object buscarFormularioResposta(Integer id) {
        final Usuario usuario = getUsuarioLogado();
        String tenantID = usuario.getTenat();
        if (tenantID == null) {
            // Se for o usuário pelo App ABAPAV
            List<Cliente> clientes = clienteService.buscarPor(usuario);
            if (!clientes.isEmpty()) {
                tenantID = clientes.get(0).getTenatID();
            } else {
                return gerarErro(Status.NOT_ACCEPTABLE, "Usuario inválido.");
            }

        }
        logarComTenant(tenantID);
        FormularioResposta formularioResposta = formularioRespostaService.buscar(id);
        if (formularioResposta == null || !formularioResposta.getTenatID().equals(tenantID)) {
            throw new NotFoundException("Não foi encontrado nenhum formulário resposta com o ID " + id);
        }
        return vistoriaMapper.toDTO(formularioResposta);
    }

    public Object listarFormulariosResposta(MultivaluedMap<String, String> urlInfo) {
        FormularioRespostaFiltro filtro = getFilter(new FormularioRespostaFiltro(), urlInfo);
        final Usuario usuario = getUsuarioLogado();
        String tenantID = usuario.getTenat();
        if (tenantID == null) {
            // Se for o usuário pelo App ABAPAV
            List<Cliente> clientes = clienteService.buscarPor(usuario);
            if (!clientes.isEmpty()) {
                tenantID = clientes.get(0).getTenatID();
                filtro.setCliente(clientes.get(0));
            } else {
                return gerarErro(Status.NOT_ACCEPTABLE, "Usuario inválido.");
            }
        }
        logarComTenant(tenantID);
        return formularioRespostaService.getListaFiltrada(filtro).stream()
                .map(vistoriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Object removerFormulario(Integer id) {
        Formulario formulario = formularioService.buscar(id);
        formulario.setAtivo("N");
        formularioService.salvar(formulario);
        return OK;
    }
    //</editor-fold>

}
