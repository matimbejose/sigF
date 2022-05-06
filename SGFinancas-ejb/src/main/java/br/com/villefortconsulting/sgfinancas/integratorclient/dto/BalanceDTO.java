package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {

    private String id;

    private String lastUpdate;

    private float tedFee;

    private boolean balanceBlocked;

    private float amountBlocked;

    private float transferFee;

    private float futureBalance;

    private float currentBalance;

    private float availableBalance;

    private float anticipatedBalance;

}
