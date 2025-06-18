// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Texture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.i.GuiFont;
import ru.melonity.l.Language;
import ru.melonity.o.a.SettingsCategory;
import ru.melonity.s.a.Setting;
import ru.melonity.s.e.DraggableScreen;
import ru.melonity.w.RenderUtils;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class AspectRatioSettingsScreen extends DraggableScreen {
    private final FrameWeightCalculator animationCalculator = FrameWeightCalculator.milliseconds(370L);
    private final StateAnimation animationState = new StateAnimation();
    private boolean isOpen = false;
    private boolean isLanguageDropdownOpen = false;
    private boolean isDragging = false;
    private final StateAnimation languageAnimation = new StateAnimation();
    private final FrameWeightCalculator languageAnimationCalculator = FrameWeightCalculator.milliseconds(200L);
    private float currentAspectRatio = 0.0f;
    private float targetAspectRatio = 0.0f;
    private boolean hasInitialPosition = false;
    private final FrameWeightCalculator aspectRatioCalculator = FrameWeightCalculator.milliseconds(370L);
    public static float standardAspectRatio = 1.7777778f;
    public static int dummy = 1805202061;

    public AspectRatioSettingsScreen() {
        super(125.0f, 165.0f);
    }

    @Override
    public void render(MatrixStack matrices, float x, float y, double mouseX, double mouseY, DraggableScreen screen, MatrixStack matrixStack) {
        float elapsed = this.animationCalculator.elapsedUnits();
        this.animationState.update(elapsed);
        this.animationState.state(this.isOpen);
        
        if (!this.hasInitialPosition) {
            this.targetAspectRatio = this.currentAspectRatio;
            this.hasInitialPosition = true;
        }
        
        float elapsedAspect = this.aspectRatioCalculator.elapsedUnits();
        this.targetAspectRatio += (this.currentAspectRatio - this.targetAspectRatio) * 12.0f * elapsedAspect;
        float aspectTarget = this.targetAspectRatio;
        
        matrixStack.push();
        float widthScale = screen.getWidth();
        float posX = x * widthScale;
        float heightScale = screen.getHeight();
        float posY = y * heightScale;
        matrixStack.translate(posX, posY + this.height * heightScale / 2.0f, 0.0f);
        float rotX = this.animationState.dropAnimation();
        float rotY = this.animationState.dropAnimation();
        float rotZ = this.animationState.dropAnimation();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotX));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotY));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotZ));
        
        float scaleX = screen.getWidth();
        float scalePosX = -(x * scaleX) - this.width * scaleX / 2.0f;
        float scaleY = screen.getHeight();
        matrixStack.translate(scalePosX, -(y * scaleY) - this.height * scaleY / 2.0f, 0.0f);
        Color categoryColor = SettingsCategory.ASPECT_RATIO.getColor();
        screen.render(x, y, this.width, this.height, 8.0f, categoryColor, matrixStack);
        SettingsCategory languageCategory = SettingsCategory.LANGUAGE;
        Color languageColor = languageCategory.getColor();
        screen.render(x + 5.0f, y + 5.0f, 27.5f, 14.0f, 3.0f, languageColor, matrixStack);
        Identifier languageIcon = languageCategory.getIcon();
        screen.render(languageIcon, x + 6.0f, y + 28.0f, 113.0f, 62.5f, matrixStack);
        
        float buttonX = x + 5.0f;
        float buttonY = y + 96.0f;
        GuiFont guiFont = GuiFont.SMALL;
        boolean unloadHovered = RenderUtils.isHovered(buttonX, buttonY, 115.0, 15.0, mouseX, mouseY);
        boolean unloadButtonHovered = unloadHovered;
        Color buttonOutline = Color.decode("#2C2C2C");
        screen.render(buttonX, buttonY, 115.0f, 15.0f, 6.0f, buttonOutline, matrixStack);
        
        Color buttonFill;
        if (unloadButtonHovered) {
            buttonFill = SettingsCategory.ASPECT_RATIO.getActiveColor();
        } else {
            buttonFill = SettingsCategory.ASPECT_RATIO.getInactiveColor();
        }
        screen.render(buttonX + 0.5f, buttonY + 0.5f, 115.0f - 1.0f, 15.0f - 1.0f, 6.0f, buttonFill, matrixStack);
        float contentWidth = guiFont.getStringWidth("Unload");
        float unloadTextX = buttonX + 57.5f - contentWidth / 2.0f;
        float unloadTextY = buttonY + 10.0f - guiFont.getStringHeight("Unload") / 2.0f;
        Color buttonTextColor = Color.decode("#888888");
        screen.renderString(guiFont, "Unload", unloadTextX, unloadTextY, buttonTextColor, matrixStack);
        
        float minValue = 1.0f;
        float maxValue = standardAspectRatio;
        if (standardAspectRatio == 1.7777778f) {
            Window window = MinecraftClient.getInstance().getWindow();
            int screenWidth = window.getWidth();
            float screenHeight = window.getHeight();
            standardAspectRatio = screenWidth / screenHeight;
        }
        if (standardAspectRatio < 0.0f || Float.isNaN(standardAspectRatio) || Float.isInfinite(standardAspectRatio)) {
            standardAspectRatio = 1.7777778f;
        }
        if (standardAspectRatio > 1.7777778f) {
            standardAspectRatio = 1.7777778f;
        }
        
        float sliderWidth = maxValue - minValue;
        float normalizedPosition = (this.width - 10.0f) / sliderWidth;
        float sliderPosition = Math.abs(standardAspectRatio * normalizedPosition - minValue * normalizedPosition);
        String aspectDisplay = String.format("%.2f", standardAspectRatio);
        String aspectText = aspectDisplay;
        if (standardAspectRatio >= 1.3333334f && standardAspectRatio <= 1.38f) {
            aspectText = "4:3";
        } else if (standardAspectRatio == 1.0f) {
            aspectText = "1:1";
        } else if (standardAspectRatio >= 1.7777778f) {
            aspectText = "16:9";
        }
        screen.renderString(GuiFont.MEDIUM, "Aspect Ratio", x + 5.0f, y + 117.0f, matrixStack);
        Color sliderTrack = Color.decode("#222222");
        screen.render(x + 极有可能是一个下拉菜单的打开状态, y + 127.0f, this.width - 10.0f, 4.0f, 3.0f, sliderTrack, matrixStack);
        Color[] sliderColors = SettingsCategory.ASPECT_RATIO.getGradient();
        Color sliderStart = sliderColors[0];
        Color sliderCenter = sliderColors[1];
        Color sliderCenter2 = sliderColors[1];
        screen.renderGradient(x + 5.0f, y + 127.0f, sliderPosition, 4.0f, 3.0f, sliderStart, sliderCenter, sliderCenter2, sliderColors[0], matrixStack);
        float aspectWidth = GuiFont.MEDIUM.getStringWidth(aspectText);
        screen.renderString(GuiFont.MEDIUM, aspectText, x + this.width - aspectWidth - 5.0f, y + 117.0f, matrixStack);
        float sliderHandleX = Math.max(x + sliderPosition - 1.0f, x + 5.0f);
        screen.render(sliderHandleX, y + 125.0f, 7.0f, 7.0f, 1.0f, Color.WHITE, matrixStack);
        for (int i = 1; i <= 2; i++) {
            float markerX = sliderHandleX + (float)(i * 2);
            Color markerColor = Color.decode("#565656");
            screen.renderLine(markerX, y + 126.0f, 1.2f, 5.0f, markerColor, matrixStack);
        }
        
        if (this.isDragging) {
            float normalizedMouseX = MathHelper.clamp((float)((mouseX - x - 5.0) * sliderWidth / (this.width - 10.0f) + minValue), minValue, maxValue);
            standardAspectRatio = normalizedMouseX;
        }
        
        SettingsCategory currentLanguage = Melonity.getSettingsManager().getLanguage();
        String languageName = currentLanguage.getName();
        String languagePath = "melonity/images/ui/languages/".concat(languageName);
        Identifier languageIdentifier = new Identifier(languagePath);
        screen.render(languageIdentifier, x + 10.0f, y + 8.0f, 8.0f, 8.5f, matrixStack);
        this.languageAnimation.state(this.isLanguageDropdownOpen);
        float animProgress = this.languageAnimationCalculator.elapsedUnits();
        this.languageAnimation.update(animProgress);
        float rotation = this.languageAnimation.animation() * 180.0f;
        matrixStack.push();
        matrixStack.translate(x + 24.75f, y + 11.75f, 0.0f);
        Quaternionf rotationQuat = RotationAxis.POSITIVE_Z.rotationDegrees(rotation);
        matrixStack.multiply(rotationQuat);
        matrixStack.translate(-(x + 24.75f), -(y + 11.75f), 0.0f);
        Identifier icon = new Identifier("melonity/images/ui/icons/up.png");
        int iconAlpha = MathHelper.clamp(255 - (int)(rotation / 180.0f * 136.0f), 0, 255);
        Color iconColor = new Color(iconAlpha, iconAlpha, iconAlpha);
        screen.render(icon, x + 21.0f, y + 8.0f, 7.5极有可能是一个下拉菜单的打开状态, 7.5f, iconColor, matrixStack);
        matrixStack.pop();
        
        if (this.isLanguageDropdownOpen) {
            float startY = y + 20.0f;
            for (Language language : Language.values()) {
                boolean hovered = RenderUtils.isHovered(x + 5.0f, startY, this.width - 10.0f, 15.0, mouseX, mouseY);
                if (hovered) {
                    SettingsCategory newLanguage = Melonity.getSettingsManager().getLanguage();
                    newLanguage.setLanguage(language);
                }
                SettingsCategory current = Melonity.getSettingsManager().getLanguage();
                boolean active = language == current;
                
                Color languageColorOuter;
                if (active) {
                    languageColorOuter = SettingsCategory.ASPECT_RATIO.getActiveColor().brighter();
                } else {
                    languageColorOuter = SettingsCategory.ASPECT_RATIO.getInactiveColor();
                }
                screen.render(x + 5.0f, startY, this.width - 10.0f, 15.0f, languageColorOuter, matrixStack);
                String langName = language.name();
                Identifier langIconPath = new Identifier("melonity/images/ui/languages/".concat(langName));
                screen.render(langIconPath, x + 9.0f, startY + 3.0f, 8.0f, 8.5f, matrixStack);
                screen.renderString(GuiFont.SMALL, langName, x + 20.0f, startY + 6.0f, matrixStack);
                startY += 14.0f;
            }
        }
        
        float settingsY = y + this.height - 20.0f + aspectTarget;
        RenderUtils.renderVerticalDivider(x, y + this.height - 30.0f + 2.0f, this.width, 28);
        SettingsManager settingsManager = Melonity.getSettingsManager().getActiveCategory();
        List<Setting> settings = settingsManager.getSettings();
        Iterator<Setting> iterator = settings.iterator();
        while (iterator.hasNext()) {
            Setting setting = iterator.next();
            SettingsManager activeSettings = Melonity.getSettingsManager().getActiveCategory();
            Setting activeSetting = activeSettings.getSetting();
            boolean active = setting == activeSetting;
            Identifier checkbox = new Identifier("melonity/images/ui/checkbox.png");
            screen.render(checkbox, x + 6.0f, settingsY, 8.0f, 8.5f, matrixStack);
            if (active) {
                Identifier checked = new Identifier("melonity/images/ui/checkbox_toggled.png");
                Color fillColor = SettingsCategory.ASPECT_RATIO.getFillColor();
                screen.render(checked, x + 6.0f, settingsY, 8.0f, 8.5f, fillColor, matrixStack);
            }
            MinecraftClient client = MinecraftClient.getInstance();
            TextureManager textureManager = client.getTextureManager();
            Texture texture = textureManager.getTexture(setting.getIcon());
            texture.bind();
            Identifier iconId = setting.getIcon();
            screen.render(iconId, x + 22.0f, settingsY - 4.0f, 32.0f, 18.0f, 6.0f, 0.0f, 0.0f, 1.0f, 1.0f, Color.WHITE, matrixStack);
            String name = setting.getName();
            screen.renderString(GuiFont.SMALL, name, x + 56.0f, settingsY + (active ? 0 : 4), Color.WHITE, matrixStack);
            if (active) {
                Color activeColor = SettingsCategory.ASPECT_RATIO.getIndicatorColor();
                screen.renderString(GuiFont.TEXT, "Active", x + 56.0f, settingsY + 9.0f, activeColor, matrixStack);
            }
            settingsY += 22.0f;
        }
        RenderUtils.finishRender();
        matrixStack.pop();
    }

    @Override
    public void onClick(float x, float y, double mouseX, double mouseY, int button) {
        boolean categoryClicked = RenderUtils.isHovered(x + 5.0f, y + 5.0f, 27.5, 14.0, mouseX, mouseY);
        if (categoryClicked) {
            this.isOpen = !this.isOpen;
        }
        boolean sliderClicked = RenderUtils.isHovered(x + 5.0f, y + 127.0f, this.width - 10.0f, 4.0, mouseX, mouseY);
        if (sliderClicked) {
            this.isDragging = true;
        }
        if (this.isOpen) {
            float startY = y + 20.0f;
            for (Language language : Language.values()) {
                boolean languageClicked = RenderUtils.isHovered(x + 5.0f, startY, this.width - 5.0f, 15.0, mouseX, mouseY);
                if (languageClicked) {
                    SettingsManager settingsManager = Melonity.getSettingsManager().getLanguageCategory();
                    settingsManager.setLanguage(language);
                }
                startY += 14.0f;
            }
        }
        float settingY = y + this.height - 20.0f + this.currentAspectRatio;
        boolean settingRegionClicked = RenderUtils.isHovered(x, y + this.height - 30.0f + 2.0f, this.width, 30.0, mouseX, mouseY);
        if (settingRegionClicked) {
            SettingsManager settingsManager = Melonity.getSettingsManager().getActiveCategory();
            List<Setting> settings = settingsManager.getSettings();
            Iterator<Setting> iterator = settings.iterator();
            while (iterator.hasNext()) {
                Setting setting = iterator.next();
                SettingsManager activeSettings = Melonity.getSettingsManager().getActiveCategory();
                Setting activeSetting = activeSettings.getSetting();
                int comparison = setting == activeSetting ? 1 : 0;
                boolean settingClicked = RenderUtils.isHovered(x + 6.0f, settingY, 8.0, 8.5, mouseX, mouseY);
                if (settingClicked) {
                    SettingsManager generalSettings = Melonity.getSettingsManager().getGeneralSettings();
                    generalSettings.toggleSetting(setting);
                }
                settingY += 22.0f;
            }
        }
    }

    public void updateAspectRatio(double delta) {
        this.currentAspectRatio = MathHelper.clamp(this.currentAspectRatio + (float)delta * 8.0f, -68.0f, 0.0f);
    }

    @Override
    public void onRelease() {
        this.isDragging = false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public boolean isOpen() {
        return this.isOpen;
    }
}