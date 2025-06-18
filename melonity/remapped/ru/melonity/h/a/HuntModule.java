// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import ru.melonity.h.c.ModuleBase;
import ru.melonity.o.HuntHelper;

@Environment(EnvType.CLIENT)
public class HuntModule extends ModuleBase {

    public HuntModule() {
        super("Hunt", ModuleBase.Category.COMBAT);
        initialize();
    }

    @Override
    public void onToggle(boolean enabled) {
        super.onToggle(enabled);
        MinecraftClient client = MinecraftClient.getInstance();
        client.player.networkHandler.sendChatMessage("#stop");
        if (enabled) {
            client.player.networkHandler.sendChatMessage("#follow entities");
            HuntHelper.activate(null);
        }
    }

    private void initialize() {}
}