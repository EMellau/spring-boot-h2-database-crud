package com.mellau.spring.jpa.h2.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ErrorDTO {
    private int status;
    private String reason;
    private String code;
    private String message;
    private String messageCode;
    private Instant occurredAt;
    private List<FieldErrorDTO> fieldErrors;
}
