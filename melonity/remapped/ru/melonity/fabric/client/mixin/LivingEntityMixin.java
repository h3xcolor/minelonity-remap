// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.melonity.Melonity;
import ru.melonity.f.b.JumpEvent;
import ru.melonity.fabric.client.model.ILivingEntity;
import ru.melonity.fabric.client.model.IMobEffectInstance;
import ru.melonity.h.b.AutoDrinkModule;
import ru.melonity.h.b.HandSwingSpeedModule;
import ru.melonity.h.b.JumpModifierModule;
import ru.melonity.h.b.ModuleManager;
import ru.melonity.h.b.a.BoundingBoxExpander;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public class LivingEntityMixin implements ILivingEntity {
    @Shadow
    private int jumpingCooldown;
    @Unique
    private float rotationPitchHead;
    @Unique
    private float prevRotationPitchHead;
    @Unique
    private BoundingBoxExpander boundingBoxExpander;

    @Override
    public void resetJumpDelay() {
        this.jumpingCooldown = 0;
    }

    @Override
    public void setPitchHead(float pitch) {
        this.rotationPitchHead = pitch;
    }

    @Override
    public float prevPitchHead() {
        return this.prevRotationPitchHead;
    }

    @Override
    public float pitchHead() {
        return this.rotationPitchHead;
    }

    @Override
    public BoundingBoxExpander getBox() {
        return this.boundingBoxExpander;
    }

    @Override
    public void setBox(BoundingBoxExpander expander) {
        this.boundingBoxExpander = expander;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void baseTick(CallbackInfo callbackInfo) {
        this.prevRotationPitchHead = this.rotationPitchHead;
    }

    @Inject(method = "getHandSwingDuration", at = @At("HEAD"), cancellable = true)
    private void getHandSwingDuration(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        HandSwingSpeedModule handSwingSpeedModule = Melonity.getModuleManager().getModule(HandSwingSpeedModule.class).orElseThrow();
        if (handSwingSpeedModule.isEnabled() && (LivingEntity) (Object) this == MinecraftClient.getInstance().player) {
            callbackInfoReturnable.setReturnValue(10);
        }
    }

    @Inject(method = "consumeItem", at = @At("HEAD"))
    protected void consumeItem(CallbackInfo callbackInfo) {
        AutoDrinkModule autoDrinkModule = Melonity.getModuleManager().getModule(AutoDrinkModule.class).orElseThrow();
        int durationTicks = autoDrinkModule.getDrinkDuration().intValue() * 20;
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity == MinecraftClient.getInstance().player && autoDrinkModule.isEnabled()) {
            PlayerEntity player = (PlayerEntity) entity;
            Item consumedItem = player.getStackInHand(player.getActiveHand()).getItem();
            if ((consumedItem == Items.POTION || consumedItem == Items.HONEY_BOTTLE) && !player.getItemCooldownManager().isCoolingDown(consumedItem)) {
                player.getItemCooldownManager().set(consumedItem, durationTicks);
            }
        }
    }

    @Inject(method = "onStatusEffectApplied", at = @At("HEAD"))
    protected void onStatusEffectApplied(StatusEffectInstance effect, Entity source, CallbackInfo callbackInfo) {
        ((IMobEffectInstance) effect).setTimes(effect.getDuration());
    }

    @Inject(method = "onStatusEffectUpgraded", at = @At("HEAD"))
    protected void onStatusEffectUpgraded(StatusEffectInstance effect, boolean upgrade, Entity source, CallbackInfo callbackInfo) {
        ((IMobEffectInstance) effect).setTimes(effect.getDuration());
    }

    @Inject(method = "onStatusEffectRemoved", at = @At("HEAD"))
    protected void onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo callbackInfo) {
        ((IMobEffectInstance) effect).setTimes(-1L);
    }

    @Inject(method = "jump", at = @At("HEAD"))
    public void jump(CallbackInfo callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity == MinecraftClient.getInstance().player) {
            Melonity.getEventBus().post(new JumpEvent());
        }
    }

    @Inject(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V", ordinal = 0, shift = At.Shift.AFTER), cancellable = true)
    public void jumpAfter(CallbackInfo callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;
        MinecraftClient client = MinecraftClient.getInstance();
        if (entity == client.player && Melonity.getModuleManager() != null) {
            ModuleManager moduleManager = Melonity.getModuleManager();
            JumpModifierModule jumpModule = moduleManager.getModule(JumpModifierModule.class).orElseThrow();
            if (jumpModule.isEnabled() && jumpModule.getJumpModifier() != null && jumpModule.getJumpModifier().isActive()) {
                if (entity.isSprinting()) {
                    float yawRadians = jumpModule.getJumpModifier().getYawOffset() * ((float) Math.PI / 180);
                    entity.setVelocity(entity.getVelocity().add(
                            -MathHelper.sin(yawRadians) * 0.2f,
                            0.0,
                            MathHelper.cos(yawRadians) * 0.2f
                    ));
                }
                entity.setOnGround(true);
                callbackInfo.cancel();
            }
        }
    }
}