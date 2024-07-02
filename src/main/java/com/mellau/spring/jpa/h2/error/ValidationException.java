package com.mellau.spring.jpa.h2.error;

public class ValidationException extends DomainException {

    private static final long serialVersionUID = 635224158999586445L;

    public ValidationException(final Error error, final Object... args) {
        super(error, args);
    }

}
