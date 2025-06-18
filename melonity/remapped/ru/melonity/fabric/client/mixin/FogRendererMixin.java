// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.h.b.FogColorSetting;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class FogRendererMixin {
    @Shadow
    private static float fogRed;
    @Shadow
    private static float fogGreen;
    @Shadow
    private static float fogBlue;

    @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/BackgroundRenderer;fogBlue:F", ordinal = 4))
    private static void render(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo callbackInfo) {
        FogColorSetting fogColorSetting = Melonity.CLIENT_SETTINGS.getFogColorSetting().orElse(null);
        if (fogColorSetting != null && fogColorSetting.isEnabled()) {
            fogRed = (float) fogColorSetting.getColor().getRed() / 255.0f;
            fogGreen = (float) fogColorSetting.getColor().getGreen() / 255.0f;
            fogBlue = (float) fogColorSetting.getColor().getBlue() / 255.0f;
        }
    }
}