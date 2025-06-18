// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public class SprintHelper {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean canSprint(boolean unusedFlag) {
        PlayerEntity player = CLIENT.player;
        if (player == null) {
            return false;
        }
        Input input = player.input;
        if (input.pressingForward) {
            return true;
        }
        boolean hasSpeedEffect = player.hasStatusEffect(StatusEffects.SPEED);
        boolean shouldSprint;
        if (hasSpeedEffect) {
            shouldSprint = true;
        } else {
            Input inputAgain = player.input;
            shouldSprint = inputAgain.pressingForward;
        }
        float movementModifier = getMovementModifier(player, 0.5f);
        if (movementModifier < 0.93f) {
            return false;
        }
        if (player.forwardSpeed < 0.2f) {
            return false;
        }
        if (shouldSprint) {
            return true;
        }
        boolean isOnGround = player.isOnGround();
        return !isOnGround && player.forwardSpeed > 0.0f;
    }

    private static float getMovementModifier(PlayerEntity player, float modifier) {
        
        return 1.0f;
    }
}