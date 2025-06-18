// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s.d;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.s.c.RenderHelper;
import ru.melonity.s.e.Widget;
import ru.melonity.w.SuggestionHelper;
import ru.melonity.w.SuggestionProvider;

@Environment(value = EnvType.CLIENT)
public class TextFieldWidget extends Widget {
    private final String name;
    private final boolean isSearchField;
    private String text = "";
    private boolean isFocused = false;
    private boolean isActive = false;
    private long lastSwitchTime = 0L;
    private boolean cursorVisible = true;
    private final StateAnimation focusAnimation = new StateAnimation();
    private final FrameWeightCalculator focusAnimationTimer = FrameWeightCalculator.milliseconds(400L);
    private boolean hasText = false;
    private boolean isSuggesting = false;
    private String suggestion = null;
    private static final Set<Integer> allowedKeyCodes = new HashSet<>();
    private SuggestionProvider suggestionProvider = null;
    private TextFieldWidget nextTextField = null;
    private Color placeholderColor = null;
    public static int ANTI_OBFUSCATION_SEED = 1533081571;

    public TextFieldWidget(String name, boolean isSearchField, float width, float height) {
        super(width, height);
        this.name = name;
        this.isSearchField = isSearchField;
    }

    public static boolean isAllowedKey(int keyCode) {
        return !allowedKeyCodes.contains(keyCode);
    }

    @Override
    public void render(Widget.Theme theme, float x, float y, double mouseX, double mouseY, RenderHelper renderer, MatrixStack matrices) {
        focusAnimation.state(isFocused && isActive);
        float elapsed = focusAnimationTimer.elapsedUnits();
        focusAnimation.update(elapsed);
        float animationProgress = focusAnimation.animation();

        if (isSearchField) {
            Color searchColor = theme.getFocusedColor();
            renderer.drawRect(x, y, width, height, 4.0f, searchColor, matrices);
            Color brighter = new Color(
                Math.min(searchColor.getRed() + 5, 255),
                Math.min(searchColor.getGreen() + 5, 255),
                Math.min(searchColor.getBlue() + 5, 255)
            );
            int alpha = MathHelper.clamp((int) (animationProgress * 255.0f), 0, 255);
            Color animatedColor = RenderHelper.withAlpha(brighter, alpha);
            renderer.drawRect(x, y, width, height, 4.0f, animatedColor, matrices);
        } else {
            Color baseColor = theme.getBaseColor();
            renderer.drawRect(x, y, width, height, 4.0f, baseColor, matrices);
            if (!(theme instanceof SuggestionProvider)) {
                Color brighter = baseColor.brighter();
                int alpha = MathHelper.clamp((int) (animationProgress * 255.0f), 0, 255);
                Color animatedColor = RenderHelper.withAlpha(brighter, alpha);
                renderer.drawRect(x, y, width, height, 4.0f, animatedColor, matrices);
            }
        }

        if (isSearchField) {
            Identifier searchIcon = new Identifier("melonity/images/ui/search_icon.png");
            Color iconColor = theme.getIconColor();
            renderer.drawTexture(searchIcon, x + 6.0f, y + 4.0f, 8.5f, 8.5f, iconColor, matrices);
        }

        String displayText = text;
        while (RenderHelper.getTextWidth(displayText) > width - 5.0f) {
            displayText = displayText.substring(0, displayText.length() - 1);
        }

        float textX = x - (isSuggesting ? 7 : 0) + (isSearchField ? 16 : 6);
        float textY = y + 0.5f + height / 2.0f;
        float textWidth = RenderHelper.getTextWidth(displayText);
        float adjustedY = textY - textWidth / 2.0f + 1.5f;

        Color textColor;
        if (isSearchField) {
            textColor = theme.getFocusedTextColor();
        } else {
            textColor = Color.decode("#888888");
        }
        RenderHelper.drawText(RenderHelper.getFontRenderer(), displayText, textX, adjustedY, textColor, matrices);

        if (hasText && !isFocused && suggestionProvider != null) {
            String suggested = suggestionProvider.suggest(text);
            if (suggested != null && !suggested.isEmpty()) {
                this.suggestion = suggested;
                float suggestionX = x + (isSearchField ? 16 : 6);
                float suggestionY = y + 1.0f + height / 2.0f;
                float suggestionWidth = RenderHelper.getTextWidth(name);
                float suggestionAdjustedY = suggestionY - suggestionWidth / 2.0f + 1.5f;
                Color suggestionColor;
                if (isSearchField) {
                    suggestionColor = theme.getFocusedTextColor();
                } else {
                    suggestionColor = Color.decode("#888888");
                }
                RenderHelper.drawText(RenderHelper.getFontRenderer(), name, suggestionX, suggestionAdjustedY, suggestionColor, matrices);
                SuggestionHelper.clearSuggestions();
            }
        }

        if (isFocused && isActive && cursorVisible) {
            float cursorX = x - (isSuggesting ? 7 : 0) - 1.0f + (isSearchField ? 17 : 7) + RenderHelper.getTextWidth(text);
            RenderHelper.drawRect(cursorX, y + height / 2.0f - 4.0f, 0.5f, 8.0f, Color.WHITE, matrices);
        }

        boolean hovered = SuggestionHelper.isHovered(x, y, width, height, mouseX, mouseY);
        if (hovered) {
            Melonity.setHoveredTextField(true);
        }
        lastSwitchTime++;
        MinecraftClient client = MinecraftClient.getInstance();
        int fps = client.getCurrentFps();
        if (lastSwitchTime > (long) (fps / 2)) {
            lastSwitchTime = 0L;
            cursorVisible = !cursorVisible;
        }
    }

    @Override
    public void onClick(float x, float y, double mouseX, double mouseY, int button) {
        boolean hovered = SuggestionHelper.isHovered(x, y, width, height, mouseX, mouseY);
        if (hovered) {
            isFocused = true;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isFocused || !isActive) {
            return false;
        }
        if (suggestionProvider != null && text != null && !text.isEmpty() && suggestion != null && !suggestion.isEmpty() && keyCode == 257) {
            setText(suggestion);
            return true;
        }
        if (keyCode == 65) {
            if (!text.isEmpty()) {
                if (Screen.hasControlDown()) {
                }
            }
        }
        if (keyCode == 67) {
            if (!text.isEmpty()) {
                if (Screen.hasControlDown()) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    Window window = client.getWindow();
                    long handle = window.getHandle();
                    GLFW.glfwSetClipboardString(handle, text);
                    return false;
                }
            }
        }
        if (keyCode == 86) {
            if (Screen.hasControlDown()) {
                MinecraftClient client = MinecraftClient.getInstance();
                Window window = client.getWindow();
                long handle = window.getHandle();
                String clipboard = GLFW.glfwGetClipboardString(handle);
                text = text + clipboard;
                return false;
            }
        }
        if (keyCode == 259) {
            if (text.isEmpty()) {
                return false;
            }
            int length = text.length();
            text = text.substring(0, length - 1);
            if (Screen.hasControlDown()) {
                text = "";
            }
            return false;
        }
        if (!isAllowedKey(keyCode)) {
            return false;
        }
        String keyName = GLFW.glfwGetKeyName(keyCode, 0);
        if (keyCode == 32) {
            keyName = " ";
        }
        if (keyName == null) {
            return false;
        }
        if (Screen.hasShiftDown()) {
            keyName = keyName.toUpperCase();
        } else {
            keyName = keyName.toLowerCase();
        }
        if (keyName.equals(";") && Screen.hasShiftDown()) {
            keyName = ":";
        }
        text = text + keyName;
        return false;
    }

    @Override
    public void onRelease() {
    }

    @Generated
    public String getName() {
        return name;
    }

    @Generated
    public boolean isSearchField() {
        return isSearchField;
    }

    @Generated
    public String getText() {
        return text;
    }

    @Generated
    public boolean isFocused() {
        return isFocused;
    }

    @Generated
    public boolean isActive() {
        return isActive;
    }

    @Generated
    public long getLastSwitchTime() {
        return lastSwitchTime;
    }

    @Generated
    public boolean isCursorVisible() {
        return cursorVisible;
    }

    @Generated
    public StateAnimation getFocusAnimation() {
        return focusAnimation;
    }

    @Generated
    public FrameWeightCalculator getFocusAnimationTimer() {
        return focusAnimationTimer;
    }

    @Generated
    public boolean hasText() {
        return hasText;
    }

    @Generated
    public boolean isSuggesting() {
        return isSuggesting;
    }

    @Generated
    public String getSuggestion() {
        return suggestion;
    }

    @Generated
    public void setText(String text) {
        this.text = text;
    }

    @Generated
    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    @Generated
    public void setActive(boolean active) {
        isActive = active;
    }

    @Generated
    public void setLastSwitchTime(long lastSwitchTime) {
        this.lastSwitchTime = lastSwitchTime;
    }

    @Generated
    public void setCursorVisible(boolean cursorVisible) {
        this.cursorVisible = cursorVisible;
    }

    @Generated
    public void setHasText(boolean hasText) {
        this.hasText = hasText;
    }

    @Generated
    public void setSuggesting(boolean suggesting) {
        isSuggesting = suggesting;
    }

    @Generated
    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    @Generated
    public void setSuggestionProvider(SuggestionProvider suggestionProvider) {
        this.suggestionProvider = suggestionProvider;
    }

    @Generated
    public void setNextTextField(TextFieldWidget nextTextField) {
        this.nextTextField = nextTextField;
    }

    @Generated
    public void setPlaceholderColor(Color placeholderColor) {
        this.placeholderColor = placeholderColor;
    }

    static {
        for (int i = 32; i <= 126; ++i) {
            allowedKeyCodes.add(i);
        }
        allowedKeyCodes.add(259);
    }

    @Environment(value = EnvType.CLIENT)
    public static interface SuggestionProvider {
        public static int ANTI_OBFUSCATION_SEED = 472106125;

        public String suggest(String input);
    }

    @Environment(value = EnvType.CLIENT)
    public static interface ItemSuggestionProvider {
        public static int ANTI_OBFUSCATION_SEED = 1517784070;

        public ItemStack suggest(String input);
    }
}