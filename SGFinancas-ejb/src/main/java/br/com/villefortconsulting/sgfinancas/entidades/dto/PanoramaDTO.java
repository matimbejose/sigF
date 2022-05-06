package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanoramaDTO {

    private static final long serialVersionUID = 1L;

    private String status;

    private Date data;

    private Long quantidade;

    private String curso;

}
