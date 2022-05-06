package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponseDTO {

    private HolderDTO holder;

    private BankAccountDTO bankAccount;

    private BusinessActivityDTO businessActivity;

    private AddressDTO address;

    private RegistrationOriginDTO registrationOrigin;

    private AccountConfigurationDTO account;

}
