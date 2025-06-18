// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h;

import lombok.NonNull;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import org.jetbrains.annotations.Nullable;
import ru.melonity.animation.StateAnimation;
import ru.melonity.o.FeatureCategory;

@Environment(EnvType.CLIENT)
@ru.melonity.f.a.FeatureInfo
public interface FeatureModule {
    class_310 MINECRAFT = class_310.method_1551();
    int SETTING_ID = 629015785;

    @NonNull
    String getName();

    @NonNull
    FeatureCategory getCategory();

    @NonNull
    ru.melonity.o.b.a.ModuleSettings getSettings();

    @Nullable
    ru.melonity.o.b.a.b.ModuleIcon getIcon();

    boolean isEnabled();

    void setEnabled(boolean enabled);

    boolean shouldRender();

    StateAnimation getAnimationState();
}