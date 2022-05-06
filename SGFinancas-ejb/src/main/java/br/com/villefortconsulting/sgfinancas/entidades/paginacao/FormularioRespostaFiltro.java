package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.FormularioResposta;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormularioRespostaFiltro extends BasicFilter<FormularioResposta> {

    private static final long serialVersionUID = 1L;

    private Cliente cliente;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        if (urlInfo.getFirst("cliente") != null) {
            cliente = new Cliente();
            cliente.setId(stringToId(urlInfo.getFirst("cliente")));
        }
    }

}
