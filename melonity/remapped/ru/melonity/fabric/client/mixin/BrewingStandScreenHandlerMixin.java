// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ru.melonity.fabric.client.model.IBrewingStandScreenHandler;

@Environment(EnvType.CLIENT)
@Mixin(BrewingStandScreenHandler.class)
public class BrewingStandScreenHandlerMixin implements IBrewingStandScreenHandler {
    @Shadow
    @Final
    private PropertyDelegate propertyDelegate;

    @Override
    public PropertyDelegate propertyDelegate() {
        return this.propertyDelegate;
    }
}