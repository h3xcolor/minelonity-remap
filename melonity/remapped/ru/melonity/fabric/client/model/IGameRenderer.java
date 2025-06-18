// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;

@Environment(value = EnvType.CLIENT)
public interface IGameRenderer {
    int DEFAULT_RENDER_KEY = 1390562054;

    double getFovSafe(Camera camera, float tickDelta, boolean useFovSetting);
}