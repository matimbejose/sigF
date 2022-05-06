package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidacaoNFSeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean certificadoOK;

    private boolean inscricaoMunicipalOK;

    private boolean ctissOK;

    private boolean numeroUltimoNotaOK;

    public ValidacaoNFSeDTO() {
        certificadoOK = true;
        ctissOK = true;
        inscricaoMunicipalOK = true;
        numeroUltimoNotaOK = true;
    }

    public boolean isTodosOK() {
        return ctissOK && certificadoOK && inscricaoMunicipalOK && numeroUltimoNotaOK;
    }

}
