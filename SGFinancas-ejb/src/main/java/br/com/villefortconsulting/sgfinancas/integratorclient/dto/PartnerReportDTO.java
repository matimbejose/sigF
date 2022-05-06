package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerReportDTO {

    private String count;

    private List<Integer> sellers;

}
