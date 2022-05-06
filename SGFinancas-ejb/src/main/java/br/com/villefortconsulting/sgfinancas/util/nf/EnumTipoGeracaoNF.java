package br.com.villefortconsulting.sgfinancas.util.nf;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoGeracaoNF {

    AVULSO("A", "Avulso"),
    SISTEMA("S", "Sistema");

    private final String tipo;

    private final String descricao;

    public static EnumTipoGeracaoNF retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
