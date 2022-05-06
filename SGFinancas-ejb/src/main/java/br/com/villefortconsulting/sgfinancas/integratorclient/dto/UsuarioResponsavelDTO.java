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
public class UsuarioResponsavelDTO {

    private String login;

    private String password;

    private String document;

    private Integer smsCode;

    private String tokenSFA;

    private String tokenApp;

    private String mobilePhone;

    private List<RequisicaoTransferenciaDTO> transferRequests;

}
