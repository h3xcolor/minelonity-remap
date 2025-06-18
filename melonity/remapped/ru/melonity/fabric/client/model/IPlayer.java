// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector4d;

@Environment(EnvType.CLIENT)
public interface IPlayer {
    int ESP_MODEL_VERSION = 1202902155;

    Vector4d espPosition();

    void setEspPosition(Vector4d position);
}