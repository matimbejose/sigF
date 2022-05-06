package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.Ponto;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroPontoFiltro extends BasicFilter<Ponto> {

    private static final long serialVersionUID = 1L;

    private Date competencia;

    private Funcionario funcionario;

}
