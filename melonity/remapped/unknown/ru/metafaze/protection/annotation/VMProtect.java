// ремапили ребята из https://t.me/dno_rumine
package ru.metafaze.protection.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@Environment(EnvType.CLIENT)
public @interface VMProtect {
    ProtectionType value();

    @Environment(EnvType.CLIENT)
    enum ProtectionType {
        VIRTUALIZATION,
        ULTRA
    }
}