package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auth2FARequestDTO {

    private static final Integer EXPIRES_IN = 2;

    private String document;

    private String password;

    private String token;

}
