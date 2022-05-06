package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraImportacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String xml;

    private List<CompraProdutoDTO> produtos;

}
