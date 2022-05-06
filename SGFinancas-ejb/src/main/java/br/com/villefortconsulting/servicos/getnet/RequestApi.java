/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.servicos.getnet;

import br.com.villefortconsulting.sgfinancas.entidades.dto.BoletoGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CartaoGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CompraGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CreditoGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.DebitoGetnetDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestApi {

    private double valor;

    private CompraGetnetDTO compra;

    private ClienteGetnetDTO cliente;

    private CreditoGetnetDTO credito;

    private DebitoGetnetDTO debito;

    private Integer idSistema;

    private BoletoGetnetDTO boleto;

    public CartaoGetnetDTO getCartao() {
        if (credito != null) {
            return credito.getCartao();
        } else if (debito != null) {
            return debito.getCartao();
        }
        return null;
    }

}
