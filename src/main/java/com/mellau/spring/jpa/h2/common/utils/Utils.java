package com.mellau.spring.jpa.h2.common.utils;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class Utils {

    public static <T> void addToListIfNotNull(final List<T> list, final T object) {
        if (nonNull(object) && nonNull(list)) list.add(object);
    }

    public static <T> int getSizeOrZeroIfEmpty(final List<T> objects) {
        return isNotEmpty(objects) ? objects.size() : 0;
    }
}
