package com.mellau.spring.jpa.h2.api;

import com.mellau.spring.jpa.h2.common.model.SearchRequest;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserSearchRequestDTO extends SearchRequest {
    private String email;
}
