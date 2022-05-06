package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioMinDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nome;

    private String cargo;

    private List<ServicoDTO> funcionarioServicoList;

    private List<FuncionarioAtendimentoDTO> funcionarioAtendimentoList;

    private String thumbnail;

}
