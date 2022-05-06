package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoSegmentoConta;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe se a conta é juridica ou física.")
    private String isJuristic;

    @NotNull(message = "Informe o código do banco")
    @Size(max = 20, message = "código do banco superior a 9 caracteres.")
    private String bankNumber;

    @NotNull(message = "Informe o Número da conta.")
    @Size(max = 30, message = "Número da conta bancária superior a 30 caracteres.")
    private String accountNumber;

    @NotNull(message = "Informe o número da agência.")
    @Size(max = 20, message = "Numero da agência bancária superior a 20 caracteres.")
    private String bankBranchNumber;

    private String variation;

    @NotNull(message = "Informe o tipo da conta bancária.")
    @Size(max = 8, message = "Tipo da conta bancária superior a 8 caracteres.")
    private String type;

    private String holderFullName;

    private String holderTaxDocument;

    public static BankAccountDTO buildBankAccount(Empresa empresa) {

        EnumTipoSegmentoConta tipoConta = EnumTipoSegmentoConta.retornaEnumSelecionado(empresa.getIdContaBancariaDigital().getTipoConta());
        return BankAccountDTO.builder()
                .isJuristic(Boolean.TRUE.toString())
                .bankNumber(empresa.getIdContaBancariaDigital().getIdBanco().getNumero())
                .accountNumber(isEmpty(empresa.getIdContaBancariaDigital().getDigitoConta())
                        ? empresa.getIdContaBancariaDigital().getConta() : empresa.getIdContaBancariaDigital().getContaCompleta())
                .bankBranchNumber(StringUtils.isEmpty(empresa.getIdContaBancariaDigital().getDigitoAgencia())
                        ? empresa.getIdContaBancariaDigital().getAgencia() : empresa.getIdContaBancariaDigital().getAgenciaCompleta())
                .variation(null)
                .type(tipoConta.getReferenciaPagCerto())
                .build();
    }

}
