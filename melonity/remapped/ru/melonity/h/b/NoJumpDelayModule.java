// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import ru.melonity.f.b.ClientTickEvent;
import ru.melonity.h.c.Module;
import ru.melonity.o.ModCategory;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class NoJumpDelayModule extends Module {
    private final Consumer<ClientTickEvent> tickEventListener = (ClientTickEvent event) -> {
        if (!this.isEnabled()) {
            return;
        }
        LivingEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.resetJumpDelay();
        }
    };

    public NoJumpDelayModule() {
        super("NoJumpDelay", ModCategory.MOVEMENT);
    }
}