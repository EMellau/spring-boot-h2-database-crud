package com.mellau.spring.jpa.h2.error;

public class NotFoundException extends DomainException {

    private static final long serialVersionUID = -995869585633254856L;

    public NotFoundException(final Error error, final Object... args) {
        super((Error) error, args);
    }
}
