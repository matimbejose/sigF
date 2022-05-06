package br.com.villefortconsulting.sgfinancas.cnab;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CampoDelimitado {

    String posicao();

    String tipo();

    String digitos();

    boolean completar() default true;

}
