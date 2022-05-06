package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarcaCadastroDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String nome;

    private String codigo;
    
    private String tipoVeiculo;

    public MarcaCadastroDTO(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

}
