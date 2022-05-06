package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import br.com.villefortconsulting.entity.DtoId;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Importavel(nome = "categoria de produto/serviço ")
public class CategoriaDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    @Importavel(nome = "Nome", obrigatorio = true)
    private String descricao;

    private String ativo = "S";

    private List<SubCategoriaDTO> subCategriaList;

    public CategoriaDTO(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

}
