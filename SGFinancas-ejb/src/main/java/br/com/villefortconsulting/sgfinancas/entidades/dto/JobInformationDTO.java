package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInformationDTO implements Serializable {

    private static final long serialVersionUID = 8430944292564663080L;

    private long instanceID;

    private long executionID;

    private String jobName;

    private Date startTime;

    private Date endTime;

    private String batchStatus;

    private String responsavel;

    private String prefeitura;

    private Date competencia;

    private String layout;

    private String nomeArquivo;

    private String situacao;

    private String consignatariaProduto;

    private Integer nroTotalRegistros;

    private Integer nroRegistrosProcessados;

    private String tipo;

    private String fase;

    private String mensagemErro;

    public Double getPorcentagem() {
        if (nroTotalRegistros != null && nroRegistrosProcessados != null) {
            Double totalRegistros = Double.valueOf(nroTotalRegistros);
            Double registrosProcessados = Double.valueOf(nroRegistrosProcessados);

            return registrosProcessados / totalRegistros;
        }
        return 0d;
    }

}
