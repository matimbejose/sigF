package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MediaPrazoRelatorioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;

    private String tipoPessoa;

    private Double mediaDias;

    public MediaPrazoRelatorioDTO(String fornecedor, String cliente, Double mediaDias, Long count) {
        this.nome = cliente != null ? cliente : fornecedor;
        this.tipoPessoa = cliente != null ? "C" : "F";
        this.mediaDias = mediaDias / count;
    }

}
