package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
import java.io.Serializable;
import lombok.Data;

@Data
public class ParametroOrcamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean geraContaUnificada;

    private EnumStatusOrdemDeServico statusNovaOS;

    private String observacao;

}
