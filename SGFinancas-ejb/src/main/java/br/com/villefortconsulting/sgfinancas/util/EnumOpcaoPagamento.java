package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumOpcaoPagamento {

    CONTRATO("C", "Contrato"),
    PAA("P", "PAA");

    private final String opcao;

    private final String descricao;

    public static String getDescricao(String opcao) {
        EnumOpcaoPagamento e = retornaEnumSelecionado(opcao);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumOpcaoPagamento retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.opcao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
