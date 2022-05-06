package br.com.villefortconsulting.sgfinancas.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumConheceuNosPor {

    RADIO("Rádio"),
    TV("Televisão"),
    FACEBOOK("Facebook"),
    INSTAGRAM("Instagram"),
    TWITTER("Twitter");

    private final String descricao;

}
