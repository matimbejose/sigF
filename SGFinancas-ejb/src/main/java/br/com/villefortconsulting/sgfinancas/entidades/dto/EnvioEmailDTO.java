package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvioEmailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;

    private String mensagem;

    private boolean enviado;

}
