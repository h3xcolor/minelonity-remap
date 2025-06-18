// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.h.b.ModuleType;

@Environment(EnvType.CLIENT)
@Mixin(Framebuffer.class)
public class FramebufferMixin {
    @Shadow
    protected int colorAttachment;

    @Inject(method = "drawInternal", at = @At("HEAD"), cancellable = true)
    private void overrideDrawing(int width, int height, boolean clear, CallbackInfo callbackInfo) {
        if (Melonity.moduleRegistry != null && (Framebuffer) (Object) this == MinecraftClient.getInstance().getFramebuffer() && 
            Melonity.moduleRegistry.getModule(ModuleType.BLACK_AND_WHITE).isEnabled() && 
            MinecraftClient.getInstance().world != null) {
            
            RenderSystem.assertOnRenderThread();
            GlStateManager._colorMask(true, true, true, false);
            GlStateManager._disableDepthTest();
            GlStateManager._depthMask(false);
            GlStateManager._viewport(0, 0, width, height);
            
            if (clear) {
                GlStateManager._disableBlend();
            }
            
            MinecraftClient client = MinecraftClient.getInstance();
            ShaderEffect shader = Melonity.moduleRegistry.getModule("black_and_white").get().getShader();
            
            shader.addSampler("DiffuseSampler", colorAttachment);
            shader.prepare();
            
            BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormat.POSITION);
            bufferBuilder.vertex(0.0, 0.0, 0.0).next();
            bufferBuilder.vertex(1.0, 0.0, 0.0).next();
            bufferBuilder.vertex(1.0, 1.0, 0.0).next();
            bufferBuilder.vertex(0.0, 1.0, 0.0).next();
            BufferRenderer.draw(bufferBuilder);
            
            shader.release();
            GlStateManager._depthMask(true);
            GlStateManager._colorMask(true, true, true, true);
            callbackInfo.cancel();
        }
    }
}