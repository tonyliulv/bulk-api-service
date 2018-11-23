package com.umg.technology.bulkapiservice.exception;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * The class {@code EntityNotFoundException} can be use to throw an
 * exception indicate that an instance of the class cannot be founded.
 */

public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(final Class clazz, final String... searchParamsMap) {
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), toMap(String.class, String.class, searchParamsMap)));
    }

    private static String generateMessage(final String entity, final Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) + " was not found for paremeters " + searchParams;
    }

    private static <K, V> Map<K, V> toMap(final Class<K> keyType, final Class<V> valueType, final Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");

        return IntStream.range(0, entries.length / 2)
                .map(i -> i*2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast((entries[i + 1]))),
                        Map::putAll);
    }
}
