// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.GlyphInfo;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public interface ITextRenderer {
    public static int DEFAULT_PACKED_COLOR = 536738491;

    public FontSet getFontSet(ResourceLocation identifier);

    public boolean shouldValidateGlyphAdvances();

    public void renderGlyph(GlyphInfo glyph, boolean bold, boolean italic, float x, float y, float z, Matrix4f poseMatrix, VertexConsumer vertexBuffer, float red, float green, float blue, float alpha, int light);
}