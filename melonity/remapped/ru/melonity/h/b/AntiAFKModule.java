// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import ru.melonity.f.Listener;
import ru.melonity.f.b.Setting;
import ru.melonity.h.c.Module;
import ru.melonity.o.ModuleCategory;
import ru.melonity.w.RotationManager;

@Environment(EnvType.CLIENT)
public class AntiAFKModule extends Module {
    private int tickCounter;
    private final Listener<Setting> settingListener = setting -> {
        if (!this.isEnabled()) {
            return;
        }
        if (minecraftClient.player == null) {
            return;
        }
        if (!RotationManager.shouldRotatePlayer()) {
            double angle = Math.toRadians(9 * (minecraftClient.player.age % 40));
            float sin = (float) MathHelper.clamp(Math.sin(angle), -1.0, 1.0);
            float cos = (float) MathHelper.clamp(Math.cos(angle), -1.0, 1.0);
            minecraftClient.player.yaw = Math.round(sin);
            minecraftClient.player.pitch = Math.round(cos);
        }
        if (minecraftClient.player.isOnGround()) {
            minecraftClient.player.jump();
        }
    };
    
    public AntiAFKModule() {
        super("AntiAFK", ModuleCategory.MOVEMENT);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            minecraftClient.options.attackKey.setPressed(true);
            this.tickCounter = 0;
        }
    }
}