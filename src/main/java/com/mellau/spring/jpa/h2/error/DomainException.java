package com.mellau.spring.jpa.h2.error;

import lombok.Getter;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

@Getter
abstract class DomainException extends RuntimeException {

    private static final long serialVersionUID = 985569696547855483L;

    private final Error error;
    private final String messageCode;

    private DomainException(final Throwable cause, final Error error, final String messageCode, final Object... args) {
        super(isNotEmpty(args) ? String.format(error.getMessage(), args) : error.getMessage(), cause);
        this.error = error;
        this.messageCode = messageCode;
    }

    DomainException(final Throwable cause, final Error error, final Object... args) {
        this(cause, error, null, args);
    }

    private DomainException(final Error error, final String messageCode, final Object... args) {
        super(isNotEmpty(args) ? String.format(error.getMessage(), args) : error.getMessage());
        this.error = error;
        this.messageCode = messageCode;
    }

    DomainException(final Error error, final Object... args) {
        this(error, null, args);
    }
}
