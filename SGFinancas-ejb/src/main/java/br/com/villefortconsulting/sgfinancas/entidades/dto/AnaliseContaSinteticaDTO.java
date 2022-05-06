package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnaliseContaSinteticaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nomeCabecalho;

    private String nomeColuna;

    private String descricao;

    private Double valorAReceberPagar;

    private Double valorRecebidoPago;

    private Double valorEmAtraso;

    private Long quantidadeEmAtraso;

    private Long quantidadeRecebidoPago;

    private Long quantidadeAReceberPagar;

    public AnaliseContaSinteticaDTO(String descricao, Double valorAReceberPagar, Double valorEmAtraso, Double valorRecebidoPago,
            Long quantidadeAReceberPagar, Long quantidadeEmAtraso, Long quantidadeRecebidoPago,
            String nomeCabecalho, String nomeColuna) {
        this.descricao = descricao;
        this.valorAReceberPagar = valorAReceberPagar;
        this.valorEmAtraso = valorEmAtraso;
        this.valorRecebidoPago = valorRecebidoPago;
        this.quantidadeAReceberPagar = quantidadeAReceberPagar;
        this.quantidadeEmAtraso = quantidadeEmAtraso;
        this.quantidadeRecebidoPago = quantidadeRecebidoPago;
        this.nomeCabecalho = nomeCabecalho;
        this.nomeColuna = nomeColuna;
    }

}
