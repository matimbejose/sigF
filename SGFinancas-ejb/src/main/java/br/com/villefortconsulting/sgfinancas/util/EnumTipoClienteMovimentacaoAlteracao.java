package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoClienteMovimentacaoAlteracao {

    ALTERACAO("A", "Alteração"),
    EXCLUSAO("E", "Exclusão");

    private final String tipo;

    private final String descricao;

    public static EnumTipoClienteMovimentacaoAlteracao retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.stream(values())
                .filter(item -> item.tipo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
