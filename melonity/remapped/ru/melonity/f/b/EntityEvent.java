// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;

@Environment(value = EnvType.CLIENT)
public class EntityEvent extends ru.melonity.f.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll {
    private final Entity entity;
    public static int eventId = 583502208;

    @Generated
    public EntityEvent(Entity entity) {
        this.entity = entity;
    }

    @Generated
    public Entity getEntity() {
        return this.entity;
    }
}