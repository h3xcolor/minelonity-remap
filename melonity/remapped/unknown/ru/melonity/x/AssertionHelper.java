// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.x;

import java.util.function.Function;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public final class AssertionHelper {
    public static void ensureFalse(boolean condition, Function<String, RuntimeException> exceptionFactory, String message, Object... args) {
        if (condition) {
            String formattedMessage = message.formatted(args);
            throw exceptionFactory.apply(formattedMessage);
        }
    }

    public static <T> void ensureFalse(Predicate<T> predicate, T object, Function<String, RuntimeException> exceptionFactory, String message, Object... args) {
        ensureFalse(predicate.test(object), exceptionFactory, message, args);
    }

    public static void ensureTrue(boolean condition, Function<String, RuntimeException> exceptionFactory, String message, Object... args) {
        ensureFalse(!condition, exceptionFactory, message, args);
    }

    public static <T> void ensureTrue(Predicate<T> predicate, T object, Function<String, RuntimeException> exceptionFactory, String message, Object... args) {
        ensureTrue(predicate.test(object), exceptionFactory, message, args);
    }
}