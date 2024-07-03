package com.mellau.spring.jpa.h2.api;

import com.mellau.spring.jpa.h2.error.*;
import com.mellau.spring.jpa.h2.properties.RestExceptionProperties;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE = "{} error";
    private static final String ERROR_MESSAGE_WITH_MESSAGE = "{} error: {}";
    private final RestExceptionProperties restExceptionProperties;

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDTO> handlePersistenceError(PersistenceException error) {
        log.error(DEFAULT_ERROR_MESSAGE, INTERNAL_SERVER_ERROR.getReasonPhrase(), error);
        return buildResponse(INTERNAL_SERVER_ERROR, error.getError().getCode(), error);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorDTO> handleNotFoundError(NotFoundException error) {
        log.warn(ERROR_MESSAGE_WITH_MESSAGE, NOT_FOUND.getReasonPhrase(), error.getMessage());
        log.debug(DEFAULT_ERROR_MESSAGE, NOT_FOUND.getReasonPhrase(), error);
        return buildResponse(NOT_FOUND, error.getError().getCode(), error);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleValidationError(ValidationException error) {
        log.warn(ERROR_MESSAGE_WITH_MESSAGE, BAD_REQUEST.getReasonPhrase(), error.getMessage());
        log.debug(DEFAULT_ERROR_MESSAGE, BAD_REQUEST.getReasonPhrase(), error);
        return buildResponse(BAD_REQUEST, error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleValidationError(ConstraintViolationException error) {
        log.warn(ERROR_MESSAGE_WITH_MESSAGE, BAD_REQUEST.getReasonPhrase(), error.getMessage());
        log.debug(DEFAULT_ERROR_MESSAGE, BAD_REQUEST.getReasonPhrase(), error);
        return buildResponse(BAD_REQUEST, error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleRequestError(IllegalArgumentException error) {
        log.warn(ERROR_MESSAGE_WITH_MESSAGE, BAD_REQUEST.getReasonPhrase(), error.getMessage());
        log.debug(DEFAULT_ERROR_MESSAGE, BAD_REQUEST.getReasonPhrase(), error);
        return buildResponse(BAD_REQUEST, error);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDTO> handleInternalServerError(Exception error) {
        log.error(DEFAULT_ERROR_MESSAGE, INTERNAL_SERVER_ERROR.getReasonPhrase(), error);
        return buildResponse(INTERNAL_SERVER_ERROR, error);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(PAYLOAD_TOO_LARGE)
    public ResponseEntity<ErrorDTO> handleMaxSizeException(MaxUploadSizeExceededException error) {
        log.error(DEFAULT_ERROR_MESSAGE, PAYLOAD_TOO_LARGE.getReasonPhrase(), error);
        return buildResponse(PAYLOAD_TOO_LARGE, error);
    }

    protected ResponseEntity<ErrorDTO> buildResponse(HttpStatus status, Throwable error) {
        var errorDTO = ErrorMapper.toDTO(status, error);
        return ResponseEntity
                .status(status)
                .body(buildProfileErrorDTO(errorDTO));
    }

    protected ResponseEntity<ErrorDTO> buildResponse(HttpStatus status, String errorCode, Throwable error) {
        var errorDTO = ErrorMapper.toDTO(status, errorCode, error);
        return ResponseEntity
                .status(status)
                .body(buildProfileErrorDTO(errorDTO));
    }

    private ErrorDTO buildProfileErrorDTO(ErrorDTO dto) {
        if (restExceptionProperties.isDetailMessageEnabled()) {
            return dto;
        }

        return ErrorDTO.builder()
                .code(dto.getCode())
                .status(dto.getStatus())
                .reason(dto.getReason())
                .build();
    }
}
