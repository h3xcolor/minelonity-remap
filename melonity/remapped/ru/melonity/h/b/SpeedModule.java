// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import ru.melonity.Melonity;
import ru.melonity.hack.event.EventListener;
import ru.melonity.hack.event.events.PlayerTickEvent;
import ru.melonity.hack.module.Module;
import ru.melonity.hack.setting.EnumSetting;
import ru.melonity.utils.MovementUtils;

@Environment(EnvType.CLIENT)
public class SpeedModule extends Module {
    private final EnumSetting modeSetting = new EnumSetting("Mode", EnumSetting.Mode.TIMER, "Timer", "Timer");
    private final EventListener<PlayerTickEvent> playerTickListener = event -> {
        if (!isEnabled()) {
            return;
        }
        if (!MovementUtils.canMove()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }
        if (client.player.isOnGround()) {
            client.player.jump();
        }
        Melonity.playerSpeed = client.player.isOnGround() ? 0.9f : (client.player.forwardSpeed >= 0.3f ? 2.0f : 0.8f);
    };

    public SpeedModule() {
        super("Speed", Module.Category.MOVEMENT);
    }

    @Override
    public void onToggle(boolean enabled) {
        super.onToggle(enabled);
        if (!enabled) {
            Melonity.playerSpeed = 1.0f;
        }
    }
}