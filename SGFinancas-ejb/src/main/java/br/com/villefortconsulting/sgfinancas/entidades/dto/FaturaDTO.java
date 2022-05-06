package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaturaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cpfCnpj;

    private String nome;

    private EnderecoGetnetDTO endereco;

}
