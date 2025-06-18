// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.a;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Retention(RetentionPolicy.RUNTIME)
@Environment(EnvType.CLIENT)
public @interface ClientSideAnnotation {
    int DEFAULT_ID = 984267308;

    int value() default 0;
}