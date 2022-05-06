package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoLayoutCampo {

    CABECALHO("CB", "Cabeçalho"),
    DETALHE("DT", "Detalhe"),
    RODAPE("RD", "Rodapé");

    private final String tipo;

    private final String descricao;

    public static EnumTipoLayoutCampo retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
