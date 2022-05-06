package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.swconsultoria.nfe.dom.enuns.EstadosEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String username;

    private String token;

    private EstadosEnum state;

    private NfceCredentialsDTO nfceCredentials;

    private CertificateDTO certificate;

    private String auth;

    private String message;// Para exceptions

}
