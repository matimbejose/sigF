package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.util.NumeroUtil;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class DesempenhoDTO {

    protected PlanoConta idPlanoConta;

    protected Double previsao;

    protected Double realizado;

    /*
     * faixa fixa:
     * acima de 100: azul
     * 80 - 100: verde
     * 80 - 60: amarelo
     * 60 - 0: vermelho
     */
    public double getDesempenho() {
        Double desempenho = calcularDesempenho(previsao, realizado, 2);
        return desempenho == null ? 0d : desempenho;
    }

    public double getDesempenhoFix() {
        return Math.abs(realizado / previsao) * 100;
    }

    public String getCorDesempenho() {
        double desempenho = getDesempenho();
        if (desempenho < 60) {
            return "FF0000";
        } else if (desempenho < 80) {
            return "FFFF00";
        } else if (desempenho <= 100) {
            return "3CB371";
        }
        return "6495ED";
    }

    public String getCorDesempenhoIndicadorTela() {
        double desempenho = getDesempenho();
        if (desempenho < 60) {
            return "number vermelho";
        } else if (desempenho < 80) {
            return "number amarelo";
        } else if (desempenho <= 100) {
            return "number verde";
        }
        return "number azul";
    }

    public String getCorFonteDesempenho() {
        double desempenho = getDesempenho();
        if (desempenho < 60) {
            return "FFFAFA";
        } else if (desempenho < 80) {
            return "000000";
        } else if (desempenho <= 100) {
            return "FFFAFA";
        }
        return "FFFAFA";
    }

    protected Double calcularDesempenho(Double meta, Double realizado, int casasDecimais) {
        if (meta == null || realizado == null) {
            return null;
        }

        if (meta == 0 && realizado == 0) {
            return arredondar(100.0, casasDecimais);
        }

        boolean ambosMaioresQueZero = meta > 0 && realizado > 0;
        boolean apenasMetaMaiorQueZero = meta > 0 && realizado < 0;
        boolean apenasRealizadoMaiorQueZero = meta < 0 && realizado > 0;
        boolean ambosMenoresQueZero = meta < 0 && realizado < 0;
        boolean qualquerUmIgualAZero = meta == 0 || realizado == 0;
        boolean metaIgualAoRealizado = meta.compareTo(realizado) == 0;

        // Maior Melhor - M
        double desempMaiorMenor = 0;
        if (ambosMaioresQueZero || apenasMetaMaiorQueZero) { // Meta -> Positivo, Realizado -> Positivo, Meta -> Positivo, Realizado -> Negativo
            desempMaiorMenor = (realizado / meta) * 100;
        } else if (apenasRealizadoMaiorQueZero) { // Meta -> Negativo, Realizado -> Positivo
            desempMaiorMenor = (((realizado / meta) * -1) + 2) * 100;
        } else if (ambosMenoresQueZero) { // Meta -> Negativo, Realizado -> Negativo
            desempMaiorMenor = metaIgualAoRealizado ? ((realizado / meta) * 100) : ((2 - (realizado / meta)) * 100);
        } else if (qualquerUmIgualAZero) { // Meta -> Igual a zero, Realizado -> Igual a zero
            desempMaiorMenor = meta == 0 ? 100.0 : 0.0;
        }
        return arredondar(realizado - previsao, casasDecimais);
    }

    public Double arredondar(Double valor, int qtdCasas) {
        if (!(Double.isInfinite(valor) || Double.isNaN(valor))) {
            BigDecimal bd = BigDecimal.valueOf(valor);
            bd = bd.setScale(qtdCasas, BigDecimal.ROUND_HALF_UP);
            valor = bd.doubleValue();
        }
        return valor;
    }

    public DesempenhoDTO add(DesempenhoDTO dto) {
        if (dto.previsao != null) {
            this.previsao = NumeroUtil.somar(this.previsao, dto.previsao);
        }
        if (dto.realizado != null) {
            this.realizado = NumeroUtil.somar(this.realizado, dto.realizado);
        }
        return this;
    }

}
