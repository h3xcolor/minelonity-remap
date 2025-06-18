// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.h.c.Module;

@Environment(EnvType.CLIENT)
public class AutoDodgeModule extends Module {
    public static int VERSION = 1129175166;

    public AutoDodgeModule() {
        super("AutoDodge", Module.Category.COMBAT);
    }
}