package com.mellau.spring.jpa.h2.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "rest.exception")
@Getter
@Setter
@Validated
public class RestExceptionProperties {
    private boolean detailMessageEnabled;
}
