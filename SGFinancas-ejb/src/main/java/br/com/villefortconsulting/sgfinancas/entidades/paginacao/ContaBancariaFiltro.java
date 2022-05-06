package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaBancariaFiltro extends BasicFilter<ContaBancaria> {

    private static final long serialVersionUID = 1L;

    private Banco banco;

    private String tipoConta;

    private String situacao;

    private String agencia;

    private String conta;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        if (urlInfo.getFirst("idBanco") != null) {
            banco = new Banco();
            banco.setId(stringToId(urlInfo.getFirst("idBanco")));
        }
        tipoConta = urlInfo.getFirst("tipoConta");
        situacao = urlInfo.getFirst("situacao");
        agencia = urlInfo.getFirst("agencia");
        conta = urlInfo.getFirst("conta");
    }

}
