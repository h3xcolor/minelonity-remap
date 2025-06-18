// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s.c;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import ru.melonity.Melonity;
import ru.melonity.h.b.ShaderManager;
import ru.melonity.s.a.Shader;
import ru.melonity.s.b.ShaderProgramWrapper;
import ru.melonity.w.RenderSystemUtils;
import ru.melonity.w.ShaderUniforms;

@Environment(EnvType.CLIENT)
public class RenderHelper {
    private float scaleFactor;
    private float alphaModifier = 1.0f;
    private final Map<String, ShaderProgramWrapper> shaderCache = new HashMap<>();
    private final MinecraftClient minecraftClient = MinecraftClient.getInstance();
    private Window window = this.minecraftClient.getWindow();
    private Framebuffer framebuffer;
    public static int obfuscationGuard = 1796804488;

    public float getScaleFactor() {
        this.scaleFactor = (float) this.window.getScaleFactor();
        return 2.0f / this.scaleFactor;
    }

    public static Color adjustAlpha(Color color, int alpha) {
        int originalAlpha = color.getAlpha();
        if (alpha == originalAlpha) {
            return color;
        }
        alpha = MathHelper.clamp(alpha, 0, 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public void drawRect(float x, float y, float width, float height, Color color, MatrixStack matrixStack) {
        Color adjustedColor = adjustAlpha(color, MathHelper.clamp((int) ((float) color.getAlpha() * this.alphaModifier), 0, 255));
        RenderSystemUtils.run(() -> {
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x, y, 0.0f).color(adjustedColor.getRed(), adjustedColor.getGreen(), adjustedColor.getBlue(), adjustedColor.getAlpha()).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x, y + height, 0.0f).color(adjustedColor.getRed(), adjustedColor.getGreen(), adjustedColor.getBlue(), adjustedColor.getAlpha()).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x + width, y + height, 0.0f).color(adjustedColor.getRed(), adjustedColor.getGreen(), adjustedColor.getBlue(), adjustedColor.getAlpha()).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x + width, y, 0.0f).color(adjustedColor.getRed(), adjustedColor.getGreen(), adjustedColor.getBlue(), adjustedColor.getAlpha()).next();
            BufferRenderer.draw(builtBuffer);
        });
    }

    public void drawLine(float x1, float y1, float x2, float y2, float width, Color color) {
        Color adjustedColor = adjustAlpha(color, MathHelper.clamp((int) ((float) color.getAlpha() * this.alphaModifier), 0, 255));
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        RenderSystem.lineWidth(width);
        RenderSystemUtils.run(() -> {
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
            builtBuffer.vertex(x1, y1, 0.0f).color(adjustedColor.getRGB()).next();
            builtBuffer.vertex(x2, y2, 0.0f).color(adjustedColor.getRGB()).next();
            BufferRenderer.draw(builtBuffer);
        });
        RenderSystem.lineWidth(1.0f);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public void drawRectOutline(float x, float y, float width, float height, Color color, MatrixStack matrixStack) {
        this.drawRect(x, y, width, height, 0.0f, color, matrixStack);
    }

    public void drawRoundedRect(float x, float y, float width, float height, float radius, Color color, MatrixStack matrixStack) {
        if (radius <= 0.0f) {
            this.drawRect(x, y, width, height, color, matrixStack);
            return;
        }
        int alpha = MathHelper.clamp((int) ((float) color.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedColor = adjustAlpha(color, alpha);
        float scaledX = x * this.getScaleFactor();
        float scaledY = y * this.getScaleFactor();
        float scaledWidth = width * this.getScaleFactor();
        float scaledHeight = height * this.getScaleFactor();
        ShaderProgramWrapper shader = this.getShader("round_rect");
        if (shader == null) {
            return;
        }
        shader.bind();
        float windowScale = (float) this.window.getScaleFactor();
        shader.setUniform("location", scaledX * windowScale, (float) this.window.getHeight() - scaledHeight * windowScale - scaledY * windowScale);
        shader.setUniform("rectSize", scaledWidth * windowScale, scaledHeight * windowScale);
        shader.setUniform("radius", radius);
        shader.setUniform("color", adjustedColor);
        this.drawTexturedRect(scaledX, scaledY, scaledWidth, scaledHeight, matrixStack);
        shader.unbind();
    }

    public void drawColorPicker(float x, float y, float width, float height, float radius, float hue, MatrixStack matrixStack) {
        ShaderProgramWrapper shader = this.getShader("color_picker");
        if (shader == null) {
            return;
        }
        shader.bind();
        float windowScale = (float) this.window.getScaleFactor();
        shader.setUniform("location", x * windowScale, (float) this.window.getHeight() - height * windowScale - y * windowScale);
        shader.setUniform("rectSize", width * windowScale, height * windowScale);
        shader.setUniform("radius", radius);
        shader.setUniform("hue", hue);
        this.drawTexturedRect(x, y, width, height, matrixStack);
        shader.unbind();
    }

    private void drawTexturedRect(float x, float y, float width, float height, MatrixStack matrixStack) {
        RenderSystemUtils.run(() -> {
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x, y, 0.0f).texture(0.0f, 0.0f).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x, y + height, 极0.0f).texture(0.0f, 1.0f).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x + width, y + height, 0.0f).texture(1.0f, 1.0f).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x + width, y, 0.0f).texture(1.0f, 0.0f).next();
            BufferRenderer.draw(builtBuffer);
        });
    }

    private ShaderProgramWrapper getShader(String name) {
        boolean contains = this.shaderCache.containsKey(name);
        if (contains) {
            ShaderProgramWrapper shader = this.shaderCache.get(name);
            return shader;
        }
        ShaderManager shaderManager = Melonity.getInstance().getShaderManager();
        Optional<ShaderProgramWrapper> optional = shaderManager.getShader(name);
        Optional<ShaderProgramWrapper> optionalWrapper = optional;
        boolean empty = optionalWrapper.isEmpty();
        if (empty) {
            return null;
        }
        ShaderProgramWrapper shader = optionalWrapper.get();
        this.shaderCache.put(name, shader);
        return shader;
    }

    public void drawRoundedRectOutline(float x, float y, float width, float height, float radius, float thickness, Color color, MatrixStack matrixStack) {
        int alpha = MathHelper.clamp((int) ((float) color.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedColor = adjustAlpha(color, alpha);
        float scaledX = x * this.getScaleFactor();
        float scaledY = y * this.getScaleFactor();
        float scaledWidth = width * this.getScaleFactor();
        float scaledHeight = height * this.getScaleFactor();
        ShaderProgramWrapper shader = this.getShader("round_rect_outline");
        if (shader == null) {
            return;
        }
        shader.bind();
        float windowScale = (float) this.window.getScaleFactor();
        shader.setUniform("location", scaledX * windowScale, (float) this.window.getHeight() - scaledHeight * windowScale - scaledY * windowScale);
        shader.setUniform("rectSize", scaledWidth * windowScale, scaledHeight * windowScale);
        shader.setUniform("radius", radius);
        shader.setUniform("thickness", thickness);
        shader.setUniform("color", new Color(0, 0, 0, 0));
        shader.setUniform("outlineColor", adjustedColor);
        RenderSystemUtils.run(() -> {
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX - thickness, scaledY - (1.0f + thickness), 0.0f).texture(0.0f, 0.0f).next();
            float yPos = scaledY - (1.0f + thickness) + scaledHeight + (2.0f + thickness * 2.0f);
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX - thickness, yPos, 0.0f).texture(0.0f, 1.0f).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX - thickness + scaledWidth + thickness * 2.0f, yPos, 0.0f).texture(1.0f, 1.0f).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX - thickness + scaledWidth + thickness * 2.0f, scaledY - (1.0f + thickness), 0.0f).texture(1.0f, 0.0f).next();
            BufferRenderer.draw(builtBuffer);
        });
        shader.unbind();
    }

    public void drawOuterShadow(float x, float y, float width, float height, float radius, float corners, Color color, MatrixStack matrixStack) {
        int alpha = MathHelper.clamp((int) ((float) color.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedColor = adjustAlpha(color, alpha);
        ShaderProgramWrapper shader = this.getShader("outer_shadow");
        if (shader == null) {
            return;
        }
        shader.bind();
        float windowScale = (float) this.window.getScaleFactor();
        shader.setUniform("location", x * windowScale, (float) this.window.getHeight() - height * windowScale - y * window极Scale);
        shader.setUniform("size", width * windowScale, height * windowScale);
        shader.setUniform("radius", radius);
        shader.setUniform("corners", corners, corners, corners, corners);
        shader.setUniform("color", adjustedColor);
        this.drawTexturedRect(x, y, width, height, matrixStack);
        shader.unbind();
    }

    public void drawRoundedTexture(Identifier texture, float x, float y, float width, float height, float radius, float u, float v, float w, float h, Color color, MatrixStack matrixStack) {
        float scaledX = x * this.getScaleFactor();
        float scaledY = y * this.getScaleFactor();
        float scaledWidth = width * this.getScaleFactor();
        float scaledHeight = height * this.getScaleFactor();
        ShaderProgramWrapper shader = this.getShader("round_texture");
        if (shader == null) {
            return;
        }
        shader.bind();
        float windowScale = (float) this.window.getScaleFactor();
        shader.setUniform("location", scaledX * windowScale, (float) this.window.getHeight() - scaledHeight * windowScale - scaledY * windowScale);
        shader.setUniform("rectSize", scaledWidth * windowScale, scaledHeight * windowScale);
        shader.setUniform("radius", radius);
        shader.setUniform("u", u);
        shader.setUniform("v", v);
        shader.setUniform("w", w);
        shader.setUniform("h", h);
        shader.setUniform("tex", 0);
        shader.setUniform("alpha", MathHelper.clamp((float) ((float) color.getAlpha() * this.alphaModifier / 255.0f), 0.0f, 1.0f));
        this.drawTexture(this.minecraftClient.getTextureManager().getTexture(texture).getGlId(), scaledX, scaledY, scaledWidth, scaledHeight, Color.WHITE, matrixStack);
        shader.unbind();
    }

    public void drawGradientRect(float x, float y, float width, float height, float radius, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft, MatrixStack matrixStack) {
        Color adjustedTopLeft = adjustAlpha(topLeft, MathHelper.clamp((int) ((float) topLeft.getAlpha() * this.alphaModifier), 0, 255));
        Color adjustedTopRight = adjustAlpha(topRight, MathHelper.clamp((int) ((float) topRight.getAlpha() * this.alphaModifier), 0, 255));
        Color adjustedBottomRight = adjustAlpha(bottomRight, MathHelper.clamp((int) ((float) bottomRight.getAlpha() * this.alphaModifier), 0, 255));
        Color adjustedBottomLeft = adjustAlpha(bottomLeft, MathHelper.clamp((int) ((float) bottomLeft.getAlpha() * this.alphaModifier), 0, 255));
        float scaledX = x * this.getScaleFactor();
        float scaledY = y * this.getScaleFactor();
        float scaledWidth = width * this.getScaleFactor();
        float scaledHeight = height * this.getScaleFactor();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystemUtils.run(() -> {
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX, scaledY, 0.0f).color(adjustedTopLeft.getRGB()).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX, scaledY + scaledHeight, 0.0f).color(adjustedBottomLeft.getRGB()).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX + scaledWidth, scaledY + scaledHeight, 0.0f).color(adjustedBottomRight.getRGB()).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX + scaledWidth, scaledY, 0.0f).color(adjustedTopRight.getRGB()).next();
            BufferRenderer.draw(builtBuffer);
        });
    }

    public void drawGradientRoundedRect(float x, float y, float width, float height, float radius, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft, MatrixStack matrixStack) {
        if (radius <= 0.0f) {
            this.drawGradientRect(x, y, width, height, radius, topLeft, topRight, bottomRight, bottomLeft, matrixStack);
            return;
        }
        int topLeftAlpha = MathHelper.clamp((int) ((float) topLeft.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedTopLeft = adjustAlpha(topLeft, topLeftAlpha);
        int topRightAlpha = MathHelper.clamp((int) ((float) topRight.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedTopRight = adjustAlpha(topRight, topRightAlpha);
        int bottomRightAlpha = MathHelper.clamp((int) ((float) bottomRight.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedBottomRight = adjustAlpha(bottomRight, bottomRightAlpha);
        int bottomLeftAlpha = MathHelper.clamp((int) ((float) bottomLeft.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedBottomLeft = adjustAlpha(bottomLeft, bottomLeftAlpha);
        float scaledX = x * this.getScaleFactor();
        float scaledY = y * this.getScaleFactor();
        float scaledWidth = width * this.getScaleFactor();
        float scaledHeight = height * this.getScaleFactor();
        ShaderProgramWrapper shader = this.getShader("round_rect_gradient");
        if (shader == null) {
            return;
        }
        shader.bind();
        float windowScale = (float) this.window.getScaleFactor();
        shader.setUniform("location", scaledX * windowScale, (float) this.window.getHeight() - scaledHeight * windowScale - scaledY * windowScale);
        shader.setUniform("rectSize", scaledWidth * windowScale, scaledHeight * windowScale);
        shader.setUniform("radius", radius);
        shader.setUniform("color1", adjustedTopLeft);
        shader.setUniform("color2", adjustedTopRight);
        shader.setUniform("color3", adjustedBottomRight);
        shader.setUniform("color4", adjustedBottomLeft);
        this.drawTexturedRect(scaledX, scaledY, scaledWidth, scaledHeight, matrixStack);
        shader.unbind();
    }

    public void drawGradientRoundedRectCorners(float x, float y, float width, float height, float topLeftRadius, float topRightRadius, float bottomRightRadius, float bottomLeftRadius, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft, MatrixStack matrixStack) {
        int topLeftAlpha = MathHelper.clamp((int) ((float) topLeft.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedTopLeft = adjustAlpha(topLeft, topLeftAlpha);
        int topRightAlpha = MathHelper.clamp((int) ((float) topRight.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedTopRight = adjustAlpha(topRight, topRightAlpha);
        int bottomRightAlpha = MathHelper.clamp((int) ((float) bottomRight.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedBottomRight = adjustAlpha(bottomRight, bottomRightAlpha);
        int bottomLeftAlpha = MathHelper.clamp((int) ((float) bottomLeft.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedBottomLeft = adjustAlpha(bottomLeft, bottomLeftAlpha);
        ShaderProgramWrapper shader = this.getShader("round_rect_gradient_corners");
        if (shader == null) {
            return;
        }
        shader.bind();
        float windowScale = (float) this.window.getScaleFactor();
        shader.setUniform("location", x * windowScale, (float) this.window.getHeight() - height * windowScale - y * windowScale);
        shader.setUniform("size", width * windowScale, height * windowScale);
        shader.setUniform("round", topRightRadius, topLeftRadius, bottomLeftRadius, bottomRightRadius);
        shader.setUniform("color1", adjustedTopLeft);
        shader.setUniform("color2", adjustedTopRight);
        shader.setUniform("color3", adjustedBottomRight);
        shader.setUniform("color4", adjustedBottomLeft);
        this.drawTexturedRect(x, y, width, height, matrixStack);
        shader.unbind();
    }

    public void drawHueBar(float x, float y, float width, float height, float radius, MatrixStack matrixStack) {
        ShaderProgramWrapper shader = this.getShader("hue_bar");
        if (shader == null) {
            return;
        }
        shader.bind();
        float windowScale = (float) this.window.getScaleFactor();
        shader.setUniform("location", x * windowScale, (float) this.window.getHeight() - height * windowScale - y * windowScale);
        shader.setUniform("rectSize", width * windowScale, height * windowScale);
        shader.setUniform("radius", radius);
        this.drawTexturedRect(x, y, width, height, matrixStack);
        shader.unbind();
    }

    public void drawCircle(float x, float y, float startAngle, float endAngle, float radius, float lineWidth, boolean filled, Color color, MatrixStack matrixStack) {
        int alpha = MathHelper.clamp((int) ((float) color.getAlpha() * this.alphaModifier), 0, 255);
        Color adjustedColor = adjustAlpha(color, alpha);
        float scaledX = x * this.getScaleFactor();
        float scaledY = y * this.getScaleFactor();
        float scaledRadius = radius * this.getScaleFactor();
        ShaderProgramWrapper shader = this.getShader("circle_r");
        if (shader == null) {
            return;
        }
        shader.bind();
        double windowScale = this.window.getScaleFactor();
        float windowScaleFactor = (float) windowScale;
        shader.setUniform("rectSize", scaledRadius * 2.0f * windowScaleFactor, scaledRadius * 2.0f * windowScaleFactor);
        int windowHeight = this.window.getHeight();
        shader.setUniform("center", scaledX * windowScaleFactor, (float) windowHeight - scaledY * windowScaleFactor);
        shader.setUniform("radius", scaledRadius);
        shader.setUniform("startAngle", startAngle);
        shader.setUniform("endAngle", endAngle);
        shader.setUniform("lineWidth", lineWidth / 3.0f);
        shader.setUniform("filled", filled ? 1 : 0);
        shader.setUniform("color", adjustedColor);
        RenderSystemUtils.run(() -> {
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX - scaledRadius * 2.0f, scaledY - scaledRadius * 2.0f, 0.0f).texture(0.0f, 0.0f).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX - scaledRadius * 2.0f, scaledY + scaledRadius * 2.0f, 0.0f).texture(0.0f, 1.0f).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX + scaledRadius * 2.0f, scaledY + scaledRadius * 2.0f, 0.0f).texture(1.0f, 1.0f).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), scaledX + scaledRadius * 2.0f, scaledY - scaledRadius * 2.0f, 0.0f).texture(1.0f, 0.0f).next();
            BufferRenderer.draw(builtBuffer);
        });
        shader.unbind();
    }

    public void drawTexture(Identifier texture, float x, float y, float width, float height, MatrixStack matrixStack) {
        this.drawTexture(texture, x, y, width, height, Color.WHITE, matrixStack);
    }

    public void drawTexture(int textureId, float x, float y, float width, float height, MatrixStack matrixStack) {
        Color adjustedColor = adjustAlpha(Color.WHITE, MathHelper.clamp((int) ((float) Color.WHITE.getAlpha() * this.alphaModifier), 0, 255));
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager._bindTexture(textureId);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        RenderSystem.setShaderTexture(0, textureId);
        RenderSystemUtils.run(() -> {
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x, y, 0.0f).color(adjustedColor.getRGB()).texture(0.0f, 1.0f).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x, y + height, 0.0f).color(adjustedColor.getRGB()).texture(0.0f, 0.0f).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x + width, y + height, 0.0f).color(adjustedColor.getRGB()).texture(1.0f, 0.0f).next();
            builtBuffer.vertex(matrixStack.peek().getPositionMatrix(), x + width, y, 0.0f).color(adjustedColor.getRGB()).texture(1.0f, 1.0f).next();
            BufferRenderer.draw(builtBuffer);
        });
    }

    public void drawBlurRoundedRect(float x, float y, float width, float height, float cornerRadius, float blurRadius, MatrixStack matrixStack) {
        if (blurRadius <= 0.0f || cornerRadius <= 0.0f) {
            return;
        }
        Window window = this.minecraftClient.getWindow();
        Framebuffer framebuffer = this.minecraftClient.getFramebuffer();
        framebuffer.beginWrite(false);
        int windowWidth = window.getWidth();
        int maxWidth = Math.max(windowWidth, 10);
        int windowHeight = window.getHeight();
        int maxHeight = Math.max(windowHeight, 10);
        if (framebuffer != null && (framebuffer.textureWidth != maxWidth || framebuffer.textureHeight != maxHeight)) {
            int framebufferWidth = window.getFramebufferWidth();
            int framebufferHeight = window.getFramebufferHeight();
            framebuffer.resize(framebufferWidth, framebufferHeight, false);
        }
        if (framebuffer == null) {
            NativeImageBackedTexture texture = new NativeImageBackedTexture(maxWidth, maxHeight, false);
            framebuffer = texture;
        }
        float scaledX = x * this.getScaleFactor();
        float scaledY = y * this.getScaleFactor();
        float scaledWidth = width * this.getScaleFactor();
        float scaledHeight = height * this.getScaleFactor();
        ShaderProgramWrapper shader = this.getShader("blur_round");
        if (shader == null) {
            return;
        }
        shader.bind();
        float windowScale = (float) window.getScaleFactor();
        shader.setUniform("location", scaledX * windowScale, (float) window.getHeight() - scaledHeight * windowScale - scaledY * windowScale);
        shader.setUniform("rectSize", scaledWidth * windowScale, scaledHeight * windowScale);
        shader.setUniform("cornerRadius", cornerRadius);
        shader.setUniform("blurRadius", blurRadius);
        shader.setUniform("textureSize", (float) this.minecraftClient.getFramebuffer().textureWidth, (float) this.minecraftClient.getFramebuffer().textureHeight);
        shader.setUniform("color", 1.0f, 1.0f, 1.0f, 1.0f);
        shader.setUniform("weights", ShaderUniforms.getGaussianWeights(blurRadius));
        this.drawTexturedRect(x, y, width, height, matrixStack);
        shader.unbind();
        GlStateManager._bindTexture(0);
    }

    private Framebuffer getFramebuffer(Framebuffer framebuffer) {
        Window window = this.minecraftClient.getWindow();
        int windowWidth = window.getWidth();
        int maxWidth = Math.max(windowWidth, 10);
        int windowHeight = window.getHeight();
        int maxHeight = Math.max(windowHeight, 10);
        if (framebuffer != null && (framebuffer.textureWidth != maxWidth || framebuffer.textureHeight != maxHeight)) {
            int framebufferWidth = window.getFramebufferWidth();
            int framebufferHeight = window.getFramebufferHeight();
            framebuffer.resize(framebufferWidth, framebufferHeight, false);
        }
        if (framebuffer == null) {
            NativeImageBackedTexture texture = new NativeImageBackedTexture(maxWidth, maxHeight, false);
            framebuffer = texture;
        }
        return framebuffer;
    }

    public void drawText(TextRenderer textRenderer, String text, float x, float y, MatrixStack matrixStack) {
        this.drawText(textRenderer, text, x, y, Color.WHITE, matrixStack);
    }

    public void drawText(TextRenderer textRenderer, String text, float x, float y, Color color, MatrixStack matrixStack) {
        this.drawText(textRenderer, text, x, y, color, false, matrixStack);
    }

    public void drawText(TextRenderer textRenderer, String text, float x, float y, Color color, boolean shadow, MatrixStack matrixStack) {
        if (text == null) {
            return;
        }
        ru.melonity.h.c.ModuleManager moduleManager = Melonity.getInstance().getModuleManager();
        if (moduleManager != null) {
            ru.melonity.h.c.ModuleManager manager = Melonity.getInstance().getModuleManager();
            Optional<ru.melonity.h.b.NameProtect> optional = manager.getModule(NameProtect.class);
            if (optional.isPresent()) {
                NameProtect nameProtect = optional.get();
                if (nameProtect.isEnabled() && this.minecraftClient.player != null) {
                    Text playerName = this.minecraftClient.player.getName();
                    String playerNameString = playerName.getString();
                    boolean contains = text.contains(playerNameString);
                    if (contains) {
                        String replacedText = text.replace(playerNameString, "Melonity");
                        text = replacedText;
                    }
                }
            }
        }
        Color adjustedColor = adjustAlpha(color, MathHelper.clamp((int) ((float) color.getAlpha() * this.alphaModifier), 0, 255));
        textRenderer.draw(matrixStack, text, (double) x, y, adjustedColor.getRGB());
    }

    public void drawEntityHead(Entity entity, float x, float y, float width, float height, float scale, MatrixStack matrixStack) {
        if (entity == null) {
            return;
        }
        EntityRenderDispatcher dispatcher = this.minecraftClient.getEntityRenderDispatcher();
        EntityRenderer<?> renderer = dispatcher.getRenderer(entity);
        Identifier texture = renderer.getTexture(entity);
        if (entity instanceof PlayerEntity) {
            ClientPlayerInteractionManager interactionManager = this.minecraftClient.interactionManager;
            if (interactionManager != null) {
                UUID uuid = entity.getUuid();
                PlayerListEntry entry = interactionManager.getPlayerListEntry(uuid);
                if (entry != null) {
                    GameProfile profile = entry.getProfile();
                    if (profile != null) {
                        texture = profile.getSkinTexture();
                    }
                }
            }
        }
        if (texture == null) {
            texture = new Identifier("melonity/images/generic/default_head.png");
        }
        float uvScale = 0.015625f;
        this.drawRoundedTexture(texture, x, y, width, height, scale, uvScale * 8.0f, uvScale * 8.0f, 0.125f, 0.125f, Color.WHITE, matrixStack);
    }

    @Generated
    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    @Generated
    public void setAlphaModifier(float alphaModifier) {
        this.alphaModifier = alphaModifier;
    }

    @Generated
    public void setWindow(Window window) {
        this.window = window;
    }

    @Generated
    public void setFramebuffer(Framebuffer framebuffer) {
        this.framebuffer = framebuffer;
    }

    @Generated
    public float getScaleFactorField() {
        return this.scaleFactor;
    }

    @Generated
    public float getAlphaModifierField() {
        return this.alphaModifier;
    }

    @Generated
    public Map<String, ShaderProgramWrapper> getShaderCache() {
        return this.shaderCache;
    }

    @Generated
    public MinecraftClient getMinecraftClient() {
        return this.minecraftClient;
    }

    @Generated
    public Window getWindow() {
        return this.window;
    }

    @Generated
    public Framebuffer getFramebuffer() {
        return this.framebuffer;
    }
}