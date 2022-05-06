package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitacaoCadastroCliente;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitacaoCadastroClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitacaoCadastroClienteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitacaoCadastroClienteVeiculoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.CombustivelService;
import br.com.villefortconsulting.sgfinancas.servicos.CorVeiculoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.ModeloInformacaoService;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusSolicitacaoCadastroCliente;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, imports = DataUtil.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class ClienteMapper {

    @Inject
    private DocumentoService documentoService;

    @Inject
    private DocumentoAnexoService documentoAnexoService;

    @Inject
    private ModeloInformacaoService modeloInformacaoService;

    @Inject
    private CorVeiculoService corVeiculoService;

    @Inject
    private CombustivelService combustivelService;

    @InheritInverseConfiguration
    @Mapping(target = "tipoPessoa", source = "tipo")
    @Mapping(target = "cpfCnpj", source = "cpfCNPJ")
    @Mapping(target = "contribuiIcms", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "contato.email", source = "email")
    @Mapping(target = "contato.telefoneComercial", source = "telefoneComercial")
    @Mapping(target = "contato.telefoneResidencial", source = "telefoneResidencial")
    @Mapping(target = "contato.telefoneCelular", source = "telefoneCelular")
    @Mapping(target = "ehSeguradora", source = "seguradora")
    @Mapping(target = "listaVeiculos", source = "clienteVeiculoList")
    public abstract ClienteDTO toDTO(Cliente cliente);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "endereco.idCidade", ignore = true)
    @Mapping(target = "idPlanoConta", ignore = true)
    @Mapping(target = "cpfCNPJ", source = "cpfCnpj")
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "razaoSocial", source = "razaoSocial")
    @Mapping(target = "temInscricaoEstadual", ignore = true)
    @Mapping(target = "inscricaoEstadual", source = "inscricaoEstadual")
    @Mapping(target = "indicadorInscricaoEstadual", ignore = true)
    @Mapping(target = "inscricaoMunicipal", source = "inscricaoEstadual")
    @Mapping(target = "optaSimples", ignore = true)
    @Mapping(target = "endereco.cep", source = "cep")
    @Mapping(target = "endereco.numero", source = "numero")
    @Mapping(target = "endereco.endereco", source = "endereco")
    @Mapping(target = "endereco.bairro", source = "bairro")
    @Mapping(target = "endereco.complemento", source = "complemento")
    @Mapping(target = "descricaoNFS", ignore = true)
    @Mapping(target = "pontuacao", ignore = true)
    @Mapping(target = "clienteContatoList", ignore = true)
    @Mapping(target = "ativo", constant = "S")
    @Mapping(target = "dataNascimento", ignore = true)
    @Mapping(target = "seguradora", source = "ehSeguradora")
    public abstract Cliente toEntity(ClienteCadastroDTO clienteDTO);

    @Mapping(target = "cep", source = "endereco.cep")
    @Mapping(target = "numero", source = "endereco.numero")
    @Mapping(target = "endereco", source = "endereco.endereco")
    @Mapping(target = "bairro", source = "endereco.bairro")
    @Mapping(target = "complemento", source = "endereco.complemento")
    @Mapping(target = "tenantID", source = "tenatID")
    @Mapping(target = "veiculos", source = "solicitacaoCadastroClienteVeiculoList")
    @Mapping(target = "codIbgeCidade", source = "endereco.idCidade.codigoIBGE")
    @Mapping(target = "cliente", expression = "java(toDTO(scc.getIdCliente()))")
    public abstract SolicitacaoCadastroClienteDTO toDTO(SolicitacaoCadastroCliente scc);

    @Mapping(target = "endereco.cep", source = "dto.cep")
    @Mapping(target = "endereco.numero", source = "dto.numero")
    @Mapping(target = "endereco.endereco", source = "dto.endereco")
    @Mapping(target = "endereco.bairro", source = "dto.bairro")
    @Mapping(target = "endereco.complemento", source = "dto.complemento")
    @Mapping(target = "tenatID", source = "dto.tenantID")
    @Mapping(target = "solicitacaoCadastroClienteVeiculoList", expression = "java(toEntity(dto.getVeiculos(), usuario))")
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "nome", source = "dto.nome")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "idCliente", ignore = true)
    public abstract SolicitacaoCadastroCliente toEntity(SolicitacaoCadastroClienteDTO dto, Usuario usuario);

    @Mapping(target = "anexos", source = "idDocumento")
    @Mapping(target = "marca", source = "idModeloInformacao.idModelo.idMarca.nome")
    @Mapping(target = "modelo", source = "idModeloInformacao.idModelo")
    @Mapping(target = "modelo.descricao", source = "idModeloInformacao.idModelo.nome")
    @Mapping(target = "anoModelo", source = "idModeloInformacao.ano")
    @Mapping(target = "corVeiculo", source = "idCorVeiculo")
    @Mapping(target = "combustivel", source = "idCombustivel")
    @Mapping(target = "tipo", source = "idModeloInformacao.idModelo.tipo")
    public abstract SolicitacaoCadastroClienteVeiculoDTO toDTO(SolicitacaoCadastroClienteVeiculo scc);

    public List<SolicitacaoCadastroClienteVeiculo> toEntity(List<SolicitacaoCadastroClienteVeiculoDTO> list, Usuario usuario) {
        return list.stream()
                .map(dto -> toEntity(dto, usuario))
                .collect(Collectors.toList());
    }

    public SolicitacaoCadastroClienteVeiculo toEntity(SolicitacaoCadastroClienteVeiculoDTO dto, Usuario usuario) {
        SolicitacaoCadastroClienteVeiculo scc = new SolicitacaoCadastroClienteVeiculo();

        scc.setIdModeloInformacao(modeloInformacaoService.buscarPorFipeCodigo(dto.getModelo().getId(), dto.getAnoModelo()));
        if (dto.getAnexos() != null) {
            Documento doc;
            if (dto.getAnexos().getId() != null) {
                doc = documentoService.buscar(dto.getAnexos().getId());
            } else {
                doc = documentoService.criarDocumento(usuario, "Fotos do produto");
            }
            scc.setIdDocumento(doc);
            documentoAnexoService.persistirAnexoDTO(doc, usuario, dto.getAnexos().getListaAnexo());
        }
        scc.setIdCorVeiculo(corVeiculoService.buscar(dto.getCorVeiculo().getId()));
        scc.setIdCombustivel(combustivelService.buscar(dto.getCombustivel().getId()));
        scc.setAnoFabricacao(dto.getAnoFabricacao());
        scc.setPlaca(dto.getPlaca());
        scc.setCambio(dto.getCambio());
        scc.setNroPortas(dto.getNroPortas());
        scc.setNroPassageiros(dto.getNroPassageiros());
        scc.setRenavam(dto.getRenavam());
        scc.setChassi(dto.getChassi());
        scc.setValorFipe(dto.getValorFipe());
        scc.setStatus(EnumStatusSolicitacaoCadastroCliente.AGUARDANDO.getSigla());
        scc.setDataModificacao(new Date());
        scc.setObservacao(dto.getObservacao());

        return scc;
    }

}
