package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Solicitante;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitanteHistorico;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitanteHistoricoFiltro extends BasicFilter<SolicitanteHistorico> {

    private static final long serialVersionUID = 1L;

    private Solicitante solicitante;

}
