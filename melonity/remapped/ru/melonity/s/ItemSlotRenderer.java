// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import ru.melonity.Melonity;
import ru.melonity.utils.InputHelper;
import ru.melonity.utils.RoundRenderer;
import ru.metafaze.protection.annotation.Compile;

@Environment(EnvType.CLIENT)
public class ItemSlotRenderer {
    private static ItemStack hoveredItem = null;
    public static int hoveredSlotIndex = -1;
    public static boolean isSlotHovered = false;

    @Compile
    public static native void renderSlot(DrawContext context, Screen screen, MinecraftClient client, int x, int y, int slotIndex, int mouseX);

    public static void renderItemInSlot(int slotIndex, ItemStack stack, float x, float y, int mouseX, int mouseY, Color color, DrawContext context) {
        RoundRenderer roundRenderer = Melonity.getRoundRenderer();
        boolean isHovered = InputHelper.isMouseOver(x, y, 18.0, 18.0, mouseX, mouseY);
        isSlotHovered = isHovered;
        MatrixStack matrixStack = context.getMatrices();
        Color slotColor = isHovered ? color.brighter() : color;
        roundRenderer.drawRoundedRectangle(x, y, 18.0f, 18.0f, 5.0f, slotColor, matrixStack);
        Melonity.renderItem(stack, x, y, 18.0f, 5.0f, matrixStack);
        matrixStack.push();
        matrixStack.translate(x, y, 0.0f);
        matrixStack.scale(0.75f, 0.75f, 0.75f);
        matrixStack.translate(-x, -y, 0.0f);
        context.drawItem(stack, (int) x + 4, (int) y + 4);
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        int textX = (int) (x + 5.0f);
        int textY = (int) (y + 5.0f);
        int count = stack.getCount();
        String countText = count > 1 ? String.valueOf(count) : "";
        context.drawItemInSlot(textRenderer, stack, textX, textY, countText);
        matrixStack.pop();
        if (isHovered) {
            hoveredItem = stack;
            hoveredSlotIndex = slotIndex;
        }
    }
}