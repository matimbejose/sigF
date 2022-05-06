package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.entity.Time;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioServico;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.ServicoProduto;
import br.com.villefortconsulting.sgfinancas.entidades.TipoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.VendaFormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CentroCustoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContatoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.DocumentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FornecedorDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VeiculoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.COFINS;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.COFINSST;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ICMS;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.II;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ISSQN;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.PIS;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.PISST;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.TipoImposto;
import br.com.villefortconsulting.util.NumeroUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.StringUtils;

public class EntityMapper {

    @Inject
    private UsuarioMapper usuarioMapper;

    @Inject
    private ClienteMapper clienteMapper;

    @Inject
    private CentroCustoMapper centroCustoMapper;

    @Inject
    private PlanoContaMapper planoContaMapper;

    @Inject
    private ContaMapper contaMapper;

    @Inject
    private ProdutoMapper produtoMapper;

    @Inject
    private ServicoMapper servicoMapper;

    @Inject
    private DocumentoMapper documentoMapper;

    @Inject
    private FornecedorMapper fornecedorMapper;

    @Inject
    private VeiculoMapper veiculoMapper;

    public Integer map(EntityId obj) {
        return obj == null ? null : obj.getId();
    }

    public UsuarioDTO mapFull(Usuario obj) {
        return usuarioMapper.toDTO(obj);
    }

    public FornecedorDTO mapFull(Fornecedor obj) {
        return fornecedorMapper.toDTO(obj);
    }

    public String mapNome(Banco obj) {
        return obj == null ? null : obj.getDescricao();
    }

    public ClienteDTO mapFull(Cliente obj) {
        return clienteMapper.toDTO(obj);
    }

    public CentroCustoDTO mapFull(CentroCusto obj) {
        return centroCustoMapper.toDTO(obj);
    }

    public PlanoContaDTO mapFull(PlanoConta obj) {
        return planoContaMapper.toDTO(obj);
    }

    public ContaRestDTO mapFull(Conta obj) {
        return contaMapper.toDTO(obj);
    }

    public ProdutoDTO mapFull(Produto obj) {
        return produtoMapper.toDTO(obj);
    }

    public ServicoDTO mapFull(Servico obj) {
        return servicoMapper.toDTO(obj);
    }

    public Long map(Date obj) {
        return obj.getTime();
    }

    public VeiculoDTO map(ClienteVeiculo obj) {
        return veiculoMapper.toDto(obj);
    }

    public Produto mapProduto(Integer id) {
        return id == null ? null : new Produto(id);
    }

    public VendaProduto mapVendaProduto(Integer id) {
        return id == null ? null : new VendaProduto(id);
    }

    public Servico mapServico(Integer id) {
        return id == null ? null : new Servico(id);
    }

    public VendaServico mapVendaServico(Integer id) {
        return id == null ? null : new VendaServico(id);
    }

    public FormaPagamento mapFormaPagamento(Integer id) {
        return id == null ? null : new FormaPagamento(id);
    }

    public VendaFormaPagamento mapVendaFormaPagamento(Integer id) {
        return id == null ? null : new VendaFormaPagamento(id);
    }

    public Usuario mapUsuario(Integer id) {
        return id == null ? null : new Usuario(id);
    }

    public Cliente mapCliente(Integer id) {
        return id == null ? null : new Cliente(id);
    }

    public CentroCusto mapCentroCusto(Integer id) {
        return id == null ? null : new CentroCusto(id);
    }

    public PlanoConta mapPlanoConta(Integer id) {
        return id == null ? null : new PlanoConta(id);
    }

    public Banco mapBanco(Integer id) {
        return id == null ? null : new Banco(id);
    }

    public Conta mapConta(Integer id) {
        return id == null ? null : new Conta(id);
    }

    public TipoPagamento mapTipoPagamento(Integer id) {
        return id == null ? null : new TipoPagamento(id);
    }

    public ContaBancaria mapContaBancaria(Integer id) {
        return id == null ? null : new ContaBancaria(id);
    }

    public Date map(Long data) {
        if (data == null) {
            return null;
        }
        return new Date(data);
    }

    public DocumentoDTO mapDocumentotoDocumentoDTO(Documento documento) {
        return documentoMapper.toDTO(documento);
    }

    public Map<Integer, Double> listaServicoProdutoToMap(List<ServicoProduto> list) {
        Map<Integer, Double> map = new HashMap<>();
        list.forEach(item -> map.put(item.getId(), item.getQuantidade()));
        return map;
    }

    public List<ServicoProduto> listaServicoProdutoToList(Map<Integer, Double> map) {
        List<ServicoProduto> list = new ArrayList<>();
        if (map != null) {
            map.forEach((id, qte) -> {
                ServicoProduto sp = new ServicoProduto();
                sp.setId(id);
                sp.setQuantidade(qte);
                list.add(sp);
            });
        }
        return list;
    }

    public Map listToMap(List<JAXBElement<?>> lista) {
        Map<String, TipoImposto> mapa = new HashMap<>();
        lista.forEach(elem -> mapa.put(elem.getName().getLocalPart(), (TipoImposto) elem.getValue()));
        return mapa;
    }

    public List<JAXBElement> mapToList(Map<String, TipoImposto> mapa, NFeMapper mapper) {
        List<JAXBElement> lista = new ArrayList<>();
        if (mapa == null) {
            return lista;
        }
        for (String item : new String[]{"ICMS", "IPI", "II", "PIS", "PISST", "COFINS", "COFINSST", "ISSQN", "ICMSUFDest", "vTotTrib"}) {
            Object v = mapa.get(item);
            if (v == null) {
                continue;
            }
            Object o = null;
            switch (v.getClass().getSimpleName()) {
                case "PISST":
                    o = mapper.toHis((PISST) v);
                    break;
                case "PIS":
                    o = mapper.toHis((PIS) v);
                    break;
                case "ISSQN":
                    o = mapper.toHis((ISSQN) v);
                    break;
                case "II":
                    o = mapper.toHis((II) v);
                    break;
                case "ICMS":
                    o = mapper.toHis((ICMS) v);
                    break;
                case "COFINSST":
                    o = mapper.toHis((COFINSST) v);
                    break;
                case "COFINS":
                    o = mapper.toHis((COFINS) v);
                    break;
                default:
                    break;
            }
            if (o != null) {
                lista.add(new JAXBElement(new QName("http://www.portalfiscal.inf.br/nfe", item), o.getClass(), o));
            }
        }
        return lista;
    }

    public String map(Double val) {
        if (val == null) {
            return null;
        }
        return NumeroUtil.formatarCasasDecimaisComPonto(val, 2);
    }

    public Time mapToTime(Date data) {
        return new Time(data);
    }

    public List<Integer> mapToIdFuncionarioList(List<FuncionarioServico> lista) {
        return lista.stream()
                .map(FuncionarioServico::getIdFuncionario)
                .map(Funcionario::getId)
                .collect(Collectors.toList());
    }

    public Servico toDTO(FuncionarioServico fs) {
        return fs.getIdServico();
    }

    public boolean map(String s) {
        if (s == null) {
            return false;
        }
        final String compare = s.toLowerCase();
        return Arrays.asList("s", "true", "1").stream().anyMatch(a -> a.equals(compare));
    }

    public String map(Boolean b) {
        return (b != null && b) ? "S" : "N";
    }

    public ContatoDTO map(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        ContatoDTO contato = new ContatoDTO();
        contato.setCep(endereco.getCep());
        contato.setComplemento(endereco.getComplemento());
        if (StringUtils.isNotEmpty(endereco.getNumero())) {
            contato.setNumero(Integer.parseInt(endereco.getNumero()));
        }
        contato.setBairro(endereco.getBairro());
        contato.setLogradouro(endereco.getEndereco());
        return contato;
    }

    public Endereco map(ContatoDTO contato) {
        if (contato == null) {
            return null;
        }
        Endereco endereco = new Endereco();
        endereco.setCep(contato.getCep());
        endereco.setComplemento(contato.getComplemento());
        endereco.setNumero("" + contato.getNumero());
        endereco.setBairro(contato.getBairro());
        endereco.setEndereco(contato.getLogradouro());
        return endereco;
    }

}
