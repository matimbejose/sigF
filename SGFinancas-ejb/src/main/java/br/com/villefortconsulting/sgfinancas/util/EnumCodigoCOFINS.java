package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumCodigoCOFINS {

    COD_01("01", "Operação Tributável com Alíquota Básica"),
    COD_02("02", "Operação Tributável com Alíquota Diferenciada"),
    COD_03("03", "Operação Tributável com Alíquota por Unidade de Medida de Produto"),
    COD_04("04", "Operação Tributável Monofásica - Revenda a Alíquota Zero"),
    COD_05("05", "Operação Tributável por Substituição Tributária"),
    COD_06("06", "Operação Tributável a Alíquota Zero"),
    COD_07("07", "Operação Isenta da Contribuição"),
    COD_08("08", "Operação sem Incidência da Contribuição"),
    COD_09("09", "Operação com Suspensão da Contribuição"),
    COD_49("49", "Outras Operações de Saída"),
    COD_50("50", "Operação com Direito a Crédito - Vinculada Exclusivamente a Receita Tributada no Mercado Interno"),
    COD_51("51", "Operação com Direito a Crédito - Vinculada Exclusivamente a Receita Não-Tributada no Mercado Interno"),
    COD_52("52", "Operação com Direito a Crédito - Vinculada Exclusivamente a Receita de Exportação"),
    COD_53("53", "Operação com Direito a Crédito - Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno"),
    COD_54("54", "Operação com Direito a Crédito - Vinculada a Receitas Tributadas no Mercado Interno e de Exportação"),
    COD_55("55", "Operação com Direito a Crédito - Vinculada a Receitas Não Tributadas no Mercado Interno e de Exportação"),
    COD_56("56", "Operação com Direito a Crédito - Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno e de Exportação"),
    COD_60("60", "Crédito Presumido - Operação de Aquisição Vinculada Exclusivamente a Receita Tributada no Mercado Interno"),
    COD_61("61", "Crédito Presumido - Operação de Aquisição Vinculada Exclusivamente a Receita Não-Tributada no Mercado Interno"),
    COD_62("62", "Crédito Presumido - Operação de Aquisição Vinculada Exclusivamente a Receita de Exportação"),
    COD_63("63", "Crédito Presumido - Operação de Aquisição Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno"),
    COD_64("64", "Crédito Presumido - Operação de Aquisição Vinculada a Receitas Tributadas no Mercado Interno e de Exportação"),
    COD_65("65", "Crédito Presumido - Operação de Aquisição Vinculada a Receitas Não-Tributadas no Mercado Interno e de Exportação"),
    COD_66("66", "Crédito Presumido - Operação de Aquisição Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno e de Exportação"),
    COD_67("67", "Crédito Presumido - Outras Operações"),
    COD_70("70", "Operação de Aquisição sem Direito a Crédito"),
    COD_71("71", "Operação de Aquisição com Isenção"),
    COD_72("72", "Operação de Aquisição com Suspensão"),
    COD_73("73", "Operação de Aquisição a Alíquota Zero"),
    COD_74("74", "Operação de Aquisição sem Incidência da Contribuição"),
    COD_75("75", "Operação de Aquisição por Substituição Tributária"),
    COD_98("98", "Outras Operações de Entrada"),
    COD_99("99", "Outras Operações");

    private final String codigo;

    private final String descricao;

    public static EnumCodigoCOFINS retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.codigo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
