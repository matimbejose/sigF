package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoGetnetDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cidade;

    private String complemento;

    private String bairro;

    private String numero;

    private String rua;

    private String uf;

    private String cep;

    public EnderecoGetnetDTO(Endereco endereco) {
        if (endereco.getIdCidade() != null) {
            this.cidade = endereco.getIdCidade().getDescricao();
        }
        this.complemento = endereco.getComplemento();
        this.bairro = endereco.getBairro();
        this.numero = endereco.getNumero();
        this.rua = endereco.getEndereco();
        if (endereco.getIdCidade() != null && endereco.getIdCidade().getIdUF() != null) {
            this.uf = endereco.getIdCidade().getIdUF().getDescricao();
        }
        this.cep = endereco.getCep();
        if (this.cep == null) {
            this.cep = "";
        }
        this.cep = this.cep.replaceAll("\\D", "");
    }

}
