package br.com.villefortconsulting.sgfinancas.servicos.exception;

public class EmailException extends Exception {

    private static final long serialVersionUID = 1L;

    public EmailException(Throwable e) {
        super(e);
    }

    public EmailException(String message, Throwable e) {
        super(message, e);
    }

}
