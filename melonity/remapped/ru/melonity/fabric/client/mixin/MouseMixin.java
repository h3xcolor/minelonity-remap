// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.SmoothUtil;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.f.b.MouseMovementEvent;
import ru.melonity.o.ComponentDisplayEvent;
import ru.melonity.o.b.KeyBindManager;
import ru.melonity.o.b.a.UIComponentManager;
import ru.melonity.o.b.a.b.MelonityButton;

@Environment(value=EnvType.CLIENT)
@Mixin(value={Mouse.class})
public class MouseMixin {
    @Shadow
    private final SmoothUtil horizontalSmoother = new SmoothUtil();
    @Shadow
    private final SmoothUtil verticalSmoother = new SmoothUtil();
    @Shadow
    private double cursorDeltaX;
    @Shadow
    private double cursorDeltaY;

    @Inject(method={"onMouseButton"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/util/Window;getScaledWidth()I")})
    private void onMouseButton(long window, int button, int action, int modifiers, CallbackInfo callbackInfo) {
        if (action != 1) {
            KeyBindManager keyBindManager = Melonity.INSTANCE.getKeyBindManager();
            keyBindManager.getAllBindings().stream()
                .filter(binding -> binding.isEnabled())
                .forEach(binding -> binding.onMouseButton());
        }
    }

    @Inject(method={"onMouseButton"}, at={@At(value="HEAD")})
    private void onMouseButton_head(long window, int button, int action, int modifiers, CallbackInfo callbackInfo) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof ComponentDisplayEvent) {
            return;
        }
        UIComponentManager componentManager = Melonity.INSTANCE.getUIComponentManager();
        if (componentManager == null) {
            return;
        }
        for (UIComponentManager.Component component : componentManager.getAllComponents()) {
            for (UIComponentManager.UIElement element : component.getUIElements()) {
                if (!(element instanceof MelonityButton)) {
                    continue;
                }
                MelonityButton melonityButton = (MelonityButton)element;
                long minecraftWindowHandle = client.getWindow().getHandle();
                if (melonityButton.getMouseButtonBinding() == 3 && GLFW.glfwGetMouseButton(minecraftWindowHandle, 3) == 1) {
                    melonityButton.toggle();
                    return;
                }
                if (melonityButton.getMouseButtonBinding() == 4 && GLFW.glfwGetMouseButton(minecraftWindowHandle, 4) == 1) {
                    melonityButton.toggle();
                    return;
                }
            }
        }
    }

    @Inject(method={"updateMouse"}, at={@At(value="HEAD")}, cancellable=true)
    private void updateMouse(double mouseSensitivity, CallbackInfo callbackInfo) {
        double adjustedDeltaX;
        double adjustedDeltaY;
        MinecraftClient client = MinecraftClient.getInstance();
        GameOptions options = client.options;
        double sensitivityFactor = (Double)options.getMouseSensitivity().getValue() * 0.6 + 0.2;
        double sensitivityMultiplier = sensitivityFactor * sensitivityFactor * sensitivityFactor;
        double finalSensitivity = sensitivityMultiplier * 8.0;
        if (options.getSmoothCameraEnabled().getValue()) {
            double smoothedHorizontalDelta = this.horizontalSmoother.smooth(this.cursorDeltaX * finalSensitivity, mouseSensitivity * finalSensitivity);
            double smoothedVerticalDelta = this.verticalSmoother.smooth(this.cursorDeltaY * finalSensitivity, mouseSensitivity * finalSensitivity);
            adjustedDeltaX = smoothedHorizontalDelta;
            adjustedDeltaY = smoothedVerticalDelta;
        } else if (options.getCinematicCameraEnabled().getValue() && client.player.isSpectator()) {
            this.horizontalSmoother.clear();
            this.verticalSmoother.clear();
            adjustedDeltaX = this.cursorDeltaX * sensitivityMultiplier;
            adjustedDeltaY = this.cursorDeltaY * sensitivityMultiplier;
        } else {
            this.horizontalSmoother.clear();
            this.verticalSmoother.clear();
            adjustedDeltaX = this.cursorDeltaX * finalSensitivity;
            adjustedDeltaY = this.cursorDeltaY * finalSensitivity;
        }
        int invertY = 1;
        if (((Boolean)options.getInvertYMouse().getValue()).booleanValue()) {
            invertY = -1;
        }
        client.getTutorialManager().onUpdateMouse(adjustedDeltaX, adjustedDeltaY * invertY);
        if (client.player != null) {
            MouseMovementEvent event = new MouseMovementEvent(adjustedDeltaX, adjustedDeltaY * invertY);
            KeyBindManager keyBindManager = Melonity.INSTANCE.getKeyBindManager();
            if (keyBindManager != null) {
                keyBindManager.handleMouseMovementEvent(event);
            }
            if (!event.isCancelled()) {
                client.player.changeLookDirection(event.getDeltaX(), event.getDeltaY());
            }
        }
        callbackInfo.cancel();
    }
}