// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.d;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3532;
import org.lwjgl.glfw.GLFW;
import ru.melonity.Melonity;
import ru.melonity.o.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIll极长类名;

@Environment(EnvType.CLIENT)
public class WheelBindModule extends ru.melonity.h.c.Module {
    private boolean isOpen = false;
    private final WheelOption wheelOption = new WheelOption("", -1);
    private final EventListener<KeyEvent> keyListener = this::handleKeyEvent;
    private final EventListener<RenderEvent> renderListener = this::handleRenderEvent;

    public WheelBindModule() {
        super("Wheel bind", ModuleCategory.UTILITY);
        initListeners();
        addOption(wheelOption);
    }

    private void disableAllModules() {
        List<Module> modules = Melonity.getModuleManager().getModules();
        Iterator<Module> iterator = modules.iterator();
        while (iterator.hasNext()) {
            Module module = iterator.next();
            module.setEnabled(false);
        }
    }

    private void handleRenderEvent(RenderEvent event) {
        int stage = wheelOption.getKeyValue();
        if (stage == -1) {
            return;
        }
        KeyEvent keyHandler = Melonity.getKeyHandler();
        KeyHandlerState keyState = keyHandler.getState();
        RenderManager renderManager = event.getRenderManager();
        int keyCode = Melonity.getKeyHandler().getKeyState().getKeyCode();
        long windowHandle = GLFW.glfwGetCurrentContext();
        boolean keyPressed = GLFW.glfwGetKey(windowHandle, keyCode) == GLFW.GLFW_PRESS;
        if (keyPressed) {
            Melonity.getInstance().getFrameBufferManager().enable();
            isOpen = true;
        }
        KeyHandler keyBinding = Melonity.getKeyHandler();
        List<RenderComponent> components = keyBinding.getComponents();
        renderManager.reset();
        bufferBuilder.begin();
        double screenCenterX = event.getScaledResolution().getWidth() / 2.0;
        double screenCenterY = event.getScaledResolution().getHeight() / 2.0;
        if (components.get(0).getAnimationProgress() > 0.0f) {
            RenderComponent component = components.get(0);
            float animationProgress = component.getAnimationProgress();
            Color backgroundColor = new Color(0, 0, 0, (int) (animationProgress * 140));
            renderManager.drawRectangle(
                (float) (screenCenterX - 37.0),
                (float) (screenCenterY - 86.0),
                200.0f,
                200.0f,
                200.0f,
                180.0f,
                backgroundColor,
                bufferBuilder
            );
        }
        bufferBuilder.finish();
        renderManager.setupOrthoProjection();
        Dimension dimension = event.getWindowSize();
        float radius = 30.0f;
        if (keyPressed && !isOpen) {
            Melonity.getInstance().getFrameBufferManager().disable();
            isOpen = false;
            Iterator<RenderComponent> compIterator = components.iterator();
            while (compIterator.hasNext()) {
                RenderComponent comp = compIterator.next();
                if (comp.isEnabled() != null && !comp.isEnabled()) {
                    continue;
                }
                comp.toggle();
                disableAllModules();
                break;
            }
        }
        components.get(0).render(keyState, 0.0f, 0.0f, -1.1f, class_3532.method_15374(0.436332f), screenCenterX, screenCenterY, renderManager, keyPressed, bufferBuilder);
        components.get(1).render(keyState, 0.0f, 0.0f, -1.2f, -0.15f, screenCenterX, screen极长类名
    }
}