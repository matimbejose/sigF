package br.com.villefortconsulting.servicos.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {

    private String login;

    private String senha;

    private String deviceId;

}
