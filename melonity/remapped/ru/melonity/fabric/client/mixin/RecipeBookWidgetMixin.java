// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetMixin {

    @Inject(method = "setOpen", at = @At("HEAD"), cancellable = true)
    protected void preventRecipeBookToggle(boolean shouldOpen, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}