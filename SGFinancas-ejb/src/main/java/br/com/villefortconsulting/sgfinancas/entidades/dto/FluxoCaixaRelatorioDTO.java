package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class FluxoCaixaRelatorioDTO extends ValorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String LUCRATIVIDADE = "L";

    @NonNull
    private String descricao;

    @NonNull
    private String tipo;

    @NonNull
    private String tipoRelatorio;

    @Override
    public String getValor(int mes) {
        Double lancamentoDespesa = listaLancamento.stream()
                .filter(p -> p.getMes().equals(mes))
                .findFirst().orElse(new ValorLancamentoDTO()).getValor();

        if (lancamentoDespesa != null && lancamentoDespesa != 0) {
            return NumeroUtil.converterValorParaMonetario(lancamentoDespesa, 2) + (LUCRATIVIDADE.equals(tipo) ? "%" : "");
        }

        return "-";
    }

    public void addValorLancamento(ValorLancamentoDTO lancamentoDTO) {
        listaLancamento.add(lancamentoDTO);
    }

}
