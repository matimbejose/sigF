package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastroMovimentacaoEstoqueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer produto;

    private Double quantidade;

    private TipoMovimentacao tipo;

    @Getter
    @AllArgsConstructor
    public enum TipoMovimentacao {
        ENTRADA("EN"), SAIDA("SA");

        private final String tipo;

    }

}
