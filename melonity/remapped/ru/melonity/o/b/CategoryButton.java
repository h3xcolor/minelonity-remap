// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.i.FontHelper;
import ru.melonity.o.RenderHelper;
import ru.melonity.s.a.Theme;
import ru.melonity.s.e.AbstractGuiElement;
import ru.melonity.w.Category;

import java.awt.Color;

@Environment(value = EnvType.CLIENT)
public class CategoryButton extends AbstractGuiElement {
    private final Category category;
    private final StateAnimation animation;
    private final FrameWeightCalculator frameWeightCalculator;
    private boolean expanded;
    public static int obfuscationDummy = 420250392;

    public CategoryButton(Category category) {
        super(94.0f, 22.0f);
        this.category = category;
        this.animation = new StateAnimation();
        this.frameWeightCalculator = FrameWeightCalculator.milliseconds(200L);
        this.expanded = false;
    }

    @Override
    public void render(Theme theme, float x, float y, double mouseX, double mouseY, MatrixStack matrices) {
        FontHelper fontHelper = FontHelper.INSTANCE;
        this.animation.state(this.expanded);
        float elapsedUnits = this.frameWeightCalculator.elapsedUnits();
        this.animation.update(elapsedUnits);
        boolean isMouseOver = RenderHelper.isMouseOver(x, y, this.posX, this.posY, mouseX, mouseY);
        if (isMouseOver) {
            Color backgroundColor;
            if (theme instanceof ru.melonity.o.a.b.Theme) {
                backgroundColor = Color.decode("#081C38");
            } else {
                backgroundColor = Color.decode("#181818");
            }
            RenderHelper.drawBackground(x, y, this.posX, this.posY, 8.0f, backgroundColor, matrices);
            Melonity.hovered = true;
        }
        if (this.expanded) {
            Theme.ThemeColors themeColors = theme.getThemeColors();
            Color[] colors = themeColors.getColors();
            Color primaryColor = colors[0];
            float animValue = this.animation.animation();
            int alpha = MathHelper.clamp((int) (animValue * 255.0f), 0, 255);
            Color primaryWithAlpha = RenderHelper.withAlpha(primaryColor, alpha);
            Theme.ThemeColors themeColors2 = theme.getThemeColors();
            Color[] colors2 = themeColors2.getColors();
            Color secondaryColor = colors2[1];
            Theme.ThemeColors themeColors3 = theme.getThemeColors();
            Color[] colors3 = themeColors3.getColors();
            Color tertiaryColor = colors3[1];
            Theme.ThemeColors themeColors4 = theme.getThemeColors();
            Color[] colors4 = themeColors4.getColors();
            Color accentColor = colors4[0];
            RenderHelper.drawCategoryButton(x, y, this.posX, this.posY, 8.0f, primaryWithAlpha, secondaryColor, tertiaryColor, accentColor, matrices);
        }
        String categoryName = this.category.name();
        String lowerCaseName = categoryName.toLowerCase();
        String imagePath = "melonity/images/ui/clickgui/category/" + lowerCaseName + ".png";
        Identifier textureId = new Identifier(imagePath);
        float iconY = y + this.height / 2.0f - 4.75f;
        Color iconColor;
        if (this.expanded) {
            Theme.ThemeColors themeColors5 = theme.getThemeColors();
            iconColor = themeColors5.getIconColor();
        } else {
            Theme.ThemeColors themeColors6 = theme.getThemeColors();
            iconColor = themeColors6.getTextColor();
        }
        RenderHelper.drawTexture(textureId, x + 8.0f, iconY, 9.5f, 9.5f, iconColor, matrices);
        String displayName = this.category.getDisplayName();
        float textCenterY = y + this.height / 2.0f;
        float textWidth = fontHelper.getTextWidth(displayName);
        float textY = textCenterY - textWidth / 2.0f + 3.0f;
        Color textColor;
        if (this.expanded) {
            Theme.ThemeColors themeColors7 = theme.getThemeColors();
            textColor = themeColors7.getTextColor();
        } else {
            Theme.ThemeColors themeColors8 = theme.getThemeColors();
            textColor = themeColors8.getTextColor();
        }
        fontHelper.draw(theme, displayName, x + 19.5f, textY, textColor, matrices);
    }

    @Override
    public void mouseClicked(float x, float y, double mouseX, double mouseY, int button) {
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void mouseReleased() {
    }

    public Category getCategory() {
        return this.category;
    }

    public StateAnimation getAnimation() {
        return this.animation;
    }

    public FrameWeightCalculator getFrameWeightCalculator() {
        return this.frameWeightCalculator;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}