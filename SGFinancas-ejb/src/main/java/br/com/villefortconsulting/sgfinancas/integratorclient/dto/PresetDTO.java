package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresetDTO {

    private String password;

    private String phone;

    private String mobile;

    private String commercialName;

    private String softDescriptor;

    private TransferPlanDTO transferPlanObject;

    private BankAccountDTO bankAccountObject;

    private AddressDTO addressObject;

    private ContactDTO contactObject;

}
