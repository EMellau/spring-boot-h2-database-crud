package com.mellau.spring.jpa.h2.error;

public enum LogCommonError implements Error {

    ALREADY_CREATED("The object has already been submitted for creation"),
    UNABLE_TO_UPGRADE_FOUNDS("Unable to upgrade due to founds"),
    NOT_FOUND("The object with %s '%s' could not be found"),
    HAS_NO_ACCESS("The user with id %s has no access to resource %s with id '%s'"),
    DELETION_INVALID("Unable to delete the object");

    private final String defaultMessage;

    LogCommonError(final String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getMessage() {
        return defaultMessage;
    }
}
