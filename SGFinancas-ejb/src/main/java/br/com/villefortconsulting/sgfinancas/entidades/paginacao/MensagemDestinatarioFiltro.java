package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MensagemDestinatarioFiltro extends BasicFilter<EntityId> {

    private static final long serialVersionUID = 1L;

    private Usuario usuarioDestinatario;

    private boolean somentePrioritarias;

    private boolean excluidas;

}
