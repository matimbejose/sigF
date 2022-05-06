package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoCadastro {

    FORNECEDOR("FO", "Fornecedor"),
    SERVICO("SE", "Serviço"),
    CONTA_BANCARIA("CB", "Conta bancária"),
    CLIENTE("CL", "Cliente"),
    PRODUTO("PR", "Produto"),
    TRANSPORTADORA("TR", "Transportadora"),
    FUNCIONARIO("FR", "Funcionário");

    private final String tipo;

    private final String descricao;

    public static EnumTipoCadastro retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
