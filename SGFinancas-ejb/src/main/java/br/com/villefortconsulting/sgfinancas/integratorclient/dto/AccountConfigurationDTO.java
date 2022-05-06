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
public class AccountConfigurationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean active;

    private boolean saleOnline;

    private boolean approved;

    private boolean reproved;

    private boolean freeTrial;

    private boolean balanceBlocked;

    private boolean anticipatedTransfer;

    private boolean oldAnticipationPlan;

    private boolean isOriginPartner;

    private float vanBanese;

    private String commercialName;

    private String softDescriptor;

    private String blockedPaymentMethod;

    private TransferPlanDTO transferPlan;

}
