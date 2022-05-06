package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequisicaoTransferenciaDTO {

    private Float amount;

    private String holderEmail;

    private String holderTaxDocument;

    private boolean recipientAssumesFee;

    private LocalDateTime requestDateTime = LocalDateTime.now();

    private Boolean transferSuccess;

}
