// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.melonity.Melonity;
import ru.melonity.f.b.RenderWorldEvent;
import ru.melonity.fabric.client.model.IGameRenderer;
import ru.melonity.h.b.TotemOverlayModule;
import ru.melonity.o.b.DisplayItemHideEvent;
import ru.melonity.w.MatricesHolder;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin implements IGameRenderer {
    @Shadow
    private float zoomScale;
    @Shadow
    private float zoomTranslationX;
    @Shadow
    private float zoomTranslationY;
    
    @Inject(method = "loadPrograms", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;loadBlurPostProcessor(Lnet/minecraft/resource/ResourceFactory;)V", shift = At.Shift.BEFORE))
    void loadPrograms(GameRenderer.ResourceFactory resourceFactory, CallbackInfo ci) {
        Melonity.EVENT_BUS.requestShaderPrograms(resourceFactory);
    }
    
    @Shadow
    private double getFovInternal(Camera camera, float tickDelta, boolean changingFov) {
        return 0.0;
    }

    @Shadow
    public float getFarPlaneDistance() {
        return 0.0f;
    }

    @Override
    public double getFovSafe(Camera camera, float tickDelta, boolean changingFov) {
        return this.getFovInternal(camera, tickDelta, changingFov);
    }

    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    public void showFloatingItem(ItemStack itemStack, CallbackInfo ci) {
        TotemOverlayModule module = Melonity.MODULE_MANAGER.getModule(TotemOverlayModule.class).orElse(null);
        if (module != null && module.isEnabled() && module.getHiddenItems().contains("Totem")) {
            ci.cancel();
        }
    }

    @Inject(method = "getBasicProjectionMatrix", at = @At("HEAD"), cancellable = true)
    public void getBasicProjectionMatrix(double fov, CallbackInfoReturnable<Matrix4f> cir) {
        Matrix4f matrix = new Matrix4f();
        if (this.zoomScale != 1.0f) {
            matrix.translate(this.zoomTranslationX, -this.zoomTranslationY, 0.0f);
            matrix.scale(this.zoomScale, this.zoomScale, 1.0f);
        }
        float aspectRatio = (float) MinecraftClient.getInstance().getWindow().getWidth() / (float) MinecraftClient.getInstance().getWindow().getHeight();
        cir.setReturnValue(matrix.perspective((float) Math.toRadians(fov), aspectRatio, 0.05f, this.getFarPlaneDistance()));
    }

    @Inject(method = "renderWorld", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = 180, ordinal = 0))
    public void renderWorld(RenderTickCounter tickCounter, CallbackInfo ci) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        MatrixStack matrixStack = new MatrixStack();
        RenderSystem.getModelViewStack().pushMatrix();
        RenderSystem.getModelViewStack().mul(new Matrix4f((Matrix4fc) matrixStack.peek().getPositionMatrix()));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));
        RenderSystem.applyModelViewMatrix();
        MatricesHolder.PROJECTION_MATRIX.set(new Matrix4f((Matrix4fc) RenderSystem.getProjectionMatrix()));
        MatricesHolder.MODEL_VIEW_MATRIX.set(new Matrix4f((Matrix4fc) RenderSystem.getModelViewMatrix()));
        MatricesHolder.WORLD_MATRIX.set(new Matrix4f((Matrix4fc) matrixStack.peek().getPositionMatrix()));
        Melonity.EVENT_BUS.emit(new RenderWorldEvent(matrixStack, matrixStack.peek().getPositionMatrix(), tickCounter.getCurrentDelta(true), camera, RenderSystem.getProjectionMatrix()));
        RenderSystem.getModelViewStack().popMatrix();
        RenderSystem.applyModelViewMatrix();
    }
}