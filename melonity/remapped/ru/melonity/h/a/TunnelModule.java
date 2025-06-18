// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import ru.melonity.h.c.Module;
import ru.melonity.h.a.b.SettingsGroup;
import ru.melonity.o.Category;

@Environment(EnvType.CLIENT)
public class TunnelModule extends Module {
    private final SettingsGroup parameters = new SettingsGroup("Parameters", "H", "W", "D");

    public TunnelModule() {
        super("Tunnel", Category.MINING);
        this.addSetting(this.parameters);
    }

    @Override
    public void onToggle(boolean enabled) {
        super.onToggle(enabled);
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        player.networkHandler.sendChatMessage("#stop");
        if (enabled) {
            int height = this.parameters.getHeight();
            int width = this.parameters.getWidth();
            int depth = this.parameters.getDepth();
            player.networkHandler.sendChatMessage("#tunnel " + height + " " + width + " " + depth);
        }
    }
}