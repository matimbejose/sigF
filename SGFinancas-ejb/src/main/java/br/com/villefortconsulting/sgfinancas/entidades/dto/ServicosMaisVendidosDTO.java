package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicosMaisVendidosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //Serviços mais vendidos
    String descricaoServico;

    Double quantidade;

    Double valor;

    public ServicosMaisVendidosDTO(String descricaoServico, Long quantidade, Double valor) {
        this.descricaoServico = descricaoServico;
        this.quantidade = quantidade.doubleValue();
        this.valor = valor;
    }

}
