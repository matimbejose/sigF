package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FornecedorFiltro extends BasicFilter<Fornecedor> {

    private static final long serialVersionUID = 1L;

    private String tipoPessoa;

    private String cpfCnpj;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        descricao = urlInfo.getFirst("nome");
        tipoPessoa = urlInfo.getFirst("tipoPessoa");
        cpfCnpj = urlInfo.getFirst("cpfCnpj");
    }

}
