package br.com.villefortconsulting.sgfinancas.entidades.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestRetornoMicroServico {

    private static final long serialVersionUID = 1L;

    private String idPagamento;

    private String status;

    private String idAutorizacao;

    private String dataPagamento;

    private String mensagem;

    private String md;

    private String linkEmissor;

    private String certEmissor;

    private RequestBoletoMicroServico boleto;

}
