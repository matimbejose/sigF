package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferPlanDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "O Número de dias é obrigatório.")
    @Size(max = 2, message = "Número de dias superior a 2 caracteres.")
    private String days;

    @NotNull(message = "O campo antecipação é obrigatório.")
    private boolean anticipated;

    @NotNull(message = "O Número de dias que o split irá receber os pagamentos digitados é obrigatório.")
    @Size(max = 3, message = "Número de dias que o split irá receber os pagamentos digitados superior a 3 caracteres.")
    private String daysOnlineSplit;

    private boolean oldPlan;

    public static TransferPlanDTO buildTransferPlanDTO(Empresa empresa) {
        String dias = String.valueOf(2);
        String diasSplit = String.valueOf(2);

        return TransferPlanDTO.builder()
                .days(dias)
                .daysOnlineSplit(diasSplit)
                .anticipated(Boolean.TRUE)
                .build();
    }

}
