package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusOrdemServicoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private EnumStatusOrdemDeServico status;

    private Date horario;

    private FuncionarioDTO funcionario;

    public StatusOrdemServicoDTO(String e) {
        status = EnumStatusOrdemDeServico.valueOf(e);
        horario = new Date();
    }

}
