package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImpostosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double ir;

    private Double pis;

    private Double csll;

    private Double inss;

    private Double cofins;

    private Double iss;

}
