package br.com.villefortconsulting.sgfinancas.util.nf;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumCancelamentoNotaNFS {

    ERRO_NA_EMISSAO("1", "Erro na emissão da nota"),
    SERVICO_NAO_PRESTADO("2", "Serviço não prestado"),
    DUPLICIDADE_DA_NOTA("4", "Duplicidade da nota"),;

    private final String situacao;

    private final String descricao;

    public static String getDescricao(String opcao) {
        EnumCancelamentoNotaNFS e = retornaEnumSelecionado(opcao);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumCancelamentoNotaNFS retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
