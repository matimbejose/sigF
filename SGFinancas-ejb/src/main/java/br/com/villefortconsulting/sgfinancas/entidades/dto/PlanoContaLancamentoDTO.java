package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlanoContaLancamentoDTO extends DesempenhoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    Integer idPlano;

    private Double valor;

    public PlanoContaLancamentoDTO(Integer idPlano, Double previsao, Double realizado) {
        this.idPlano = idPlano;
        this.previsao = previsao;
        this.realizado = realizado;
    }

    public PlanoContaLancamentoDTO(Integer idPlano, Double valor) {
        this.idPlano = idPlano;
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return 89 * hash + Objects.hashCode(this.idPlano);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlanoContaLancamentoDTO other = (PlanoContaLancamentoDTO) obj;
        return Objects.equals(this.idPlano, other.idPlano);
    }

}
