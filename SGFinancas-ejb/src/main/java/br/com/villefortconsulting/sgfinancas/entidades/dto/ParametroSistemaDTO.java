package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class ParametroSistemaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private ParametroPlanoContaDTO planoConta;

    private ParametroProdutoDTO produto;

    private ParametroOrcamentoDTO orcamento;

    private ParametroAppMobileDTO app;

    private ParametroAgedamentoDTO agendamento;

    private ParametroVistoriaDTO vistoria;

}
