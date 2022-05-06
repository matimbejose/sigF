package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaContatoCliente;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaContatoClienteFiltro extends BasicFilter<EmpresaContatoCliente> {

    private static final long serialVersionUID = 1L;

    private Empresa empresa;

    private Date dataProximoContato;

}
