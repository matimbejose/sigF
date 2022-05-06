package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import java.io.Serializable;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 300, message = "Endereço do contato superior a 300 caracteres.")
    private String address;

    @Size(max = 15, message = "Número do telefone móvel superior a 15 caracteres.")
    private String mobile;

    @Size(max = 14, message = "Número do telefone móvel superior a 14 caracteres.")
    private String phone;

    @Size(max = 4, message = "Número do ramal superior a 4 caracteres.")
    private String phoneExtension;

    @Size(max = 120, message = "Número do ramal superior a 120 caracteres.")
    private String email;

    @Size(max = 120, message = "Site superior a 120 caracteres.")
    private String site;

}
