package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class ValorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter
    @Getter
    protected transient ArrayList<ValorLancamentoDTO> listaLancamento = new ArrayList<>();

    public boolean hasValue() {
        return listaLancamento.stream()
                .map(ValorLancamentoDTO::getValor)
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b) != 0;
    }

    public Double getValorAsDouble(int mes) {
        for (ValorLancamentoDTO dto : listaLancamento) {
            if (dto.getMes() == mes) {
                return dto.getValor();
            }
        }
        return 0d;
    }

    public String getValor(int mes) {
        Double valor = getValorAsDouble(mes);
        if (valor == 0) {
            return "-";
        }
        return NumeroUtil.converterValorParaMonetario(valor, 2);
    }

    public boolean setValor(int mes, Double valor) {
        for (ValorLancamentoDTO dto : listaLancamento) {
            if (dto.getMes() == mes) {
                dto.setValor(valor);
                return true;
            }
        }
        return false;
    }

    public String getValor1() {
        return getValor(1);
    }

    public String getValor2() {
        return getValor(2);
    }

    public String getValor3() {
        return getValor(3);
    }

    public String getValor4() {
        return getValor(4);
    }

    public String getValor5() {
        return getValor(5);
    }

    public String getValor6() {
        return getValor(6);
    }

    public String getValor7() {
        return getValor(7);
    }

    public String getValor8() {
        return getValor(8);
    }

    public String getValor9() {
        return getValor(9);
    }

    public String getValor10() {
        return getValor(10);
    }

    public String getValor11() {
        return getValor(11);
    }

    public String getValor12() {
        return getValor(12);
    }

    public String getValor13() {
        return getValor(13);
    }

}
