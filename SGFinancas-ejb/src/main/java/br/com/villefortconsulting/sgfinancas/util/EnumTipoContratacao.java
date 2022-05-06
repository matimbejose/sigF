package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoContratacao {

    FUNCIONARIO("F", "Funcionário"),
    ESTAGIARIO("E", "Estagiário"),
    CONTRATADO("C", "Contratado"),
    DEMITIDO("D", "Demitido"),
    MENOR_APRENDIZ("M", "Menor aprendiz"),
    GESTAO_PESSOA("G", "Gestão de pessoa");

    private final String tipo;

    private final String descricao;

    public static String getDescricao(String tipo) {
        EnumTipoContratacao e = retornaEnumSelecionado(tipo);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumTipoContratacao retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getTipo(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.descricao.equals(enumSelecionado))
                .map(item -> item.tipo)
                .findAny()
                .orElse("");
    }

}
