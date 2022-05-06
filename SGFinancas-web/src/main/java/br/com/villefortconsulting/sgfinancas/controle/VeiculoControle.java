package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Combustivel;
import br.com.villefortconsulting.sgfinancas.entidades.CorVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Marca;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import br.com.villefortconsulting.sgfinancas.entidades.ModeloInformacao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VeiculoCadastroDTO;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteVeiculoService;
import br.com.villefortconsulting.sgfinancas.servicos.CombustivelService;
import br.com.villefortconsulting.sgfinancas.servicos.CorVeiculoService;
import br.com.villefortconsulting.sgfinancas.servicos.FipeService;
import br.com.villefortconsulting.sgfinancas.servicos.MarcaService;
import br.com.villefortconsulting.sgfinancas.servicos.ModeloService;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVeiculoFipe;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ClienteVeiculoService clienteVeiculoService;

    @EJB
    private CombustivelService combustivelService;

    @EJB
    private CorVeiculoService corVeiculoService;

    @EJB
    private ModeloService modeloService;

    @EJB
    private MarcaService marcaService;

    @Inject
    private CadastroControle cadastroControle;

    @EJB
    private FipeService fipeService;

    private VeiculoCadastroDTO dtoCadastro;

    public List<Marca> getListaMarca() {
        return fipeService.listaMarcasSistema();
    }

    public List<Marca> getListarMarcaByTipo(String descricao) {
        if (!descricao.isEmpty()) {
            return fipeService.listaMarcasSistema(EnumTipoVeiculoFipe.valueOf(descricao.toUpperCase()));
        }
        return fipeService.listaMarcasSistema();
    }

    public List<Modelo> getListaModelo(Marca marca, String tipo) {
        if (marca == null) {
            return new ArrayList<>();
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            return fipeService.listaModelosSistema(marca);
        }
        return fipeService.listaModelosSistema(EnumTipoVeiculoFipe.valueOf(tipo.toUpperCase()), marca);
    }

    public List<Combustivel> getListaCombustivel() {
        return combustivelService.listar();
    }

    public List<CorVeiculo> getListaCorVeiculo() {
        return corVeiculoService.listar();
    }

    public List<Integer> getAnosModelo(Modelo modelo) {
        if (modelo == null) {
            return new ArrayList<>();
        }
        return fipeService.listaModeloInformacaosSistema(modelo).stream()
                .map(ModeloInformacao::getAno)
                .collect(Collectors.toList());
    }

    public List<ClienteVeiculo> getListaVeiculo(Cliente cliente) {
        return clienteVeiculoService.listaVeiculosPor(cliente);
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(item -> {
                    VeiculoCadastroDTO cadastro = (VeiculoCadastroDTO) item;
                    switch (cadastro.getTipo()) {
                        case COMBUSTIVEL:
                            return combustivelService.importDto(cadastro);
                        case COR:
                            return corVeiculoService.importDto(cadastro);
                        case VEICULO:
                            return clienteVeiculoService.importDto(cadastro);
                        default:
                            return null;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new VeiculoCadastroDTO();
        if (context != null) {
            dtoCadastro.setDadosVeiculo(dtoCadastro.new DadosVeiculo());
            dtoCadastro.getDadosVeiculo().setCliente((Cliente) context);
        }
    }

    public void atualizaVeiculoModelo() {
        ModeloInformacao mi = dtoCadastro.getDadosVeiculo().getModelo().getAnos().stream()
                .filter(ano -> ano.getAno().equals(dtoCadastro.getDadosVeiculo().getAnoModelo()))
                .findAny()
                .orElse(null);
        if (mi != null) {
            dtoCadastro.getDadosVeiculo().setValorProtegido(mi.getPreco());
        }
    }

}
