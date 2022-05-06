package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String descricao;

    private List<AnexoDTO> listaAnexo;

}
