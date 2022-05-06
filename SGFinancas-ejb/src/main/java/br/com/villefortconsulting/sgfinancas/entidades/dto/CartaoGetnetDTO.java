package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartaoGetnetDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nomeCartao;

    private String mesValidade;

    private String anoValidade;

    private String numeroCartao;

    private String cvv;

}
