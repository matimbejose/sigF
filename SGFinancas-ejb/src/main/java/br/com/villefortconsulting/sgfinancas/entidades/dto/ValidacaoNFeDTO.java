package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidacaoNFeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean certificadoOK;

    private boolean cnpjOK;

    private boolean inscricaoEstadualOK;

    private boolean enderecoOK;

    private boolean numeroNFeOK;

    private boolean serieNFeOK;

    private boolean indicadorInscricaoEstadualOK;

    private boolean inscricaoMunicipalOK;

    private boolean porteEmpresaOK;

    private boolean regimeTributarioOK;

    public ValidacaoNFeDTO() {
        certificadoOK = true;
        cnpjOK = true;
        inscricaoEstadualOK = true;
        enderecoOK = true;
        numeroNFeOK = true;
        serieNFeOK = true;
        indicadorInscricaoEstadualOK = true;
        inscricaoMunicipalOK = true;
        porteEmpresaOK = true;
        regimeTributarioOK = true;
    }

    public boolean isTodosOK() {
        return cnpjOK && certificadoOK && inscricaoEstadualOK && enderecoOK && numeroNFeOK && serieNFeOK && indicadorInscricaoEstadualOK && inscricaoMunicipalOK && porteEmpresaOK && regimeTributarioOK;
    }

}
