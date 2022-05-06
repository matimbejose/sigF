package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PontoEntradaSaidaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String PATTERN_HORA = "HH:mm:ss";

    private String funcionario;

    private Integer dia;

    private String diaSemana;

    private Date entradaManha;

    private Date saidaManha;

    private Date entradaTarde;

    private Date saidaTarde;

    private Date entradaExtra;

    private Date saidaExtra;

    private String somaHoras;

    private String totalHoras;

    private String observacao;

    private Integer idFuncionario;

    private Long horasObrigatorias;

    private String tipoContratacao;

    public Long getTempoTotalTrabalhado() {
        Long tempoTotal = 0l;

        if (entradaManha != null && saidaManha != null) {
            LocalDateTime timeEntradaManha = entradaManha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime timeSaidaManha = saidaManha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            tempoTotal += Duration.between(timeEntradaManha, timeSaidaManha).toMillis();

        }

        if (entradaTarde != null && saidaTarde != null) {
            LocalDateTime timeEntradaTarde = entradaTarde.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime timeSaidaTarde = saidaTarde.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            tempoTotal += Duration.between(timeEntradaTarde, timeSaidaTarde).toMillis();
        }

        if (entradaExtra != null && saidaExtra != null) {
            LocalDateTime timeEntradaExtra = entradaExtra.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime timeSaidaExtra = saidaExtra.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            tempoTotal += Duration.between(timeEntradaExtra, timeSaidaExtra).toMillis();
        }

        return tempoTotal;

    }

    public Long getDiferencaHoraObrigatoriaTempo() {
        if (this.horasObrigatorias == null || this.horasObrigatorias == 0l) {
            return getTempoTotalTrabalhado();
        }
        return getTempoTotalTrabalhado() - this.horasObrigatorias;
    }

    public String getHoraObrigatoriaFormata() {
        if (this.horasObrigatorias != 0l) {
            LocalTime localTime = LocalTime.ofSecondOfDay(this.horasObrigatorias / 1000);
            return localTime.format(DateTimeFormatter.ofPattern(PATTERN_HORA));
        }
        return null;
    }

    public String getHorasFeitas() {
        Long tempoTotalTrabalhado = getTempoTotalTrabalhado();
        if (tempoTotalTrabalhado != 0l) {
            LocalTime localTime = LocalTime.ofSecondOfDay(tempoTotalTrabalhado / 1000);
            return localTime.format(DateTimeFormatter.ofPattern(PATTERN_HORA));
        }
        return null;
    }

    public String getDiferencaHoraFormatada() {

        Long diferencaHora = getDiferencaHoraObrigatoriaTempo();

        if (diferencaHora == null || diferencaHora == 0l) {
            return null;
        } else if (diferencaHora < 0) {

            diferencaHora *= -1;

            LocalTime localTime = LocalTime.ofSecondOfDay(diferencaHora / 1000);

            return localTime.format(DateTimeFormatter.ofPattern("- HH:mm:ss"));
        } else {

            LocalTime localTime = LocalTime.ofSecondOfDay(diferencaHora / 1000);

            return localTime.format(DateTimeFormatter.ofPattern(PATTERN_HORA));
        }

    }

    public Date getDataHoraPonto(Integer id) {
        switch (id) {
            case 0:
                return this.entradaManha;
            case 1:
                return this.saidaManha;
            case 2:
                return this.entradaTarde;
            case 3:
                return this.saidaTarde;
            case 4:
                return this.entradaExtra;
            case 5:
                return this.saidaExtra;
            default:
                return null;
        }
    }

}
