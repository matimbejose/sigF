package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.basic.BasicNFS;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
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
@Table(name = "NFS")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class NFS extends BasicNFS implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o cliente")
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente idCliente;

    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID")
    @ManyToOne
    private Venda idVenda;

    @OneToMany(mappedBy = "idNFS")
    private List<ContaParcela> contaParcelaList = new LinkedList<>();

    @NotNull(message = "Informe o CTISS")
    @JoinColumn(name = "ID_CTISS", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Ctiss idCtiss;

    @NotNull(message = "Informe o município ISSQN")
    @JoinColumn(name = "ID_MUNICIO_ISSQN", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cidade idMunicipioISSQN;

    @NotNull(message = "Informe a natureza de operação")
    @Column(name = "NATUREZA_OPERACAO")
    @Size(max = 1, message = "Máximo de 1 caracter para natureza de operação")
    private String naturezaOperacao;

    @Column(name = "REGIME_TRIBUTACAO")
    @Size(max = 1, message = "Máximo de 1 caracter para regime de tributação")
    private String regimeTributacao;

    @NotNull(message = "Informe o tipo pessoa(física ou jurídica) para o cliente")
    @Column(name = "TIPO_PESSOA_CLIENTE")
    @Size(max = 2, message = "Máximo de 2 caracteres para tipo pessoa(física ou jurídica) para o cliente")
    private String tipoPessoaCliente;

    @Column(name = "NOME_CLIENTE")
    @NotNull(message = "Informe o nome do cliente")
    @Size(max = 200, message = "Máximo de 200 caracteres para nome do cliente")
    private String nomeCliente;

    @Column(name = "CPF_CNPJ_CLIENTE")
    @NotNull(message = "Informe o CNPJ ou CPF do cliente")
    @Size(max = 20, message = "Máximo de 20 caracteres para cpf/cnpj do cliente")
    private String cpfCnpjCliente;

    @Column(name = "TELEFONE_CLIENTE")
    @Size(max = 50, message = "Máximo de 50 caracteres para telefone do cliente")
    private String telefoneCliente;

    @Column(name = "EMAIL_CLIENTE")
    @Size(max = 50, message = "Máximo de 50 caracteres para email do cliente")
    private String emailCliente;

    @Column(name = "RG_CLIENTE")
    @Size(max = 20, message = "Máximo de 20 caracteres para rg do cliente")
    private String rgCliente;

    @Column(name = "CEP_CLIENTE")
    @NotNull(message = "Informe o CEP do cliente")
    @Size(max = 15, message = "Máximo de 15 caracteres para cep do cliente")
    private String cepCliente;

    @Column(name = "ENDERECO_CLIENTE")
    @NotNull(message = "Informe o endereço do cliente")
    @Size(max = 2000, message = "Máximo de 2000 caracteres para endereço do cliente")
    private String enderecoCliente;

    @Column(name = "NUMERO_CLIENTE")
    @NotNull(message = "Informe o número do endereço do cliente")
    @Size(max = 50, message = "Máximo de 50 caracteres para numero do cliente")
    private String numeroCliente;

    @Column(name = "COMPLEMENTO_CLIENTE")
    @Size(max = 200, message = "Máximo de 200 caracteres para complemento do cliente")
    private String complementoCliente;

    @Column(name = "BAIRRO_CLIENTE")
    @NotNull(message = "Informe o bairo do cliente")
    @Size(max = 200, message = "Máximo de 200 caracteres para bairro do cliente")
    private String bairroCliente;

    @Column(name = "CIDADE_CLIENTE")
    @NotNull(message = "Informe o município do cliente")
    @Size(max = 200, message = "Máximo de 200 caracteres para cidade do cliente")
    private String cidadeCliente;

    @Column(name = "UF_CLIENTE")
    @NotNull(message = "Informe a UF do cliente")
    @Size(max = 200, message = "Máximo de 2 caracteres para uf do cliente")
    private String ufCliente;

    @NotNull(message = "Informe a cidade cliente")
    @JoinColumn(name = "ID_CIDADE_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cidade idCidadeCliente;

    @NotNull(message = "Informe a UF do cliente")
    @JoinColumn(name = "ID_UF_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UF idUFCliente;

    @Column(name = "INSCRICAO_MUNICIPAL_CLIENTE")
    @Size(max = 50, message = "Máximo de 50 caracteres para inscrição municipal do cliente")
    private String inscricaoMunicipalCliente;

    @NotNull(message = "Informe a descricao do servico")
    @Column(name = "DESCRICAO_SERVICO")
    @Size(max = 5000, message = "Máximo de 5000 caracteres para descrição do cliente")
    private String descricaoServico;

    @Column(name = "VALOR_TOTAL")
    @NotNull(message = "Informe o valor total")
    private Double valorTotal;

    @Column(name = "VALOR_DEDUCOES")
    private Double valorDeducoes;

    @Column(name = "DESCONTO_CONDICIONADO")
    private Double descontoCondicionado;

    @Column(name = "DESCONTO_INCONDICIONADO")
    private Double descontoIncondicionado;

    @Column(name = "VALOR_INSS")
    private Double valorInss;

    @Column(name = "VALOR_IR")
    private Double valorIr;

    @Column(name = "VALOR_PIS")
    private Double valorPis;

    @Column(name = "VALOR_COFINS")
    private Double valorCofins;

    @Column(name = "VALOR_CSLL")
    private Double valorCsll;

    @Column(name = "OUTRAS_RETENCOES")
    private Double outrasRetencoes;

    @Column(name = "VALOR_ISS")
    private Double valorIss;

    @Column(name = "VALOR_ISS_RETIDO")
    private Double valorIssRetido;

    @Column(name = "ISS_RETIDO")
    @Size(max = 1, message = "Máximo de 1 caracter para ISS retido cliente")
    private String issRetido;

    @Column(name = "TIPO_PESSOA_INTERMEDIARIO")
    @Size(max = 2, message = "Máximo de 2 caracteres para tipo pessoa(física ou jurídica) para o intermediário")
    private String tipoPessoaIntermediario;

    @Column(name = "CPF_CNPJ_INTERMEDIARIO")
    @Size(max = 20, message = "Máximo de 20 caracteres para cpf/cnpj do intermediario")
    private String cpfCnpjIntermediario;

    @Column(name = "NOME_INTERMEDIARIO")
    @Size(max = 200, message = "Máximo de 200 caracteres para o nome do intermediário")
    private String nomeIntermediario;

    @Column(name = "INSCRICAO_MUNICIPAL_INTERMEDIARIO")
    @Size(max = 50, message = "Máximo de 50 caracteres para inscrição municipal do intermediário")
    private String inscricaoMunicipalIntermediario;

    @Column(name = "NUMERO_CEI")
    @Size(max = 200, message = "Máximo de 200 caracteres para o número CEI")
    private String numeroCei;

    @Column(name = "NUMERO_ART")
    @Size(max = 200, message = "Máximo de 200 caracteres para o número ART")
    private String numeroArt;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    @Size(max = 1, message = "Máximo de 1 caracter para situação")
    private String situacao;

    @Column(name = "INCENTIVADOR_CULTURAL")
    @Size(max = 1, message = "Máximo de 1 caracter para situação")
    private String incentivadorCultural;

    @Column(name = "OPTA_SIMPLES")
    @Size(max = 1, message = "Máximo de 1 caracter para situação")
    private String optaSimples;

    @NotNull(message = "Informe a data de compentencia")
    @Column(name = "DATA_COMPETENCIA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date competencia;

    @NotNull(message = "Informe a data de emissão")
    @Column(name = "DATA_EMISSAO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEmissao;

    @Size(max = 50, message = "Protocolo com no máximo 50 caracteres")
    @Column(name = "PROTOCOLO")
    private String protocolo;

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

    @Column(name = "CODIGO_VERIFICACAO")
    private Integer codigoVerificacao;  // deve ser gerado com o random de 8 numeros

    @Column(name = "NUMERO_NOTA_FISCAL")
    private String numeroNotaFiscal;  // Gerado no retorno

    @Column(name = "NUMERO_RPS")
    private Integer numeroRPS;  // O sistema deverá sugerir mas o cliente podera altera-lo para um numero que ainda nao exista

    @Column(name = "SERIE")
    private Integer serie;

    @Column(name = "MENSAGEM_ERRO_ENVIO")
    private String mensagemErroEnvio;

    @Column(name = "CODIGO_CANCELAMENTO")
    private String codigoCancelamento;

    @Column(name = "DATA_RETORNO_PROCESSAMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataRetornoProcessamento;

    @Column(name = "DATA_CANCELAMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataCancelamento;

    @Transient
    private String cancelarParcelas;

    public String getNumeroNotaFiscalFormatada() {
        if (numeroNotaFiscal != null && numeroNotaFiscal.length() == 15) {
            return numeroNotaFiscal.substring(0, 4) + numeroNotaFiscal.substring(4).replaceAll("^0+", "/");
        }
        return numeroNotaFiscal;
    }

    public boolean hasContaParcela() {
        return contaParcelaList != null && !contaParcelaList.isEmpty();
    }

    public void setXmlRetorno(TipoRetorno tipo, String xml, String xmlArmazenamento) {
        switch (tipo) {
            case CANCELAMENTO:
                this.xmlRetornoCancelamento = xml;
                this.xmlArmazenamentoCancelamento = xmlArmazenamento;
                this.mensagemErroEnvio = null;
                this.situacao = EnumSituacaoNF.CANCELADA.getSituacao();
                this.dataCancelamento = new Date();
                break;
            case ENVIO:
                this.xmlRetorno = xml;
                this.xmlArmazenamento = xmlArmazenamento;
                this.mensagemErroEnvio = null;
                this.situacao = EnumSituacaoNF.ENVIADA.getSituacao();
                break;
            default:
                break;
        }
    }

    public void setErro(String mensagem) {
        mensagemErroEnvio = mensagem;
        if (situacao == null) {
        } else if (EnumSituacaoNF.PROCESSANDO_CANCELAMENTO.getSituacao().equals(situacao)) {
            situacao = EnumSituacaoNF.REJEITADA_CANCELAMENTO.getSituacao();
        } else {
            situacao = EnumSituacaoNF.REJEITADA.getSituacao();
        }
    }

    // --- Métodos auxiliares ---
    @Override
    public String getDescricaoMunicipioISSQN() {
        if (idMunicipioISSQN == null) {
            return "";
        }
        return idMunicipioISSQN.getDescricao();
    }

    @Override
    public String getCodigoIBGEMunicipioISSQN() {
        if (idCidadeCliente == null) {
            return "";
        }
        return idCidadeCliente.getCodigoIBGE();
    }

    @Override
    public Double getAliquotaCtiss() {
        if (idCtiss == null) {
            return 0d;
        }
        return idCtiss.getAliquota();
    }

    @Override
    public String getCodigoCtiss() {
        if (idCtiss == null) {
            return "";
        }
        return idCtiss.getCodigo();
    }

    @Override
    public String getSubitemCtiss() {
        if (idCtiss == null) {
            return "";
        }
        return idCtiss.getSubitem();
    }

    @Override
    public String getDescricaoUfCidadeCliente() {
        if (idCidadeCliente == null || idCidadeCliente.getIdUF() == null) {
            return "";
        }
        return idCidadeCliente.getIdUF().getDescricao();
    }

    @Override
    public String getObservacaoVenda() {
        if (idVenda == null) {
            return "";
        }
        return idVenda.getObservacao();
    }

    @Override
    public boolean hasCidadeCliente() {
        return idCidadeCliente != null;
    }

    @Override
    public String toString() {
        return "NFS{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

    public enum TipoRetorno {
        ENVIO, CANCELAMENTO

    }

}
