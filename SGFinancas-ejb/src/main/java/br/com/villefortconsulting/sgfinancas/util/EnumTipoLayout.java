package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoLayout {

    CLIENTE("CL", "Cliente"),
    FORNECEDOR("FO", "Fornecedor"),
    SERVICO("SV", "Serviço"),
    PRODUTO("PR", "Produto"),
    FUNCIONARIO("FR", "Funcionario"),
    SOLICITANTE("SL", "Solicitante"),
    EXPORTACAO("EX", "Exportação");

    public final String tipo;

    public final String descricao;

    public static EnumTipoLayout retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getDescricaoPorTipo(String tipo) {
        EnumTipoLayout enumTipoArquivoImportacao = retornaEnumSelecionado(tipo);
        if (enumTipoArquivoImportacao != null) {
            return enumTipoArquivoImportacao.getDescricao();
        }
        return null;
    }

}
