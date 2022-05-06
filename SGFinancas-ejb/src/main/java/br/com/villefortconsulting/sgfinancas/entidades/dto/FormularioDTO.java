package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;

@Data
public class FormularioDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String descricao;

    private String observacoes;

    private String ativo;

    private boolean readOnly;

    private List<SecaoDTO> secoes = new LinkedList<>();

}
