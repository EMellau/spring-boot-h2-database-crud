package com.mellau.spring.jpa.h2.domain.model;

import com.mellau.spring.jpa.h2.common.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private Integer level;
    private Integer gold;
    private Sex sex;
}
