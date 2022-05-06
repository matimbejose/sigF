package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.util.EnumTipoConfirmacaoAgendamento;
import java.io.Serializable;
import lombok.Data;

@Data
public class ParametroAgedamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean habilitaUsoAgenda;

    private boolean envioSmsParaClienteAoAgendar;

    private boolean envioSmsParaClienteAoAlterar;

    private boolean envioSmsParaClienteUmDiaAntes;

    private boolean envioSmsParaFuncionarioAoConfirmar;

    private boolean envioSmsParaEmpresaAoSolicitar;

    private String envioSmsTelefone;

    private EnumTipoConfirmacaoAgendamento tipoConfirmacao;

}
