// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class Option<T> {
    private static final Option<?> EMPTY = new Option<>(null);
    private final T value;

    private Option(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public T orElseThrow(Function<String, RuntimeException> exceptionFunction, String messageTemplate, Object... args) {
        if (value == null) {
            String formattedMessage = messageTemplate.formatted(args);
            throw exceptionFunction.apply(formattedMessage);
        }
        return value;
    }

    public static <T> Option<T> of(T value) {
        if (value == null) {
            return (Option<T>) EMPTY;
        }
        return new Option<>(value);
    }
}