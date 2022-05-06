package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaImpressaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //Primeiro quadrado de campos
    private Integer notaChaveAcesso;

    private Integer numeroNfe;

    private String versaoNfe;

    //Dados da nfe
    private Integer modeloNota;

    private Integer serieNota;

    private Date dataEmissaoNota;

    private Date dataHoraSaidaEntradaNota;

    private Double valorTotalNota;

    //Dados emitente
    private String cnpjEmitente;

    private String razaoSocialEmitente;

    private String inscricaoEstadualEmitente;

    private String ufEmitente;

    //Dados destinatário
    private String cnpjDestinatario;

    private String razaoSocialDestinatario;

    private String inscricaoEstadualDestinatario;

    private String ufDestinatario;

    private String destinoOperacao;

    private String consumidorFinal;

    private String presencaComprador;

    //Emissao
    private String processo;

    private String versaoProcesso;

    private String tipoEmissao;

    private String finalidade;

    private String naturezaOperacao;

    private String tipoOperacao;

    private String formaPagamento;

    private String digestValueNfe;

    //SituacaoAtual
    private String situacao;

    private String eventoNfe;

    private String protocolo;

    private Date dataAutorizacao;

    private Date dataInclusaoAn;

    //Dados emitente
    private String enderecoEmitente;

    private String bairroEmitente;

    private String cepEmitente;

    private String municipioEmitente;

    private String telefoneEmitente;

    private String paisEmitente;

    private String inscricaoEstadualSubsTribuEmitente;

    private String inscricaoMunicipalEmitente;

    private String municipioOcorrenciaFatoGeradorICMSEmitente;

    private String cnaeFiscalEmitente;

    private String codRegimeTributarioEmitente;

    //Dados destinatario
    private String enderecoDestinatario;

    private String bairroDestinatario;

    private String cepDestinatario;

    private String municipioDestinatario;

    private String telefoneDestinatario;

    private String paisDestinatario;

    private String indicadorIeDestinatario;

    private String inscricaoSuframaDestinatario;

    private String inscricaoMunicipalDestinatario;

    private String emailDestinatario;

    //Totais ICMS
    private Double baseCalculoIcms;

    private Double valorIcms;

    private Double valorIcmsDesonerado;

    private Double valorBaseCalculoIcmsSt;

    private Double valorIcmsSubstituicao;

    private Double valorTotalProdutos;

    private Double valorTotalFrete;

    private Double valorSeguro;

    private Double outrasDespesas;

    private Double valorTotalIpi;

    private Double valorTotalDescontos;

    private Double valorTotalII;

    private Double valorPis;

    private Double valorCofins;

    private Double valorApxTributos;

    private Double valorTotalIcmsFcp;

    private Double valorIcmsInteerestUfDestino;

    private Double valorTotalIcmsInteerstUfRemente;

    //Dados do transporte: transporte
    private String modalidadeFrete;

    private String cnpjTransportador;

    private String razaoSocialTransportador;

    private String inscricaoEstadualTransportador;

    private String endCompTransportador;

    private String municipioTransportador;

    private String ufTransportador;

    //veiculo
    private String placaVeiculo;

    private String ufVeiculo;

    private String rntcVeiculo;

    //Volumes
    private Double qntProdutos;

    private String especie;

    private String marcaVolume;

    private String numeracaoVolume;

    private Double pesoBrutoTotal;

    private Double pesoLiquidoTotal;

}
