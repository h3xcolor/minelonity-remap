// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import ru.melonity.f.event.EventHandler;
import ru.melonity.f.event.Listener;
import ru.melonity.f.module.Module;
import ru.melonity.f.setting.BooleanSetting;
import ru.melonity.f.setting.EnumSetting;
import ru.melonity.f.setting.Setting;
import ru.melonity.f.util.Timer;

@Environment(EnvType.CLIENT)
public class TriggerBotModule extends Module {
    private final EnumSetting<TargetMode> targetModeSetting = new EnumSetting<>("triggerbot.target_mode", TargetMode.HOVER, TargetMode.values());
    private final BooleanSetting invisiblesSetting = new BooleanSetting("triggerbot.invisibles", false);
    private final BooleanSetting onlyCritsSetting = new BooleanSetting("triggerbot.only_crits", true);
    private PlayerEntity target;
    private final Timer attackTimer = new Timer();
    private final Listener<AttackEvent> attackEventListener = event -> {
        if (!isEnabled()) return;
        
        if (targetModeSetting.getValue() == TargetMode.SINGLE) {
            PlayerEntity potentialTarget = findTarget();
            this.target = potentialTarget;
        }
    };
    private final Listener<UpdateEvent> updateEventListener = event -> {
        if (!isEnabled()) return;
        
        if (targetModeSetting.getValue() == TargetMode.HOVER) {
            PlayerEntity potentialTarget = findTarget();
            this.target = potentialTarget;
        }
        
        if (target == null) return;
        
        if (targetModeSetting.getValue() == TargetMode.SINGLE) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.crosshairTarget == null) return;
            if (client.crosshairTarget.getType() != HitResult.Type.ENTITY) return;
            EntityHitResult entityHit = (EntityHitResult) client.crosshairTarget;
            if (entityHit.getEntity().getId() != target.getId()) return;
        }
        
        if (attackTimer.hasElapsed(150L)) {
            float attackCooldown = MinecraftClient.getInstance().player.getAttackCooldownProgress(0.5f);
            if (attackCooldown < 0.93f) return;
            
            if (MinecraftClient.getInstance().player.getAttackCooldownProgress(0.0f) < 0.2f) {
                if (onlyCritsSetting.getValue()) return;
            }
            
            MinecraftClient.getInstance().interactionManager.attackEntity(MinecraftClient.getInstance().player, target);
            MinecraftClient.getInstance().player.swingHand(Hand.MAIN_HAND);
            attackTimer.reset();
        }
    };

    public TriggerBotModule() {
        super("TriggerBot", Module.Category.COMBAT);
        addSetting(targetModeSetting);
        addSetting(invisiblesSetting);
        addSetting(onlyCritsSetting);
        addEvent(AttackEvent.class, attackEventListener);
        addEvent(UpdateEvent.class, updateEventListener);
    }

    private PlayerEntity findTarget() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.crosshairTarget == null) return null;
        if (client.crosshairTarget.getType() != HitResult.Type.ENTITY) return null;
        
        EntityHitResult entityHit = (EntityHitResult) client.crosshairTarget;
        Entity entity = entityHit.getEntity();
        
        if (entity.isInvisible() && !invisiblesSetting.getValue()) return null;
        if (!entity.isAttackable()) return null;
        if (!(entity instanceof PlayerEntity)) return null;
        
        return (PlayerEntity) entity;
    }

    private enum TargetMode {
        HOVER, SINGLE
    }
}


class AttackEvent {}
class UpdateEvent {}