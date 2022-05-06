package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Compra;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CompraDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CompraProdutoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.servicos.CentroCustoService;
import br.com.villefortconsulting.sgfinancas.servicos.CompraService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.FornecedorService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoPagamento;
import br.com.villefortconsulting.util.ListUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class CompraMapper {

    @Inject
    CentroCustoService centroCustoService;

    @Inject
    ContaService contaService;

    @Inject
    ContaBancariaService contaBancariaService;

    @Inject
    CompraService compraService;

    @Inject
    FornecedorService fornecedorService;

    @Inject
    PlanoContaService planoContaService;

    @Inject
    ProdutoService produtoService;

    @Mapping(target = "fornecedor", source = "idFornecedor")
    @Mapping(target = "contaBancaria", source = "idContaBancaria")
    @Mapping(target = "transportadora", source = "idTransportadora")
    @Mapping(target = "transportadora.contato.telefoneComercial", source = "idTransportadora.contato")
    @Mapping(target = "planoConta", source = "idPlanoConta")
    @Mapping(target = "centroCusto", source = "idCentroCusto")
    @Mapping(target = "condicaoPagamento", source = "formaPagamento")
    @Mapping(target = "statusPagamento", expression = "java(br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta.getDescricao(compra.getIdConta().getSituacao()))")
    public abstract CompraDTO toDTO(Compra compra);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idFornecedor.id", source = "fornecedor.id")
    @Mapping(target = "idFornecedor.contato", ignore = true)
    @Mapping(target = "idContaBancaria.id", source = "contaBancaria.id")
    @Mapping(target = "idNaturezaOperacao", ignore = true)
    @Mapping(target = "idTransportadora.id", source = "transportadora.id")
    @Mapping(target = "idTransportadora.contato", ignore = true)
    @Mapping(target = "idPlanoConta.id", source = "planoConta.id")
    @Mapping(target = "idCentroCusto.id", source = "centroCusto.id")
    @Mapping(target = "pagarFrete", ignore = true)
    @Mapping(target = "listParcelaDTO", ignore = true)
    @Mapping(target = "idPlanoPagamento", ignore = true)
    @Mapping(target = "formaPagamento", source = "condicaoPagamento")
    public abstract Compra toEntity(CompraDTO compraDTO);

    @Mapping(target = "produto", source = "dadosProduto.idProduto")
    @Mapping(target = "quantidade", source = "dadosProduto.quantidade")
    @Mapping(target = "valorVenda", source = "dadosProduto.valor")
    @Mapping(target = "detalhesItem", source = "dadosProduto.detalhesItem")
    @Mapping(target = "desconto", source = "dadosProduto.desconto")
    public abstract CompraProdutoDTO toDTO(CompraProduto compraProduto);

    @InheritConfiguration
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "idCompra", source = "compra")
    @Mapping(target = "dadosProduto.idProduto", source = "dto.produto")
    @Mapping(target = "dadosProduto.quantidade", source = "dto.quantidade")
    @Mapping(target = "dadosProduto.valor", source = "dto.valorVenda")
    @Mapping(target = "dadosProduto.detalhesItem", source = "dto.detalhesItem")
    @Mapping(target = "dadosProduto.desconto", source = "dto.desconto")
    @Mapping(target = "descricaoProdutoXML", ignore = true)
    @Mapping(target = "idUnidadeMedida", ignore = true)
    @Mapping(target = "codigoBarras", ignore = true)
    @Mapping(target = "ncm", ignore = true)
    @Mapping(target = "devolvido", constant = "N")
    public abstract CompraProduto toEntity(CompraProdutoDTO dto, Compra compra);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idConta", ignore = true)
    @Mapping(target = "listCompraProduto", ignore = true)
    protected abstract Compra updateCompra(@MappingTarget Compra compra, Compra compraFromDto);

    public List<CompraProduto> toCompraProdutoEntityList(List<CompraProdutoDTO> lista, Compra compra) {
        if (lista == null) {
            return new ArrayList<>();
        }
        return lista.stream().map(dto -> toEntity(dto, compra)).collect(Collectors.toList());
    }

    public Compra toEntityFromDB(CompraDTO dto, String tenantID) throws IllegalAccessException, InvocationTargetException {
        Compra compraFromDto = toEntity(dto);
        compraFromDto.setIdContaBancaria(contaBancariaService.getContaBancariaPadrao(dto));
        compraFromDto.setIdPlanoConta(planoContaService.getPlanoContaPadrao(dto));
        Compra compra = dto.getId() != null ? compraService.buscar(dto.getId()) : new Compra();
        compra.setTenatID(tenantID);
        updateCompra(compra, compraFromDto);
        if (dto.getContaBancaria() != null) {
            compra.setIdContaBancaria(contaBancariaService.buscar(dto.getContaBancaria().getId()));
        }
        if (dto.getCentroCusto() != null) {
            compra.setIdCentroCusto(centroCustoService.buscar(dto.getCentroCusto().getId()));
        }
        if (dto.getFornecedor() != null) {
            compra.setIdFornecedor(fornecedorService.buscar(dto.getFornecedor().getId()));
        } else if (compra.getIdFornecedor() == null) {
            throw new CadastroException("Informe o fornecedor", null);
        }
        compra.setSituacao("A");
        if (compra.getNumParcela() == null) {
            compra.setNumParcela(1);
        }
        if (dto.getId() != null) {
            List<CompraProduto> listaCP = ListUtil.persist(compraService.listarCompraProduto(compra), toCompraProdutoEntityList(dto.getListCompraProduto(), compra), CompraProduto::contains);
            compra.getListCompraProduto().clear();
            compra.getListCompraProduto().addAll(listaCP);
        } else {
            ListUtil.persist(compra.getListCompraProduto(), toCompraProdutoEntityList(dto.getListCompraProduto(), compra), CompraProduto::contains);
        }
        compra.getListCompraProduto().forEach(cp -> {
            cp.setIdCompra(compra);
            cp.setDevolvido("N");
            cp.getDadosProduto().setIdProduto(produtoService.buscar(cp.getDadosProduto().getIdProduto().getId()));
        });

        compra.setValorDesconto(NumeroUtil.somar(
                compra.getListCompraProduto().stream()
                        .map(CompraProduto::getDadosProduto)
                        .mapToDouble(v -> v.getDesconto() != null ? v.getDesconto() : 0).sum()));
        compra.setValorTotal(NumeroUtil.somar(
                compra.getListCompraProduto().stream()
                        .map(CompraProduto::getDadosProduto)
                        .map(DadosProduto::getValorTotal)
                        .filter(Objects::nonNull)
                        .reduce(0d, (a, v) -> a + v)));
        compra.setDataCompra(new Date());
        compra.setCondicaoPagamento(EnumTipoPagamento.CREDITO.getTipo());

        return compra;
    }

}
