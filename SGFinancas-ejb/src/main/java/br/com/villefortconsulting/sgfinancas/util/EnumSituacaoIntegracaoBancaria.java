package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoIntegracaoBancaria {

    NOVO("N", "Novo"),
    AGUARDANDO_PROCESSAMENTO("AP", "Aguardando processamento"),
    PROCESSAMENTO_EM_ANDAMENTO("P", "Processando"),
    PROCESSAMENTO_COM_ERRO("PE", "Processamento com erro"),
    PROCESSAMENTO_OK("PO", "Processamento OK");

    public final String situacao;

    public final String descricao;

    public static EnumSituacaoIntegracaoBancaria retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getDescricaoPorSituacao(String situacao) {
        EnumSituacaoIntegracaoBancaria enumSituacaoArquivoImportacao = retornaEnumSelecionado(situacao);
        if (enumSituacaoArquivoImportacao != null) {
            return enumSituacaoArquivoImportacao.getDescricao();
        }
        return null;
    }

}
