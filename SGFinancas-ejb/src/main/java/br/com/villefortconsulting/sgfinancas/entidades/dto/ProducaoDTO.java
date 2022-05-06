package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProducaoDTO {

    private static final long serialVersionUID = 1L;

    private Produto produto;

    private Double quantidade;

    private Date dataProducao;

}
