// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import ru.melonity.hack.module.Module;

@Environment(EnvType.CLIENT)
public class FarmModule extends Module {
    public FarmModule() {
        super("Farm", Module.Category.FARM);
        initialize();
    }

    @Override
    public void onToggle(boolean enabled) {
        super.onToggle(enabled);
        MinecraftClient client = MinecraftClient.getInstance();
        client.player.sendChatMessage("#stop");
        if (enabled) {
            client.player.sendChatMessage("#farm");
            startFarming(null);
        }
    }
}