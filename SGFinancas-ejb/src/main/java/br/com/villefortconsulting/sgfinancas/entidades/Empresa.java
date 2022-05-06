package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EnderecoGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import br.com.villefortconsulting.sgfinancas.util.EnumRegimeTributario;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsoSistema;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@Audited
@Cacheable
@Inheritance
@Table(name = "EMPRESA")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Empresa extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "numero", column = @Column(name = "NUMERO_ENDERECO")),
        @AttributeOverride(name = "complemento", column = @Column(name = "COMPLEMENTO_ENDERECO"))
    })
    private Endereco endereco = new Endereco();

    @NotNull(message = "Informe a descrição")
    @Size(max = 200, message = "Descrição deve possuir no máximo 200 caracteres")
    @Column(name = "DESCRICAO")
    private String descricao;

    @NotNull(message = "Informe o nome fantasia")
    @Size(max = 300, message = "Nome fantasia deve possuir no máximo 300 caracteres")
    @Column(name = "NOME_FANTASIA")
    private String nomeFantasia;

    @Size(max = 20, message = "CNPJ deve possuir no máximo 20 caracteres")
    @Column(name = "CNPJ")
    private String cnpj;

    @Size(max = 25, message = "Senha do certificado deve possuir no máximo 25 caracteres")
    @Column(name = "SENHA_CERTIFICADO")
    private String senhaCertificado;

    @Size(max = 100, message = "Responsável deve possuir no máximo 100 caracteres")
    @Column(name = "RESPONSAVEL")
    private String responsavel;

    @Size(max = 100, message = "Telefone deve possuir no máximo 100 caracteres")
    @Column(name = "FONE")
    private String fone;

    @Size(max = 20, message = "Celular deve possuir no máximo 20 caracteres")
    @Column(name = "CELULAR")
    private String celular;

    @Size(max = 100, message = "Email deve possuir no máximo 100 caracteres")
    @Column(name = "EMAIL")
    private String email;

    @NotNull(message = "Informe se a empresa está ativa")
    @Size(max = 1, message = "Ativo deve possuir no máximo 1 caracter")
    @Column(name = "ATIVO")
    private String ativo;

    @NotNull(message = "Informe o tipo da empresa")
    @Size(max = 2, message = "Tipo deve possuir no máximo 2 caracteres")
    @Column(name = "TIPO")
    private String tipo;

    @NotNull(message = "Informe o tipo da conta")
    @Size(max = 2, message = "Tipo deve possuir no máximo 2 caracteres")
    @Column(name = "TIPO_CONTA")
    private String tipoConta;

    @Size(max = 4, message = "Regime tributário deve possuir no máximo 4 caracteres")
    @Column(name = "REGIME_TRIBUTARIO")
    private String regimeTributario;

    @Size(max = 50, message = "Inscrição estadual deve possuir no máximo 50 caracter")
    @Column(name = "INSCRICAO_ESTADUAL")
    private String inscricaoEstadual;

    @Size(max = 2, message = "Indicador inscrição estadual deve possuir no máximo 2 caracteres")
    @Column(name = "INDICADOR_INSCRICAO_ESTADUAL")
    private String indicadorInscricaoEstadual;

    @Column(name = "INSCRICAO_MUNICIPAL")
    @Size(max = 50, message = "Inscrição municipal deve possuir no máximo 50 caracteres")
    private String inscricaoMunicipal;

    @Size(max = 20, message = "NIRE deve possuir no máximo 20 caracteres")
    @Column(name = "NIRE")
    private String nire;

    @Size(max = 20, message = "Número de registro no cartório deve possuir no máximo 20 caracteres")
    @Column(name = "NUM_REGISTRO_CARTORIO")
    private String numRegistroCartorio;

    @Column(name = "FATURAMENTO")
    private Double faturamento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_CONSTITUICAO")
    private Date dataConstituicao;

    @NotNull(message = "Informe da data de credenciamento da empresa")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_CREDENCIAMENTO")
    private Date dataCredenciamento;

    @JoinColumn(name = "ID_CNAE", referencedColumnName = "ID")
    @ManyToOne
    private Cnae idCnae;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private Documento idDocumento;

    @JoinColumn(name = "ID_CONTABILIDADE", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private Contabilidade idContabilidade;

    @Column(name = "TIPO_CERTIFICADO_DIGITAL")
    private String tipoCertificadoDigital;

    @Column(name = "ACESSO_PRIVADO")
    private String acessoPrivado;

    @Column(name = "NOME_CERTIFICADO_DIGITAL")
    private String nomeCertificadoDigital;

    @Column(name = "PODE_PEDIR_ADIANTAMANETO")
    private String podePedirAdiantamento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresa", orphanRemoval = true)
    private List<ClassificacaoEmpresa> listClassificacaoEmpresa = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresa", orphanRemoval = true)
    private List<GrupoEmpresa> listGrupoEmpresa = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresa", orphanRemoval = true)
    private List<EmpresaCnae> listEmpresaCnae = new LinkedList<>();

    @OneToMany(mappedBy = "idEmpresa")
    private List<LoginAcesso> loginAcessoList = new LinkedList<>();

    @OneToMany(mappedBy = "idEmpresa")
    private List<PagamentoMensalidade> pagamentoMensalidadeList = new LinkedList<>();

    @Lob
    @Column(name = "LOGO")
    private byte[] logo;

    @Column(name = "PRIMEIRO_LOGIN")
    private String primeiroLogin;

    @Column(name = "MENSALIDADE")
    private String mensalidade;

    @Column(name = "CREDITO_ICMS")
    private Double creditoIcms;

    @Column(name = "INCLUIR_INFORMACOES_DE_IMPOSTO_NA_NOTA")
    private String incluirInformacoesDeImpostoNaNota;

    @Size(max = 1, message = "Nacional deve possuir no máximo 1 caracter")
    @Column(name = "NACIONAL")
    private String nacional;

    @Size(max = 4, message = "Tipo Logradouro deve possuir no máximo 4 caracteres")
    @Column(name = "TP_LOGRADOURO")
    private String tpLogradouro;

    @JoinColumn(name = "ID_CATEGORIA_EMPRESA", referencedColumnName = "ID")
    @ManyToOne
    private CategoriaEmpresa idCategoriaEmpresa;

    @Size(max = 1, min = 1, message = "Nacional deve possuir 1 caracter")
    @NotNull(message = "Informe o tipo de uso do sistema")
    @Column(name = "TIPO_USO")
    private String tipoUso = "A";

    @Column(name = "TAXA_CORTE")
    private Double taxaCorte;

    @Column(name = "TX_REMUNERACAO_PLATAFORMA")
    private Double taxaRemunecacaoPlataforma;

    @Column(name = "SALDO_DISPONIVEL")
    private Double saldoDisponivel;

    @Size(max = 2, message = "Tipo de Empresa deve possuir no máximo 2 caracteres")
    @Column(name = "TIPO_EMPRESA")
    private String tipoEmpresa;

    @Column(name = "PORTE")
    private String porte;

    //---Dados adicionais conta digital
    @Basic(optional = false)
    @Size(min = 8, message = "Informe o telefone")
    @Column(name = "TELEFONE")
    private String telefone;

    @Column(name = "DATA_NASCIMENTO_REPRESENTANTE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataNascimentoRepresentante;

    @Basic(optional = false)
    @Column(name = "CELULAR_REPRESENTANTE")
    private String celularRepresentante;

    @Basic(optional = false)
    @Column(name = "CPF_REPRESENTANTE")
    private String cpfRepresentante;

    @Basic(optional = false)
    @Column(name = "CODIGO_IBGE")
    private Integer codigoIBGE;

    @Basic(optional = false)
    @Column(name = "SENHA")
    private String senha;

    @JoinColumn(name = "ID_PLANO_TRANSF", referencedColumnName = "ID")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private PlanoTransferencia planoTransferencia;

    @Basic(optional = false)
    @Column(name = "SEXO_REPRESENTANTE")
    private String sexoRepresentante;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_CONTATO_TITULAR", referencedColumnName = "ID")
    private ContatoTitular contatoTitular;

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_SOCIAL_EMPRESA", referencedColumnName = "ID")
    private TipoSocialEmpresa tipoSocialEmpresa;

    @OneToOne
    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    private ContaBancaria idContaBancariaDigital;

    @Column(name = "ONDE_CONHECEU")
    private String ondeConheceu;

    @Column(name = "RAMO")
    private String ramo;

    @Column(name = "PRAZO_USAR_SEM_COMPRAR")
    private Integer prazoUsarSemComprar;

    public Empresa() {
        this.ativo = "S";
        this.acessoPrivado = "N";
        this.dataCredenciamento = new Date();
    }

    public Empresa(Integer id) {
        this.id = id;
        this.ativo = "S";
        this.acessoPrivado = "N";
        this.dataCredenciamento = new Date();
    }

    public Empresa(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = "S";
        this.acessoPrivado = "N";
        this.dataCredenciamento = new Date();
    }

    public Endereco getEndereco() {
        if (endereco == null) {
            endereco = new Endereco();
        }
        return endereco;
    }

    public String getLogo64() {
        if (logo == null) {
            return "";
        }
        return Base64.getEncoder().encodeToString(logo);
    }

    public void addClassificacao(ClassificacaoEmpresa classificacaoEmpresa) {
        classificacaoEmpresa.setIdEmpresa(this);
        listClassificacaoEmpresa.add(classificacaoEmpresa);
    }

    public void removeClassificacao(ClassificacaoEmpresa classificacaoEmpresa) {
        listClassificacaoEmpresa.remove(classificacaoEmpresa);
    }

    public void addClassificacao(Classificacao classificacao) {
        ClassificacaoEmpresa classificacaoEmpresa = new ClassificacaoEmpresa();
        classificacaoEmpresa.setIdClassificacao(classificacao);
        classificacaoEmpresa.setIdEmpresa(this);

        listClassificacaoEmpresa.add(classificacaoEmpresa);
    }

    public void addCnae(EmpresaCnae empresaCnae) {
        empresaCnae.setIdEmpresa(this);
        listEmpresaCnae.add(empresaCnae);
    }

    public void removeCnae(EmpresaCnae empresaCnae) {
        listEmpresaCnae.remove(empresaCnae);
    }

    public void addGrupo(Grupo grupo) {
        GrupoEmpresa grupoEmpresa = new GrupoEmpresa();
        grupoEmpresa.setIdGrupo(grupo);
        grupoEmpresa.setIdEmpresa(this);

        if (!listGrupoEmpresa.contains(grupoEmpresa)) {
            listGrupoEmpresa.add(grupoEmpresa);
        }
    }

    public void removeUsuario(GrupoEmpresa grupoEmpresa) {
        listGrupoEmpresa.remove(grupoEmpresa);
    }

    public EnumTipoUsoSistema getEnumTipoUsoSistema() {
        return EnumTipoUsoSistema.retornaEnumSelecionado(tipoUso);
    }

    public boolean isPJ() {
        return cnpj == null || CpfCnpjUtil.validarCNPJ(cnpj);
    }

    public boolean isCredor() {
        return tipoSocialEmpresa != null && "CR".equals(tipoSocialEmpresa.getCodigoAcronimo());
    }

    public EnderecoGetnetDTO getEnderecoGetnet() {
        if (endereco == null) {
            return new EnderecoGetnetDTO();
        }
        Endereco addr = new Endereco();
        addr.setBairro(endereco.getBairro());
        addr.setCep(endereco.getCep());
        addr.setComplemento(endereco.getComplemento());
        addr.setEndereco(endereco.getEndereco());
        addr.setIdCidade(endereco.getIdCidade());
        addr.setNumero(endereco.getNumero());
        return new EnderecoGetnetDTO(addr);
    }

    public String getDescricaoRegimeTributario() {
        EnumRegimeTributario e = EnumRegimeTributario.retornaEnumSelecionado(regimeTributario);
        return e != null ? e.getDescricao() : "";
    }

    public boolean isCadastroIncompleto() {
        return cnpj == null || cnpj.trim().isEmpty();
    }

    public String getStyleClass() {
        return isCadastroIncompleto() ? "text-danger" : "";
    }

    @PrePersist
    private void prePersist() {
        if (this.tenatID == null) {
            this.tenatID = "";
        }
    }

    @PostPersist
    private void postPersist() {
        this.tenatID = id.toString();
    }

    @Override
    public String toString() {
        return "Empresa{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
