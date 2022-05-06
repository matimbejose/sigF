package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumDescricaoLayoutCampoFuncionario {

    NOME("Nome"),
    MATRICULA("Matrícula"),
    CPF("CPF"),
    DATA_NASCIMENTO("Data de nascimento"),
    DATA_CONTRATACAO("Data de contratação"),
    DATA_DEMISSAO("Data de demissão"),
    SALARIO("Salário"),
    EMAIL("Email"),
    TELEFONE_RESIDENCIAL("Telefone residencial"),
    TELEFONE_CELULAR("Telefone celular"),
    CEP("CEP"),
    ENDERECO("Endereco"),
    NUMERO("Número"),
    COMPLEMENTO("Complemento"),
    BAIRRO("Bairro"),
    CIDADE("Cidade"),
    NOME_MAE("Nome da mãe");

    private final String descricao;

    public static EnumDescricaoLayoutCampoFuncionario retornaEnumSelecionado(String descricao) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.descricao.contains(descricao))
                .findAny()
                .orElse(null);
    }

}
