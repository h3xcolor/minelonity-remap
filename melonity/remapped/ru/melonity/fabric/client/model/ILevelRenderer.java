// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.gl.Framebuffer;

@Environment(EnvType.CLIENT)
public interface ILevelRenderer {
    void unusedConstant();
    
    Frustum getFrustum();
    Framebuffer entitiesFramebuffer();
}