/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
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
public class ParcelaFaturaDto {

    @JsonProperty("installment")
    private String numeroParcela;

    @JsonProperty("return_date")
    private String dataVencimento;

    private String status;

    @JsonProperty("amount")
    private String valorParcela;

    @JsonProperty("taxes")
    private String valorTaxa;

    private String idFatura;

    private Date dataLiquidacao;

}
