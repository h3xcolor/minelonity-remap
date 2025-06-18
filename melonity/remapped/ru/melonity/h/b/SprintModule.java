// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import ru.melonity.fabric.api.events.PlayerMoveEvent;
import ru.melonity.fabric.api.events.PlayerUpdateEvent;
import ru.melonity.fabric.api.settings.BooleanSetting;
import ru.melonity.h.c.Module;
import ru.melonity.h.c.ModuleCategory;

@Environment(EnvType.CLIENT)
public class SprintModule extends Module {
    private final BooleanSetting keepSprintSetting = new BooleanSetting("sprint.keep", true);
    private final EventListener<PlayerMoveEvent> onPlayerMove = event -> {
        if (!isEnabled()) {
            return;
        }
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && player.canStartSprinting()) {
            player.setSprinting(true);
        }
    };
    private final EventListener<PlayerUpdateEvent> onUpdateEvent = event -> {
        if (keepSprintSetting.getValue() && isEnabled()) {
            event.setSprinting(true);
        }
    };

    public SprintModule() {
        super("Sprint", ModuleCategory.MOVEMENT);
        addSetting(keepSprintSetting);
        addEventListener(onPlayerMove);
        addEventListener(onUpdateEvent);
    }
}