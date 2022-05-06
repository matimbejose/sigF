package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CepDTO extends br.com.villefortconsulting.entity.CepDTO {

    private static final long serialVersionUID = 1L;

    private Cidade cidade;

    private UF uf;

    public CepDTO(br.com.villefortconsulting.entity.CepDTO cep) {
        this.endereco = cep.getEndereco();
        this.bairro = cep.getBairro();
        this.descricaoCidade = cep.getDescricaoCidade();
        this.descricaoUf = cep.getDescricaoUf();
        this.latLong = cep.getLatLong();
    }

}
