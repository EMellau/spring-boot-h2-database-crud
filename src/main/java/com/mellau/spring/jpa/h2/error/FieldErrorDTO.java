package com.mellau.spring.jpa.h2.error;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FieldErrorDTO {
    private String objectName;
    private String field;
    private String message;
}
