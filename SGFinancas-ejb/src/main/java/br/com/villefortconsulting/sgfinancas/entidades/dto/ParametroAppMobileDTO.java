package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class ParametroAppMobileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private ContaBancariaDTO contaBancariaPadrao;

}
