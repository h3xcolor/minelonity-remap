// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.h.b.CustomNameTagsModule;
import ru.melonity.h.b.PlayerESPModule;

@Environment(value = EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V", at = @At(value = "HEAD"), cancellable = true)
    protected void renderLabelIfPresent(AbstractClientPlayerEntity player, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo callbackInfo) {
        if (!Melonity.isNameTagRenderingEnabled || Melonity.moduleManager != null && (Melonity.moduleManager.getModule(CustomNameTagsModule.class).isEnabled() || Melonity.moduleManager.getModule(PlayerESPModule.class).isEnabled())) {
            callbackInfo.cancel();
        }
    }
}