package com.mellau.spring.jpa.h2.error;

public class ConflictException extends DomainException {

    private static final long serialVersionUID = -6582451236522689548L;

    public ConflictException(final Error error, final String fieldName, final Object fieldValue) {
        this(error, (Object) fieldName, fieldValue);
    }

    private ConflictException(final Error error, final Object... args) {
        super((Error) error, args);
    }
}
