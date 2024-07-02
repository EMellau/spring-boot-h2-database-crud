package com.mellau.spring.jpa.h2.error;

import java.io.Serializable;

public interface Error extends Serializable {

    String getCode();

    String getMessage();
}
