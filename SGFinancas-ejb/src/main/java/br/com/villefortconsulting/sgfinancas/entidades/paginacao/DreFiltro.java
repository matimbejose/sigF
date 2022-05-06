package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.util.DataUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DreFiltro extends BasicFilter<EntityId> {

    private static final long serialVersionUID = 1L;

    private Integer ano = DataUtil.getAnoCorrente();

}
