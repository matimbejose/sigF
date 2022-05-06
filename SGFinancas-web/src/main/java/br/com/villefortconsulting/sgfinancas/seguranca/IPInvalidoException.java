/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.seguranca;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author Christopher
 */
public class IPInvalidoException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public IPInvalidoException(String msg) {
        super(msg);
    }

}
