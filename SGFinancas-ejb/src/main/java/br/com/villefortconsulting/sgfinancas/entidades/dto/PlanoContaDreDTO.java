package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlanoContaDreDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private final String descricao;

    private final String tipo;

    private final String codigo;

    private final String codigoPai;

    public PlanoContaDreDTO(PlanoConta pc) {
        this.codigo = pc.getCodigo();
        this.descricao = pc.getDescricao();
        this.tipo = pc.getTipo();
        if (pc.getIdContaPai() != null) {
            this.codigoPai = pc.getIdContaPai().getCodigo();
        } else {
            this.codigoPai = null;
        }
    }

}
