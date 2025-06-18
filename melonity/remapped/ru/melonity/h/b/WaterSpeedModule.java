// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import ru.melonity.h.c.Module;
import ru.melonity.h.event.EventListener;
import ru.melonity.h.event.MovementUpdateEvent;
import ru.melonity.h.setting.FloatSetting;
import ru.melonity.h.util.MovementHelper;

@Environment(EnvType.CLIENT)
public class WaterSpeedModule extends Module {
    private final FloatSetting speedSetting = new FloatSetting("global.speed", value -> String.format("%.2f", value), 0.1f, 0.6f, 0.4f);
    private final EventListener<MovementUpdateEvent> onMovementUpdate = event -> {
        if (!isActive()) {
            return;
        }
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || player.isSneaking() || player.isUsingItem()) {
            return;
        }
        if (player.isTouchingWater()) {
            float speed = speedSetting.getValue();
            float boost = speed / 15.0f;
            double currentSpeed = MovementHelper.getSpeed();
            MovementHelper.setSpeed(currentSpeed + boost);
        }
    };

    public WaterSpeedModule() {
        super("WaterSpeed", Module.Category.MOVEMENT);
        addSetting(speedSetting);
        addEventListener(onMovementUpdate);
    }
}