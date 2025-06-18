// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b.c;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.m.ActionBarConfig;
import ru.melonity.s.c.RenderContext;
import ru.melonity.s.e.Widget;
import ru.melonity.w.InteractionHelper;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ActionBarWidget extends Widget {
    private final FrameWeightCalculator hoverAnimationTimer = FrameWeightCalculator.milliseconds(150L);
    private final StateAnimation hoverAnimation = new StateAnimation();
    private final FrameWeightCalculator clickAnimationTimer = FrameWeightCalculator.milliseconds(250L);
    private final StateAnimation clickAnimation = new StateAnimation();
    private ActionBarConfig config = null;
    private boolean isHovered = false;

    public ActionBarWidget(float width, float height) {
        super(width, height);
    }

    @Override
    public void renderWidget(RenderContext context, float x, float y, double mouseX, double mouseY, MatrixStack matrices) {
        Color[] colors = context.getColors();
        renderBackground(x - 0.5f, y - 0.5f, width + 1.0f, height + 2.0f, 6.0f, colors[1], matrices);
        Color[] innerColors = context.getColors();
        renderBackground(x, y, width, height, 6.0f, innerColors[0], matrices);

        if (this.config == null) {
            Identifier addIcon = new Identifier("melonity", "images/ui/add.png");
            renderTexture(addIcon, x + width / 2.0f - 5.0f, y + height / 2.0f - 4.5f, 10.0f, 10.0f, matrices);
        } else {
            float totalDuration = 0.0f;
            List<ActionBarConfig.Action> actions = this.config.getActions();
            Iterator<ActionBarConfig.Action> iterator = actions.iterator();
            while (iterator.hasNext()) {
                ActionBarConfig.Action action = iterator.next();
                totalDuration += action.getDuration();
            }
            List<ActionBarConfig.Action> actionList = this.config.getActions();
            int actionCount = actionList.size();
            Object[] formatArgs = {totalDuration / 1000.0f};
            String durationString = String.format("%.1f", formatArgs);
            String formattedDuration = durationString.replace(',', '.');
            String name = this.config.getName();
            float nameWidth = DrawableHelper.getTextWidth(name);
            renderText(DrawableHelper.TEXT_RENDERER, name, x + width / 2.0f - nameWidth / 2.0f, y + 9.0f, matrices);
            String infoText = actionCount + " actions  " + formattedDuration + " s";
            float infoWidth = DrawableHelper.getTextWidth(infoText);
            renderText(DrawableHelper.TEXT_RENDERER, infoText, x + width / 2.0f - infoWidth / 2.0f, y + 21.0f, Color.decode("#888888"), matrices);
        }
    }

    public void renderHovered(RenderContext context, float x, float y, float mouseRelX, float mouseRelY, double mouseX, double mouseY, MatrixStack matrices, boolean hovered) {
        MinecraftClient client = MinecraftClient.getInstance();
        hoverAnimation.setState(hovered);
        float elapsed = hoverAnimationTimer.elapsedUnits();
        hoverAnimation.update(elapsed);
        float animProgress = hoverAnimation.getAnimation();
        if (animProgress <= 0.0f) {
            return;
        }
        float barHeight = 22.0f;
        float rotation = animProgress * 90.0f;
        matrices.push();
        matrices.translate(mouseRelX * rotation, mouseRelY * rotation, 0.0f);
        if (rotation >= 90.0f) {
            Window window = client.getWindow();
            int screenWidth = window.getScaledWidth();
            double hoverX = (float) screenWidth / 2.0f + 30.0f - 37.0f + mouseRelX * rotation - 30.0f;
            Window windowObj = client.getWindow();
            int screenHeight = windowObj.getScaledHeight();
            isHovered = InteractionHelper.isHovered(hoverX, (float) screenHeight / 2.0f - 15.0f + mouseRelY * rotation, width, barHeight, mouseX, mouseY);
        }
        clickAnimation.setState(isHovered);
        float clickElapsed = clickAnimationTimer.elapsedUnits();
        clickAnimation.update(clickElapsed);
        Color bgColor = new Color(24, 24, 24, 140);
        renderBackground(x - 2.0f, y - 2.0f, width + 4.0f, barHeight + 4.0f, 6.0f, bgColor, matrices);
        Color baseColor = Color.decode("#181818");
        Color animatedColor = RenderContext.withAlpha(baseColor, (int) (clickAnimation.getAnimation() * 255.0f));
        renderBackground(x, y, width, barHeight, 6.0f, 5.0f, animatedColor, matrices);
        Color hoverColor = new Color(24, 24, 24, (int) (clickAnimation.getAnimation() * 210.0f));
        renderBackground(x, y, width, barHeight, 6.0f, hoverColor, matrices);
        if (this.config == null) {
            float centerX = x + width / 2.0f;
            float emptyWidth = DrawableHelper.getTextWidth("Empty");
            float textX = centerX - emptyWidth / 2.0f;
            Color textColor = new Color(255, 255, 255, 153);
            renderText(DrawableHelper.TEXT_RENDERER, "Empty", textX, y + 10.0f, textColor, matrices);
        } else {
            float totalDuration = 0.0f;
            List<ActionBarConfig.Action> actions = this.config.getActions();
            Iterator<ActionBarConfig.Action> iterator = actions.iterator();
            while (iterator.hasNext()) {
                ActionBarConfig.Action action = iterator.next();
                totalDuration += action.getDuration();
            }
            List<ActionBarConfig.Action> actionList = this.config.getActions();
            int actionCount = actionList.size();
            Object[] formatArgs = {totalDuration / 1000.0f};
            String durationString = String.format("%.1f", formatArgs);
            String formattedDuration = durationString.replace(',', '.');
            String name = this.config.getName();
            float nameWidth = DrawableHelper.getTextWidth(name);
            renderText(DrawableHelper.TEXT_RENDERER, name, x + width / 2.0f - nameWidth / 2.0f, y + 6.0f, matrices);
            String infoText = actionCount + " actions  " + formattedDuration + " s";
            float infoWidth = DrawableHelper.getTextWidth(infoText);
            renderText(DrawableHelper.TEXT_RENDERER, infoText, x + width / 2.0f - infoWidth / 2.0f, y + 16.0f, Color.decode("#888888"), matrices);
        }
        matrices.pop();
    }

    @Override
    public void onClick(float x, float y, double mouseX, double mouseY, int button) {
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void onClose() {
    }

    public StateAnimation getHoverAnimation() {
        return hoverAnimation;
    }

    public ActionBarConfig getConfig() {
        return config;
    }

    public void setConfig(ActionBarConfig config) {
        this.config = config;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }
}