// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.Session;

@Environment(EnvType.CLIENT)
public interface IMinecraft {
    int USE_ITEM_FLAG = 756570406;

    void setUser(Session session);

    void tryUseItem();
}