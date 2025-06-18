// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.n;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4587;
import ru.melonity.Melonity;
import ru.melonity.i.FontRenderer;
import ru.melonity.o.a.Theme;
import ru.melonity.o.a.ThemeColor;
import ru.melonity.s.a.Renderer;
import ru.melonity.w.InputHelper;

@Environment(EnvType.CLIENT)
public class ConfirmationDialog extends ru.melonity.s.e.GuiElement {
    private final Runnable cancelCallback;
    private final Runnable continueCallback;

    public ConfirmationDialog(Runnable cancelCallback, Runnable continueCallback) {
        super(202.0f, 42.0f);
        this.cancelCallback = cancelCallback;
        this.continueCallback = continueCallback;
    }

    @Override
    public void render(float x, float y, double mouseX, double mouseY, Renderer renderer, class_4587 matrix) {
        FontRenderer font = FontRenderer.INSTANCE;
        Theme theme = Melonity.CLIENT.getTheme();
        ThemeColor themeColor = theme.getThemeColor();

        double cancelButtonX = x + this.width - 13.0f - 71.0f;
        float cancelButtonY = y + getHeight() - 12.0f - 15.0f;
        boolean isCancelButtonHovered = InputHelper.isHovered(cancelButtonX, cancelButtonY, 30.0, 15.0, mouseX, mouseY);

        double continueButtonX = x + this.width - 13.0f - 36.0f;
        float continueButtonY = y + getHeight() - 12.0f - 15.0f;
        boolean isContinueButtonHovered = InputHelper.isHovered(continueButtonX, continueButtonY, 36.0, 15.0, mouseX, mouseY);

        float backgroundY = y + getHeight() - 12.0f - 15.5f;
        Color backgroundColor = Color.decode("#222222");
        renderer.drawRoundedRect(x + this.width - 13.5f - 71.0f, backgroundY, 31.0f, 16.0f, 8.0f, backgroundColor, matrix);

        float cancelButtonBackgroundY = y + getHeight() - 12.0f - 15.0f;
        Color cancelButtonColor = Color.decode(isCancelButtonHovered ? "#222222" : "#141414");
        renderer.drawRoundedRect(x + this.width - 13.0f - 71.0f, cancelButtonBackgroundY, 30.0f, 15.0f, 8.0f, cancelButtonColor, matrix);

        renderer.drawText(font, "Cancel", x + this.width - 13.0f - 67.0f, y + getHeight() - 12.0f - 8.5f, Color.WHITE, matrix);

        float continueButtonBackgroundY = y + getHeight() - 12.0f - 15.0f;
        Color continueButtonColor;
        if (isContinueButtonHovered) {
            continueButtonColor = themeColor.getColor().darker();
        } else {
            continueButtonColor = themeColor.getColor();
        }
        renderer.drawRoundedRect(x + this.width - 13.0f - 36.0f, continueButtonBackgroundY, 36.0f, 16.0f, 6.0f, continueButtonColor, matrix);
        renderer.drawText(font, "Continue", x + this.width - 13.0f - 32.0f, y + getHeight() - 12.0f - 8.0f, Color.BLACK, matrix);
    }

    @Override
    public void onMouseClick(float x, float y, double mouseX, double mouseY, int button) {
        double cancelButtonX = x + this.width - 13.0f - 71.0f;
        float cancelButtonY = y + getHeight() - 12.0f - 15.0f;
        boolean isCancelButtonHovered = InputHelper.isHovered(cancelButtonX, cancelButtonY, 30.0, 15.0, mouseX, mouseY);

        double continueButtonX = x + this.width - 13.0f - 36.0f;
        float continueButtonY = y + getHeight() - 12.0f - 15.0f;
        boolean isContinueButtonHovered = InputHelper.isHovered(continueButtonX, continueButtonY, 36.0, 15.0, mouseX, mouseY);

        if (isCancelButtonHovered) {
            cancelCallback.run();
        } else if (isContinueButtonHovered) {
            continueCallback.run();
        }
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void onClose() {
    }
}