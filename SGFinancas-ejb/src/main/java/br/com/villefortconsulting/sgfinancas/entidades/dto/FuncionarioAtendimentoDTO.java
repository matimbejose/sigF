package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.Time;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioAtendimentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer diaSemana;

    private Time horaInicial;

    private Time horaFinal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicial() {
        return horaInicial.toString();
    }

    public Time obterHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(String hora) {
        if (hora.split(":").length == 2) {
            hora += ":00";
        }
        this.horaInicial = new Time(hora);
    }

    public void setHoraInicial(Date hora) {
        this.horaInicial = new Time(hora);
    }

    public String getHoraFinal() {
        return horaFinal.toString();
    }

    public Time obterHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String hora) {
        if (hora.split(":").length == 2) {
            hora += ":00";
        }
        this.horaFinal = new Time(hora);
    }

    public void setHoraFinal(Date hora) {
        this.horaFinal = new Time(hora);
    }

}
