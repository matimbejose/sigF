package br.com.villefortconsulting.sgfinancas.servicos.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ContratoException extends Exception {

    private static final long serialVersionUID = 1L;

    public ContratoException(Throwable e) {
        super(e);
    }

    public ContratoException(String message, Throwable e) {
        super(message, e);
    }

}
