package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

@Audited
@Entity
@Table(name = "CLIENTE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Cliente extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Embedded
    private Endereco endereco = new Endereco();

    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private PlanoConta idPlanoConta;

    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario idUsuario;

    @Column(name = "NOME")
    @Size(max = 200, message = "Máximo de 200 caracteres para o nome")
    private String nome;

    @NotNull(message = "Informe o cpf/cnpj")
    @Column(name = "CPF_CNPJ", nullable = false, length = 200)
    @Size(max = 20, message = "Máximo de 20 caracteres para o cpf/cnpj")
    private String cpfCNPJ;

    @NotNull(message = "Informe o tipo")
    @Column(name = "TIPO")
    @Size(max = 2, message = "Máximo de 2 caracteres para o tipo")
    private String tipo;

    @Column(name = "RAZAO_SOCIAL")
    @Size(max = 200, message = "Máximo de 200 caracteres para a razão social")
    private String razaoSocial;

    @Column(name = "TEM_INSCRICAO_ESTADUAL")
    @Size(max = 1, message = "Máximo de 1 caracteres para tem inscrição estadual")
    private String temInscricaoEstadual;

    @Column(name = "INSCRICAO_ESTADUAL")
    @Size(max = 50, message = "Máximo de 50 caracteres para a inscrição estadual")
    private String inscricaoEstadual;

    @Column(name = "INDICADOR_INSCRICAO_ESTADUAL")
    @Size(max = 1, message = "Máximo de 1 caracter para indicador inscrição estadual")
    private String indicadorInscricaoEstadual;

    @Column(name = "INSCRICAO_MUNICIPAL")
    @Size(max = 50, message = "Máximo de 50 caracteres para a inscrição municipal")
    private String inscricaoMunicipal;

    @Column(name = "OPTA_SIMPLES")
    @Size(max = 1, message = "Máximo de 1 caracteres para OPTA_SIMPLES")
    private String optaSimples;

    @Column(name = "EMAIL")
    @Size(max = 200, message = "Máximo de 200 caracteres para o email")
    private String email;

    @Column(name = "TELEFONE_COMERCIAL")
    @Size(max = 50, message = "Máximo de 50 caracteres para o telefone comercial")
    private String telefoneComercial;

    @Column(name = "TELEFONE_RESIDENCIAL")
    @Size(max = 50, message = "Máximo de 50 caracteres para o telefone residencial")
    private String telefoneResidencial;

    @Column(name = "TELEFONE_CELULAR")
    @Size(max = 50, message = "Máximo de 50 caracteres para o telefone celular")
    private String telefoneCelular;

    @Column(name = "DESCRICAO_NFS")
    @Size(max = 5000, message = "Máximo de 5000 caracteres para descrição do NFS-e")
    private String descricaoNFS;

    @Column(name = "VALOR_IR")
    private Double valorIR;

    @Column(name = "VALOR_PIS")
    private Double valorPIS;

    @Column(name = "VALOR_CSLL")
    private Double valorCSLL;

    @Column(name = "VALOR_INSS")
    private Double valorINSS;

    @Column(name = "VALOR_COFINS")
    private Double valorCOFINS;

    @Column(name = "VALOR_ISS")
    private Double valorISS;

    @Column(name = "PONTUACAO")
    private Double pontuacao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCliente", orphanRemoval = true)
    private List<ClienteContato> clienteContatoList = new LinkedList<>();

    @Column(name = "ATIVO")
    private String ativo;

    @Column(name = "SEGURADORA")
    private String seguradora;

    @Column(name = "DATA_NASCIMENTO")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @Column(name = "DATA_EXPEDICAO")
    @Temporal(TemporalType.DATE)
    private Date dataExpedicao;

    @Column(name = "RG")
    private String rg;

    @Column(name = "SEXO")
    private String sexo;

    @Column(name = "CNH")
    private String cnh;

    @Column(name = "ORGAO_EXPEDIDOR")
    private String orgaoExpedidor;

    @Column(name = "CATEGORIA_CNH")
    private String categoriaCNH;

    @Column(name = "MATRICULA")
    private Integer matricula;

    @JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private CentroCusto idCentroCusto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCliente", orphanRemoval = true)
    private List<ClienteVeiculo> clienteVeiculoList = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCliente", orphanRemoval = true)
    private List<ClienteMovimentacao> clienteMovimentacaoList = new LinkedList<>();

    @Column(name = "SALDO_ATUAL")
    private Double saldoAtual;

    @Column(name = "SALDO_INICIAL")
    private Double saldoInicial;

    @Column(name = "ID_INTEGRACAO")
    private Long idIntegracao;

    public Cliente() {
        this.ativo = "S";
    }

    public Cliente(String nome, String cpfCnpj) {
        this.ativo = "S";
        this.nome = nome;
        this.cpfCNPJ = cpfCnpj;
        this.tipo = CpfCnpjUtil.removerMascaraCnpj(cpfCnpj).length() == 11 ? "PF" : "PJ";
    }

    public Cliente(Integer id) {
        this.id = id;
    }

    public void addClienteContato(ClienteContato clienteContato) {
        clienteContato.setIdCliente(this);
        clienteContatoList.add(clienteContato);
    }

    public void removeClienteContato(ClienteContato clienteContato) {
        clienteContatoList.remove(clienteContato);
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
        if ("PF".equals(tipo)) {
            this.temInscricaoEstadual = "N";
        }
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

    public void merge(Cliente update) {
        if (update == null || !this.getClass().isAssignableFrom(update.getClass())) {
            return;
        }
        for (Method method : this.getClass().getMethods()) {
            if (method.getDeclaringClass().equals(this.getClass()) && method.getName().startsWith("get")) {
                try {
                    Method toMetod = this.getClass().getMethod(method.getName().replace("get", "set"), method.getReturnType());
                    Object value = method.invoke(update, (Object[]) null);
                    if (value != null) {
                        toMetod.invoke(this, value);
                    }
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
