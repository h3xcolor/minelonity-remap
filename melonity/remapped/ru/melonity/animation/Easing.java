// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(value = EnvType.CLIENT)
public interface Easing {
    public static int DEFAULT_CONSTANT = 2143349088;

    double ease(double progress);
}