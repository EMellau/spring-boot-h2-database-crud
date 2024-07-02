package com.mellau.spring.jpa.h2.api;

import com.mellau.spring.jpa.h2.common.enums.Sex;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class UserCreateRequestDTO {
    @NotNull
    private String email;
    @NotNull
    private Sex sex;
}
