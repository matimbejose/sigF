package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteGetnetDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idCliente;

    private String primeiroNome;

    private String ultimoNome;

    private String nomeCompleto;

    private String tipoDocumento;

    private String numeroDocumento;

    private EnderecoGetnetDTO endereco;

}
