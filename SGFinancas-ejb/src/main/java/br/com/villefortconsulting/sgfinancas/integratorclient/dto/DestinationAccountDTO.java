package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationAccountDTO {

    private String id;

    private String holderName;

    private String bankAccountHolderName;

    private String holderTaxDocument;

    private String bankAccountHolderTaxDocument;

    private String bankNumber;

    private String bankAccountNumber;

    private String bankBranchNumber;

    private String bankAccountVariation;

    private String bankAccountType;

}
