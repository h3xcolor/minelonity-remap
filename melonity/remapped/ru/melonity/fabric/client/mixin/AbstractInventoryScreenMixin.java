// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(AbstractInventoryScreen.class)
public class AbstractInventoryScreenMixin {
    @Inject(method = "drawStatusEffects", at = @At("HEAD"), cancellable = true)
    private void disableStatusEffectRendering(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}