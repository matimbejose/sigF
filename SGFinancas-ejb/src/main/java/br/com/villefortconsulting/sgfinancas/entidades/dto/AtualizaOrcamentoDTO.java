package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.util.EnumTipoAtualizacao;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class AtualizaOrcamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NonNull
    @ApiModelProperty(dataType = "string", allowableValues = "APROVAR, REJEITAR, GERAR_OS")
    private EnumTipoAtualizacao tipoAtualizacao;

    private VendaFormaPagamentoCadastroRestDTO formaPagamento;

}
