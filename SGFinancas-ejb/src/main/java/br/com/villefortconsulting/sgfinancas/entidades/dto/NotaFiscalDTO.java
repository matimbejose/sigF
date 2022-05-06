package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaFiscalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String xmlNota;

    private String codigoUF;

    private String xmlRetorno;

    private Integer codigoNfe;

    private String tenat;

    private String login;

    private String senha;

    private Integer idNF;

}
