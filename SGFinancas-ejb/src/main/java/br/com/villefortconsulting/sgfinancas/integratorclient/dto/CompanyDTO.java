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
public class CompanyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Nome comercial da empresa (nome fantasia) é obrigatório")
    @Size(max = 120, message = "Nome comercial da empresa (nome fantasia) superior a 120 caracteres.")
    private String tradeName;

    @NotNull(message = "Nome de registro da empresa (razão social) é obrigatório")
    @Size(max = 120, message = "Nome de registro da empresa (razão social) superior a 120 caracteres.")
    private String fullName;

    @NotNull(message = "CNPJ da empresa é obrigatório")
    @Size(max = 18, message = "CNPJ da empresa superior a 18 caracteres.")
    private String taxDocument;

    private BusinessTypeDTO businessType;

    @NotNull(message = "Identificação do tipo da empresa (MEI, SA, LTDA, entre outros) é obrigatório.")
    private String businessTypeId;

    public static CompanyDTO buildCompany(Empresa empresa) {
        String acronimo = empresa.getTipoSocialEmpresa() != null
                ? empresa.getTipoSocialEmpresa().getCodigoAcronimo() : null;

        return CompanyDTO.builder()
                .tradeName(empresa.getNomeFantasia())
                .fullName(empresa.getDescricao())
                .taxDocument(empresa.getCnpj())
                .businessTypeId(acronimo)
                .build();
    }

}
