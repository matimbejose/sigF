package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.concurrent.Callable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class WizardTelaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NonNull
    private String step;

    @NonNull
    private String nome;

    @NonNull
    private Boolean obrigatorio;

    private transient Callable<Boolean> saveFnc;

}
