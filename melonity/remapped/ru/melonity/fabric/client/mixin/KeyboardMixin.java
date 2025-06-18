// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.events.KeyEvent;
import ru.melonity.client.gui.CustomScreen;

@Environment(EnvType.CLIENT)
@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"))
    public void onKey(long window, int keyCode, int scanCode, int action, int modifiers, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (window == client.getWindow().getHandle() &&
            action == 1 && 
            (!(client.currentScreen instanceof ChatScreen) || 
                ((ChatScreen) client.currentScreen).openTime <= System.currentTimeMillis() - 20L) &&
            (client.currentScreen == null || client.currentScreen instanceof CustomScreen) &&
            keyCode != -1 && keyCode != 65540 && keyCode != 65539
        ) {
            Melonity.EVENT_BUS.post(new KeyEvent(keyCode));
        }
    }
}