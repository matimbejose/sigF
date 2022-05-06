package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuloDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String nome;

    private String descricao;

    private Double valorAdesao;

    private Double valorMensalidade;

    private String ativo;

    private String permiteRenovacao;

    private String obrigatorio;

    private String flag;

    private List<ModuloPermissaoDTO> listaPermissoes;

}
