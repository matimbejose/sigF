package br.com.villefortconsulting.sgfinancas.util.nf;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoEmissaoNF {

    EMISSAO_NORMAL("1", "Emissão normal (não em contingência)", false),
    CONTIGENCIA_FS_IA("2", "Contingência FS-IA, com impressão do DANFE em formulário de segurança", false),
    CONTIGENCIA_SCAN("3", "Contingência SCAN (Sistema de Contingência do Ambiente Nacional)", false),
    CONTIGENCIA_DPEC("4", "Contingência DPEC (Declaração Prévia da Emissão em Contingência)", false),
    CONTIGENCIA_FS_DA("5", "Contingência FS-DA, com impressão do DANFE em formulário de segurança", true),
    CONTIGENCIA_SVC_AN("6", "Contingência SVC-AN (SEFAZ Virtual de Contingência do AN)", false),
    CONTIGENCIA_SVC_RS("7", "Contingência SVC-RS (SEFAZ Virtual de Contingência do RS)", false),
    CONTIGENCIA_OFF_LINE_NFC_E("9", "Contingência off-line da NFC-e", true);// as demais opções de contingência são válidas também para a NFC-e

    private final String tipo;

    private final String descricao;

    private final boolean podeUsarNFCe;

    public static EnumTipoEmissaoNF retornaEnumSelecionado(String enumSelecionado) {
        for (EnumTipoEmissaoNF en : EnumTipoEmissaoNF.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;
    }

}
