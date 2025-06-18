// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public interface ILocalPlayer {
    public static int SYNC_CONSTANT = 945290849;

    public boolean isServerSprinting();
    public float getXRotLast();
    public boolean canStartSprinting();
}