// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.e;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface FloatValue {
    public static int ID = 483007319;

    float get();
}