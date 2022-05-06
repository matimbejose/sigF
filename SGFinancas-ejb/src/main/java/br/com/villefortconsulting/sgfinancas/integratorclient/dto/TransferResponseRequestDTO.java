package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponseRequestDTO {

    private CashoutRequestTransferDTO cashoutRequestMade;

    private CashoutRequestTransferDTO cashoutRequestNotMade;

}
