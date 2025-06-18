// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b;

import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.o.a.RenderHelper;
import ru.melonity.o.b.NavbarIcon;
import ru.melonity.s.e.GuiElement;
import ru.melonity.s.c.UIController;
import ru.melonity.w.InteractionHelper;

@Environment(EnvType.CLIENT)
public class NavbarRenderer extends GuiElement {
    private final List<NavbarIcon> icons;
    private final FrameWeightCalculator safeIconWeight = FrameWeightCalculator.milliseconds(400L);
    private final StateAnimation safeIconAnimation = new StateAnimation();
    private final FrameWeightCalculator keybindsIconWeight = FrameWeightCalculator.milliseconds(400L);
    private final StateAnimation keybindsIconAnimation = new StateAnimation();
    private final FrameWeightCalculator miscIconWeight = FrameWeightCalculator.milliseconds(400L);
    private final StateAnimation miscIconAnimation = new StateAnimation();

    public NavbarRenderer(float width) {
        super(width, 36.0f);
        this.icons = Arrays.asList(
            new NavbarIcon(new Identifier("melonity/images/ui/icons/navbar/safe.png"), 16.0f, 16.0f, 8.0f, 8.0f),
            new NavbarIcon(new Identifier("melonity/images/ui/icons/navbar/keybinds.png"), 16.0f, 16.0f, 8.0f, 8.0f),
            new NavbarIcon(new Identifier("melonity/images/ui/icons/navbar/misc.png"), 16.0f, 16.0f, 8.0f, 8.0f)
        );
    }

    @Override
    public void render(RenderHelper renderHelper, float x, float y, double mouseX, double mouseY, UIController uiController, MatrixStack matrixStack) {
        UIController mainUIController = Melonity.getUIController();
        UIController.SafeIconController safeIconController = mainUIController.getSafeIconController();
        boolean isSafeActive = safeIconController.isActive();
        this.safeIconAnimation.setState(isSafeActive);
        boolean isKeybindsActive = Melonity.isKeybindsActive();
        this.keybindsIconAnimation.setState(isKeybindsActive);
        UIController miscUIController = Melonity.getUIController();
        boolean isMiscActive = miscUIController.isMiscActive();
        this.miscIconAnimation.setState(isMiscActive);
        float safeAnimationProgress = this.safeIconWeight.elapsedUnits();
        this.safeIconAnimation.update(safeAnimationProgress);
        float keybindsAnimationProgress = this.keybindsIconWeight.elapsedUnits();
        this.keybindsIconAnimation.update(keybindsAnimationProgress);
        float miscAnimationProgress = this.miscIconWeight.elapsedUnits();
        this.miscIconAnimation.update(miscAnimationProgress);
        float navbarWidth = this.getWidth();
        float navbarHeight = this.getHeight();
        Color[] backgroundColor = renderHelper.getBackgroundColors();
        Color topLeftColor = backgroundColor == null ? Color.decode("#181818") : backgroundColor[0];
        Color topRightColor = backgroundColor == null ? Color.decode("#181818") : backgroundColor[1];
        Color bottomLeftColor = backgroundColor == null ? Color.decode("#181818") : backgroundColor[1];
        Color bottomRightColor = backgroundColor == null ? Color.decode("#181818") : backgroundColor[0];
        renderHelper.drawBackgroundQuad(x, y, navbarWidth, navbarHeight, 0.0f, topLeftColor, topRightColor, bottomLeftColor, bottomRightColor, matrixStack);
        boolean isModernStyle = renderHelper.isModernStyle();
        if (isModernStyle) {
            Identifier navbarBlocksTexture = new Identifier("melonity/images/ui/navbar/navbar_blocks.png");
            Color blocksColor = renderHelper.getBlocksColor();
            renderHelper.drawTexture(navbarBlocksTexture, x + 35.0f, y, 341.5f, 36.0f, blocksColor, matrixStack);
        }
        Identifier overlayTexture = new Identifier("melonity/images/ui/overlay_blocks.png");
        Color overlayColor = backgroundColor == null ? Color.decode("#181818") : backgroundColor[0];
        renderHelper.drawTexture(overlayTexture, x, y - 17.0f + 4.0f, navbarWidth, 17.0f, overlayColor, matrixStack);
        float dividerY = y + navbarHeight;
        renderHelper.drawDividerLine(x, dividerY, navbarWidth, 0.5f, Color.decode("#222222"), matrixStack);
        Identifier navbarLogo = new Identifier("melonity/images/ui/navbar/logo.png");
        Color logoColor = renderHelper.getLogoColor();
        renderHelper.drawTexture(navbarLogo, x + 8.0f, y + 4.0f, 26.5f, 29.0f, logoColor, matrixStack);
        float drawX = navbarWidth - 8.0f;
        for (NavbarIcon icon : this.icons) {
            drawX -= icon.getWidth() + 4.0f;
        }
        Iterator<NavbarIcon> iconIterator = this.icons.iterator();
        while (iconIterator.hasNext()) {
            NavbarIcon icon = iconIterator.next();
            double iconRenderX = x + drawX;
            double iconRenderY = y + 9.0f;
            float iconWidth = icon.getWidth();
            float iconHeight = icon.getHeight();
            boolean isHovered = InteractionHelper.isMouseOver(iconRenderX, iconRenderY, iconWidth, iconHeight, mouseX, mouseY);
            icon.setHovered(isHovered);
            icon.render(renderHelper, x + drawX, y + 9.0f, mouseX, mouseY, uiController, matrixStack);
            int iconIndex = this.icons.indexOf(icon);
            if (iconIndex == 2) {
                float miscIconWidth = icon.getWidth();
                float miscIconHeight = icon.getHeight();
                Color miscIconColor = renderHelper.getIconColor();
                float miscAnimState = this.safeIconAnimation.getProgress();
                int miscAlpha = MathHelper.clamp((int) (miscAnimState * 255.0f), 0, 255);
                Color miscIconAlphaColor = renderHelper.applyAlpha(miscIconColor, miscAlpha);
                renderHelper.drawIconQuad(x + drawX, y + 9.0f, miscIconWidth, miscIconHeight, 4.0f, miscIconAlphaColor, matrixStack);
                UIController miscUIControllerInstance = Melonity.getUIController();
                UIController.MiscIconController miscController = miscUIControllerInstance.getMiscIconController();
                if (miscController.isActive()) {
                    Identifier miscIconActive = new Identifier("melonity/images/ui/icons/navbar/misc_opened.png");
                    renderHelper.drawTexture(miscIconActive, x + drawX + 4.0f, y + 13.0f, 8.0f, 8.0f, Color.BLACK, matrixStack);
                }
            }
            if (iconIndex == 0) {
                float safeIconWidth = icon.getWidth();
                float safeIconHeight = icon.getHeight();
                Color safeIconColor = renderHelper.getIconColor();
                float safeAnimState = this.keybindsIconAnimation.getProgress();
                int safeAlpha = MathHelper.clamp((int) (safeAnimState * 255.0f), 0, 255);
                Color safeIconAlphaColor = renderHelper.applyAlpha(safeIconColor, safeAlpha);
                renderHelper.drawIconQuad(x + drawX, y + 9.0f, safeIconWidth, safeIconHeight, 4.0f, safeIconAlphaColor, matrixStack);
                if (Melonity.isSafeActive()) {
                    Identifier safeIconActive = new Identifier("melonity/images/ui/icons/navbar/safe_enabled.png");
                    renderHelper.drawTexture(safeIconActive, x + drawX + 4.0f, y + 13.0f, 8.0f, 8.0f, Color.BLACK, matrixStack);
                }
            }
            if (iconIndex == 1) {
                float keybindsIconWidth = icon.getWidth();
                float keybindsIconHeight = icon.getHeight();
                Color keybindsIconColor = renderHelper.getIconColor();
                float keybindsAnimState = this.miscIconAnimation.getProgress();
                int keybindsAlpha = MathHelper.clamp((int) (keybindsAnimState * 255.0f), 0, 255);
                Color keybindsIconAlphaColor = renderHelper.applyAlpha(keybindsIconColor, keybindsAlpha);
                renderHelper.drawIconQuad(x + drawX, y + 9.0f, keybindsIconWidth, keybindsIconHeight, 4.0f, keybindsIconAlphaColor, matrixStack);
                UIController keybindsUIController = Melonity.getUIController();
                if (keybindsUIController.isKeybindsActive()) {
                    Identifier keybindsIcon = new Identifier("melonity/images/ui/icons/navbar/keybinds.png");
                    renderHelper.drawTexture(keybindsIcon, x + drawX + 4.0f, y + 13.0f, 8.0f, 8.0f, Color.BLACK, matrixStack);
                }
            }
            drawX += icon.getWidth() + 4.0f;
        }
    }

    @Override
    public void onMouseDragged(float draggedX, float draggedY, double deltaX, double deltaY, int button) {
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public void onRemoved() {
    }

    @Generated
    public List<NavbarIcon> getIcons() {
        return this.icons;
    }

    @Generated
    public FrameWeightCalculator getSafeIconWeight() {
        return this.safeIconWeight;
    }

    @Generated
    public StateAnimation getSafeIconAnimation() {
        return this.safeIconAnimation;
    }

    @Generated
    public FrameWeightCalculator getKeybindsIconWeight() {
        return this.keybindsIconWeight;
    }

    @Generated
    public StateAnimation getKeybindsIconAnimation() {
        return this.keybindsIconAnimation;
    }

    @Generated
    public FrameWeightCalculator getMiscIconWeight() {
        return this.miscIconWeight;
    }

    @Generated
    public StateAnimation getMiscIconAnimation() {
        return this.miscIconAnimation;
    }
}