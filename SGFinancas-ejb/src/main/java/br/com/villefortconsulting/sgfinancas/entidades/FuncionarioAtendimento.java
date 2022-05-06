package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.entity.Time;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "FUNCIONARIO_ATENDIMENTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class FuncionarioAtendimento extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_FUNCIONARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Funcionario idFuncionario;

    @NotNull(message = "Informe o dia da semana")
    @Column(name = "DIA_SEMANA")
    @Min(0)
    @Max(6)
    private Integer diaSemana;

    @NotNull(message = "Informe o horário inicial")
    @Column(name = "HORA_INICIAL")
    @Temporal(TemporalType.TIME)
    private Date horaInicial;

    @NotNull(message = "Informe o horário final")
    @Column(name = "HORA_FINAL")
    @Temporal(TemporalType.TIME)
    private Date horaFinal;

    public Time getTempoDisponibilidade() {
        return new Time(horaFinal).sub(new Time(horaInicial));
    }

    @Override
    public String toString() {
        return "FuncionarioAtendimento{" + "id=" + id + "tenat=" + tenatID + '}';
    }

    public static Optional<FuncionarioAtendimento> contains(List<FuncionarioAtendimento> list, FuncionarioAtendimento item) {
        return list.stream()
                .filter(fa -> fa.getDiaSemana().equals(item.getDiaSemana()) && fa.getIdFuncionario().getId().equals(item.getIdFuncionario().getId()))
                .findAny();
    }

}
