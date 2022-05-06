/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.servicos.getnet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {

    @JsonProperty("access_token")
    private String token;

    @JsonProperty("token_type")
    private String tipoToken;

    @JsonProperty("refresh_token")
    private String tokenAtualizado;

    @JsonProperty("expires_in")
    private Long expiracao;

    private String scope;

}
