/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.servicos.getnet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseApi {

    private String idPagamento;

    private String status;

    private String idAutorizacao;

    private String dataPagamento;

    private String mensagem;

    private String md;

    private String linkEmissor;

    private String certEmissor;

    private BoletoApi boleto;

    @Getter
    @Setter
    public class BoletoApi {

        private String idBoleto;

        private String codigoBanco;

        private String codigoBarra;

        private String linhaDigitavel;

        private Long dataVenciamento;

        private String situacao;

        private String urlBoleto;

    }

}
