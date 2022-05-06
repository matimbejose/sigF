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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "RECORRENCIA_AGENDAMENTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class RecorrenciaAgendamento extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "ENVIA_EMAIL")
    private String enviaEmail;

    @Column(name = "ENVIA_SMS")
    private String enviaSms;

    @NotNull(message = "informe a quantidade de dias para o alerta")
    @Min(value = 1, message = "A quantidade de dias deve ser maior que 1")
    @Column(name = "QUANTIDADE_DIAS")
    private Integer quatidadeDias;

    @NotNull
    @JoinColumn(name = "ID_PARAMETRO_SISTEMA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ParametroSistema idParametroSistema;

    public RecorrenciaAgendamento() {
    }

    public RecorrenciaAgendamento(ParametroSistema idParametroSistema) {
        this.idParametroSistema = idParametroSistema;
    }

    public RecorrenciaAgendamento(String enviaEmail, String enviaSms, Integer quatidadeDias, ParametroSistema idParametroSistema) {
        this.enviaEmail = enviaEmail;
        this.enviaSms = enviaSms;
        this.quatidadeDias = quatidadeDias;
        this.idParametroSistema = idParametroSistema;
        this.tenatID = idParametroSistema.getTenatID();
    }

    public Boolean getEnviaEmailBool() {
        return "S".equals(enviaEmail);
    }

    public void setEnviaEmailBool(Boolean envia) {
        enviaEmail = (envia != null && envia) ? "S" : "N";
    }

    public Boolean getEnviaSmsBool() {
        return "S".equals(enviaSms);
    }

    public void setEnviaSmsBool(Boolean envia) {
        enviaSms = (envia != null && envia) ? "S" : "N";
    }

    @Override
    public String toString() {
        return "RecorrenciaAgendamento{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
