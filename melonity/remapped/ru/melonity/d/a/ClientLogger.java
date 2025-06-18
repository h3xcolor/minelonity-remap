// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.d.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.d.Logger;

@Environment(EnvType.CLIENT)
public class ClientLogger implements Logger {
    public static int LOGGER_ID = 1130876506;

    @Override
    public void log(String message) {
        System.out.println(message);
    }
}