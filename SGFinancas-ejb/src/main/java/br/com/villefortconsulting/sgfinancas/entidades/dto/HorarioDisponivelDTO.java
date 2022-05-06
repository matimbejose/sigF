package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.Time;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HorarioDisponivelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Time horario;

    private Boolean livre;

    public String getHorario() {
        return horario.toString();
    }

    public Time obterHorario() {
        return horario;
    }

}
