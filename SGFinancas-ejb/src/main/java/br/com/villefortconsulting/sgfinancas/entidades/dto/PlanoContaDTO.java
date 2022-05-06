package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoContaDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String descricao;

    private Integer idContaPai;

    private String tipo;

    private String codigo;

    private Boolean podeTerFilho;

    private Integer idContaContrapartida;

    private Boolean podeSelecionar;

}
