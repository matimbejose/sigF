package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.sgfinancas.entidades.Marca;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import br.com.villefortconsulting.sgfinancas.entidades.ModeloInformacao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MarcaCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModeloAnoCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModeloAnosCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModeloCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModeloInformacaoCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.FipeMapper;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVeiculoFipe;
import br.com.villefortconsulting.util.MicroServiceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FipeService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private MarcaService marcaService;

    @EJB
    private ModeloService modeloService;

    @EJB
    private ModeloInformacaoService modeloInformacaoService;

    @Inject
    private FipeMapper fipeMapper;

    private static String getUrl(EnumTipoVeiculoFipe tipo) {
        return "http://parallelum.com.br/fipe/api/v1/" + tipo.name().toLowerCase() + "/";
    }

    private static <T> List<T> fetchList(String url, Class<T> classe) {
        try {
            ResponseEntity<String> re = MicroServiceUtil.doGet(MicroServiceUtil.MicroServicos.NONE, url);
            if (re.getStatusCode() != HttpStatus.OK) {
                return new ArrayList<>();
            }
            ObjectMapper om = new ObjectMapper();
            return om.readValue(re.getBody(), om.getTypeFactory().constructCollectionType(List.class, classe));
        } catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    private static <T> Optional<T> fetchSingle(String url, Class<T> classe) {
        try {
            final String json = MicroServiceUtil.doGet(MicroServiceUtil.MicroServicos.NONE, url).getBody();
            return Optional.ofNullable(new ObjectMapper().readValue(json, classe));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }

    private static List<MarcaCadastroDTO> listaMarcasFipe(EnumTipoVeiculoFipe tipo) {
        List<MarcaCadastroDTO> marcas = fetchList(getUrl(tipo) + "marcas", MarcaCadastroDTO.class);
        marcas.forEach(marca -> {
            marca.setTipoVeiculo(tipo.name());
        });
        return marcas;
    }

    private static List<ModeloCadastroDTO> listaModelosFipe(EnumTipoVeiculoFipe tipo, Integer marca) {
        Optional<ModeloAnoCadastroDTO> aux = fetchSingle(getUrl(tipo) + "marcas/" + marca + "/modelos", ModeloAnoCadastroDTO.class);
        if (aux.isPresent()) {
            return aux.get().getModelos();
        }
        return new ArrayList<>();
    }

    private static Optional<ModeloInformacaoCadastroDTO> dadosModelosInformacaoFipe(EnumTipoVeiculoFipe tipo, Integer marca, Integer modelo, String codigoAno) {
        return fetchSingle(getUrl(tipo) + "marcas/" + marca + "/modelos/" + modelo + "/anos/" + codigoAno, ModeloInformacaoCadastroDTO.class);
    }

    private static List<ModeloAnosCadastroDTO> listaAnosModeloFipe(EnumTipoVeiculoFipe tipo, Integer marca, Integer modelo) {
        return fetchList(getUrl(tipo) + "marcas/" + marca + "/modelos/" + modelo + "/anos", ModeloAnosCadastroDTO.class);
    }

    public List<Marca> listaMarcasSistema() {
        return Arrays.stream(EnumTipoVeiculoFipe.values())
                .map(this::listaMarcasSistema)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<Marca> listaMarcasSistema(EnumTipoVeiculoFipe tipo) {
        List<Marca> listaSistema = marcaService.listarMarcasByTipo(tipo.name());
        listaMarcasFipe(tipo).stream()
                .filter(dto -> listaSistema.stream().map(Marca::getFipeId).noneMatch(id -> id.equals(Integer.parseInt(dto.getCodigo()))))
                .forEach(dto -> marcaService.salvar(fipeMapper.toEntity(dto)));
        return marcaService.listarMarcasByTipo(tipo.name());
    }

    public void atualizaTipoVeiculoMarcasSistema() {
        List<Marca> listaSistema = marcaService.listar();
        for (EnumTipoVeiculoFipe e : EnumTipoVeiculoFipe.values()) {
            List<MarcaCadastroDTO> aux = listaMarcasFipe(e);
            for (MarcaCadastroDTO dto : aux) {
                Marca marca = marcaService.buscarByFipeId(Integer.parseInt(dto.getCodigo()));
                marca.setFipeOrder(dto.getTipoVeiculo());
                marcaService.salvar(marca);
            }
        }
    }

    public List<Modelo> listaModelosSistema(Marca marca) {
        return Arrays.stream(EnumTipoVeiculoFipe.values())
                .map(e -> listaModelosSistema(e, marca))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<Modelo> listaModelosSistema(EnumTipoVeiculoFipe tipo, Marca marca) {
        List<Modelo> listaSistema = modeloService.listar(tipo, marca);
        listaModelosFipe(tipo, marca.getFipeId()).stream()
                .filter(dto -> listaSistema.stream().map(Modelo::getFipeId).noneMatch(id -> id.equals(Integer.parseInt(dto.getCodigo()))))
                .forEach(dto -> modeloService.salvar(fipeMapper.toEntity(dto, marca, tipo.name().toLowerCase())));
        return modeloService.listar(tipo, marca);
    }

    public List<ModeloInformacao> listaModeloInformacaosSistema(Modelo modelo) {
        return Arrays.stream(EnumTipoVeiculoFipe.values())
                .map(e -> listaModeloInformacaosSistema(e, modelo))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<ModeloInformacao> listaModeloInformacaosSistema(EnumTipoVeiculoFipe tipo, Modelo modelo) {
        List<ModeloInformacao> listaSistema = modeloInformacaoService.listar(tipo, modelo);
        for (ModeloAnosCadastroDTO dto : listaAnosModeloFipe(tipo, modelo.getIdMarca().getFipeId(), modelo.getFipeId())) {
            dadosModelosInformacaoFipe(tipo, modelo.getIdMarca().getFipeId(), modelo.getFipeId(), dto.getCodigoAno())
                    .ifPresent(aux -> {
                        if (listaSistema.stream().map(ModeloInformacao::getFipeCodigo).noneMatch(cod -> cod.equals(aux.getCodigoFipe()))) {
                            ModeloInformacao mi = fipeMapper.toEntity(aux, modelo);
                            mi.setTipo(tipo.name().toLowerCase());
                            modeloInformacaoService.salvar(mi);
                        }
                    });
        }
        return modeloInformacaoService.listar(tipo, modelo);
    }

    public Marca getMarca(Integer fipeId) {
        Marca marca = marcaService.buscarByFipeId(fipeId);
        if (marca == null) {
            marca = listaMarcasSistema().stream()
                    .filter(m -> m.getFipeId().equals(fipeId))
                    .findAny().orElse(null);
        }
        return marca;
    }

    public Modelo getModelo(Marca marca, Integer fipeId) {
        Modelo modelo = modeloService.buscarByModeloFipeId(fipeId);
        if (modelo == null) {
            modelo = listaModelosSistema(marca).stream()
                    .filter(m -> m.getFipeId().equals(fipeId))
                    .findAny().orElse(null);
        }
        return modelo;
    }

}
