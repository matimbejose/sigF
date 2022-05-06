package br.com.villefortconsulting.sgfinancas.servicos.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BoletoException extends Exception {

    private static final long serialVersionUID = 1L;

    public BoletoException(Throwable e) {
        super(e);
    }

    public BoletoException(String message, Throwable e) {
        super(message, e);
    }

}
