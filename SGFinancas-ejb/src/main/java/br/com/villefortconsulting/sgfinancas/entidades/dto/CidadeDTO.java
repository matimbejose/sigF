package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CidadeDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String descricao;

    private String nomeUF;

    private String siglaUF;

    private Integer codigoIBGE;

}
