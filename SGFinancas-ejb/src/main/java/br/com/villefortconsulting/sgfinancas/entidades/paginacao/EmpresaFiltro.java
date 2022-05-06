package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaFiltro extends BasicFilter<Empresa> {

    private static final long serialVersionUID = 1L;

    public EmpresaFiltro() {
        ativo = "S";
    }

    private Integer codIBGE;

    private String agendaHabilitada;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        if (urlInfo.getFirst("codIBGE") != null) {
            codIBGE = Integer.parseInt(urlInfo.getFirst("codIBGE"));
        }
    }

}
