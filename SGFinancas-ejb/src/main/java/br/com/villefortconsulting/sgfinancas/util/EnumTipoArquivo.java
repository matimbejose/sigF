package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoArquivo {

    ARQUIVO_QUITACAO("QU", "Arquivo quitação"),
    ARQUIVO_IMPORTACAO("IM", "Arquivo de importação"),
    ARQUIVO_EXPORTACAO("EX", "Arquivo de exportação"),
    ARQUIVO_MOVIMENTACAO("MO", "Arquivo de movimento"),
    ARQUIVO_ENVIO("AE", ""),
    ANEXO_MENSAGEM("ME", "Anexo de mensagem");

    private final String tipo;

    private final String observacao;

    public static EnumTipoArquivo retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
