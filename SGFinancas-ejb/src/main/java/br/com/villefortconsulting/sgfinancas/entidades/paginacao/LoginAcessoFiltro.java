package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.LoginAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.util.operator.In;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAcessoFiltro extends BasicFilter<LoginAcesso> {

    private static final long serialVersionUID = 1L;

    public LoginAcessoFiltro() {
        this.ativo = "S";
    }

    public LoginAcessoFiltro(Usuario usuario) {
        this.usuario = usuario;
        this.ativo = "S";
    }

    private Usuario usuario;

    private Empresa empresa;

    private String cnpj;

    private String inscricaoEstadual;

    private String responsavel;

    private String tipo;

    private In<String> tipoEmpresa;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        cnpj = urlInfo.getFirst("cnpj");
        inscricaoEstadual = urlInfo.getFirst("inscricaoEstadual");
        responsavel = urlInfo.getFirst("responsavel");
        tipo = urlInfo.getFirst("tipo");
    }

}
