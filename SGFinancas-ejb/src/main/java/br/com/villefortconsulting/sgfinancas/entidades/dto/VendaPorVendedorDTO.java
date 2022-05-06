package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaPorVendedorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //Lista de produtos vendidos pelo vendedor Selecionado
    private String produto;

    private Double valor;

    private Double quantidade;

    private String quantidadeStr;

    private String nomeVendedor;

    public VendaPorVendedorDTO(String nomeVendedor, String produto, String unidadeMedida, Double valor, Double quantidade) {
        this.produto = produto;
        this.valor = valor;
        this.quantidadeStr = NumeroUtil.converterDecimalParaString(quantidade, 2) + " " + unidadeMedida;
        this.quantidade = quantidade;
        this.nomeVendedor = nomeVendedor;
    }

    public VendaPorVendedorDTO(String nomeVendedor, String produto, Double valor, Double quantidade) {
        this.produto = produto;
        this.valor = valor;
        this.quantidadeStr = NumeroUtil.converterDecimalParaString(quantidade, 2);
        this.quantidade = quantidade;
        this.nomeVendedor = nomeVendedor;
    }

    public VendaPorVendedorDTO(String nomeVendedor, Double valor, Double quantidade) {
        this.valor = valor;
        this.quantidadeStr = NumeroUtil.converterDecimalParaString(quantidade, 2);
        this.quantidade = quantidade;
        this.nomeVendedor = nomeVendedor;
    }

}
