package com.mellau.spring.jpa.h2.error;

public class PersistenceException extends DomainException {

    private static final long serialVersionUID = 6659556852235548599L;

    public PersistenceException(final Throwable cause, final Error error, final Object... args) {
        super(cause, error, args);
    }

    public PersistenceException(final Error error, final Object... args) {
        super(error, args);
    }
}
