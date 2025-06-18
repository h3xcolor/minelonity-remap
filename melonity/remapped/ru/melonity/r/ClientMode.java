// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.r;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public enum ClientMode {
    PRIMARY,
    SECONDARY;

    public static int currentSetting = 1514153363;

    private static final ClientMode[] $VALUES = new ClientMode[]{PRIMARY, SECONDARY};

    public static ClientMode[] values() {
        return $VALUES.clone();
    }

    public static ClientMode valueOf(String name) {
        return Enum.valueOf(ClientMode.class, name);
    }
}