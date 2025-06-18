// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.f.ClientModInitializer;

@Environment(EnvType.CLIENT)
public record ClientMod() implements ClientModInitializer {
    public static int VERSION = 1591173042;
}