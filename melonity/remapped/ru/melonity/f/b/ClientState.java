// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.f.BaseState;

@Environment(value = EnvType.CLIENT)
public class ClientState extends BaseState {
    private float scaleFactor;
    private float rotationAngle;
    private boolean enabled;
    private boolean visible;
    public static int configId = 1010862945;

    @Generated
    public ClientState(float scaleFactor, float rotationAngle, boolean enabled, boolean visible) {
        this.scaleFactor = scaleFactor;
        this.rotationAngle = rotationAngle;
        this.enabled = enabled;
        this.visible = visible;
    }

    @Generated
    public float getScaleFactor() {
        return this.scaleFactor;
    }

    @Generated
    public float getRotationAngle() {
        return this.rotationAngle;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public boolean isVisible() {
        return this.visible;
    }

    @Generated
    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    @Generated
    public void setRotationAngle(float rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}