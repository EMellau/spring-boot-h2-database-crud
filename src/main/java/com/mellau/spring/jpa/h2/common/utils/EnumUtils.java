package com.mellau.spring.jpa.h2.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Lazy;

import java.security.SecureRandom;
import java.util.List;
import java.util.SplittableRandom;

@Slf4j
public class EnumUtils {
    public static <T extends Enum<?>> T randomEnum(final Class<T> clazz) {
        final SecureRandom random = new SecureRandom();
        final int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public static <T extends Enum<?>> T getRandomEnumFromList(final List<T> list) {
        final SplittableRandom random = Lazy.of(SplittableRandom::new).get();
        return list.get(random.nextInt(list.size()));
    }
}
