package br.com.villefortconsulting.sgfinancas.util;

import br.com.villefortconsulting.exception.MessageListException;

@FunctionalInterface
public interface ImportadorConsumer<T, R> {

    R accept(T t) throws MessageListException;

}
