package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String businessActivityId;

    private String marketingMediaId;

    private String commercialName;

    private String appUrl;

    private UserDTO user;

    private HolderDTO holder;

    private AddressDTO address;

    private BankAccountDTO bankAccount;

    private TransferPlanDTO transferPlan;

}
