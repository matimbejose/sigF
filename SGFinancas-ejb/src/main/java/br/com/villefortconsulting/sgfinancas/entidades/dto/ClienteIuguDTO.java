package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteIuguDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String email;

    @JsonProperty("cpf_cnpj")
    private String cpfCnpj;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("update_at")
    private Date updatedAt;

    private String notes;

}
