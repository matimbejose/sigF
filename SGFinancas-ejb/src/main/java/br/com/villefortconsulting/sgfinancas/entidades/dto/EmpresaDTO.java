package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Data
public class EmpresaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @JsonProperty("atividade_principal")
    private AtividadeEmpresaDTO atividadePrincipal;

    @JsonProperty("data_situacao")
    private String dataSituacao;

    private String complemento;

    private String tipo;

    private String nome;

    private String email;

    @JsonProperty("atividades_secundarias")
    private List<AtividadeEmpresaDTO> atividadesSecundarias;

    private List<SocioEmpresaDTO> qsa;

    private String situacao;

    private String bairro;

    private String logradouro;

    private String numero;

    private String cep;

    private String cidade;

    private Integer codCidadeIBGE;

    private String municipio;

    private String fantasia;

    private String porte;

    private String abertura;

    @JsonProperty("natureza_juridica")
    private String naturezaJuridica;

    private String uf;

    private String telefone;

    private String cnpj;

    @JsonProperty("ultima_atualizacao")
    private String ultimaAtualizacao;

    private String status;

    private String efr;

    @JsonProperty("motivo_situacao")
    private String motivoSituacao;

    @JsonProperty("situacao_especial")
    private String situacaoEspecial;

    @JsonProperty("data_situacao_especial")
    private String dataSituacaoEspecial;

    @JsonProperty("capital_social")
    private String capitalSocial;

    private String message;// Apenas em caso de erro

    @JsonProperty("numero_de_inscricao")
    private String numeroDeInscricao;

    private byte[] logo;

    private String categoria;

    private UsuarioDTO usuarioLogado;

    private boolean periodoTeste;

    private Date dataValidadePagamento;

    public EmpresaDTO() {
        atividadesSecundarias = new ArrayList<>();
        qsa = new ArrayList<>();
    }

    public EmpresaDTO(String status) {
        atividadesSecundarias = new ArrayList<>();
        qsa = new ArrayList<>();
        this.status = status;
    }

    public void setDtSituacaoCadastral(String obj) {
        this.dataSituacao = obj;
    }

    public void setEntidadeFederativoResponsavel(String obj) {
        this.efr = obj;
    }

    public void setMotivoSituacaoCadastral(String obj) {
        this.motivoSituacao = obj;
    }

    public Boolean getPrecisaCompletarDados() {
        return cnpj == null || cnpj.trim().isEmpty();
    }

}
