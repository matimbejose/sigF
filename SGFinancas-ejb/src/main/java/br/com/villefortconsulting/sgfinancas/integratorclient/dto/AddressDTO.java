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
public class AddressDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o codigo do IBGE")
    @Size(max = 10, message = "O codigo do IBGE deverá ser menor que 10 caracteres")
    private String cityCode;

    @NotNull(message = "Informe o Nome do bairro")
    @Size(max = 49, message = "Nome do bairro superior a 50 caracteres.")
    private String district;

    @NotNull(message = "Informe o Nome da Rua")
    @Size(max = 149, message = "Nome da Rua superior a 150 caracteres.")
    private String line1;

    @NotNull(message = "Informe o Nome da Rua")
    @Size(max = 149, message = "Nome da Rua superior a 150 caracteres.")
    private String line2;

    @NotNull(message = "Informe o número do endereço")
    @Size(max = 50, message = "Número do endereço superior a 20 caracteres.")
    private String streetNumber;

    @NotNull(message = "Informe o CEP")
    @Size(max = 9, message = "Numero do CEP superior a 9 caracteres.")
    private String zipCode;

    private CityDTO city;

    public static AddressDTO buildAddress(Empresa empresa) {
        String enderecoComercial1 = empresa.getEndereco().getEndereco();

        String enderecoComercial2 = empresa.getEndereco().getComplemento().concat(", ")
                .concat(empresa.getEndereco().getBairro());

        return AddressDTO.builder()
                .cityCode(empresa.getEndereco().getIdCidade().getCodigoIBGE())
                .district(empresa.getEndereco().getIdCidade().getIdUF().getNomeCompleto())
                .line1(enderecoComercial1)
                .line2(enderecoComercial2)
                .streetNumber(empresa.getEndereco().getNumero())
                .zipCode(empresa.getEndereco().getCep().replace(".", ""))
                .build();
    }

}
