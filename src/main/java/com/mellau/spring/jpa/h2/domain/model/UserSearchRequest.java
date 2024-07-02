package com.mellau.spring.jpa.h2.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class UserSearchRequest {
    private String email;
}
