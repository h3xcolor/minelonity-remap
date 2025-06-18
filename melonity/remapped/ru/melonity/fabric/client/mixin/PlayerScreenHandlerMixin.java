// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ru.melonity.fabric.client.model.IPlayerScreenHandler;

@Environment(EnvType.CLIENT)
@Mixin(PlayerScreenHandler.class)
public class PlayerScreenHandlerMixin implements IPlayerScreenHandler {
    @Shadow
    @Final
    private CraftingResultSlot resultSlot;

    @Override
    public CraftingResultSlot getCraftingResultSlot() {
        return this.resultSlot;
    }
}