// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s.e.a;

import java.awt.Color;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import ru.melonity.Melonity;
import ru.melonity.s.a.TextRenderer;
import ru.melonity.s.e.AbstractComponent;
import ru.melonity.w.GraphicsContext;

@Environment(value = EnvType.CLIENT)
public class CustomButton extends AbstractComponent {
    protected class_2960 texture;
    protected float textureWidth;
    protected float textureHeight;
    protected String text;
    private Color textColor = Color.WHITE;
    private Color borderColor = Color.decode("#888888");
    protected boolean isHovered = false;
    protected boolean isPressed = false;
    public static final int ICON_SIZE = 16;
    public static final int BORDER_SIZE = 4;
    public static final float ICON_SCALE = 0.5f;
    private TextRenderer fontRenderer = Melonity.getInstance().getFontRenderer();
    private float textX = 0.0f;
    private float textY = 0.0f;
    private boolean isTextCentered = false;
    private boolean isTextVisible = false;

    public CustomButton(float width, float height) {
        super(width, height);
        this.texture = null;
        this.text = null;
    }

    public CustomButton(class_2960 texture, float width, float height, float textureWidth, float textureHeight) {
        super(width, height);
        this.texture = texture;
        this.text = null;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public CustomButton(class_2960 texture, String text, float width, float height, float textureWidth, float textureHeight) {
        super(width, height);
        this.texture = texture;
        this.text = text;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public CustomButton(String text, float width, float height) {
        super(width, height);
        this.texture = null;
        this.text = text;
    }

    @Override
    public void render(float x, float y, double mouseX, double mouseY, GraphicsContext context, class_4587 matrix) {
        if (!this.enabled) {
            this.isHovered = false;
            return;
        }

        this.isHovered = isMouseOver(x, y, this.width, this.height, mouseX, mouseY);

        Color[] colors = context.getColors();
        Color backgroundColor = colors[0];
        Color borderColor = colors[1];
        context.drawRect(x, y, this.width, this.height, 6.0f, backgroundColor, matrix);

        if (this.isHovered) {
            Color hoverColor = colors[1];
            context.drawRect(x + 0.5f, y + 0.5f, this.width - 1.0f, this.height - 1.0f, 6.0f, hoverColor, matrix);
        } else {
            Color idleColor = colors[0];
            context.drawRect(x + 0.5f, y + 0.5f, this.width - 1.0f, this.height - 1.0f, 6.0f, idleColor, matrix);
        }

        Color innerBorderColor = colors[1];
        context.drawRect(x + 0.5f, y + this.height - 2.0f, this.width - 1.0f, 2.0f, 6.0f, innerBorderColor, matrix);

        float offset = 4.0f;
        if (this.texture != null) {
            if (this.isTextCentered) {
                float centerX = x + this.width / 2.0f;
                float textWidth = this.fontRenderer.getTextWidth(this.text);
                float centeredX = centerX - textWidth / 2.0f - this.textureWidth - 2.0f;
                float centeredY = y + this.height / 2.0f - this.textureHeight / 2.0f;
                context.drawTexture(this.texture, centeredX, centeredY, this.textureWidth, this.textureHeight, this.textColor, matrix);
            } else {
                context.drawTexture(this.texture, x + offset + this.textX, y + this.height / 2.0f - this.textureHeight / 2.0f, this.textureWidth, this.textureHeight, this.textColor, matrix);
            }
            offset += 10.0f;
        }
        if (this.text != null) {
            if (this.isTextVisible) {
                float centerX = x + this.textX + this.width / 2.0f;
                float textWidth = this.fontRenderer.getTextWidth(this.text);
                float centeredX = centerX - textWidth / 2.0f;
                float textHeight = this.fontRenderer.getTextHeight(this.text);
                float centeredY = y + 2.0f + this.height / 2.0f;
                context.drawText(this.text, centeredX, centeredY - textHeight / 2.0f, this.textColor, matrix);
            } else {
                float centeredY = y + this.height / 2.0f;
                float textHeight = this.fontRenderer.getTextHeight(this.text);
                context.drawText(this.text, x + offset + this.textX, centeredY - textHeight / 2.0f + 2.0f, this.textColor, matrix);
            }
        }

        if (isMouseOver(x, y, this.width, this.height, mouseX, mouseY)) {
            Melonity.setHoverStatus(true);
        }
    }

    @Override
    public void mouseClicked(float x, float y, double mouseX, double mouseY, int button) {
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public void darkModeToggle() {
    }

    @Generated
    public class_2960 getTexture() {
        return this.texture;
    }

    @Generated
    public float getTextureWidth() {
        return this.textureWidth;
    }

    @Generated
    public float getTextureHeight() {
        return this.textureHeight;
    }

    @Generated
    public String getText() {
        return this.text;
    }

    @Generated
    public Color getTextColor() {
        return this.textColor;
    }

    @Generated
    public Color getBorderColor() {
        return this.borderColor;
    }

    @Generated
    public boolean isHovered() {
        return this.isHovered;
    }

    @Generated
    public boolean isPressed() {
        return this.isPressed;
    }

    @Generated
    public TextRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    @Generated
    public float getTextX() {
        return this.textX;
    }

    @Generated
    public float getTextY() {
        return this.textY;
    }

    @Generated
    public boolean isTextCentered() {
        return this.isTextCentered;
    }

    @Generated
    public boolean isTextVisible() {
        return this.isTextVisible;
    }

    @Generated
    public void setTexture(class_2960 texture) {
        this.texture = texture;
    }

    @Generated
    public void setTextureWidth(float textureWidth) {
        this.textureWidth = textureWidth;
    }

    @Generated
    public void setTextureHeight(float textureHeight) {
        this.textureHeight = textureHeight;
    }

    @Generated
    public void setText(String text) {
        this.text = text;
    }

    @Generated
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    @Generated
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    @Generated
    public void setHovered(boolean isHovered) {
        this.isHovered = isHovered;
    }

    @Generated
    public void setPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    @Generated
    public void setFontRenderer(TextRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    @Generated
    public void setTextX(float textX) {
        this.textX = textX;
    }

    @Generated
    public void setTextY(float textY) {
        this.textY = textY;
    }

    @Generated
    public void setTextCentered(boolean isTextCentered) {
        this.isTextCentered = isTextCentered;
    }

    @Generated
    public void setTextVisible(boolean isTextVisible) {
        this.isTextVisible = isTextVisible;
    }
}