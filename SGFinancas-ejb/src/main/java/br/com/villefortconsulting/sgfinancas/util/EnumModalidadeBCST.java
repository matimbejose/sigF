package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumModalidadeBCST {

    M_0("0", "Preço tabelado ou máximo sugerido"),
    M_1("1", "Lista Negativa (valor)"),
    M_2("2", "Lista Positiva (valor)"),
    M_3("3", "Lista Neutra (valor)"),
    M_4("4", "Margem Valor Agregado (%)"),
    M_5("5", "Pauta (valor)"),
    M_6("6", "Valor da Operação");

    private final String codigo;

    private final String descricao;

    public static EnumModalidadeBCST retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.codigo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
