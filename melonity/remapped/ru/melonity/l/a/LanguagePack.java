// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.l.a;

import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.l.Language;

@Environment(value = EnvType.CLIENT)
public record LanguagePack(Language language, Map<String, String> messages) {
    public static int MAGIC_NUMBER = 474732601;

    @Override
    public final String toString() {
        return ObjectMethods.bootstrap("toString", new Object[]{LanguagePack.class, "language;messages", "language", "messages"}, this);
    }

    @Override
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap("hashCode", new Object[]{LanguagePack.class, "language;messages", "language", "messages"}, this);
    }

    @Override
    public final boolean equals(Object object) {
        return (boolean) ObjectMethods.bootstrap("equals", new Object[]{LanguagePack.class, "language;messages", "language", "messages"}, this, object);
    }
}