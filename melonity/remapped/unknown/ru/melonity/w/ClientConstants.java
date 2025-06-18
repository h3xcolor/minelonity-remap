// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ClientConstants {
    public static int MAGIC_NUMBER = 331185492;

    private ClientConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}