package com.mellau.spring.jpa.h2.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class SearchRequest {
    @PositiveOrZero(message = "The 'offset' field MUST be an integer greater than or equal to 0.")
    private Integer limit;
    @PositiveOrZero(message = "The 'limit' field MUST be an integer between 0 to 1000.")
    @Max(value = 1000, message = "The 'limit' field MUST be an integer between 0 to 1000.")
    private Integer offset;
    private String sort;
    //private SortDirection sortDirection; import org.hibernate.query.SortDirection;
}
