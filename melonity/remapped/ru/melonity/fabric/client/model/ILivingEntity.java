// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.h.b.a.BoundingBox;

@Environment(value = EnvType.CLIENT)
public interface ILivingEntity {
    int INTERFACE_VERSION_ID = 593582933;

    void resetJumpDelay();

    void setPitchHead(float pitch);

    float prevPitchHead();

    float pitchHead();

    BoundingBox getBoundingBox();

    void setBoundingBox(BoundingBox boundingBox);
}