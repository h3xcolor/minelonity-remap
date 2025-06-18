// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.hack.module;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import ru.melonity.hack.event.events.MovementInputCallback;
import ru.melonity.hack.event.events.MovementInputEvent;
import ru.melonity.hack.module.Module;
import ru.melonity.hack.module.ModuleCategory;

@Environment(EnvType.CLIENT)
public class SafeWalkModule extends Module {
    private static final double STEP_DISTANCE = 0.3;
    private final MovementInputCallback movementCallback = this::handleMovementInput;

    public SafeWalkModule() {
        super("SafeWalk", ModuleCategory.MOVEMENT);
    }

    private void handleMovementInput(MovementInputEvent event) {
        if (!isEnabled()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen != null) {
            return;
        }
        double playerX = client.player.getX();
        double playerY = client.player.getY();
        double playerZ = client.player.getZ();
        float playerYaw = client.player.getYaw();

        if (client.options.forwardKey.isPressed() && isBlockSafe(playerX, playerY, playerZ, playerYaw)) {
            event.setForward(false);
        }
        if (client.options.backKey.isPressed() && isBlockSafe(playerX, playerY, playerZ, playerYaw + 180.0f)) {
            event.setBackward(false);
        }
        if (client.options.rightKey.isPressed() && isBlockSafe(playerX, playerY, playerZ, playerYaw + 90.0f)) {
            event.setRight(false);
        }
        if (client.options.leftKey.isPressed() && isBlockSafe(playerX, playerY, playerZ, playerYaw - 90.0f)) {
            event.setLeft(false);
        }
    }

    private boolean isBlockSafe(double x, double y, double z, float yaw) {
        double radianYaw = Math.toRadians(yaw);
        double xOffset = -Math.sin(radianYaw) * STEP_DISTANCE;
        double zOffset = Math.cos(radianYaw) * STEP_DISTANCE;
        BlockPos checkPos = BlockPos.ofFloored(x + xOffset, y - 1.0, z + zOffset);
        return MinecraftClient.getInstance().world.getBlockState(checkPos).isSolid();
    }
}