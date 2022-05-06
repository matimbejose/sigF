package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class ParametroVistoriaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private FormularioDTO formularioPadrao;

}
