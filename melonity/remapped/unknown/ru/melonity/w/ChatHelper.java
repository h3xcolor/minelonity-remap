// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.apache.commons.lang3.StringUtils;

@Environment(EnvType.CLIENT)
public final class ChatHelper {

    public static void sendChatMessage(String message) {
        String normalized = normalizeAndTruncate(message);
        if (normalized.isEmpty()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        ChatHud chatHud = client.inGameHud.getChatHud();
        chatHud.addMessage(normalized);
        if (normalized.startsWith("/")) {
            String command = normalized.substring(1);
            ClientPlayerInteractionManager interactionManager = client.player.networkHandler;
            interactionManager.sendCommand(command);
        } else {
            client.player.networkHandler.sendChatMessage(normalized);
        }
    }

    private static String truncateToMaxLength(String input) {
        int length = input.length();
        if (length <= 256) {
            return input;
        }
        return input.substring(0, 256);
    }

    private static String normalizeAndTruncate(String input) {
        return truncateToMaxLength(StringUtils.normalizeSpace(input.trim()));
    }

    @Generated
    private ChatHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}