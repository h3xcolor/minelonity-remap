// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import ru.melonity.h.c.Module;
import ru.melonity.o.Category;
import ru.melonity.w.Settings;

@Environment(EnvType.CLIENT)
public class JesusModule extends Module {
    private final ToggleCallback onToggle = module -> {
        if (!this.isActive()) {
            return;
        }
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        if (!player.isTouchingWater()) {
            return;
        }
        Vec3d currentVelocity = player.getVelocity();
        Vec3d newVelocity = new Vec3d(currentVelocity.x, 0.0, currentVelocity.z);
        player.setVelocity(newVelocity);

        boolean useSpeed = Settings.isSpeedEnabled();
        if (useSpeed) {
            double speedValue = Settings.getSpeedValue();
            if (speedValue < 0.9) {
                Vec3d updatedVelocity = player.getVelocity();
                Vec3d boostedVelocity = new Vec3d(updatedVelocity.x * 1.25, updatedVelocity.y, updatedVelocity.z * 1.25);
                player.setVelocity(boostedVelocity);
            }
        }
    };

    public JesusModule() {
        super("Jesus", Category.MOVEMENT, true);
    }
}