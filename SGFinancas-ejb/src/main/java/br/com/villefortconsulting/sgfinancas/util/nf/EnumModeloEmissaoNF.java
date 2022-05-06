package br.com.villefortconsulting.sgfinancas.util.nf;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumModeloEmissaoNF {

    VENDA("VE", "Venda", EnumTipoEmissaoNF.EMISSAO_NORMAL, true, false, false, false, false),
    SERVICO("SE", "Prestação de serviço", EnumTipoEmissaoNF.EMISSAO_NORMAL, true, false, false, false, false),
    COMPLEMENTAR("CO", "Complementar", EnumTipoEmissaoNF.EMISSAO_NORMAL, false, false, true, false, true),
    DEVOLUCAO("DE", "Devolução", EnumTipoEmissaoNF.CONTIGENCIA_DPEC, false, true, false, false, false),
    ENTRADA_DEVOLUCAO("ED", "Entrada de devolução", EnumTipoEmissaoNF.EMISSAO_NORMAL, false, true, false, false, true),
    ENTRADA_DA_COMPRA("EC", "Entrada de compra", EnumTipoEmissaoNF.EMISSAO_NORMAL, false, false, false, false, false),
    TRANSFERENCIA("TR", "Transferência", EnumTipoEmissaoNF.EMISSAO_NORMAL, true, false, false, false, true),
    OUTRA("OU", "Outra (Manual)", EnumTipoEmissaoNF.EMISSAO_NORMAL, false, true, true, true, true);

    /**
     * Código da finalidade da nota (do sistema apenas)
     */
    private final String modelo;

    /**
     * Nome exibido para o usuário
     */
    private final String descricao;

    /**
     * Tipo de emissão da nota
     */
    private final EnumTipoEmissaoNF tipoEmissao;

    /**
     * Realiza o cálculo automático dos impostos
     */
    private final boolean calculaImpostos;

    /**
     * Exibe os campos destinados apenas a devolução
     */
    private final boolean exibeCamposDevolucao;

    /**
     * Exibe os campos que o próprio sistema infere para colocar na nota (como String)
     */
    private final boolean exibeCamposDerivados;

    /**
     * Exibe os campos que o sistema calcula para o usuário digitar (calculaImpostos deve ser false se este for true)
     */
    private final boolean exibeCamposCalculados;

    /**
     * Permite que o usuário selecione esta finalidade para uma nota avulsa
     */
    private final boolean permiteSelecao;

    public static EnumModeloEmissaoNF retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.modelo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
