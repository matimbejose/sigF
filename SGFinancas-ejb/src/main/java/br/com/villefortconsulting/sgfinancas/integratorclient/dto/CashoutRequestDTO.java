package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashoutRequestDTO {

    private String id;

    private String created;

    private float amount;

    private String note;

    private String status;

    private boolean automatic;

    private float bankTransferFee;

    private String holderIdDestination;

    private String splitterIdDestination;

    private String type;

}
