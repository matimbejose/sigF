/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author caio.mota
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FaturaCallbackDto {

    private String idFatura;

    private ClienteIuguDTO idCliente;

    private Integer idEmpresa;

    private String cnpjCentroCusto;

    private Date dataVencimento;

    private Date dataPagamento;

    private Date dataCancelamento;

    private Date dataExtorno;

    private Date dataExpiracao;

    private Date dataReembolso;

    private Double totalFatura;

    private Double totalTaxaPaga;

    private Double totalPago;

    private Double totalJuros;

    private Double totalDesconto;

    private Date dataCriacao;

    private String formaPagamento;

    private Integer quantidadeParcelas;

    private List<ParcelaFaturaDto> parcelas;

    private Date dataLiquidacao;

    public Date getDataCancelamentoSG() {
        return Stream.of(dataCancelamento, dataExpiracao, dataExtorno, dataReembolso).filter(Objects::nonNull).findFirst().orElse(null);
    }

}
