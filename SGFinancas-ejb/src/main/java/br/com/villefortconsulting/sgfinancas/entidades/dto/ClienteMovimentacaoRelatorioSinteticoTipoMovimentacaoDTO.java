package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteMovimentacaoRelatorioSinteticoTipoMovimentacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String origem;

    private String tipoMovimentacao;

    private Double valorTotal;

}
