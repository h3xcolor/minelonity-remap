// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.d.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class ChatSender implements ru.melonity.d.ChatService {
    public static int VERSION_ID = 904780954;

    @Override
    public void sendMessage(String message) {
        MinecraftClient.getInstance().player.sendChatMessage(message);
    }
}