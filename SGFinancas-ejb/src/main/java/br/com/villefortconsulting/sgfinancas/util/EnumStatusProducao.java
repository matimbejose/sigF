package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumStatusProducao {

    NOVO("N", "Novo"),
    EM_PRODUCAO("E", "Em produção"),
    PRODUZIDO("P", "Produzido"),
    CANCELADO("C", "Cancelado");

    private final String tipo;

    private final String descricao;

    public static EnumStatusProducao retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
