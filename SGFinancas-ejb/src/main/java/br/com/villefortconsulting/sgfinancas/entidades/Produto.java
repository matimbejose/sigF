package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PRODUTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Produto extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Size(max = 200, message = "Descrição deverá ter entre 200 caracteres.")
    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "VALOR_VENDA")
    private Double valorVenda;

    @Column(name = "VALOR_CUSTO")
    @NotNull(message = "Informe o valor de custo")
    private Double valorCusto;

    @Column(name = "CUSTO_FIXO")
    private Double custoFixo;

    @Column(name = "CUSTO_VARIAVEL")
    private Double custoVariavel;

    @Column(name = "MARKUP")
    private Double markup;

    @Column(name = "USA_PRECO_CALCULADO")
    private String usaPrecoCalculado;

    @Column(name = "PONTOS")
    private Double pontos;

    @JoinColumn(name = "ID_PRODUTO_CATEGORIA", referencedColumnName = "ID")
    @ManyToOne
    private ProdutoCategoria idProdutoCategoria;

    @JoinColumn(name = "ID_PRODUTO_CATEGORIA_SUBCATEGORIA", referencedColumnName = "ID")
    @ManyToOne
    private ProdutoCategoriaSubcategoria idProdutoCategoriaSubcategoria;

    @NotNull(message = "Informe a unidade de medida")
    @JoinColumn(name = "ID_UNIDADE_MEDIDA", referencedColumnName = "ID")
    @ManyToOne
    private UnidadeMedida idUnidadeMedida;

    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private PlanoConta idPlanoConta;

    @NotNull(message = "Informe o tipo do produto")
    @Size(max = 1, message = "Tipo deverá ter entre 1  caracteres.")
    @Column(name = "TIPO")
    private String tipo;

    @NotNull(message = "Informe o tipo da entrada")
    @Size(max = 2, message = "Tipo deverá ter entre 2  caracteres.")
    @Column(name = "ULTIMA_ENTRADA")
    private String ultimaEntrada;

    @NotNull(message = "Informe a data entrada do produto")
    @Column(name = "DATA_ENTRADA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEntrada;

    @JoinColumn(name = "ID_NCM", referencedColumnName = "ID")
    @ManyToOne
    private Ncm idNcm;

    @JoinColumn(name = "ID_CST", referencedColumnName = "ID")
    @ManyToOne
    private Cst idCst;

    @JoinColumn(name = "ID_CSOSN", referencedColumnName = "ID")
    @ManyToOne
    private Csosn idCsosn;

    @JoinColumn(name = "ID_ORIGEM_MERCADORIA", referencedColumnName = "ID")
    @ManyToOne
    private OrigemMercadoria idOrigemMercadoria;

    @Column(name = "CODIGO")
    @Size(max = 20, message = "Código deverá ter entre 20 caracteres.")
    private String codigo;

    @NotNull(message = "Favor informar o estoque disponível")
    @Column(name = "ESTOQUE_DISPONIVEL")
    private Double estoqueDisponivel;

    @Column(name = "ESTOQUE_MINIMO")
    private Double estoqueMinimo;

    @Column(name = "ESTOQUE_MAXIMO")
    private Double estoqueMaximo;

    @Column(name = "CODIGO_BARRA")
    @Size(max = 50, message = "Código de barra deverá ter entre 50 caracteres.")
    private String codigoBarra;

    @Column(name = "PESO_LIQUIDO")
    private Double pesoLiquido;

    @Column(name = "PESO_BRUTO")
    private Double pesoBruto;

    @Transient
    private String controle;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "idProduto", orphanRemoval = true)
    private List<FornecedorProduto> produtoFornecedorList = new LinkedList<>();

    @Column(name = "ATIVO")
    private String ativo;

    @Column(name = "COMPOSTO")
    private String composto;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "idProdutoOrigem", orphanRemoval = true)
    private List<ProdutoComposicao> produtoComposicaoList = new LinkedList<>();

    @JoinColumn(name = "ID_CFOP", referencedColumnName = "ID")
    @ManyToOne
    private Cfop idCfop;

    @Column(name = "SITUACAO_TRIBUTARIA_ICMS")
    private String situacaoTributariaIcms;

    @Column(name = "PORCENTAGEM_ICMS_INTERESTADUAL")
    private Double porcentagemIcmsInterestadual;

    @Column(name = "PORCENTAGEM_ICMS_INTERNO")
    private Double porcentagemIcmsInterno;

    @Column(name = "PORCENTAGEM_FCP")
    private Double porcentagemFcp;

    @Column(name = "CODIGO_ST")
    private String codigoSt;

    @Column(name = "ALIQUOTA_PIS")
    private Double aliquotaPis;

    @Column(name = "ALIQUOTA_COFINS")
    private Double aliquotaCofins;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne
    private Documento idDocumento;

    @Column(name = "PORCENTAGEM_IPI")
    private Double porcentagemIpi;

    @Column(name = "POSSUI_SUBSTITUICAO_TRIBUTARIA")
    @Size(max = 1, message = "O tamanho máximo de possui substituição tributaria é 1 caracter")
    private String possuiSubstituicaoTributaria;

    @JoinColumn(name = "ID_FABRICANTE", referencedColumnName = "ID")
    @ManyToOne
    private Fabricante idFabricante;

    @Transient
    private boolean selecionavel = true;

    public Produto() {
        this.tipo = "P";
        this.ultimaEntrada = "SI";
        this.ativo = "S";
        this.codigoBarra = "SEM GTIN";
    }

    public Produto(Integer id) {
        this.id = id;
    }

    public Produto(String tipo) {
        this.tipo = tipo;
    }

    public void addFornecedor(FornecedorProduto fornecedorProduto) {
        fornecedorProduto.setIdProduto(this);
        produtoFornecedorList.add(fornecedorProduto);
    }

    public void removeFornecedor(FornecedorProduto fornecedorProduto) {
        produtoFornecedorList.remove(fornecedorProduto);
    }

    @Override
    public String toString() {
        return "Produto{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
