package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoListagemConta {

    ATRASO("atraso"),
    HOJE("hoje"),
    PAGAR("pagar"),
    PAGO("pago"),
    RECEBER("receber"),
    RECEBIDO("recebido");

    private final String tipo;

    public static EnumTipoListagemConta retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
