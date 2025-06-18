// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.e;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_757;
import net.minecraft.class_9779;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import ru.melonity.Melonity;
import ru.melonity.s.c.RenderUtils;

import java.awt.Color;
import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class MiniMapRenderer {
    private static final int MAP_DIMENSION = 200;
    private static final int CIRCLE_RADIUS = 95;
    private static final float BLOCK_SCALE = 0.425f;
    private static final HashMap<class_2248, Integer> blockColorCache = new HashMap<>();
    private final byte[] mapColorData;
    private final byte[] mapColorBuffer;
    private final byte[] rotatedMapBuffer;
    private volatile boolean isUpdating = false;
    private final class_310 minecraftClient = class_310.method_1551();

    public MiniMapRenderer() {
        int numberOfPixels = MAP_DIMENSION * MAP_DIMENSION;
        this.mapColorData = new byte[numberOfPixels * 3];
        this.mapColorBuffer = new byte[numberOfPixels * 3];
        this.rotatedMapBuffer = new byte[numberOfPixels * 3];
    }

    public void updateMapAsync() {
        if (this.isUpdating || this.minecraftClient.field_1687 == null || this.minecraftClient.field_1724 == null) {
            return;
        }
        this.isUpdating = true;
        new Thread(() -> {
            class_638 world = this.minecraftClient.field_1687;
            int seaLevel = world.method_31605();
            double playerX = this.minecraftClient.field_1724.method_23317();
            double playerZ = this.minecraftClient.field_1724.method_23321();
            int dataIndex = 0;

            for (int mapX = -100; mapX < 100; ++mapX) {
                for (int mapZ = -100; mapZ < 100; ++mapZ) {
                    long totalRed = 0;
                    long totalGreen = 0;
                    long totalBlue = 0;
                    int sampleCount = 0;
                    int offsetX = 0;
                    while ((float) offsetX < BLOCK_SCALE) {
                        int offsetZ = 0;
                        while ((float) offsetZ < BLOCK_SCALE) {
                            int worldX = (int) ((float) mapX * BLOCK_SCALE) + offsetX;
                            int worldZ = (int) ((float) mapZ * BLOCK_SCALE) + offsetZ;
                            class_2248 block = null;
                            for (int y = seaLevel - 1; y >= 0; --y) {
                                class_2338 blockPos = new class_2338((int) playerX + worldX, y, (int) playerZ + worldZ);
                                class_2680 blockState = world.method_8320(blockPos);
                                if (blockState.method_26215()) {
                                    continue;
                                }
                                block = blockState.method_26204();
                                break;
                            }
                            int blockColor = 0;
                            if (block != null) {
                                blockColor = blockColorCache.computeIfAbsent(block, b -> b.method_26403().field_16011);
                            }
                            int red = blockColor >> 16 & 0xFF;
                            int green = blockColor >> 8 & 0xFF;
                            int blue = blockColor & 0xFF;
                            totalRed += red;
                            totalGreen += green;
                            totalBlue += blue;
                            sampleCount++;
                            offsetZ++;
                        }
                        offsetX++;
                    }
                    int avgRed = sampleCount > 0 ? (int) (totalRed / sampleCount) : 0;
                    int avgGreen = sampleCount > 0 ? (int) (totalGreen / sampleCount) : 0;
                    int avgBlue = sampleCount > 0 ? (int) (totalBlue / sampleCount) : 0;
                    this.mapColorData[dataIndex++] = (byte) avgRed;
                    this.mapColorData[dataIndex++] = (byte) avgGreen;
                    this.mapColorData[dataIndex++] = (byte) avgBlue;
                }
            }
            synchronized (this.mapColorBuffer) {
                System.arraycopy(this.mapColorData, 0, this.mapColorBuffer, 0, this.mapColorData.length);
                this.isUpdating = false;
            }
        }).start();
    }

    public void renderMap(class_4587 matrixStack, float screenX, float screenY) {
        if (this.minecraftClient.field_1687 == null || this.minecraftClient.field_1724 == null) {
            return;
        }
        RenderUtils utils = Melonity.RENDER_UTILS;
        Color circleColor = Color.decode("#8ab0fc");
        utils.drawCircle(screenX, screenY, CIRCLE_RADIUS, CIRCLE_RADIUS, 25.0f, circleColor, matrixStack);
        RenderSystem.setShader(class_757::method_34540);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrixStack.method_22903();
        matrixStack.method_46416(screenX + 47.5f, screenY + 47.5f, 0.0f);
        class_746 player = this.minecraftClient.field_1724;
        class_9779 camera = this.minecraftClient.method_60646();
        float cameraPitch = camera.method_60637(true);
        float yaw = player.method_5705(cameraPitch) + 180.0f;
        double angleRad = Math.toRadians(yaw % 360.0f);
        double cosAngle = Math.cos(angleRad);
        double sinAngle = Math.sin(angleRad);
        int centerPos = MAP_DIMENSION / 2;

        synchronized (this.mapColorBuffer) {
            int bufferIndex = 0;
            for (int y = 0; y < MAP_DIMENSION; y++) {
                for (int x = 0; x < MAP_DIMENSION; x++) {
                    int rotatedX = (int) ((x - centerPos) * cosAngle - (y - centerPos) * sinAngle) + centerPos;
                    int rotatedY = (int) ((x - centerPos) * sinAngle + (y - centerPos) * cosAngle) + centerPos;
                    int srcIndex = (y * MAP_DIMENSION + x) * 3;
                    int destIndex = (rotatedX * MAP_DIMENSION + rotatedY) * 3;
                    if (rotatedX < 0 || rotatedX >= MAP_DIMENSION || rotatedY < 0 || rotatedY >= MAP_DIMENSION) {
                        continue;
                    }
                    this.rotatedMapBuffer[destIndex] = this.mapColorBuffer[srcIndex];
                    this.rotatedMapBuffer[destIndex + 1] = this.mapColorBuffer[srcIndex + 1];
                    this.rotatedMapBuffer[destIndex + 2] = this.mapColorBuffer[srcIndex + 2];
                }
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            class_289 drawingParameters = class_289.method_1348();
            class_287 bufferBuilder = drawingParameters.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
            synchronized (this.rotatedMapBuffer) {
                int pixelIndex = 0;
                int pixelsDrawn = 0;
                float pixelScale = 0.475f;
                for (int y = 0; y < MAP_DIMENSION; y++) {
                    for (int x = 0; x < MAP_DIMENSION; x++) {
                        int red = this.rotatedMapBuffer[pixelIndex++] & 0xFF;
                        int green = this.rotatedMapBuffer[pixelIndex++] & 0xFF;
                        int blue = this.rotatedMapBuffer[pixelIndex++] & 0xFF;
                        if (red == 0 && green == 0 && blue == 0) {
                            continue;
                        }
                        float pixelX = (float) (x - centerPos) * pixelScale;
                        float pixelY = (float) (y - centerPos) * pixelScale;
                        pixelsDrawn++;
                        Matrix4f matrix = matrixStack.method_23760().method_23761();
                        class_4588 vertex = bufferBuilder.method_22918(matrix, pixelX, pixelY + pixelScale, 0.0f);
                        vertex.method_1336(red, green, blue, 255);
                        matrix = matrixStack.method_23760().method_23761();
                        vertex = bufferBuilder.method_22918(matrix, pixelX + pixelScale, pixelY + pixelScale, 0.0f);
                        vertex.method_1336(red, green, blue, 255);
                        matrix = matrixStack.method_23760().method_23761();
                        vertex = bufferBuilder.method_22918(matrix, pixelX + pixelScale, pixelY, 0.0f);
                        vertex.method_1336(red, green, blue, 255);
                        matrix = matrixStack.method_23760().method_23761();
                        vertex = bufferBuilder.method_22918(matrix, pixelX, pixelY, 0.0f);
                        vertex.method_1336(red, green, blue, 255);
                    }
                }
                if (pixelsDrawn > 0) {
                    class_286.method_43433(bufferBuilder.method_60800());
                }
            }
            matrixStack.method_22909();
            RenderSystem.disableBlend();
        }
    }
}