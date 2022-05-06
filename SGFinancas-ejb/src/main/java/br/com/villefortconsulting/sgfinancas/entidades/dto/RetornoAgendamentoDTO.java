package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetornoAgendamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private EmpresaDTO empresa;

    private VendaRestDTO agendamento;

}
