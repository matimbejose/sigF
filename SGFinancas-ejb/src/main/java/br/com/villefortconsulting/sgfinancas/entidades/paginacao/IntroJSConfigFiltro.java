package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.IntroJSConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntroJSConfigFiltro extends BasicFilter<IntroJSConfig> {

    private static final long serialVersionUID = 1L;

    private String pageId;

    private String forcaExibicao;

}
