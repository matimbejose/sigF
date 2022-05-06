package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FechamentoContabilDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer mesCompetencia;

    private Integer anoCompetencia;

    private Long quantidadeParcelas;

    private String fechada;

}
