// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.h.c.ClientModule;

@Environment(value = EnvType.CLIENT)
public class AntiMissModule extends ClientModule {
    public static int ANTI_MISS_MODULE_ID = 950437401;

    public AntiMissModule() {
        super("AntiMiss", ClientModule.COMBAT_CATEGORY);
    }
}