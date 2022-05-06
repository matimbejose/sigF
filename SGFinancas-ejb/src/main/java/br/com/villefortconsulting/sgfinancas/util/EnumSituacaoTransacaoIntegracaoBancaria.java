package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoTransacaoIntegracaoBancaria {

    NOVO("N", "Não processada", "asterisk text-primary"),
    PARCELA_ENCONTRADA("PE", "Parcela(s) encontrada(s)", "check text-success"),
    MAIS_PARCELA_ENCONTRADA("MP", "Mais de uma parcela encontrada", "check text-success"),
    PARCELA_NAO_ENCONTRADA("PN", "Parcela(s) não encontrada(s)", "times text-danger");

    public final String situacao;

    public final String descricao;

    public final String icone;

    public static EnumSituacaoTransacaoIntegracaoBancaria retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.stream(values())
                .filter(item -> item.situacao.equals(enumSelecionado))
                .findAny().orElse(null);
    }

    public static String getDescricaoPorSituacao(String situacao) {
        EnumSituacaoTransacaoIntegracaoBancaria enumSituacaoArquivoImportacao = retornaEnumSelecionado(situacao);
        if (enumSituacaoArquivoImportacao != null) {
            return enumSituacaoArquivoImportacao.getDescricao();
        }
        return null;
    }

}
