// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface INativeImage {
    int RGBA_FORMAT = 1854439270;

    long getPointer();
}