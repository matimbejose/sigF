package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServicoAgendadoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private String config;

}
