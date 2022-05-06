package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.basic.BasicNFS;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NfsDTO extends BasicNFS implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idCidadeCliente;

    private Integer idCliente;

    private Integer idCtiss;

    private String descricaoCtiss;

    private Integer idVenda;

    private Integer idMunicipioISSQN;

    private String naturezaOperacao;

    private String regimeTributacao;

    private String tipoPessoaCliente;

    private String nomeCliente;

    private String cpfCnpjCliente;

    private String telefoneCliente;

    private String emailCliente;

    private String rgCliente;

    private String cepCliente;

    private String enderecoCliente;

    private String numeroCliente;

    private String complementoCliente;

    private String bairroCliente;

    private String cidadeCliente;

    private String ufCliente;

    private String inscricaoMunicipalCliente;

    private String descricaoServico;

    private Double valorTotal;

    private Double valorDeducoes;

    private Double descontoCondicionado;

    private Double descontoIncondicionado;

    private Double valorInss;

    private Double valorIr;

    private Double valorPis;

    private Double valorCofins;

    private Double valorCsll;

    private Double outrasRetencoes;

    private Double valorIss;

    private Double valorIssRetido;

    private String issRetido;

    private String tipoPessoaIntermediario;

    private String cpfCnpjIntermediario;

    private String nomeIntermediario;

    private String inscricaoMunicipalIntermediario;

    private String numeroCei;

    private String numeroArt;

    private String situacao;

    private String incentivadorCultural;

    private String optaSimples;

    private Date competencia;

    private Date dataEmissao;

    private String protocolo;

    private String xmlEnvio;

    private String xmlRetorno;

    private String xmlArmazenamento;

    private String xmlEnvioCancelamento;

    private String xmlRetornoCancelamento;

    private String xmlArmazenamentoCancelamento;

    private Integer codigoVerificacao;

    private String numeroNotaFiscal;

    private Integer numeroRPS;

    private Integer serie;

    private String mensagemErroEnvio;

    private String codigoCancelamento;

    private Date dataRetornoProcessamento;

    private Date dataCancelamento;

    private String descricaoMunicipioISSQN;

    private String codigoIBGEMunicipioISSQN;

    private Double aliquotaCtiss;

    private String codigoCtiss;

    private String subitemCtiss;

    private String descricaoUfCidadeCliente;

    private String observacaoVenda;

    @Override
    public boolean hasCidadeCliente() {
        return true;
    }

}
