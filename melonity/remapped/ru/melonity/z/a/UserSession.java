// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.z.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record UserSession(int userId, String username, String sessionId) {
    public static int CONSTANT_VALUE = 1751086511;
}