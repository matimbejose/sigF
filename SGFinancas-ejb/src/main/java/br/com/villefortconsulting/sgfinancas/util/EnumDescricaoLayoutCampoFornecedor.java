package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumDescricaoLayoutCampoFornecedor {

    DESCRICAO("Descrição"),
    CPF_CNPJ("Cpf/Cnpj"),
    RAZAO_SOCIAL("Razão social"),
    EMAIL("Email"),
    TELEFONE_COMERCIAL("Telefone comercial"),
    TELEFONE_RESIDENCIAL("Telefone residencial"),
    TELEFONE_CELULAR("Telefone celular"),
    CEP("CEP"),
    ENDERECO("Endereco"),
    NUMERO("Número"),
    COMPLEMENTO("Complemento"),
    BAIRRO("Bairro"),
    CIDADE("Cidade");

    private final String descricao;

    public static EnumDescricaoLayoutCampoFornecedor retornaEnumSelecionado(String descricao) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.descricao.contains(descricao))
                .findAny()
                .orElse(null);
    }

}
