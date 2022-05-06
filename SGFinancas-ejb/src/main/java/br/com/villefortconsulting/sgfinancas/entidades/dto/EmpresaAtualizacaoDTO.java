package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaAtualizacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cpfCnpj;

    private String cep;

    private String codCidade;

    private String endereco;

    private String numero;

    private String complemento;

    private String bairro;

    private String descricao;

    private String nomeFantasia;

    private String cnpj;

    private String senhaCertificado;

    private String responsavel;

    private String fone;

    private String celular;

    private String email;

    private String ativo;

    private String tipo;

    private String tipoConta;

    private String regimeTributario;

    private String inscricaoEstadual;

    private String indicadorInscricaoEstadual;

    private String inscricaoMunicipal;

    private String nire;

    private String numRegistroCartorio;

    private Double faturamento;

    private Date dataConstituicao;

    private Date dataCredenciamento;

    private String tipoCertificadoDigital;

    private String acessoPrivado;

    private String nomeCertificadoDigital;

    private String podePedirAdiantamento;

    private byte[] logo;

    private String primeiroLogin;

    private String mensalidade;

    private Double creditoIcms;

    private String incluirInformacoesDeImpostoNaNota;

    private String nacional;

    private String tpLogradouro;

    private Integer idCategoriaEmpresa;

    private String tipoUso;

    private Double taxaCorte;

    private Double taxaRemunecacaoPlataforma;

    private Double saldoDisponivel;

    private String tipoEmpresa;

    private String porte;

    private String ondeConheceu;

    private String ramo;

}
