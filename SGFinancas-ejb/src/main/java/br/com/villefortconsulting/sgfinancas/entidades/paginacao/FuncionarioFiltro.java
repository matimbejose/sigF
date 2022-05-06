package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncionarioFiltro extends BasicFilter<Funcionario> {

    private static final long serialVersionUID = 1L;

    private String cpf;

    private String matricula;

    private String cargo;

    private String tenantID;

    private List<String> tipoContratacao = new ArrayList<>();

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        if (urlInfo.getFirst("tenantID") != null) {
            tenantID = urlInfo.getFirst("tenantID");
        }
        descricao = urlInfo.getFirst("nome");
        cpf = urlInfo.getFirst("cpf");
        matricula = urlInfo.getFirst("matricula");
        cargo = urlInfo.getFirst("cargo");
        tipoContratacao = urlInfo.getOrDefault("tipoContratacao", new ArrayList<>());
    }

}
