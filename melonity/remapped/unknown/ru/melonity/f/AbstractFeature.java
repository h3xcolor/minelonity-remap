// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class AbstractFeature implements ru.melonity.f.FeatureToggle {
    private boolean enabled;
    public static int FEATURE_CONSTANT = 532587358;

    public void enable() {
        this.enabled = true;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }
}