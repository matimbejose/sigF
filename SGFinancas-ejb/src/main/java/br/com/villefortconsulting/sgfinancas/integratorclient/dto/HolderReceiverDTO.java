package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolderReceiverDTO {

    private String id;

    private Float amount;

    private String holderEmail;

    private String holderTaxDocument;

    private boolean recipientAssumesFee;

}
