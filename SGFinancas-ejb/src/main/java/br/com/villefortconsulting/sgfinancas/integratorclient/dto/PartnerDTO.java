package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDTO {

    private String id;

    private String fullName;

    private String gender;

    private String taxDocument;

    private String email;

    private String phone;

    private String mobile;

    private boolean isPartner;

    private LocalDate birthDate;

    private String createdAt;

    private CompanyDTO company;

}
