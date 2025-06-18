// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum RenderMode {
    FANCY,
    PERFORMANCE;

    public static int DEFAULT_VALUE = 410697432;

    private static final RenderMode[] $VALUES = new RenderMode[]{FANCY, PERFORMANCE};

    public static RenderMode[] values() {
        return $VALUES.clone();
    }

    public static RenderMode valueOf(String name) {
        return Enum.valueOf(RenderMode.class, name);
    }
}