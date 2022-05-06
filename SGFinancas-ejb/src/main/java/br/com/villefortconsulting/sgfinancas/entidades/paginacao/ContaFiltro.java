package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaFiltro extends BasicFilter<Conta> {

    private static final long serialVersionUID = 1L;

    private String origem;

    private String dataInicio;

    private String dataFim;

    private String tipoTransacao;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        for (String data : urlInfo.getOrDefault("data", new ArrayList<>())) {
            if (data.contains(":")) {
                String[] partes = data.split(":");
                if (partes.length == 2) {
                    try {
                        switch (partes[0]) {
                            case "ge":
                                dataInicio = partes[1];
                                break;
                            case "le":
                                dataFim = partes[1];
                                break;
                            default:
                                break;
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(ContaFiltro.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (urlInfo.getFirst("tipoTransacao") != null) {
            tipoTransacao = EnumTipoTransacao.retornaEnumSelecionado(urlInfo.getFirst("tipoTransacao")).getTipo();
        }
    }

}
