// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ru.melonity.fabric.client.model.INativeImage;

@Environment(EnvType.CLIENT)
@Mixin(NativeImage.class)
public class NativeImageMixin implements INativeImage {
    @Shadow
    private long pointer;

    @Override
    public long pointer() {
        return this.pointer;
    }
}