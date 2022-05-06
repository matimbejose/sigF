package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolderReceiverResponseDTO {

    private Float amount;

    private String email;

    private String taxDocument;

}
