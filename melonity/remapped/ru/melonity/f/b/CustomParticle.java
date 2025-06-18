// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.f.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll;

@Environment(value = EnvType.CLIENT)
public class CustomParticle extends IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll {
    private final double x;
    private final double y;
    private final double z;
    private boolean onGround;
    private float size;
    private float alpha;
    public static int id = 671895046;

    public CustomParticle(float size, float alpha, double x, double y, double z, boolean onGround) {
        this.size = size;
        this.alpha = alpha;
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }

    @Generated
    public double getX() {
        return this.x;
    }

    @Generated
    public double getY() {
        return this.y;
    }

    @Generated
    public double getZ() {
        return this.z;
    }

    @Generated
    public boolean isOnGround() {
        return this.onGround;
    }

    @Generated
    public float getSize() {
        return this.size;
    }

    @Generated
    public float getAlpha() {
        return this.alpha;
    }

    @Generated
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    @Generated
    public void setSize(float size) {
        this.size = size;
    }

    @Generated
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}