// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Screen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.fabric.client.gui.hud.CrosshairElement;
import ru.melonity.fabric.client.gui.hud.HotbarElement;
import ru.melonity.fabric.client.modules.hud.HotbarModule;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    private DrawableHelper drawableHelper = new DrawableHelper();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(MinecraftClient client, CallbackInfo ci) {
        this.drawableHelper.method_55810(this::renderMelonityHud);
    }

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    private void cancelStatusEffectOverlay(DrawContext context, GameRenderer gameRenderer, CallbackInfo ci) {
        ci.cancel();
    }

    @Unique
    private void renderMelonityHud(DrawContext context, GameRenderer gameRenderer) {
        Window window = MinecraftClient.getInstance().getWindow();
        long windowHandle = window.getHandle();
        
        if (Melonity.shouldSetFirstPersonCursor) {
            GLFW.glfwSetCursor(windowHandle, Melonity.firstPersonCursorId);
        } else if (Melonity.shouldSetThirdPersonCursor) {
            GLFW.glfwSetCursor(windowHandle, Melonity.thirdPersonCursorId);
        } else {
            GLFW.glfwSetCursor(windowHandle, Melonity.defaultCursorId);
        }
        
        Melonity.shouldSetThirdPersonCursor = false;
        Melonity.shouldSetFirstPersonCursor = false;
        
        Melonity melonity = Melonity.getInstance();
        melonity.getHudManager().update();
        melonity.getHudManager().render(context.getMatrices());
        melonity.getModuleManager().render(new CrosshairElement(context.getMatrices(), context));
        
        if (!(MinecraftClient.getInstance().currentScreen instanceof Screen)) {
            melonity.getModuleManager().render(new HotbarElement(context.getMatrices(), context));
        }
        
        melonity.getFontRenderer().render(melonity.getModuleManager().getModuleList(), context.getMatrices());
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void cancelHotbarRendering(DrawContext context, float tickDelta, CallbackInfo ci) {
        Melonity melonity = Melonity.getInstance();
        if (melonity.getModuleManager() != null && 
            melonity.getModuleManager().getModule(HotbarModule.class).isPresent() && 
            melonity.getModuleManager().getModule(HotbarModule.class).get().isEnabled() && 
            melonity.getModuleManager().getModule(HotbarModule.class).get().getSetting("Hotbar").getValueAsString().contains("Hotbar")) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHotbarItem", at = @At("HEAD"))
    private void renderCustomHotbarItem(DrawContext context, int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci) {
        Melonity.getInstance().getHudManager().renderHotbarItem(stack, x, y, 16.0f, 0.0f, context.getMatrices());
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void cancelExperienceBarRendering(DrawContext context, int x, CallbackInfo ci) {
        Melonity melonity = Melonity.getInstance();
        if (melonity.getModuleManager() != null && 
            melonity.getModuleManager().getModule(HotbarModule.class).isPresent() && 
            melonity.getModuleManager().getModule(HotbarModule.class).get().isEnabled() && 
            melonity.getModuleManager().getModule(HotbarModule.class).get().getSetting("Hotbar").getValueAsString().contains("Hotbar")) {
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceLevel", at = @At("HEAD"), cancellable = true)
    private void cancelExperienceLevelRendering(DrawContext context, GameRenderer gameRenderer, CallbackInfo ci) {
        Melonity melonity = Melonity.getInstance();
        if (melonity.getModuleManager() != null && 
            melonity.getModuleManager().getModule(HotbarModule.class).isPresent() && 
            melonity.getModuleManager().getModule(HotbarModule.class).get().isEnabled() && 
            melonity.getModuleManager().getModule(HotbarModule.class).get().getSetting("Hotbar").getValueAsString().contains("Hotbar")) {
            ci.cancel();
        }
    }

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void cancelStatusBarsRendering(DrawContext context, CallbackInfo ci) {
        Melonity melonity = Melonity.getInstance();
        if (melonity.getModuleManager() != null && 
            melonity.getModuleManager().getModule(HotbarModule.class).isPresent() && 
            melonity.getModuleManager().getModule(HotbarModule.class).get().isEnabled() && 
            melonity.getModuleManager().getModule(HotbarModule.class).get().getSetting("Hotbar").getValueAsString().contains("Hotbar")) {
            ci.cancel();
        }
    }
}