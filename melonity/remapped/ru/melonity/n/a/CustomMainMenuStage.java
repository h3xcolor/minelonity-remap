// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.n.a;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;
import ru.melonity.Melonity;
import ru.melonity.fabric.client.model.IMainMenuStage;
import ru.melonity.i.FontRenderer;
import ru.melonity.n.a.AltManagerMenuStage;
import ru.melonity.n.a.ProxyMenuStage;
import ru.melonity.o.a.ButtonRenderer;
import ru.melonity.s.a.ColorProvider;
import ru.melonity.w.MouseHelper;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class CustomMainMenuStage implements ru.melonity.n.CustomMenuStageInterface {
    private final Map<String, Runnable> menuButtons;

    public CustomMainMenuStage(TitleScreen parentScreen) {
        MinecraftClient client = MinecraftClient.getInstance();
        this.menuButtons = new LinkedHashMap<>();
        this.menuButtons.put("SinglePlayer", () -> client.setScreen(new SelectWorldScreen(parentScreen)));
        this.menuButtons.put("MultiPlayer", () -> client.setScreen(new MultiplayerScreen(parentScreen)));
        this.menuButtons.put("AltManager", () -> ((IMainMenuStage) parentScreen).setStage(new AltManagerMenuStage(parentScreen)));
        this.menuButtons.put("Proxy", () -> ((IMainMenuStage) parentScreen).setStage(new ProxyMenuStage(parentScreen)));
        this.menuButtons.put("Options", () -> client.setScreen(new OptionsScreen(parentScreen, client.options)));
    }

    @Override
    public void render(float x, float y, float width, double mouseX, double mouseY, ColorProvider colorProvider, MatrixStack matrices) {
        float buttonY = y + 9.0f;
        ButtonRenderer buttonRenderer = Melonity.getInstance().getButtonRenderer();
        ColorProvider buttonColorProvider = buttonRenderer.getColorProvider();
        boolean hoverDetected = false;
        Set<Map.Entry<String, Runnable>> entries = this.menuButtons.entrySet();
        Iterator<Map.Entry<String, Runnable>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Runnable> entry = iterator.next();
            boolean hovered = MouseHelper.isHovered(x + 4.0f, buttonY, width - 8.0f, 28.0, mouseX, mouseY);
            Color baseColor = buttonColorProvider.getButtonColor();
            Color hoverColor = new Color(
                baseColor.getRed() + 4,
                baseColor.getGreen() + 4,
                baseColor.getBlue() + 4
            );
            Color buttonColor = hovered ? hoverColor.darker() : hoverColor;
            colorProvider.drawButton(x + 4.0f, buttonY, width - 8.0f, 28.0f, 4.0f, buttonColor, matrices);
            if (hovered) {
                hoverDetected = true;
            }
            FontRenderer fontRenderer = FontRenderer.getInstance();
            String buttonText = entry.getKey();
            float textWidth = fontRenderer.getTextWidth(buttonText);
            float textHeight = fontRenderer.getTextHeight(buttonText);
            float textX = x + width / 2.0f - textWidth / 2.0f;
            float textY = buttonY + 14.0f - textHeight / 2.0f + 2.0f;
            colorProvider.drawText(fontRenderer, buttonText, textX, textY, matrices);
            buttonY += 32.0f;
        }

        Window window = MinecraftClient.getInstance().getWindow();
        long windowHandle = window.getHandle();
        if (hoverDetected) {
            GLFW.glfwSetCursor(windowHandle, Melonity.getHandCursor());
        } else {
            GLFW.glfwSetCursor(windowHandle, Melonity.getArrowCursor());
        }
    }

    @Override
    public void mouseClicked(float x, float y, float width, double mouseX, double mouseY, int button) {
        float buttonY = y + 9.0f;
        Set<Map.Entry<String, Runnable>> entries = this.menuButtons.entrySet();
        Iterator<Map.Entry<String, Runnable>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Runnable> entry = iterator.next();
            boolean hovered = MouseHelper.isHovered(x + 4.0f, buttonY, width - 8.0f, 28.0, mouseX, mouseY);
            if (hovered) {
                entry.getValue().run();
                return;
            }
            buttonY += 32.0f;
        }
    }

    @Override
    public void mouseDragged(int mouseX, int mouseY, int button) {
    }

    @Override
    public void mouseReleased(float x, float y, float width, double mouseX, double mouseY, double scrollDelta) {
    }

    @Override
    public float getHeight() {
        return 182.0f;
    }
}