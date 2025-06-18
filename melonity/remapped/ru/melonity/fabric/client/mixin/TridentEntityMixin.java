// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;

@Environment(EnvType.CLIENT)
@Mixin(TridentEntity.class)
public class TridentEntityMixin {
    @Shadow
    private boolean inGround;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;getOwner()Lnet/minecraft/entity/Entity;"))
    public void onTick(CallbackInfo callbackInfo) {
        if (!this.inGround) {
            MinecraftClient client = MinecraftClient.getInstance();
            TridentEntity trident = (TridentEntity) (Object) this;
            if (client.player != null && client.player.distanceTo(trident) <= 20.0f) {
                Vec3d tridentVelocity = trident.getVelocity().normalize();
                Vec3d playerToTrident = new Vec3d(
                    client.player.getX() - trident.getX(),
                    client.player.getY() - trident.getY(),
                    client.player.getZ() - trident.getZ()
                ).normalize();
                double velocityAlignment = tridentVelocity.dotProduct(playerToTrident);
                if (velocityAlignment > 0.8) {
                    Melonity.triggerTridentAutoPickup();
                }
            }
        }
    }
}