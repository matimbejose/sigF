package br.com.villefortconsulting.sgfinancas.servicos.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CadastroException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CadastroException(Throwable e) {
        super(e);
    }

    public CadastroException(String message, Throwable e) {
        super(message, e);
    }

}
