// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.melonity.Melonity;
import ru.melonity.client.module.ModuleManager;
import ru.melonity.client.module.modules.visual.GammaBrightnessModule;
import ru.melonity.client.settings.Setting;

@Environment(EnvType.CLIENT)
@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Inject(method = "getDarknessFactor", at = @At("HEAD"), cancellable = true)
    private void modifyDarknessFactor(float delta, CallbackInfoReturnable<Float> cir) {
        ModuleManager moduleManager = Melonity.moduleManager;
        if (moduleManager != null &&
                moduleManager.getModule(GammaBrightnessModule.class).isPresent() &&
                moduleManager.getModule(GammaBrightnessModule.class).get().isEnabled()) {

            GammaBrightnessModule gammaModule = moduleManager.getModule(GammaBrightnessModule.class).get();
            if (gammaModule.getSetting("Gamma") != null) {
                cir.setReturnValue(0.0f);
            }
        }
    }

    @Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
    private static void modifyBrightness(LightType lightType, int lightLevel, CallbackInfoReturnable<Float> cir) {
        ModuleManager moduleManager = Melonity.moduleManager;
        if (moduleManager != null &&
                moduleManager.getModule(GammaBrightnessModule.class).isPresent() &&
                moduleManager.getModule(GammaBrightnessModule.class).get().isEnabled()) {

            GammaBrightnessModule gammaModule = moduleManager.getModule(GammaBrightnessModule.class).get();
            if (gammaModule.getSetting("Gamma") != null) {
                float adjustedLight = (float) lightLevel / 15.0f;
                float calculatedBrightness = adjustedLight / (4.0f - 3.0f * adjustedLight);
                float finalValue = Math.max(MathHelper.lerp(lightType.equals(LightType.SKY) ? 1.0f : calculatedBrightness, 1.0f), 0.8f);
                cir.setReturnValue(finalValue);
            }
        }
    }
}