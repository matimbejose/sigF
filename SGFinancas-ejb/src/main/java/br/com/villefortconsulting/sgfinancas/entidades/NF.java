package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumModeloEmissaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
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
import javax.swing.text.MaskFormatter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "NF")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class NF extends EntityTenant implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    public static final String XML_ENVIO = "OBJ_ENVIO.JSON";

    public static final String XML_RETORNO = "OBJ_RETORNO.JSON";

    public static final String XML_ENVIO_CANCELAMENTO = "OBJ_ENVIO_CANCELAMENTO.JSON";

    public static final String XML_RETORNO_CANCELAMENTO = "OBJ_RETORNO_CANCELAMENTO.JSON";

    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Cliente idCliente;

    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Fornecedor idFornecedor;

    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Venda idVenda;

    @JoinColumn(name = "ID_COMPRA", referencedColumnName = "ID")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Compra idCompra;

    @JoinColumn(name = "ID_UF_PRESTACAO", referencedColumnName = "ID")
    @ManyToOne
    @NotNull(message = "Informe a UF de nota fiscal")
    private UF idUfPrestacao;

    @JoinColumn(name = "ID_MUNICIO_PRESTACAO", referencedColumnName = "ID")
    @ManyToOne
    @NotNull(message = "Informe o município da nota fiscal")
    private Cidade idMunicipioPrestacao;

    @JoinColumn(name = "ID_UF_PLACA_VEICULO", referencedColumnName = "ID")
    @ManyToOne
    private UF idUfPlacaVeiculo;

    @JoinColumn(name = "ID_TRANSPORTADORA", referencedColumnName = "ID")
    @ManyToOne
    private Transportadora idTransportadora;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    @Size(max = 1, message = "Máximo de 1 caracter para situação")
    private String situacao;

    @NotNull(message = "Informe a data de geração")
    @Column(name = "DATA_GERACAO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataGeracao;

    @NotNull(message = "Informe a data de emissão")
    @Column(name = "DATA_EMISSAO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEmissao;

    @Column(name = "VALOR_NOTA")
    @NotNull(message = "Informe o valor da nota fiscal")
    private Double valorNota;

    @NotNull(message = "Informe a cidade cliente")
    @JoinColumn(name = "ID_CIDADE_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cidade idCidadeCliente;

    @NotNull(message = "Informe a UF do cliente")
    @JoinColumn(name = "ID_UF_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UF idUFCliente;

    @Column(name = "DESCRICAO_SERVICO")
    @Size(max = 5000, message = "Máximo de 5000 caracteres para descrição do cliente")
    private String descricaoServico;

    @Column(name = "ISS_RETIDO_CLIENTE")
    private String issRetidoCliente;

    @Column(name = "ALIQUOTA_ISS")
    private Double aliquotaIss;

    @Column(name = "INFORMACOES_OBRIGATORIAS")
    @Size(max = 5000, message = "Máximo de 5000 caracteres para informações obrigatórias")
    private String informacoesObrigatorias;

    @Column(name = "VALOR_ISS")
    private Double valorIss;

    @Column(name = "VALOR_CSLL")
    private Double valorCsll;

    @Column(name = "VALOR_COFINS")
    private Double valorConfins;

    @Column(name = "VALOR_PIS")
    private Double valorPis;

    @Column(name = "VALOR_INSS")
    private Double valorInss;

    @Column(name = "VALOR_IRRF")
    private Double valorIrrf;

    @Column(name = "XML_ENVIO")
    private String xmlEnvio;

    @Column(name = "XML_RETORNO")
    private String xmlRetorno;

    @Column(name = "XML_ARMAZENAMENTO")
    private String xmlArmazenamento;

    @Column(name = "XML_ENVIO_CANCELAMENTO")
    private String xmlEnvioCancelamento;

    @Column(name = "XML_RETORNO_CANCELAMENTO")
    private String xmlRetornoCancelamento;

    @Column(name = "XML_ARMAZENAMENTO_CANCELAMENTO")
    private String xmlArmazenamentoCancelamento;

    @Column(name = "TEM_FRETE")
    @Size(max = 1, message = "Máximo de 1 caracter para tem frete")
    private String temFrete;

    @Column(name = "RESPONSAVEL_FRETE")
    @Size(max = 200, message = "Máximo de 200 caracteres para responsável frete")
    private String responsavelFrete;

    @Column(name = "USA_TRANSPORTADORA")
    @Size(max = 1, message = "Máximo de 1 caracter para usa transportadora")
    private String usaTransportadora;

    @Column(name = "RNTC_VEICULO")
    @Size(max = 20, message = "Máximo de 20 caracteres para rntc veículo")
    private String rntcVeiculo;

    @Column(name = "PLACA_VEICULO")
    @Size(max = 15, message = "Máximo de 15 caracteres para placa veículo")
    private String placaVeiculo;

    @Column(name = "NUM_IDENTICACAO_VOLUME")
    @Size(max = 50, message = "Máximo de 50 caracteres para número de identificação do volume")
    private String numIdentificacaoVolume;

    @Column(name = "QTD_VOLUME_TRANSPORTADO")
    @Size(max = 50, message = "Máximo de 50 caracteres para quantidade volume transportado")
    private String qtdVolumeTransportado;

    @Column(name = "TIPO_VOLUME_TRANSPORTADO")
    @Size(max = 50, message = "Máximo de 50 caracteres para tipo volume transportado")
    private String tipoVolumeTransportado;

    @Column(name = "PESO_BRUTO_TOTAL")
    private Double pesoBrutoTotal;

    @Column(name = "PESO_LIQUIDO_TOTAL")
    private Double pesoLiquidoTotal;

    @Column(name = "FRETE_SOMA_NOTA_FISCAL")
    @Size(max = 1, message = "Máximo de 1 caracter para frete soma nota fiscal")
    private String freteSomaNotaFiscal;

    @Column(name = "VALOR_FRETE")
    private Double valorFrete;

    @Column(name = "VALOR_SEGURO")
    private Double valorSeguro;

    @Column(name = "VALOR_DESPESAS_ADICIONAIS")
    private Double valorDespesasAdicionais;

    @Column(name = "INFORMACOES_COMPLEMENTARES")
    @Size(max = 5000, message = "Máximo de 5000 caracteres para xml envio")
    private String informacoesComplementares;

    @Column(name = "CODIGO_NUMERICO")
    @NotNull(message = "Informe o código númerico da nota fiscal")
    private Integer codigoNumerico;  // deve ser gerado com o random de 8 numeros

    @Column(name = "NUMERO_NOTA_FISCAL")
    private Integer numeroNotaFiscal;  // O sistema deverá sugerir mas o cliente podera altera-lo para um numero que ainda nao exista

    @Column(name = "PROTOCOLO")
    private String protocolo;

    @Column(name = "MENSAGEM_ERRO_ENVIO")
    private String mensagemErroEnvio;

    @Column(name = "CHAVE")
    private String chave;

    @Column(name = "DIGEST_VALUE")
    private String digestValue;

    @Column(name = "TIPO_PRESENCA_CLIENTE")
    private String tipoPresencaCliente;

    @Column(name = "DATA_RETORNO_PROCESSAMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataRetornoProcessamento;

    @Column(name = "TIPO_OPERACAO")
    @Size(max = 1, message = "Tipo de operação deverá ter entre 1 caracteres.")
    private String tipoOperacao;

    @Column(name = "DESTINO_OPERACAO")
    @Size(max = 1, message = "Destino de operação deverá ter entre 1 caracteres.")
    private String destinoOperacao;

    @Column(name = "TIPO_LOCAL_DESTINO_OPERACAO")
    @Size(max = 1, message = "Tipo de local de destino operação de operação deverá ter entre 1 caracteres.")
    private String tipoLocalDestinoOperacao;

    @Column(name = "TIPO_EMISSAO")
    @Size(max = 1, message = "Tipo de emissão deverá ter entre 1 caracteres.")
    private String tipoEmissao;

    @Column(name = "TIPO_FORMATO_IMPRESSAO")
    @Size(max = 1, message = "Tipo de formato da impressão deverá ter entre 1 caracteres.")
    private String tipoFormatoImpressao;

    @Column(name = "IDENTIFICAO_AMBIENTE")
    private String identificacaoAmbiente;

    @Column(name = "FINALIDADE_EMISSAO")
    private String finalidadeEmissao;

    @Column(name = "TIPO_OPERACAO_CONSUMIDOR_FINAL")
    private String tipoOperacaoConsumidorFinal;

    @Column(name = "INSCRICAO_SUFRAMA")
    @Size(max = 50, message = "Inscricação suframa deverá ter entre 50 caracteres.")
    private String inscricaoSuframa;

    @Column(name = "INSCRICAO_ESTADUAL")
    @Size(max = 50, message = "Inscricação estadual deverá ter entre 50 caracteres.")
    private String inscricaoEstadual;

    @Column(name = "INDICADOR_INSCRICAO_ESTADUAL")
    @Size(max = 1, message = "Indicador de inscricação estadual deverá ter entre 1 caracteres.")
    private String indicadorInscricaoEstadual;

    @Column(name = "INSCRICAO_MUNICIPAL")
    private String inscricaoMunicipal;

    @Column(name = "INFORMACOES_NOTA")
    @Size(max = 5000, message = "Informações da nota deverá ter entre 5000 caracteres.")
    private String informacoesNota;

    @Column(name = "INFORMACOES_FISCO")
    @Size(max = 2000, message = "Informações de fisco deverá ter entre 2000 caracteres.")
    private String informacoesFisco;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idNf", orphanRemoval = true)
    private List<NFCorrecao> nfCorrecaoList = new LinkedList<>();

    @Column(name = "MEIO_DE_PAGAMENTO")
    private String meioDePagamento;

    @Column(name = "JUSTIFICATIVA_DEVOLUCAO")
    private String justificativaDevolucao;

    @JoinColumn(name = "ID_NF_REFERENCIA", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private NF idNfReferencia;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Documento idDocumento;

    @NotNull
    @Size(max = 2)
    @Column(name = "MODELO_NOTA")
    private String modeloNota;

    @NotNull
    @Size(max = 1)
    @Column(name = "TIPO_GERACAO")
    private String tipoGeracao;

    @Transient
    private transient NFe objTNFe;

    public String getChaveFormatada() {
        if (this.chave != null) {
            try {
                MaskFormatter formatter;
                formatter = new MaskFormatter("#### #### #### #### #### #### #### #### #### #### ####");
                formatter.setValueContainsLiteralCharacters(false);
                return formatter.valueToString(this.chave);
            } catch (ParseException ex) {
                return null;
            }
        }
        return this.chave;
    }

    public EnumModeloEmissaoNF getEnumModelo() {
        return EnumModeloEmissaoNF.retornaEnumSelecionado(modeloNota);
    }

    @Override
    public String toString() {
        return "NF{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

    @Override
    public NF clone() {
        try {
            NF novaNF = (NF) super.clone();
            List<NFCorrecao> novaNfCorrecaoList = new ArrayList<>();

            for (NFCorrecao correcao : novaNF.getNfCorrecaoList()) {
                NFCorrecao correcaoNova = correcao.clone();
                correcaoNova.setId(null);
                correcaoNova.setIdNf(novaNF);
                novaNfCorrecaoList.add(correcaoNova);
            }

            novaNF.setId(null);
            novaNF.setSituacao(EnumSituacaoNF.RASCUNHO.getSituacao());
            novaNF.setNfCorrecaoList(novaNfCorrecaoList);
            novaNF.setNumeroNotaFiscal(null);

            return novaNF;
        } catch (CloneNotSupportedException ex) {
            return new NF();
        }
    }

    public void addNfCorrecao(NFCorrecao nfCorrecao) {
        nfCorrecao.setIdNf(this);
        if (!nfCorrecaoList.contains(nfCorrecao)) {
            nfCorrecaoList.add(nfCorrecao);
        }
    }

    public void removeNfCorrecao(NFCorrecao nfCorrecao) {
        nfCorrecaoList.remove(nfCorrecao);
    }

}
