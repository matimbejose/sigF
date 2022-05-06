package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class HolderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Nome completo do titular da conta é obrigatório.")
    @Size(max = 120, message = "Nome completo do titular da conta superior a 120 caracteres.")
    private String fullName;

    @NotNull(message = "Data de nascimento. é obrigatório.")
    @Size(max = 30, message = "Data de nascimento superior a 30 caracteres.")
    private String birthDate;

    @NotNull(message = "O Sexo é obrigatório.")
    @Size(max = 20, message = "Sexo superior a 20 caracteres.")
    private String gender;

    @NotNull(message = "O CPF do titular da conta é obrigatório.")
    @Size(max = 14, message = "CPF do titular da conta superior a 14 caracteres.")
    private String taxDocument;

    @NotNull(message = "O Número do telefone residencial ou comercial é obrigatório.")
    @Size(max = 15, message = "Número do telefone residencial ou comercial. superior a 15 caracteres.")
    private String phone;

    @NotNull(message = "O Número do telefone móvel é obrigatório.")
    @Size(max = 15, message = "Número do telefone móvel superior a 15 caracteres.")
    private String mobile;

    private CompanyDTO company;

    private ContactDTO contact;

    private String id;

    private String email;

    private String createdAt;

    private boolean blacklist;

    public static HolderDTO buildHolder(Empresa empresa) {
        ContactDTO contactDTO = ContactDTO.builder().build();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dataNascimento = empresa.getDataNascimentoRepresentante() != null ? df.format(empresa.getDataNascimentoRepresentante()) : null;

        return HolderDTO.builder()
                .fullName(empresa.getResponsavel())
                .birthDate(dataNascimento)
                .gender("M")
                .taxDocument(empresa.getCpfRepresentante())
                .phone(empresa.getTelefone())
                .mobile(empresa.getCelularRepresentante())
                .company(new CompanyDTO())
                .contact(contactDTO)
                .email(empresa.getEmail())
                .build();
    }

}
