// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public final class RenderBlendHelper {
    public static int PUBLIC_CONSTANT = 424510750;

    public static void withBlend(Runnable runnable) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        runnable.run();
        RenderSystem.disableBlend();
    }

    @Generated
    private RenderBlendHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}