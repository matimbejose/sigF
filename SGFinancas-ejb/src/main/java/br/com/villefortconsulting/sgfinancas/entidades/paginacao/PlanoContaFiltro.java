package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.util.operator.In;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanoContaFiltro extends BasicFilter<PlanoConta> {

    private static final long serialVersionUID = 1L;

    private String codigo;

    private String tipo;

    private String tipoBalanco;

    private String padrao;

    private String mostraNaTelaInicial;

    private String grupoContabil;

    private String selecionaveis;

    private In<String> codigos;

    private boolean api = false;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        codigo = urlInfo.getFirst("codigo");
        codigos = In.fromList(getArray(urlInfo, "codigos"));
        tipo = urlInfo.getFirst("tipo");
        tipoBalanco = urlInfo.getFirst("tipoBalanco");
        padrao = urlInfo.getFirst("registroPadrao");
        mostraNaTelaInicial = urlInfo.getFirst("mostraTelaInicial");
        grupoContabil = urlInfo.getFirst("grupoContabilidade");
        selecionaveis = urlInfo.getFirst("selecionaveis");
        api = true;
    }

}
