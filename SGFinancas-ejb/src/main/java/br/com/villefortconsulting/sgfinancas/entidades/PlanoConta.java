package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Table(name = "PLANO_CONTA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class PlanoConta extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Size(max = 200, message = "Descrição deverá ter até 200 caracteres.")
    @Column(name = "DESCRICAO")
    private String descricao;

    @NotNull(message = "Informe se é um registro padrão")
    @Size(max = 1, message = "Registro padrão deverá ter 1 caractere.")
    @Column(name = "REGISTRO_PADRAO")
    private String registroPadrao;

    @NotNull(message = "Informe o tipo")
    @Size(max = 1, message = "Tipo deverá ter 1 caractere.")
    @Column(name = "TIPO")
    private String tipo;

    @NotNull(message = "Informe o tipo indicador")
    @Column(name = "TIPO_INDICADOR")
    @Size(max = 1, message = "Tipo de indicador deverá ter 1 caractere.")
    private String tipoIndicador;

    @NotNull(message = "Informe o tipo de balanço")
    @Size(max = 1, message = "Tipo de balanço deverá ter tre 1 caractere.")
    @Column(name = "TIPO_BALANCO")
    private String tipoBalanco;

    @NotNull(message = "Informe o código")
    @Column(name = "CODIGO")
    @Size(max = 40, message = "O código deve ter no máximo 40 caracteres.")
    private String codigo;

    @NotNull(message = "Informe se pode incluir contas filhas")
    @Column(name = "PODE_TER_FILHO")
    @Size(max = 1, message = "Pode ter filho deve ter no máximo 1 caractere.")
    private String podeTerFilho;

    @Column(name = "BLOQUEIA_FILHO_CONTA_RECEBER")
    @Size(max = 1, message = "Bloqueia filho conta a receber deve ter no máximo 1 caractere.")
    private String bloqueiaFilhoContaReceber;

    @Column(name = "BLOQUEIA_FILHO_CONTA_PAGAR")
    @Size(max = 1, message = "Bloqueia filho conta a pagar deve ter no máximo 1 caractere.")
    private String bloqueiaFilhoContaPagar;

    @JoinColumn(name = "ID_CONTA_PAI", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idContaPai;

    @JoinColumn(name = "ID_CONTA_CONTRAPARTIDA", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idContaContrapartida;

    @Column(name = "MOSTRA_TELA_INICIAL")
    @Size(max = 1, message = "Mostrar tela inicial deverá ter 1 caractere.")
    private String mostraTelaInicial;

    @Column(name = "CODIGO_CONTABILIDADE")
    @Size(max = 20, message = "O código da contabilidade deve ter no máximo 20 caracteres.")
    private String codigoContabilidade;

    @Transient
    private String codigoPai;

    @Size(max = 1)
    @Column(name = "PLANO_ANTIGO")
    private String planoAntigo;

    public PlanoConta(Integer id) {
        this.id = id;
    }

    public PlanoConta(String descricao, String codigo, String registroPadrao, String tipo, String tipoIndicador, String mostraTelaInicial, String tipoBalanco, String tenatId, String codigoPai) {
        this.descricao = descricao;
        this.codigo = codigo;
        this.registroPadrao = registroPadrao;
        this.tipo = tipo;
        this.tipoIndicador = tipoIndicador;
        this.mostraTelaInicial = mostraTelaInicial;
        this.tipoBalanco = tipoBalanco;
        this.tenatID = tenatId;
        this.codigoPai = codigoPai;
    }

    public PlanoConta(Integer id, String codigo, String descricao, String tipo, PlanoConta planoConta, String registroPadrao, String mostraTelaInicial, String tipoBalanco) {
        this.id = id;
        this.descricao = descricao;
        this.tipo = tipo;
        this.codigo = codigo;
        this.idContaPai = planoConta;
        this.registroPadrao = registroPadrao;
        this.mostraTelaInicial = mostraTelaInicial;
        this.tipoBalanco = tipoBalanco;
    }

    public String getGrupoContabilidade() {
        try {
            return codigo.split("\\.")[0];
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "PlanoConta{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
