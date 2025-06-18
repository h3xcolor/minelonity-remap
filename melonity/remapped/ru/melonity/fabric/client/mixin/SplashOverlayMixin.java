// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.ColorUtil;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;
import java.util.Optional;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(SplashOverlay.class)
public class SplashOverlayMixin {
    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    @Final
    private SplashOverlay.ResourceReload reload;
    @Shadow
    @Final
    private Consumer<Optional<Throwable>> exceptionHandler;
    @Shadow
    @Final
    private boolean reloadOnInit;
    @Shadow
    private float progress;
    @Shadow
    private long reloadCompleteTime;
    @Shadow
    private long fadeInStartTime;

    @Shadow
    private static int modifyAlpha(int rgb, int alpha) {
        return 0;
    }

    @Shadow
    private void drawProgressBar(DrawContext context, int width, int centerY, int rightEdge, int bottom, float opacity) {
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float fadeOutFactor;
        float fadeInFactor;
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        long currentTime = Util.getMillis();
        if (this.reloadOnInit && this.fadeInStartTime == -1L) {
            this.fadeInStartTime = currentTime;
        }
        float reloadCompleteDelta = this.reloadCompleteTime > -1L ? (float) (currentTime - this.reloadCompleteTime) / 1000.0f : -1.0f;
        float fadeInDelta = this.fadeInStartTime > -1L ? (float) (currentTime - this.fadeInStartTime) / 500.0f : -1.0f;
        if (reloadCompleteDelta >= 1.0f) {
            if (this.client.currentScreen != null) {
                this.client.currentScreen.render(context, 0, 0, delta);
            }
            int overlayAlpha = (int) ((1.0f - MathHelper.clamp(reloadCompleteDelta - 1.0f, 0.0f, 1.0f)) * 255.0f);
            context.fill(0, 0, screenWidth, screenHeight, modifyAlpha(new Color(458773).getRGB(), overlayAlpha));
            fadeOutFactor = 1.0f - MathHelper.clamp(reloadCompleteDelta - 1.0f, 0.0f, 1.0f);
        } else if (this.reloadOnInit) {
            if (this.client.currentScreen != null && fadeInDelta < 1.0f) {
                this.client.currentScreen.render(context, mouseX, mouseY, delta);
            }
            int backgroundColorAlpha = (int) (MathHelper.perlinFade(fadeInDelta, 0.15, 1.0) * 255.0);
            context.fill(0, 0, screenWidth, screenHeight, modifyAlpha(new Color(458773).getRGB(), backgroundColorAlpha));
            fadeOutFactor = MathHelper.clamp(fadeInDelta, 0.0f, 1.0f);
        } else {
            int backgroundColor = new Color(458773).getRGB();
            float r = (float) (backgroundColor >> 16 & 0xFF) / 255.0f;
            float g = (float) (backgroundColor >> 8 & 0xFF) / 255.0f;
            float b = (float) (backgroundColor & 0xFF) / 255.0f;
            GlStateManager._clearColor(r, g, b, 1.0f);
            GlStateManager._clear(16384, MinecraftClient.IS_SYSTEM_MAC);
            fadeOutFactor = 1.0f;
        }
        Window window = this.client.getWindow();
        double progressBarHeight = Math.min((double) screenWidth * 0.75, screenHeight) * 0.25;
        int progressBarY = (int) ((double) screenHeight * 0.8325);
        float reloadProgress = this.reload.getProgress();
        this.progress = MathHelper.clamp(this.progress * 0.95f + reloadProgress * 0.05f, 0.0f, 1.0f);
        GlStateManager._clearColor(
            Color.decode("#141414").getRed() / 255.0f,
            Color.decode("#141414").getGreen() / 255.0f,
            Color.decode("#141414").getBlue() / 255.0f,
            1.0f
        );
        GlStateManager._clear(16384, MinecraftClient.IS_SYSTEM_MAC);
        context.setShaderColor(1.0f, 1.0f, 1.0f, fadeOutFactor);
        this.renderTexture(
            new Identifier("melonity/images/loading.png"),
            window.getScaledWidth() / 2.0f - 37.0f,
            window.getScaledHeight() / 2.0f - 41.0f,
            74.0f,
            82.0f,
            new Color(1.0f, 1.0f, 1.0f, 0.1f),
            context.getMatrices()
        );
        this.renderTexture(
            new Identifier("melonity/images/loading.png"),
            window.getScaledWidth() / 2.0f - 37.0f,
            window.getScaledHeight() / 2.0f - 41.0f,
            74.0f,
            82.0f,
            new Color(1.0f, 1.0f, 1.0f, fadeOutFactor),
            context.getMatrices()
        );
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (reloadCompleteDelta < 1.0f) {
            int widthHalf = screenWidth / 2;
            int barHalfWidth = (int) (progressBarHeight * 2.0);
            this.drawProgressBar(
                context,
                widthHalf - barHalfWidth,
                progressBarY - 5,
                widthHalf + barHalfWidth,
                progressBarY + 5,
                1.0f - MathHelper.clamp(reloadCompleteDelta, 0.0f, 1.0f)
            );
        }
        if (reloadCompleteDelta >= 2.0f) {
            this.client.setOverlay(null);
        }
        if (this.reloadCompleteTime == -1L && this.reload.isPrepareStageComplete() && (!this.reloadOnInit || fadeInDelta >= 2.0f)) {
            try {
                this.reload.apply();
                this.exceptionHandler.accept(Optional.empty());
            } catch (Throwable throwable) {
                this.exceptionHandler.accept(Optional.of(throwable));
            }
            this.reloadCompleteTime = Util.getMillis();
            if (this.client.currentScreen != null) {
                this.client.currentScreen.method_25423(this.client, context.getScaledWindowWidth(), context.getScaledWindowHeight());
            }
        }
        ci.cancel();
    }

    @Unique
    private void renderTexture(Identifier texture, float x, float y, float width, float height, Color color, MatrixStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        TextureManager textureManager = this.client.getTextureManager();
        RenderSystem.setShaderTexture(0, textureManager.getTexture(texture).getGlId());
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.push();
        matrices.translate(0.0f, 0.0f, 0.0f);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x, y, 0.0f).color(color.getRGB()).texture(0.0f, 0.0f).next();
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x, y + height, 0.0f).color(color.getRGB()).texture(0.0f, 1.0f).next();
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x + width, y + height, 0.0f).color(color.getRGB()).texture(1.0f, 1.0f).next();
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x + width, y, 0.0f).color(color.getRGB()).texture(1.0f, 0.0f).next();
        BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.end();
        Tessellator.getInstance().upload(builtBuffer);
        matrices.pop();
    }
}