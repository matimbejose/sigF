package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitantePanaromaDTO {

    private static final long serialVersionUID = 1L;

    private String curso;

    private Date data;

    private Integer matriculas;

    private Integer interessadissimos;

    private Integer totalReal;

    private Integer interessadosAConfirmar;

    private Integer prognostico;

    private Integer interessados;

    private Integer solicitantes;

}
