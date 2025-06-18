// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IClientWorld {
    int DEFAULT_TIME_OF_DAY = 929663287;

    void setTimeOfDay(long timeOfDay);
}