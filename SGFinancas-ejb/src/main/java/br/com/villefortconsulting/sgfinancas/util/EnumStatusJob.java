package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumStatusJob {

    PROCESSADO_OK("COMPLETED", "Processado OK"),
    INICIANDO("STARTING", "Iniciando processamento"),
    EXECUTANDO("STARTED", "Em processamento"),
    PAUSANDO("STOPPING", "Parando processamento"),
    PARADO("STOPPED", "Processamento parado"),
    ABANDONADO("ABANDONED", "Processamento abandonado"),
    PROCESSADO_ERRO("FAILED", "Processado com erro");

    public final String situacao;

    public final String descricao;

    public static EnumStatusJob retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getDescricaoPorSituacao(String situacao) {
        EnumStatusJob enumStatusJob = retornaEnumSelecionado(situacao);
        if (enumStatusJob != null) {
            return enumStatusJob.getDescricao();
        }
        return null;
    }

}
