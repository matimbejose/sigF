package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParametroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private transient Object value;

    private String type;

}
