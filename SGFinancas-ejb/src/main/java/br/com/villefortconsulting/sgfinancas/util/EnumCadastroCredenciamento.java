package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumCadastroCredenciamento {

    SIM("S", "Sim"),
    NAO("N", "Não");

    public final String tipo;

    public final String descricao;

    public static EnumCadastroCredenciamento retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
