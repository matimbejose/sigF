package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;

@Data
public class SecaoDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String descricao;

    private String ativo;

    private String tipoResposta;

    private List<ItemSecaoDTO> itens = new LinkedList<>();

}
