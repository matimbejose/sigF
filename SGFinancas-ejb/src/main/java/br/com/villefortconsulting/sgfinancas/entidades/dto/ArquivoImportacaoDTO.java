package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoImportacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private byte[] content;

}
