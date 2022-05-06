package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationAccountDTO {

    private String id;

    private String name;

    private String subDomain;

    private boolean active;

    private boolean antiFraud;

    private String createdAt;

    private String releasedAt;

    private PartnerDTO partner;

    private ArrayList<Object> transferPlans;

}
