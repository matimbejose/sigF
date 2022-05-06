package br.com.villefortconsulting.sgfinancas.servicos.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class UsuarioException extends Exception {

    private static final long serialVersionUID = 1L;

    public UsuarioException(Throwable e) {
        super(e);
    }

    public UsuarioException(String message, Throwable e) {
        super(message, e);
    }

}
