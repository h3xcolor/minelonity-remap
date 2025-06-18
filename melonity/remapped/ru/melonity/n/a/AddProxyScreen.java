// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.n.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_310;
import net.minecraft.class_442;
import net.minecraft.class_4587;
import org.lwjgl.glfw.GLFW;
import ru.melonity.Melonity;
import ru.melonity.fabric.client.model.IMainMenuStage;
import ru.melonity.i.FontRenderer;
import ru.melonity.n.ProxyScreen;
import ru.melonity.n.a.ProxyListScreen;
import ru.melonity.s.d.TextField;
import ru.melonity.s.c.RenderUtils;
import ru.melonity.w.InputUtils;

@Environment(EnvType.CLIENT)
public class AddProxyScreen implements ProxyScreen {
    private final class_442 parentScreen;
    private final TextField textField;
    private String errorMessage = "";

    public AddProxyScreen(class_442 parentScreen) {
        this.parentScreen = parentScreen;
        this.textField = new TextField("Your link", false, 186.0f, 16.0f);
        this.textField.setEditable(true);
    }

    @Override
    public void render(float x, float y, float tickDelta, double mouseX, double mouseY, RenderUtils renderer, class_4587 matrices) {
        Melonity.Theme theme = Melonity.getInstance().getTheme();
        RenderUtils.ThemeColors colors = theme.getColors();
        Color backgroundColor = colors.getBackground();
        Color borderColor = colors.getBorder();
        Color textColor = colors.getText();
        Color accentColor = colors.getAccent();
        
        renderer.drawRoundedRect(x, y, tickDelta, 30.0f, 0.0f, 12.0f, 12.0f, 0.0f, backgroundColor, borderColor, textColor, accentColor, matrices);
        renderer.drawText(FontRenderer.MAIN, "Add proxy", x + 6.0f, y + 14.0f, matrices);
        
        Color separatorColor = Color.decode("#222222");
        renderer.drawHorizontalLine(x, y + 30.0f, tickDelta, 1.0f, separatorColor, matrices);
        
        Color hintColor = Color.decode("#888888");
        renderer.drawText(FontRenderer.SMALL, this.errorMessage, x + 8.0f, y + 57.0f, hintColor, matrices);
        
        this.textField.render(theme, x + 8.0f, y + 50.0f + 18.0f, mouseX, mouseY, renderer, matrices);
        
        boolean isTextFieldHovered = InputUtils.isHovered(
            x + 8.0f, 
            y + 50.0f + 18.0f, 
            this.textField.getWidth(), 
            this.textField.getHeight(), 
            mouseX, mouseY
        );
        
        renderer.drawRoundedRect(x, y + 116.0f, tickDelta, 34.0f, 12.0f, accentColor, matrices);
        
        boolean isCancelHovered = InputUtils.isHovered(
            x + 8.0f, 
            y + 116.0f + 9.0f, 
            92.0, 
            15.0, 
            mouseX, mouseY
        );
        
        Color cancelOutlineColor = Color.decode("#2C2C2C");
        renderer.drawRoundedOutline(x + 8.0f - 0.3f, y + 116.0f + 9.0f - 0.3f, 92.6f, 17.0f, 8.0f, cancelOutlineColor, matrices);
        
        Color cancelButtonColor = isCancelHovered ? Color.decode("#2C2C2C") : Color.decode("#222222");
        renderer.drawRoundedRect(x + 8.0f, y + 116.0f + 9.0f, 92.0f, 15.0f, 8.0f, cancelButtonColor, matrices);
        
        float cancelTextWidth = FontRenderer.SMALL.getStringWidth("Cancel");
        renderer.drawText(FontRenderer.SMALL, "Cancel", x + 8.0f + 46.0f - cancelTextWidth / 2.0f, y + 116.0f + 16.0f, hintColor, matrices);
        
        boolean isAddHovered = InputUtils.isHovered(
            x + 8.0f + 96.0f, 
            y + 116.0f + 9.0f, 
            92.0, 
            17.0, 
            mouseX, mouseY
        );
        
        Color addButtonColor = isAddHovered ? accentColor.darker() : accentColor;
        renderer.drawRoundedRect(x + 8.0f + 96.0f, y + 116.0f + 9.0f, 92.0f, 17.0f, 8.0f, addButtonColor, matrices);
        
        float addTextWidth = FontRenderer.SMALL.getStringWidth("Add");
        renderer.drawText(FontRenderer.SMALL, "Add", x + 96.0f + 8.0f + 46.0f - addTextWidth / 2.0f, y + 116.0f + 17.0f, Color.BLACK, matrices);
        
        class_310 client = class_310.method_1551();
        class_1041 window = client.method_22683();
        long windowHandle = window.method_4490();
        if (isTextFieldHovered) {
            GLFW.glfwSetCursor(windowHandle, Melonity.TEXT_CURSOR);
        } else {
            GLFW.glfwSetCursor(windowHandle, Melonity.DEFAULT_CURSOR);
        }
    }

    @Override
    public void mouseClicked(float x, float y, float tickDelta, double mouseX, double mouseY, int button) {
        this.textField.mouseClicked(x, y + 50.0f + 18.0f, mouseX, mouseY, button);
        
        boolean isCancelHovered = InputUtils.isHovered(
            x + 8.0f, 
            y + 116.0f + 9.0f, 
            92.0, 
            15.0, 
            mouseX, mouseY
        );
        
        if (isCancelHovered) {
            IMainMenuStage stage = (IMainMenuStage) this.parentScreen;
            stage.setStage(new ProxyListScreen(this.parentScreen));
        }
        
        boolean isAddHovered = InputUtils.isHovered(
            x + 8.0f + 96.0f, 
            y + 116.0f + 9.0f, 
            92.0, 
            17.0, 
            mouseX, mouseY
        );
        
        if (isAddHovered) {
            try {
                Melonity.ProxyManager proxyManager = Melonity.getInstance().getProxyManager();
                String proxyLink = this.textField.getText();
                proxyManager.addProxy(proxyLink, true);
            } catch (Exception e) {
                this.errorMessage = e.getMessage();
                return;
            }
            IMainMenuStage stage = (IMainMenuStage) this.parentScreen;
            stage.setStage(new ProxyListScreen(this.parentScreen));
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        this.textField.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void charTyped(float x, float y, float tickDelta, double mouseX, double mouseY, double delta) {
    }

    @Override
    public float getWidth() {
        return 150.0f;
    }
}