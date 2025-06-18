// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.f.Feature;

@Environment(EnvType.CLIENT)
public record DefaultFeature() implements Feature {
    public static int FEATURE_ID = 1752420813;
}