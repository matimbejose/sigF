package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumFileType {

    PDF("application/pdf", ".pdf"),
    XLS("application/vnd.ms-excel", ".xls"),
    CSV("text/csv", ".csv"),
    TXT("text/txt", ".txt");

    private final String contentType;

    private final String extensao;

    public static EnumFileType retornaEnumPorExtensao(String extensao) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.extensao.contains(extensao))
                .findAny()
                .orElse(null);
    }

    public static EnumFileType retornaEnumPorContentType(String contentType) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.contentType.contains(contentType))
                .findAny()
                .orElse(null);
    }

}
