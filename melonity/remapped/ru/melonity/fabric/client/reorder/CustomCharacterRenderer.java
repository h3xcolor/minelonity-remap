// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.reorder;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.TextRenderer;
import net.minecraft.client.util.TextRenderer.TextLayerType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import ru.melonity.fabric.client.model.ITextRenderer;

@Environment(EnvType.CLIENT)
public class CustomCharacterRenderer implements TextVisitFactory.CharacterVisitor {
    public int characterIndex = 0;
    private final int nicknameStartIndex;
    final VertexConsumerProvider vertexConsumers;
    private final boolean shadow;
    private final float brightnessMultiplier;
    private final float baseRed;
    private final float baseGreen;
    private final float baseBlue;
    private final float baseAlpha;
    private final Matrix4f matrix;
    private final TextRenderer textRenderer;
    private final TextLayerType layerType;
    private final int light;
    float x;
    float y;
    @Nullable
    private List<GlyphRenderer.Rectangle> decorationRectangles;
    private final String replacementString = "Melonity";
    public int replacementCharIndex = 0;
    public int replacementCharsDisplayed = 0;

    private void addDecorationRectangle(GlyphRenderer.Rectangle rectangle) {
        if (this.decorationRectangles == null) {
            this.decorationRectangles = Lists.newArrayList();
        }
        this.decorationRectangles.add(rectangle);
    }

    public CustomCharacterRenderer(TextRenderer textRenderer, VertexConsumerProvider vertexConsumers, float x, float y, int color, boolean shadow, Matrix4f matrix, TextLayerType layerType, int light, int nicknameStartIndex) {
        this.textRenderer = textRenderer;
        this.nicknameStartIndex = nicknameStartIndex;
        this.vertexConsumers = vertexConsumers;
        this.x = x;
        this.y = y;
        this.shadow = shadow;
        this.brightnessMultiplier = shadow ? 0.25f : 1.0f;
        this.baseRed = (float)(color >> 16 & 0xFF) / 255.0f * this.brightnessMultiplier;
        this.baseGreen = (float)(color >> 8 & 0xFF) / 255.0f * this.brightnessMultiplier;
        this.baseBlue = (float)(color & 0xFF) / 255.0f * this.brightnessMultiplier;
        this.baseAlpha = (float)(color >> 24 & 0xFF) / 255.0f;
        this.matrix = matrix;
        this.layerType = layerType;
        this.light = light;
    }

    public boolean accept(int charIndex, Style style, int charCode) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            if (this.nicknameStartIndex != -1 && this.characterIndex >= this.nicknameStartIndex) {
                Text displayName = client.player.getDisplayName();
                String displayText = displayName.getString();
                int nicknameLength = displayText.length();
                if (this.characterIndex < this.nicknameStartIndex + nicknameLength) {
                    int replacementLength = this.replacementString.length();
                    if (this.replacementCharsDisplayed < replacementLength) {
                        char replacementChar = this.replacementString.charAt(this.replacementCharIndex);
                        charCode = replacementChar;
                        this.replacementCharIndex = (this.replacementCharIndex + 1) % replacementLength;
                        this.replacementCharsDisplayed++;
                    } else {
                        charCode = 32;
                    }
                }
            }
            this.characterIndex++;
        }
        ITextRenderer customRenderer = (ITextRenderer)this.textRenderer;
        Identifier fontId = style.getFont();
        FontStorage fontStorage = customRenderer.fontStorage(fontId);
        boolean validateAdvance = customRenderer.validateAdvance();
        Glyph glyph = fontStorage.getGlyph(charCode, validateAdvance);
        boolean bold = style.isBold();
        GlyphRenderer glyphRenderer;
        if (bold && charCode != 32) {
            glyphRenderer = fontStorage.getBoldGlyph(glyph);
        } else {
            glyphRenderer = fontStorage.getGlyphRenderer(charCode);
        }
        boolean underline = style.isUnderlined();
        boolean strikethrough = style.isStrikethrough();
        float alpha = this.baseAlpha;
        TextColor textColor = style.getColor();
        float red, green, blue;
        if (textColor != null) {
            int rgb = textColor.getRgb();
            red = (float)(rgb >> 16 & 0xFF) / 255.0f * this.brightnessMultiplier;
            green = (float)(rgb >> 8 & 0xFF) / 255.0f * this.brightnessMultiplier;
            blue = (float)(rgb & 0xFF) / 255.0f * this.brightnessMultiplier;
        } else {
            red = this.baseRed;
            green = this.baseGreen;
            blue = this.baseBlue;
        }
        if (!(glyphRenderer instanceof GlyphRenderer.Baked)) {
            float shadowOffset = this.shadow ? glyph.getShadowOffset() : 0.0f;
            float boldOffset = bold ? glyph.getBoldOffset() : 0.0f;
            RenderLayer renderLayer = glyphRenderer.getLayer(this.layerType);
            VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(renderLayer);
            boolean italic = style.isItalic();
            customRenderer.myDrawGlyph(glyphRenderer, bold, italic, boldOffset, this.x + shadowOffset, this.y + shadowOffset, this.matrix, vertexConsumer, red, green, blue, alpha, this.light);
        }
        float advance = glyph.getAdvance(bold);
        float shadowOffsetX = this.shadow ? 1.0f : 0.0f;
        if (underline) {
            GlyphRenderer.Rectangle underlineRect = new GlyphRenderer.Rectangle(this.x + shadowOffsetX - 1.0f, this.y + shadowOffsetX + 4.5f, this.x + shadowOffsetX + advance, this.y + shadowOffsetX + 4.5f - 1.0f, 0.01f, red, green, blue, alpha);
            this.addDecorationRectangle(underlineRect);
        }
        if (strikethrough) {
            GlyphRenderer.Rectangle strikethroughRect = new GlyphRenderer.Rectangle(this.x + shadowOffsetX - 1.0f, this.y + shadowOffsetX + 9.0f, this.x + shadowOffsetX + advance, this.y + shadowOffsetX + 9.0f - 1.0f, 0.01f, red, green, blue, alpha);
            this.addDecorationRectangle(strikethroughRect);
        }
        this.x += advance;
        return true;
    }

    public float drawLayer(int underlineColor, float underlineStartX) {
        if (underlineColor != 0) {
            float underlineAlpha = (float)(underlineColor >> 24 & 0xFF) / 255.0f;
            float underlineRed = (float)(underlineColor >> 16 & 0xFF) / 255.0f;
            float underlineGreen = (float)(underlineColor >> 8 & 0xFF) / 255.0f;
            float underlineBlue = (float)(underlineColor & 0xFF) / 255.0f;
            GlyphRenderer.Rectangle underlineRect = new GlyphRenderer.Rectangle(underlineStartX - 1.0f, this.y + 9.0f, this.x + 1.0f, this.y - 1.0f, 0.01f, underlineRed, underlineGreen, underlineBlue, underlineAlpha);
            this.addDecorationRectangle(underlineRect);
        }
        if (this.decorationRectangles != null) {
            ITextRenderer customRenderer = (ITextRenderer)this.textRenderer;
            FontStorage fontStorage = customRenderer.fontStorage(Style.DEFAULT_FONT_ID);
            GlyphRenderer glyphRenderer = fontStorage.getRectangleRenderer();
            RenderLayer renderLayer = glyphRenderer.getLayer(this.layerType);
            VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(renderLayer);
            Iterator<GlyphRenderer.Rectangle> iterator = this.decorationRectangles.iterator();
            while (iterator.hasNext()) {
                GlyphRenderer.Rectangle rect = iterator.next();
                glyphRenderer.drawRectangle(rect, this.matrix, vertexConsumer, this.light);
            }
        }
        return this.x;
    }
}