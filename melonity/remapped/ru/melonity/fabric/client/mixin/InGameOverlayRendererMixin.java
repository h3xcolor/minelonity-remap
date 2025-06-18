// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.h.b.FireOverlayModule;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void onRenderFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo callbackInfo) {
        if (Melonity.moduleManager == null) {
            return;
        }

        Optional<FireOverlayModule> moduleOptional = Melonity.moduleManager.getModule(FireOverlayModule.class);
        if (!moduleOptional.isPresent()) {
            return;
        }

        FireOverlayModule fireModule = moduleOptional.get();
        if (fireModule.isEnabled() && fireModule.getSetting("Fire").asBoolean()) {
            callbackInfo.cancel();
        }
    }
}