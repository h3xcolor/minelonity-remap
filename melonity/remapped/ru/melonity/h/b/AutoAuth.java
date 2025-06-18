// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import ru.melonity.f.EventListener;
import ru.melonity.f.events.PacketEvent;
import ru.melonity.h.c.Module;
import ru.melonity.o.Category;
import ru.melonity.w.ChatUtils;

@Environment(EnvType.CLIENT)
public class AutoAuth extends Module {
    private final EventListener<PacketEvent> eventListener = event -> {
        if (!isEnabled()) {
            return;
        }
        if (event.getType() != PacketEvent.Type.SEND) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (packet instanceof ChatMessageC2SPacket) {
            String message = ((ChatMessageC2SPacket) packet).getChatMessage();
            if (message == null) {
                return;
            }
            if (message.contains("/l") || message.contains("/login")) {
                ChatUtils.sendChatMessage("/login pQoMwtlaze");
            } else if (message.contains("/reg") || message.contains("/register")) {
                ChatUtils.sendChatMessage("/register pQoMwtlaze pQoMwtlaze");
                ChatUtils.sendChatMessage("/register pQoMwtlaze");
            }
        }
    };

    public AutoAuth() {
        super("AutoAuth", Category.OTHER);
    }
}