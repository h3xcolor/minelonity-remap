// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.WorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ru.melonity.fabric.client.model.IClientWorld;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin implements IClientWorld {
    @Shadow
    @Final
    private WorldProperties worldProperties;

    @Override
    public void setTimeOfDay(long time) {
        this.worldProperties.setTimeOfDay(time);
    }
}