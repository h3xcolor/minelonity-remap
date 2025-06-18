// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.hack.module.mods;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import ru.melonity.hack.event.Listener;
import ru.melonity.hack.event.events.ClientTickEvent;
import ru.melonity.hack.module.Module;
import ru.melonity.hack.module.Module.Category;

@Environment(EnvType.CLIENT)
public class AutoRespawn extends Module {
    private final Listener<ClientTickEvent> tickEventListener = event -> {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof DeathScreen && client.player != null && client.player.deathTime == 1) {
            client.setScreen(null);
            client.player.requestRespawn();
        }
    };

    public AutoRespawn() {
        super("AutoRespawn", Category.PLAYER);
    }
}