package com.mellau.spring.jpa.h2.api;

import com.mellau.spring.jpa.h2.error.*;
import com.mellau.spring.jpa.h2.properties.RestExceptionProperties;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
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
    public ResponseEntity<ErrorDTO> handlePersistenceError(final PersistenceException error) {
        log.error(DEFAULT_ERROR_MESSAGE, INTERNAL_SERVER_ERROR.getReasonPhrase(), error);
        return buildResponse(INTERNAL_SERVER_ERROR, error.getError().getCode(), error);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorDTO> handleNotFoundError(final NotFoundException error) {
        log.warn(ERROR_MESSAGE_WITH_MESSAGE, NOT_FOUND.getReasonPhrase(), error.getMessage());
        log.debug(DEFAULT_ERROR_MESSAGE, NOT_FOUND.getReasonPhrase(), error);
        return buildResponse(NOT_FOUND, error.getError().getCode(), error);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleValidationError(final ValidationException error) {
        log.warn(ERROR_MESSAGE_WITH_MESSAGE, BAD_REQUEST.getReasonPhrase(), error.getMessage());
        log.debug(DEFAULT_ERROR_MESSAGE, BAD_REQUEST.getReasonPhrase(), error);
        return buildResponse(BAD_REQUEST, error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleValidationError(final ConstraintViolationException error) {
        log.warn(ERROR_MESSAGE_WITH_MESSAGE, BAD_REQUEST.getReasonPhrase(), error.getMessage());
        log.debug(DEFAULT_ERROR_MESSAGE, BAD_REQUEST.getReasonPhrase(), error);
        return buildResponse(BAD_REQUEST, error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleRequestError(final IllegalArgumentException error) {
        log.warn(ERROR_MESSAGE_WITH_MESSAGE, BAD_REQUEST.getReasonPhrase(), error.getMessage());
        log.debug(DEFAULT_ERROR_MESSAGE, BAD_REQUEST.getReasonPhrase(), error);
        return buildResponse(BAD_REQUEST, error);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDTO> handleInternalServerError(final Exception error) {
        log.error(DEFAULT_ERROR_MESSAGE, INTERNAL_SERVER_ERROR.getReasonPhrase(), error);
        return buildResponse(INTERNAL_SERVER_ERROR, error);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(PAYLOAD_TOO_LARGE)
    public ResponseEntity<ErrorDTO> handleMaxSizeException(final MaxUploadSizeExceededException error) {
        log.error(DEFAULT_ERROR_MESSAGE, PAYLOAD_TOO_LARGE.getReasonPhrase(), error);
        return buildResponse(PAYLOAD_TOO_LARGE, error);
    }
/*
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException error, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.warn(ERROR_MESSAGE_WITH_MESSAGE, status.getReasonPhrase(), ((Throwable) error).getMessage());
        log.debug(DEFAULT_ERROR_MESSAGE, status.getReasonPhrase(), error);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorMapper.toDTO(BAD_REQUEST, error, error.getBindingResult().getFieldErrors()));
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException error, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.warn(ERROR_MESSAGE_WITH_MESSAGE, status.getReasonPhrase(), ((Throwable) error).getMessage());
        log.debug(DEFAULT_ERROR_MESSAGE, status.getReasonPhrase(), error);
        final var errorDTO = ErrorMapper.toDTO(BAD_REQUEST, error, error.getBindingResult().getFieldErrors());
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(buildProfileErrorDTO(errorDTO));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException error, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final var errorCause = error.getCause();
        if (errorCause instanceof JsonMappingException) {
            final var exception = (JsonMappingException) errorCause;
            final List<JsonMappingException.Reference> path = exception.getPath();
            if (isNotEmpty(path)) {
                final List<FieldError> fieldErrors = exception.getPath()
                        .stream()
                        .map(e -> new FieldError(e.getDescription(), e.getFieldName(), exception.getOriginalMessage()))
                        .collect(toList());
                return ResponseEntity
                        .status(BAD_REQUEST)
                        .body(buildProfileErrorDTO(ErrorMapper.toDTO(BAD_REQUEST, error, fieldErrors)));
            }
        }
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(buildProfileErrorDTO(ErrorMapper.toDTO(BAD_REQUEST, error)));
    }*/

    protected ResponseEntity<ErrorDTO> buildResponse(final HttpStatus status, final Throwable error) {
        final var errorDTO = ErrorMapper.toDTO(status, error);
        return ResponseEntity
                .status(status)
                .body(buildProfileErrorDTO(errorDTO));
    }

    protected ResponseEntity<ErrorDTO> buildResponse(final HttpStatus status, final String errorCode, final Throwable error) {
        final var errorDTO = ErrorMapper.toDTO(status, errorCode, error);
        return ResponseEntity
                .status(status)
                .body(buildProfileErrorDTO(errorDTO));
    }

    private ErrorDTO buildProfileErrorDTO(final ErrorDTO dto) {
        if (restExceptionProperties.isDetailMessageEnabled()) {
            return dto;
        }

        return ErrorDTO.builder()
                .code(dto.getCode())
                .status(dto.getStatus())
                .reason(dto.getReason())
                .build();
    }
/*
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            final Exception ex, @Nullable final Object body, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.warn("Handling Exception Internal, cause: {}, message: {}", ex.getCause(), ex.getMessage());
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }*/
}
