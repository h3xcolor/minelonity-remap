// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.f.b.MovementInputEvent;
import ru.melonity.f.b.MovementInputUpdateEvent;

@Environment(EnvType.CLIENT)
@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Shadow
    @Final
    private MinecraftClient minecraftClient;

    private static float getMovementMultiplier(boolean positive, boolean negative) {
        if (positive == negative) {
            return 0.0f;
        }
        return positive ? 1.0f : -1.0f;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        KeyboardInput keyboardInput = (KeyboardInput) (Object) this;
        MovementInputEvent movementInputEvent = new MovementInputEvent(
            this.minecraftClient,
            this.minecraftClient.options.forwardKey.isPressed(),
            this.minecraftClient.options.backKey.isPressed(),
            this.minecraftClient.options.rightKey.isPressed(),
            this.minecraftClient.options.leftKey.isPressed()
        );
        Melonity.EVENT_BUS.post(movementInputEvent);
        keyboardInput.pressingForward = movementInputEvent.getForwardPressed();
        keyboardInput.pressingBack = movementInputEvent.getBackwardPressed();
        keyboardInput.pressingRight = movementInputEvent.getRightPressed();
        keyboardInput.pressingLeft = movementInputEvent.getLeftPressed();
        keyboardInput.movementForward = getMovementMultiplier(keyboardInput.pressingForward, keyboardInput.pressingBack);
        keyboardInput.movementSideways = getMovementMultiplier(keyboardInput.pressingRight, keyboardInput.pressingLeft);
        keyboardInput.jumping = this.minecraftClient.options.jumpKey.isPressed();
        keyboardInput.sneaking = this.minecraftClient.options.sneakKey.isPressed();
        MovementInputUpdateEvent movementInputUpdateEvent = new MovementInputUpdateEvent(
            keyboardInput.movementForward,
            keyboardInput.movementSideways,
            keyboardInput.jumping,
            keyboardInput.sneaking
        );
        Melonity.EVENT_BUS.post(movementInputUpdateEvent);
        keyboardInput.movementForward = movementInputUpdateEvent.getMovementForward();
        keyboardInput.movementSideways = movementInputUpdateEvent.getMovementSideways();
        keyboardInput.jumping = movementInputUpdateEvent.isJumping();
        keyboardInput.sneaking = movementInputUpdateEvent.isSneaking();
        if (slowDown && !movementInputUpdateEvent.isSlowDownApplied()) {
            keyboardInput.movementSideways *= slowDownFactor;
            keyboardInput.movementForward *= slowDownFactor;
        }
        ci.cancel();
    }
}