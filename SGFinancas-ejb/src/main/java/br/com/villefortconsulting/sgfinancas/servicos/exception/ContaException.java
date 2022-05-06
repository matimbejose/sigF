package br.com.villefortconsulting.sgfinancas.servicos.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ContaException extends Exception {

    private static final long serialVersionUID = 1L;

    public ContaException(Throwable e) {
        super(e);
    }

    public ContaException(String message, Throwable e) {
        super(message, e);
    }

}
