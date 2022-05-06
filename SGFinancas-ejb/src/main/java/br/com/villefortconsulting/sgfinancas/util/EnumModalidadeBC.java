package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumModalidadeBC {

    M_0("0", "Margem Valor Agregado (%)"),
    M_1("1", "Pauta (Valor)"),
    M_2("2", "Preço Tabelado Máx. (valor)"),
    M_3("3", "Valor da operação");

    private final String codigo;

    private final String descricao;

    public static EnumModalidadeBC retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.codigo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
