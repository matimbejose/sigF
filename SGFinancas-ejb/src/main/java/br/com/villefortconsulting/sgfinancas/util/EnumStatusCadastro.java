package br.com.villefortconsulting.sgfinancas.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumStatusCadastro {

    OK("Ok", "icon-check font-green-jungle"),
    NAO_OBRIGATORIO_VAZIO("Cadastro opcional não efetuado", "icon-info font-blue-madison"),
    OBRIGATORIO_VAZIO("Cadastro obrigatório não efetuado", "icon-close font-red-mint");

    private final String descricao;

    private final String icone;

}
