// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Hand;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.h.b.ViewModelSettings;
import ru.melonity.h.b.ItemPositionSettings;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @Inject(method = "applyEquipOffset", at = @At(value = "HEAD"), cancellable = true)
    private void transformEquipPosition(MatrixStack matrices, Hand hand, float swingProgress, CallbackInfo ci) {
        ViewModelSettings viewModelSettings = Melonity.INSTANCE.getModule(ViewModelSettings.class).get();
        int direction = hand == Hand.MAIN_HAND ? 1 : -1;
        float verticalOffset = viewModelSettings.shouldResetHandPosition() ? 0.0f : -0.6f;
        matrices.translate(direction * 0.56f, -0.52f + swingProgress * verticalOffset, -0.72f);
        ci.cancel();
    }

    @Inject(method = "applySwingOffset", at = @At(value = "HEAD"), cancellable = true)
    private void transformSwingPosition(MatrixStack matrices, Hand hand, float swingProgress, CallbackInfo ci) {
        ViewModelSettings viewModelSettings = Melonity.INSTANCE.getModule(ViewModelSettings.class).get();
        ItemStack equippedStack = MinecraftClient.getInstance().player.getStackInHand(hand == Hand.MAIN_HAND ? Hand.MAIN_HAND : Hand.OFF_HAND);
        if (!(equippedStack.getItem() instanceof ToolItem) && !viewModelSettings.holdingMiningTool()) {
            return;
        }
        if (viewModelSettings.shouldResetHandPosition() && hand == Hand.MAIN_HAND) {
            viewModelSettings.applyMainHandSwing(matrices, swingProgress, 1);
            ci.cancel();
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingItem()Z", shift = At.Shift.BEFORE, ordinal = 1))
    private void applyItemPositionEffect(ClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        boolean isMainHand = hand == Hand.MAIN_HAND;
        ItemPositionSettings positionSettings = Melonity.INSTANCE.getModule(ItemPositionSettings.class).get();
        if (!positionSettings.enabled()) {
            return;
        }
        if (isMainHand) {
            matrices.translate(positionSettings.mainHandX.get().floatValue(), positionSettings.mainHandY.get().floatValue(), positionSettings.mainHandZ.get().floatValue());
        } else {
            matrices.translate(positionSettings.offHandX.get().floatValue(), positionSettings.offHandY.get().floatValue(), positionSettings.offHandZ.get().floatValue());
        }
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At(value = "HEAD"), cancellable = true)
    private void adjustConsumableAnimation(MatrixStack matrices, float tickDelta, Hand hand, ItemStack stack, LivingEntity livingEntity, CallbackInfo ci) {
        ItemPositionSettings positionSettings = Melonity.INSTANCE.getModule(ItemPositionSettings.class).get();
        float usageTicks = (float) livingEntity.getItemUseTime() - tickDelta + 1.0f;
        float useProgress = usageTicks / stack.getMaxUseTime();
        float verticalLift = 0.0f;
        if (useProgress < 0.8f) {
            verticalLift = MathHelper.sin(MathHelper.sqrt(usageTicks / 4.0f * (float) Math.PI)) * 0.1f;
            matrices.translate(0.0f, verticalLift, 0.0f);
        }
        float animationProgress = 1.0f - (float) Math.pow(useProgress, 27.0);
        int direction = hand == Hand.MAIN_HAND ? 1 : -1;
        if (hand == Hand.OFF_HAND) {
            matrices.translate(positionSettings.offHandX.get().floatValue(), positionSettings.offHandY.get().floatValue(), positionSettings.offHandZ.get().floatValue());
        } else {
            matrices.translate(positionSettings.mainHandX.get().floatValue(), positionSettings.mainHandY.get().floatValue(), positionSettings.mainHandZ.get().floatValue());
        }
        matrices.translate(animationProgress * 0.6f * direction, animationProgress * -0.5f, 0.0f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * animationProgress * 90.0f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(animationProgress * 10.0f));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * animationProgress * 30.0f));
        ci.cancel();
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", shift = At.Shift.AFTER, ordinal = 12))
    private void adjustSwingTranslation(ClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        boolean mainHand = hand == Hand.MAIN_HAND;
        float horizontalShift = -0.4f * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
        float verticalShift = 0.2f * MathHelper.sin(swingProgress * (float) Math.PI * 2);
        float depthShift = -0.2f * MathHelper.sin(swingProgress * (float) Math.PI);
        int direction = mainHand ? 1 : -1;
        matrices.translate(-(direction * horizontalShift), -verticalShift, -depthShift);
    }
}