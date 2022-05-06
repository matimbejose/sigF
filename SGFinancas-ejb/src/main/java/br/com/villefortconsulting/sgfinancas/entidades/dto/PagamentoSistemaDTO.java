package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class PagamentoSistemaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String[] PLANOS = new String[]{"Starter", "Avançado", "Premium", "Ilimitado"};

    private static final Double[][] PRECOS = new Double[][]{
        new Double[]{89.9d, 159.9d, 179.9d, 249.9d},//mensal
        new Double[]{69.9d, 127.9d, 149.9d, 199.9d}//anual
    };

    private String tipoPagamento;

    private Recorrencia recorrencia;

    private String plano;

    private Integer indexPlano;

    private Double preco;

    private CartaoDTO cartao;

    private FaturaDTO fatura;

    private String hash;

    public PagamentoSistemaDTO() {
        this.recorrencia = Recorrencia.MENSAL;
        this.plano = "";
        this.indexPlano = -1;
        cartao = new CartaoDTO();
        fatura = new FaturaDTO();
        fatura.setEndereco(new EnderecoGetnetDTO());
    }

    public PagamentoSistemaDTO(String recorrencia, String indexPlano) {
        this.recorrencia = Recorrencia.valueOf(recorrencia.toUpperCase());
        try {
            this.indexPlano = Integer.parseInt(indexPlano);
            this.plano = PLANOS[this.indexPlano];
            this.preco = PRECOS[this.recorrencia.getIndex()][Integer.parseInt(indexPlano)];
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            this.indexPlano = null;
            this.plano = null;
            this.preco = null;
        }
        cartao = new CartaoDTO();
        fatura = new FaturaDTO();
        fatura.setEndereco(new EnderecoGetnetDTO());
    }

    public String getTipoPagamentoFitPag() {
        if (tipoPagamento == null) {
            return null;
        }
        switch (tipoPagamento) {
            case "CC":
                return "creditCard";
            default:
                Logger.getLogger(getClass().getName()).log(Level.WARNING, () -> "M[etodo de pagamento " + tipoPagamento + " não configurado para a FitPag.");
                return null;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum Recorrencia {
        MENSAL(0),
        ANUAL(1);

        private final int index;

    }

}
