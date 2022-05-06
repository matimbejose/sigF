package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumDescricaoLayoutCampoSolicitante {

    STATUS("Status"),
    ORIGEM("Origem"),
    NOME("Nome"),
    EMAIL("Email"),
    TELEFONE("Telefone"),
    EMPRESA("Empresa"),
    OBSERVACAO("Observação"),
    CPF("CPF"),
    AREA_ATUACAO("Área de atuação"),
    CARGO("Cargo"),
    INDICACAO("Indicação"),
    CIDADE("Cidade"),
    TURMA("Turma");

    private final String descricao;

    public static EnumDescricaoLayoutCampoSolicitante retornaEnumSelecionado(String descricao) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.descricao.contains(descricao))
                .findAny()
                .orElse(null);
    }

}
