// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import org.joml.Vector4d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.f.b.AttackEvent;
import ru.melonity.f.b.PlayerTickEvent;
import ru.melonity.fabric.client.model.ILivingEntity;
import ru.melonity.fabric.client.model.IPlayer;

@Environment(value = EnvType.CLIENT)
@Mixin(value = PlayerEntity.class)
public class PlayerEntityMixin implements IPlayer {
    @Unique
    private Vector4d espPosition;

    @Inject(method = "tickNewAi", at = @At(value = "RETURN"))
    protected void onTickNewAi(CallbackInfo callbackInfo) {
        ILivingEntity livingEntity = (ILivingEntity) this;
        livingEntity.setPitchHead(((PlayerEntity) (Object) this).getPitch());
    }

    @Inject(method = "attack", at = @At(value = "HEAD"))
    public void onAttack(Entity target, CallbackInfo callbackInfo) {
        AttackEvent attackEvent = new AttackEvent(false);
        Melonity.EVENT_BUS.getEventBus().post(attackEvent);
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!attackEvent.isCancelled() && player instanceof ClientPlayerEntity) {
            player.setSprinting(true);
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    public void onTick(CallbackInfo callbackInfo) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof ClientPlayerEntity) {
            PlayerTickEvent playerTickEvent = new PlayerTickEvent();
            Melonity.EVENT_BUS.getEventBus().post(playerTickEvent);
            if (playerTickEvent.isCancelled()) {
                callbackInfo.cancel();
            }
        }
    }

    @Override
    public Vector4d espPosition() {
        return this.espPosition;
    }

    @Override
    public void setEspPosition(Vector4d position) {
        this.espPosition = position;
    }
}