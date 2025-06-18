// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.InputUtil;

@Environment(EnvType.CLIENT)
public interface IKeyBinding {
    int DEFAULT_BINDING_COLOR = 10612275;

    InputUtil.Key getBoundKey();
}