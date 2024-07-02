package com.mellau.spring.jpa.h2.error;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@UtilityClass
public final class ErrorMapper {

    public static ErrorDTO toDTO(final HttpStatus httpStatus, final Throwable error) {
        return toDTO(httpStatus, httpStatus.name(), error);
    }

    public static ErrorDTO toDTO(final HttpStatus httpStatus, final String errorCode, final Throwable error) {
        return ErrorDTO.builder()
                .status(httpStatus.value())
                .reason(httpStatus.getReasonPhrase())
                .code(errorCode)
                .message(error.getMessage())
                .messageCode(toMessageCode(error))
                .occurredAt(Instant.now())
                .build();
    }

    public static ErrorDTO toDTO(final HttpStatus httpStatus, final Throwable error, final List<FieldError> fieldErrors) {
        return toDTO(httpStatus, httpStatus.name(), error, fieldErrors);
    }

    public static ErrorDTO toDTO(final HttpStatus httpStatus, final String errorCode, final Throwable error, final List<FieldError> fieldErrors) {
        return ErrorDTO.builder()
                .status(httpStatus.value())
                .reason(httpStatus.getReasonPhrase())
                .code(errorCode)
                .message("Validation failed " + error.getMessage())
                .messageCode(toMessageCode(error))
                .occurredAt(Instant.now())
                .fieldErrors(toFieldErrorDTOs(fieldErrors))
                .build();
    }

    private static FieldErrorDTO toFieldErrorDTO(final FieldError error) {
        return FieldErrorDTO.builder()
                .objectName(error.getObjectName())
                .field(error.getField())
                .message(error.getDefaultMessage())
                .build();
    }

    private static List<FieldErrorDTO> toFieldErrorDTOs(final List<FieldError> errors) {
        return errors.stream()
                .map(ErrorMapper::toFieldErrorDTO)
                .collect(toList());
    }

    private static String toMessageCode(final Throwable error) {
        if (error instanceof DomainException) {
            return Optional.ofNullable(((DomainException) error).getMessageCode())
                    .orElse(null);
        }
        return null;
    }
}
