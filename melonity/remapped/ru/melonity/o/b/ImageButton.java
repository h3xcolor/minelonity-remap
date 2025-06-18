// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b;

import java.awt.Color;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import ru.melonity.Melonity;
import ru.melonity.o.a.ImageUtil;
import ru.melonity.s.WidgetRenderCallback;
import ru.melonity.s.e.AbstractInteractiveWidget;
import ru.melonity.w.InteractionUtil;

@Environment(value = EnvType.CLIENT)
public class ImageButton extends AbstractInteractiveWidget {
    protected final Identifier texture;
    protected float width;
    protected float height;
    protected String text;
    protected boolean isHovered = false;
    public static final int TEXT_HEIGHT = 16;
    public static final int TEXT_MARGIN = 4;
    public static final float TEXT_ALIGN = 0.5f;
    private boolean shouldCenterText = false;

    public ImageButton(float x, float y) {
        super(x, y);
        this.texture = null;
        this.text = null;
    }

    public ImageButton(Identifier texture, float x, float y, float width, float height) {
        super(x, y);
        this.texture = texture;
        this.text = null;
        this.width = width;
        this.height = height;
    }

    public ImageButton(Identifier texture, String text, float x, float y, float width, float height) {
        super(x, y);
        this.texture = texture;
        this.text = text;
        this.width = width;
        this.height = height;
    }

    public ImageButton(String text, float x, float y) {
        super(x, y);
        this.texture = null;
        this.text = text;
    }

    @Override
    public void render(WidgetRenderCallback renderCallback, float mouseX, float mouseY, double deltaX, double deltaY, MatrixStack matrices) {
        this.isHovered = InteractionUtil.isMouseWithin(mouseX, mouseY, this.x, this.y, this.widthConstraint, this.heightConstraint, deltaX, deltaY);
        Color primaryColor = renderCallback.getNormalColor();
        Color highlightedBorder = primaryColor.brighter();
        renderCallback.renderBorder(this.x, this.y, this.widthConstraint, this.heightConstraint, 6.0f, highlightedBorder, matrices);
        renderCallback.renderFill(this.x + 0.5f, this.y + 0.5f, this.widthConstraint - 1.0f, this.heightConstraint - 1.0f, 6.0f, this.isHovered ? primaryColor.brighter() : primaryColor, matrices);
        Color borderColor = renderCallback.getBorderColor();
        Color brightBorder = borderColor.brighter();
        renderCallback.renderVerticalBorder(this.x + 0.5f, this.y + this.heightConstraint - 2.0f, this.widthConstraint - 1.0f, 2.0f, 6.0f, brightBorder, matrices);
        float textureOffset = 4.0f;
        if (this.texture != null) {
            float textureY = this.y + this.heightConstraint / 2.0f - this.height / 2.0f;
            ImageUtil imageUtil = renderCallback.getImageRenderer();
            Color imageColor = imageUtil.getColor();
            renderCallback.renderImage(this.texture, this.x + textureOffset, textureY, this.width, this.height, imageColor, matrices);
            textureOffset += 10.0f;
        }
        if (this.text != null) {
            if (this.shouldCenterText) {
                float textWidth = renderCallback.getTextRenderer().getWidth(this.text);
                float centeredX = this.x + this.widthConstraint / 2.0f - textWidth / 2.0f;
                float textHeight = renderCallback.getTextRenderer().getHeight(this.text);
                float centeredY = this.y + this.heightConstraint / 2.0f - textHeight / 2.0f;
                ImageUtil textUtil = renderCallback.getTextRenderer();
                Color textColor = textUtil.getColor();
                renderCallback.renderText(renderCallback.getTextRenderer(), this.text, centeredX, centeredY, textColor, matrices);
            } else {
                float textHeight = renderCallback.getTextRenderer().getHeight(this.text);
                float textY = this.y + this.heightConstraint / 2.0f - textHeight / 2.0f;
                ImageUtil textUtil = renderCallback.getTextRenderer();
                Color textColor = textUtil.getColor();
                renderCallback.renderText(renderCallback.getTextRenderer(), this.text, this.x + textureOffset, textY, textColor, matrices);
            }
        }
        if (InteractionUtil.isMouseWithin(mouseX, mouseY, this.x, this.y, this.widthConstraint, this.heightConstraint, deltaX, deltaY)) {
            Melonity.activeElement = this;
        }
    }

    @Override
    public void onMouseDragged(float mouseX, float mouseY, double deltaX, double deltaY, int button) {
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public void onReleased() {
    }

    @Generated
    public void setWidth(float width) {
        this.width = width;
    }

    @Generated
    public void setHeight(float height) {
        this.height = height;
    }

    @Generated
    public void setText(String text) {
        this.text = text;
    }

    @Generated
    public void setHovered(boolean hovered) {
        this.isHovered = hovered;
    }

    @Generated
    public void setCenterText(boolean centerText) {
        this.shouldCenterText = centerText;
    }
}