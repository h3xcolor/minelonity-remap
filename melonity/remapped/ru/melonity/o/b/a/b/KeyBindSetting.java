// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b.a.b;

import java.awt.Color;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import ru.melonity.Melonity;
import ru.melonity.o.b.a.Setting;
import ru.melonity.s.a.TranslationHelper;
import ru.melonity.w.RenderUtils;

@Environment(value = EnvType.CLIENT)
public class KeyBindSetting implements Setting {
    private int keyCode;
    public boolean waitingForInput;
    private final String translationKey;
    private final ItemStack icon;
    
    public KeyBindSetting(String translationKey, int keyCode, ItemStack icon) {
        this.keyCode = keyCode;
        this.translationKey = translationKey;
        this.waitingForInput = false;
        this.icon = icon;
    }

    public KeyBindSetting(String translationKey, int keyCode) {
        this(translationKey, keyCode, null);
    }

    public KeyBindSetting(String translationKey) {
        this(translationKey, -1);
    }

    public KeyBindSetting(int keyCode) {
        this("global.keybind", keyCode);
    }

    @Override
    public void render(Setting setting, float x, float y, float width, double mouseX, double mouseY, RenderUtils renderUtils, MatrixStack matrices, TextRenderer textRenderer) {
        boolean hasIcon = this.icon != null;
        if (hasIcon) {
            matrices.push();
            matrices.translate(x, y, 0.0f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.translate(-x, -y, 0.0f);
            textRenderer.draw(matrices, this.icon, (int) x - 4, (int) y - 4);
            matrices.pop();
        }
        
        TranslationHelper translator = Melonity.getInstance().getTranslator();
        String displayText = translator.translate(this.translationKey, new Object[0]);
        renderUtils.drawString(displayText, x + (hasIcon ? 9 : 0), y, matrices);
        
        TextRenderer font = Melonity.getInstance().getFontRenderer();
        String keyName;
        if (this.keyCode == 3) {
            keyName = "MB4";
        } else if (this.keyCode == 4) {
            keyName = "MB5";
        } else if (this.keyCode == -1) {
            keyName = "NONE";
        } else {
            String glfwKeyName = GLFW.glfwGetKeyName(this.keyCode, 0);
            if (glfwKeyName == null) {
                keyName = "?";
            } else {
                keyName = glfwKeyName.toUpperCase();
            }
        }
        
        float keyNameWidth = font.getWidth(keyName);
        float boxWidth = keyNameWidth + 8.0f;
        boolean isHovered = RenderUtils.isHovered(x + width - 16.0f - boxWidth, y - 3.0f, boxWidth, 10.0, mouseX, mouseY);
        
        Color backgroundColor = new Color(136, 136, 136, 34);
        renderUtils.drawRoundedRect(x + width - 16.0f - boxWidth + 0.2f, y - 5.0f, boxWidth - 1.0f, 12.8f, 3.5f, backgroundColor, matrices);
        
        Color boxColor;
        if (this.waitingForInput) {
            boxColor = setting.getColor().darker();
        } else if (isHovered) {
            boxColor = setting.getColor().brighter();
        } else {
            boxColor = setting.getColor();
        }
        renderUtils.drawRoundedRect(x + width - 16.0f - boxWidth, y - 5.0f, boxWidth, 12.0f, 3.5f, boxColor, matrices);
        
        float textX = x + width - 16.0f - boxWidth + boxWidth / 2.0f - keyNameWidth / 2.0f;
        Color textColor = this.waitingForInput ? Color.WHITE : Color.decode("#888888");
        font.draw(matrices, keyName, textX, y - 0.5f, textColor.getRGB());
        
        if (this.waitingForInput) {
            Melonity.getInstance().setSettingKeybind(true);
            MinecraftClient client = MinecraftClient.getInstance();
            Window window = client.getWindow();
            long handle = window.getHandle();
            if (GLFW.glfwGetMouseButton(handle, 3) == 1) {
                this.keyCode = 3;
                this.waitingForInput = false;
            }
            if (GLFW.glfwGetMouseButton(handle, 4) == 1) {
                this.keyCode = 4;
                this.waitingForInput = false;
            }
        } else {
            boolean hovered = RenderUtils.isHovered(x + width - 16.0f - boxWidth, y - 3.0f, boxWidth, 10.0, mouseX, mouseY);
            if (hovered) {
                Melonity.getInstance().setHoveredKeybind(true);
            }
        }
    }

    @Override
    public void mouseClicked(float x, float y, float width, double mouseX, double mouseY, int button) {
        TextRenderer font = Melonity.getInstance().getFontRenderer();
        String keyName;
        if (this.keyCode == -1) {
            keyName = "NONE";
        } else {
            String glfwKeyName = GLFW.glfwGetKeyName(this.keyCode, 0);
            if (glfwKeyName == null) {
                keyName = "?";
            } else {
                keyName = glfwKeyName.toUpperCase();
            }
        }
        float keyNameWidth = font.getWidth(keyName);
        float boxWidth = keyNameWidth + 8.0f;
        boolean isHovered = RenderUtils.isHovered(x + width - 16.0f - boxWidth, y - 3.0f, boxWidth, 10.0, mouseX, mouseY);
        if (isHovered) {
            this.waitingForInput = !this.waitingForInput;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.waitingForInput) {
            if (keyCode == 256) {
                keyCode = -1;
            }
            this.keyCode = keyCode;
            this.waitingForInput = false;
            return true;
        }
        return false;
    }

    @Override
    public void onClose() {
    }

    @Override
    public float getHeight() {
        return 20.0f;
    }

    @Override
    public void onSettingsOpen() {
    }

    @Generated
    public int getKeyCode() {
        return this.keyCode;
    }

    @Generated
    public boolean isWaitingForInput() {
        return this.waitingForInput;
    }

    @Generated
    public String getTranslationKey() {
        return this.translationKey;
    }

    @Generated
    public ItemStack getIcon() {
        return this.icon;
    }

    @Generated
    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    @Generated
    public void setWaitingForInput(boolean waitingForInput) {
        this.waitingForInput = waitingForInput;
    }
}