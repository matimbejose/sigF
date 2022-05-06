package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTpEmis {

    EMISSAO_NORMAL("1", "Emissão normal"),
    CONTINGENCIA_EM_FORMULARIO_DE_SEGURANCA("2", "Contingência em Formulário de Segurança"),
    CONTINGENCIA_SCAN_DESATIVADO("3", "Contingência SCAN (desativado)"),
    CONTINGENCIA_EPEC("4", "Contingência EPEC"),
    CONTINGENCIA_EM_FORMULARIO_DE_SEGURANCA_FS_DA("5", "Contingência em Formulário de Segurança FS-DA"),
    CONTINGENCIA_SVC_AN("6", "Contingência SVC-AN"),
    CONTINGENCIA_SVC_RS("7", "Contingência SVC-RS");

    private final String tipo;

    private final String observacao;

    public static EnumTpEmis retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
