package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteFiltro extends BasicFilter<Cliente> {

    private static final long serialVersionUID = 1L;

    private Empresa idEmpresa;

    private String cpfCnpj;

    private String email;

    private String seguradora;

    private String placa;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        descricao = urlInfo.getFirst("nome");
        cpfCnpj = urlInfo.getFirst("cpfCNPJ");
        email = urlInfo.getFirst("email");
        seguradora = urlInfo.getFirst("seguradora");
        placa = urlInfo.getFirst("placa");
    }

}
