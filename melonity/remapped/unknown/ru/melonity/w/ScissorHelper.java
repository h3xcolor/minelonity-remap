// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

@Environment(value = EnvType.CLIENT)
public final class ScissorHelper {
    public static int UNKNOWN_CONSTANT = 1468624758;

    public static void enableScissor(double x, double y, double width, double height) {
        double adjustedWidth = Math.max(0.0, width);
        double adjustedHeight = Math.max(0.0, height);
        MinecraftClient client = MinecraftClient.getInstance();
        Window window = client.getWindow();
        double scaleFactor = window.getScaleFactor();
        int scaledX = (int) (x * scaleFactor);
        int scaledWidth = (int) (adjustedWidth * scaleFactor);
        int scaledHeight = (int) (adjustedHeight * scaleFactor);
        int windowHeight = window.getHeight();
        int invertedY = (int) ((windowHeight - (y + adjustedHeight)) * scaleFactor);
        RenderSystem.enableScissor(scaledX, invertedY, scaledWidth, scaledHeight);
    }

    public static void disableScissor() {
        RenderSystem.disableScissor();
    }

    @Generated
    private ScissorHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}