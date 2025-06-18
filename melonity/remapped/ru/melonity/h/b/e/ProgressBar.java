// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.e;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.h.b.e.ProgressSupplier;
import ru.melonity.s.e.GuiElement;

@Environment(EnvType.CLIENT)
public class ProgressBar extends GuiElement {
    private final ProgressSupplier progressSupplier;
    private class_2960 texture;
    private Color color;
    private final boolean hasGoldenEffect;
    private float smoothedProgress = 0.0f;
    private boolean animationStarted = false;
    private final FrameWeightCalculator frameWeightCalculator = FrameWeightCalculator.milliseconds(250L);

    public ProgressBar(ProgressSupplier progressSupplier, class_2960 texture, Color color, boolean hasGoldenEffect) {
        super(71.0f, 10.0f);
        this.progressSupplier = progressSupplier;
        this.texture = texture;
        this.color = color;
        this.hasGoldenEffect = hasGoldenEffect;
    }

    public ProgressBar(ProgressSupplier progressSupplier, class_2960 texture, Color color) {
        this(progressSupplier, texture, color, false);
    }

    @Override
    public void render(float x, float y, double mouseX, double mouseY, ru.melonity.s.c.Renderer renderer, class_4587 matrix) {
        float currentProgress = progressSupplier.get();
        if (!animationStarted) {
            smoothedProgress = currentProgress;
            animationStarted = true;
        }
        float frameWeight = frameWeightCalculator.elapsedUnits();
        smoothedProgress += (currentProgress - smoothedProgress) * 12.0f * frameWeight;
        float clampedProgress = Math.max(0.0f, Math.min(1.0f, smoothedProgress));
        smoothedProgress = clampedProgress;
        float width = clampedProgress * (getWidth() - 2.0f);
        renderer.render(texture, x, y, 9.0f, 9.0f, matrix);
        renderer.renderText(x + 12.0f, y, getTitle(), getHeight(), 8.0f, color, matrix);
        Color bgColor = Color.decode("#141414");
        renderer.renderRect(x + 12.0f + 0.5f, y + 0.5f, getWidth() - 1.0f, getHeight() - 1.0f, 8.0f, bgColor, matrix);
        renderer.renderRect(x + 12.0f + 1.0f, y + 1.0f, width, getHeight() - 2.0f, 6.0f, color, matrix);
        if (hasGoldenEffect) {
            class_2960 goldenTexture = class_2960.method_60656("melonity/images/ui/hotbar/golden_effect.png");
            renderer.renderTexture(goldenTexture, x + 12.0f + 1.0f, y + 1.0f, width, getHeight() - 2.0f, matrix);
        }
    }

    @Override
    public void onClick(float x, float y, double mouseX, double mouseY, int button) {
    }

    @Override
    public boolean onPress(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void onRelease() {
    }

    @Generated
    public void setTexture(class_2960 texture) {
        this.texture = texture;
    }

    @Generated
    public void setColor(Color color) {
        this.color = color;
    }

    @Generated
    public void setSmoothedProgress(float smoothedProgress) {
        this.smoothedProgress = smoothedProgress;
    }

    @Generated
    public void setAnimationStarted(boolean animationStarted) {
        this.animationStarted = animationStarted;
    }
}