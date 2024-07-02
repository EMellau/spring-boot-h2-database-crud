package com.mellau.spring.jpa.h2.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserUpdateRequestDTO {
    private Integer gold;
}
