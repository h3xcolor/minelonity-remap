// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.melonity.Melonity;
import ru.melonity.client.command.CommandResult;
import ru.melonity.client.ui.UIComponent;
import ru.melonity.client.util.UIUtils;
import ru.melonity.client.command.CommandHandler;

import java.awt.Color;

@Environment(EnvType.CLIENT)
@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"))
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        Melonity.getUIComponentManager().getComponents().stream()
            .filter(UIComponent::isVisible)
            .filter(component -> UIUtils.isMouseOver(
                component.getX(),
                component.getY(),
                component.getWidth(),
                component.getHeight(),
                mouseX,
                mouseY
            ))
            .findFirst()
            .ifPresent(component -> component.onMouseClicked(mouseX, mouseY));
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Melonity.getUIComponentManager().getComponents().stream()
            .filter(UIComponent::isVisible)
            .forEach(component -> component.render(mouseX, mouseY));
        
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        float centerX = screenWidth / 2.0f;
        float centerY = screenHeight / 2.0f;
        float snapDistance = 12.0f;
        float thickness = 0.5f;
        Color color = Color.WHITE;
        
        Melonity.getUIComponentManager().getComponents().stream()
            .filter(UIComponent::isVisible)
            .filter(UIComponent::isBeingDragged)
            .findFirst()
            .ifPresent(component -> {
                if (Math.abs(component.getX() + component.getWidth() / 2.0f - centerX) <= snapDistance) {
                    Melonity.getRenderer().fill(
                        centerX - thickness / 2.0f,
                        0.0f,
                        thickness,
                        screenHeight,
                        color,
                        context.getMatrices()
                    );
                }
                if (Math.abs(component.getY() + component.getHeight() / 2.0f - centerY) <= snapDistance) {
                    Melonity.getRenderer().fill(
                        0.0f,
                        centerY - thickness / 2.0f,
                        screenWidth,
                        thickness,
                        color,
                        context.getMatrices()
                    );
                }
            });
    }

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    public void sendMessage(String message, boolean addToHistory, CallbackInfo ci) {
        Melonity.getCommandHandler().handleChatMessage(message);
        CommandResult result = Melonity.getCommandHandler().parseCommand(message);
        if (result == CommandResult.CANCELLED) {
            MinecraftClient.getInstance().player.networkHandler.sendChatMessage(message);
            ci.cancel();
        }
    }
}