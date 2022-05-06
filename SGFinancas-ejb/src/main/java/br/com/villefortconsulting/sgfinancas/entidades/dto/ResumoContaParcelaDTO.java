package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumoContaParcelaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String descricao;

    private String codigo;

    private String descricaoPai;

    private String codigoPai;

    private Double valor;

    private Double valorPago;

    private List<ResumoContaParcelaDTO> children;

    public ResumoContaParcelaDTO(String descricao, Double valor) {
        this.descricao = descricao;
        this.valor = valor;
        this.valorPago = 0d;
        this.children = new ArrayList<>();
    }

    public ResumoContaParcelaDTO(Double valor, String descricao) {
        this.descricao = descricao;
        this.valor = valor;
        this.valorPago = 0d;
        this.children = new ArrayList<>();
    }

    public ResumoContaParcelaDTO(String descricao, String codigo, Double valor) {
        this.descricao = descricao;
        this.codigo = codigo;
        this.valor = valor;
        this.valorPago = 0d;
        this.children = new ArrayList<>();
    }

    public ResumoContaParcelaDTO(String descricao, String codigo, String descricaoPai, String codigoPai, Double valor, Double valorPago) {
        this.descricao = descricao;
        this.codigo = codigo;
        this.descricaoPai = descricaoPai;
        this.codigoPai = codigoPai;
        this.valor = valor;
        this.valorPago = NumeroUtil.somar(valorPago);
        this.children = new ArrayList<>();
    }

    public ResumoContaParcelaDTO update() {
        this.valor = NumeroUtil.somar(this.valor);
        this.valorPago = NumeroUtil.somar(this.valorPago);
        if (this.codigoPai == null) {
            this.codigoPai = "";
            this.descricaoPai = "";
        }
        return this;
    }

    public void addValor(Double valor) {
        this.valor = NumeroUtil.somar(this.valor, valor);
    }

    public void addValor(Double valor, Double valorPago) {
        this.valor = NumeroUtil.somar(this.valor, valor);
        this.valorPago = NumeroUtil.somar(this.valorPago, valorPago);
    }

}
