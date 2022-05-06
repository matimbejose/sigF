package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaAcessoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String descricao;

    private String tipo;

    private String tenat;

    public EmpresaAcessoDTO(String descricao, String tenat) {
        this.descricao = descricao;
        this.tenat = tenat;
    }

    public EmpresaAcessoDTO(String tenat) {
        this.tenat = tenat;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return 97 * hash + Objects.hashCode(this.tenat);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EmpresaAcessoDTO other = (EmpresaAcessoDTO) obj;
        return Objects.equals(this.tenat, other.tenat);
    }

}
