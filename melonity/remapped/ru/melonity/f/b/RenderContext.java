// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import ru.melonity.f.Renderer;

@Environment(EnvType.CLIENT)
public record RenderContext(MatrixStack matrixStack, DrawContext drawContext) implements Renderer {
}