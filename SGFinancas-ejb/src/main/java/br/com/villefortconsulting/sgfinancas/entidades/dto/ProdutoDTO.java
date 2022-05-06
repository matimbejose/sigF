package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.enums.EnumTipoDadoImportacao;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Importavel(nome = "produto")
public class ProdutoDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    @Importavel(nome = "Código de barras", tipo = EnumTipoDadoImportacao.INTEGER)
    private String codigoDeBarras;

    @Importavel(nome = "Nome", obrigatorio = true)
    private String descricao;

    @Importavel(nome = "Código interno do produto", tipo = EnumTipoDadoImportacao.STRING)
    private String codigo;

    @Importavel(nome = "Valor", obrigatorio = true, tipo = EnumTipoDadoImportacao.DOUBLE)
    private Double valor;

    @Importavel(nome = "Custo", obrigatorio = true, tipo = EnumTipoDadoImportacao.DOUBLE, padrao = "0")
    private Double custo;

    private Double custoFixo;

    private Double custoVariavel;

    private Double markup;

    private Integer categoria;

    private Integer subcategoria;

    @Importavel(nome = "Categoria", obrigatorio = true, tipo = EnumTipoDadoImportacao.ID_TABELA, padrao = "Importação")
    private String categoriaImportacao;

    private Integer unidadeDeMedida;

    @Importavel(nome = "Unidade de medida", tipo = EnumTipoDadoImportacao.ID_TABELA, obrigatorio = true)
    private String unidadeDeMedidaImportacao;

    private Long dataEntrada;

    @Importavel(nome = "Data de entrada", tipo = EnumTipoDadoImportacao.DATE, obrigatorio = true)
    private Date dataEntradaImportacao;

    @Importavel(nome = "Estoque disponível", tipo = EnumTipoDadoImportacao.DOUBLE, obrigatorio = true)
    private Double estoqueDisponivel;

    @Importavel(nome = "Estoque mínimo", tipo = EnumTipoDadoImportacao.DOUBLE)
    private Double estoqueMinimo;

    @Importavel(nome = "Estoque máximo", tipo = EnumTipoDadoImportacao.DOUBLE)
    private Double estoqueMaximo;

    @Importavel(nome = "Peso liquido", tipo = EnumTipoDadoImportacao.DOUBLE)
    private Double pesoLiquido;

    @Importavel(nome = "Peso bruto", tipo = EnumTipoDadoImportacao.DOUBLE)
    private Double pesoBruto;

    private Double pontos;

    private String composto = "N";

    private DocumentoDTO fotos;

    private List<ProdutoComposicaoDTO> listaComposicao;

    private String ativo;

    private Boolean usaPrecoCalculado;

    public ProdutoDTO(Integer id, String codigoDeBarras, String descricao, Double valor) {
        this.id = id;
        this.codigoDeBarras = codigoDeBarras;
        this.descricao = descricao;
        this.valor = valor;
    }

}
