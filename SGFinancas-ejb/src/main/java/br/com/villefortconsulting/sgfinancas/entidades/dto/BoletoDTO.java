package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // cedente
    private String nomeCedente;

    private String cpfCNPJCedente;

    // sacado
    private Cidade cidadeSacado;

    private String nomeSacado;

    private String cpfCnpjSacado;

    private String bairroSacado;

    private String logadouroSacado;

    private Integer numSacado;

    private String cepSacado;

    // sacador avalista.
    private Cidade cidadeSacadorAvalista;

    private String nomeSacAval;

    private String cpfCnpjSacAval;

    private String bairroSacAval;

    private String logadouroSacAval;

    private Integer numSacAval;

    private String cepSacAval;

    // conta bancaria do titulo
    private ContaBancaria contaBancaria;

    // titulo
    private String numDocTitulo;

    private Double valorTitulo;

    private Date dataDocTitulo;

    private Date dataVencTitulo;

    private String tipoDocTitulo;

    private Double descTitulo;

    private Double deducaoTitulo;

    private Double moraTitulo;

    private Double acresTitulo;

    private Double valCobradoTitulo;

    // instrucoes
    private String instrucaoSacTitulo;

    private String instrucaoTitulo1;

    private String instrucaoTitulo2;

    private String instrucaoTitulo3;

    private String instrucaoTitulo4;

    private String instrucaoTitulo5;

    private String instrucaoTitulo6;

    private String instrucaoTitulo7;

    private String instrucaoTitulo8;

}
