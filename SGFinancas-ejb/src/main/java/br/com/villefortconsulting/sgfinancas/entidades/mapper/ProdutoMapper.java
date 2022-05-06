package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoriaSubcategoria;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoComposicao;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeMedida;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CategoriaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoComposicaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SubCategoriaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UnidadeMedidaDTO;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import java.util.Date;
import javax.inject.Inject;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class ProdutoMapper {

    @Inject
    private ProdutoService produtoService;

    @Inject
    private DocumentoService documentoService;

    @Inject
    private DocumentoAnexoService documentoAnexoService;

    @Mapping(target = "codigoDeBarras", source = "codigoBarra")
    @Mapping(target = "valor", source = "valorVenda")
    @Mapping(target = "custo", source = "valorCusto")
    @Mapping(target = "categoria", source = "idProdutoCategoria.id")
    @Mapping(target = "categoriaImportacao", source = "idProdutoCategoria.descricao")
    @Mapping(target = "unidadeDeMedida", source = "idUnidadeMedida.id")
    @Mapping(target = "unidadeDeMedidaImportacao", source = "idUnidadeMedida.descricao")
    @Mapping(target = "fotos", source = "idDocumento")
    @Mapping(target = "listaComposicao", source = "produtoComposicaoList")
    @Mapping(target = "dataEntradaImportacao", ignore = true)
    public abstract ProdutoDTO toDTO(Produto produto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "valorVenda", source = "dto.valor")
    @Mapping(target = "valorCusto", source = "dto.custo")
    @Mapping(target = "idProdutoCategoria", ignore = true)
    @Mapping(target = "idProdutoCategoriaSubcategoria", ignore = true)
    @Mapping(target = "idUnidadeMedida", ignore = true)
    @Mapping(target = "idPlanoConta", ignore = true)
    @Mapping(target = "tipo", constant = "P")
    @Mapping(target = "ultimaEntrada", constant = "SI")
    @Mapping(target = "idNcm", ignore = true)
    @Mapping(target = "idCst", ignore = true)
    @Mapping(target = "idCsosn", ignore = true)
    @Mapping(target = "idOrigemMercadoria", ignore = true)
    @Mapping(target = "codigoBarra", source = "dto.codigoDeBarras")
    @Mapping(target = "controle", ignore = true)
    @Mapping(target = "produtoFornecedorList", ignore = true)
    @Mapping(target = "idCfop", ignore = true)
    @Mapping(target = "situacaoTributariaIcms", ignore = true)
    @Mapping(target = "porcentagemIcmsInterestadual", ignore = true)
    @Mapping(target = "porcentagemIcmsInterno", ignore = true)
    @Mapping(target = "porcentagemFcp", ignore = true)
    @Mapping(target = "codigoSt", ignore = true)
    @Mapping(target = "aliquotaPis", ignore = true)
    @Mapping(target = "aliquotaCofins", ignore = true)
    @Mapping(target = "idDocumento", ignore = true)
    @Mapping(target = "produtoComposicaoList", source = "dto.listaComposicao")
    @Mapping(target = "porcentagemIpi", ignore = true)
    @Mapping(target = "possuiSubstituicaoTributaria", ignore = true)
    @Mapping(target = "selecionavel", ignore = true)
    public abstract Produto toEntity(ProdutoDTO dto, Usuario user);

    @AfterMapping
    public Produto toEntityAfter(ProdutoDTO dto, Usuario user, @MappingTarget Produto produto) {
        if (dto.getFotos() != null) {
            Documento doc = null;
            if (produto.getId() != null) {
                Produto fromDb = produtoService.buscar(produto.getId());
                if (fromDb.getIdDocumento() != null) {
                    doc = documentoService.buscar(fromDb.getIdDocumento().getId());
                }
            }
            if (doc == null) {
                doc = documentoService.criarDocumento(user, "");
            }
            dto.getFotos().getListaAnexo().forEach(anexo -> {
                if (anexo.getDataEnvio() == null) {
                    anexo.setDataEnvio(new Date());
                }
            });
            documentoAnexoService.persistirAnexoDTO(doc, user, dto.getFotos().getListaAnexo());
        }
        return produto;
    }
    // -----------------------------------------------------------------------------------------------------------------------------------------------

    @Mapping(target = "subCategriaList", source = "listProdutoCategoriaSubcategoria")
    public abstract CategoriaDTO toDTO(ProdutoCategoria cat);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "listProdutoCategoriaSubcategoria", source = "subCategriaList")
    public abstract ProdutoCategoria toEntity(CategoriaDTO cat);

    public abstract UnidadeMedidaDTO toDTO(UnidadeMedida unidadeMedida);

    @Mapping(target = "tenatID", ignore = true)
    public abstract UnidadeMedida toEntity(UnidadeMedidaDTO unidadeMedida);

    public abstract ProdutoComposicaoDTO toDTO(ProdutoComposicao produtoComposicao);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idProdutoOrigem", ignore = true)
    @Mapping(target = "dataProducao", ignore = true)
    @Mapping(target = "quantidadeFinal", ignore = true)
    public abstract ProdutoComposicao toEntity(ProdutoComposicaoDTO produtoComposicaoDTO);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idProdutoCategoria", ignore = true)
    @Mapping(target = "controleEdicao", ignore = true)
    public abstract ProdutoCategoriaSubcategoria toEntity(SubCategoriaDTO dto);

    public abstract SubCategoriaDTO toEntity(ProdutoCategoriaSubcategoria produtoCategoriaSubcategoria);

}
