// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.a.a.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record ClientToken(String accessToken, String refreshToken) {
    public static int constantId = 371086020;
}