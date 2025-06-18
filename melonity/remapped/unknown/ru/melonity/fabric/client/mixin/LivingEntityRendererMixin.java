// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.fabric.client.model.ILivingEntity;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin extends EntityRenderer<LivingEntity> {
    @Shadow
    protected EntityModel<LivingEntity> model;

    protected LivingEntityRendererMixin() {
        super(null);
    }

    @Shadow
    protected float getAnimationProgress(LivingEntity entity, float tickDelta) {
        return 0.0f;
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;setAngles(Lnet/minecraft/entity/Entity;FFFFF)V", shift = At.Shift.AFTER))
    public void render(LivingEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        float prevBodyYaw = MathHelper.lerpAngle(tickDelta, entity.prevBodyYaw, entity.bodyYaw);
        float prevHeadYaw = MathHelper.lerpAngle(tickDelta, entity.prevHeadYaw, entity.headYaw);
        float headYawDelta = prevHeadYaw - prevBodyYaw;

        if (entity.hasVehicle() && entity.getVehicle() instanceof LivingEntity) {
            LivingEntity vehicle = (LivingEntity) entity.getVehicle();
            prevBodyYaw = MathHelper.lerpAngle(tickDelta, vehicle.prevBodyYaw, vehicle.bodyYaw);
            headYawDelta = prevHeadYaw - prevBodyYaw;
            float wrappedDelta = MathHelper.wrapDegrees(headYawDelta);

            if (wrappedDelta < -85.0f) {
                wrappedDelta = -85.0f;
            }
            if (wrappedDelta >= 85.0f) {
                wrappedDelta = 85.0f;
            }

            prevBodyYaw = prevHeadYaw - wrappedDelta;
            if (wrappedDelta * wrappedDelta > 2500.0f) {
                prevBodyYaw += wrappedDelta * 0.2f;
            }
            headYawDelta = prevHeadYaw - prevBodyYaw;
        }

        float limbAngle = this.getAnimationProgress(entity, tickDelta);
        float limbDistance = 0.0f;
        float limbSpeed = 0.0f;

        if (!entity.hasVehicle() && entity.isOnGround()) {
            limbDistance = entity.limbAnimator.getSpeed(tickDelta);
            limbSpeed = entity.limbAnimator.getPos(tickDelta);
            if (entity.isSprinting()) {
                limbSpeed *= 3.0f;
            }
            if (limbDistance > 1.0f) {
                limbDistance = 1.0f;
            }
        }

        ILivingEntity player = (ILivingEntity) MinecraftClient.getInstance().player;
        float pitch = entity == MinecraftClient.getInstance().player ?
            MathHelper.lerp(tickDelta, player.prevPitchHead(), player.pitchHead()) :
            MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());

        if (shouldFlipUpsideDown(entity)) {
            pitch *= -1.0f;
            headYawDelta *= -1.0f;
        }

        this.model.setAngles(entity, limbSpeed, limbDistance, limbAngle, headYawDelta, pitch);
    }

    @Shadow
    public static boolean shouldFlipUpsideDown(LivingEntity entity) {
        return false;
    }
}