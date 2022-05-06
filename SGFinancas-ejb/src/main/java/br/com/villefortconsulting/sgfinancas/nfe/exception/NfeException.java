package br.com.villefortconsulting.sgfinancas.nfe.exception;

public class NfeException extends Exception {

    private static final long serialVersionUID = -5054900660251852366L;

    public NfeException(Throwable e) {
        super(e);
    }

    public NfeException(String message) {
        super(message);
    }

    public NfeException(String message, Throwable e) {
        super(message, e);
    }

}
