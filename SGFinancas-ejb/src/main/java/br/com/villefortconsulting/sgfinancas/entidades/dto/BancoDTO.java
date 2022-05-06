package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BancoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String descricao;

    private String numero;

    private String site;

}
