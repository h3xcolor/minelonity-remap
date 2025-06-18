// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum ClientSetting {
    ENABLED,
    DISABLED;

    public static int publicId = 1081689875;

    public ClientSetting getOpposite() {
        if (this == ENABLED) {
            return DISABLED;
        }
        return ENABLED;
    }
}