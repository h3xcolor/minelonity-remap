// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public interface ClientMessageHandler {
    public static int DEFAULT_ID = 834477931;

    public void handleMessage(String message);
}