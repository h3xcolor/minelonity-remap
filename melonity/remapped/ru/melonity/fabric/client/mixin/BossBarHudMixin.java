// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.h.b.IlIIIlIllIIllIlIIlIIllIIlllIIIllIIllIIlIIlIIllIlIIllIlllIIllIllIIlIIlIIlIIlIIlllIlllIIllIIIlllIIIlIIlIIIllIllIIllIlIllIlllIIIlIllIIlIIIllIlllIlIIlIIIllIIlIIIllIIllIIllIIIllIIlIIllIIlIIllIIlllIIIllIIllIlIIlllIIlIIIlllIlIIIll as BossBarSetting;

@Environment(EnvType.CLIENT)
@Mixin(BossBarHud.class)
public class BossBarHudMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void beforeRender(MatrixStack matrices, CallbackInfo callbackInfo) {
        BossBarSetting bossBarSetting = Melonity.CONFIG_MANAGER.getSetting(BossBarSetting.class).get();
        if (bossBarSetting.isEnabled() && bossBarSetting.getSetting().getValue().contains("Boss Bar")) {
            callbackInfo.cancel();
        }
    }
}