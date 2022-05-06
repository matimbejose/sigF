package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumContentType {

    PDF("pdf", "application/pdf"),
    REM("REM", "multipart/form-data");

    private final String extensao;

    private final String contentType;

    public static EnumContentType retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.extensao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String retornaExtensao(String contentType) {
        for (EnumContentType en : EnumContentType.values()) {
            if (en.getContentType().contains(contentType)) {
                return en.getExtensao();
            }
        }

        return null;
    }

    public static String retornaContentType(String extensao) {
        for (EnumContentType en : EnumContentType.values()) {
            if (en.getExtensao().contains(extensao)) {
                return en.getContentType();
            }
        }

        return null;
    }

}
