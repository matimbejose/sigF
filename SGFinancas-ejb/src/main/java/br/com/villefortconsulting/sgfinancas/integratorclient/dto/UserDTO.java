package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o email")
    @Size(min = 5, max = 120, message = "O campo email deverá estar entre 5 e 120 caracteres.")
    private String email;

    @NotNull(message = "Informe a senha")
    @Size(min = 6, max = 20, message = "O campo senha deverá estar entre 6 e 20 caracteres.")
    private String password;

    public static UserDTO buildUser(Empresa empresa) {
        return UserDTO.builder()
                .email(empresa.getEmail())
                .password(empresa.getSenha())
                .build();
    }

}
