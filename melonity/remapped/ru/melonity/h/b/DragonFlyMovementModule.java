// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1656;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import ru.melonity.f.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll;
import ru.melonity.f.b.IllIllIlIIIllIIlIIllIIlllIIlllIIllIlllIIlIIllIIlllIlIllIIIllIllIIlllIlllIIlIIlIllIIllIlllIlllIIlIIlIIlIIlIIlllIllIIlIllIIIlllIIllIIIlIIIlllIlIIIlIlIlIIllIIIlIIllIlIIlIlIllIIlllIIllIIIlllIIllIIlIllIllIIllIllIIlllIIlIll;
import ru.melonity.o.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;
import ru.melonity.o.b.a.b.IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll;

@Environment(value = EnvType.CLIENT)
public class DragonFlyMovementModule extends ru.melonity.h.c.ModuleBase {
    private final IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll globalSpeedSetting = new IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll("global.speed", number -> String.format("%.1f", Float.valueOf(number.floatValue())), Float.valueOf(0.1f), Float.valueOf(15.0f), Float.valueOf(3.0f));
    private final IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll verticalSpeedSetting = new IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll("dragonfly.speed_y", number -> String.format("%.1f", Float.valueOf(number.floatValue())), Float.valueOf(0.1f), 5, Float.valueOf(0.5f));
    private final IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll<IllIllIlIIIllIIlIIllIIlllIIlllIIllIlllIIlIIllIIlllIlIllIIIllIllIIlllIlllIIlIIlIllIIllIlllIlllIIlIIlIIlIIlIIlllIllIIlIllIIIlllIIllIIIlIIIlllIlIIIlIlIlIIllIIIlIIllIlIIlIlIllIIlllIIllIIIlllIIllIIlIllIllIIllIllIIlllIIlIll> movementEventHandler = event -> {
        if (!isFeatureEnabled()) {
            return;
        }
        class_1656 player = MinecraftClientInstance.player;
        if (player == null || player.field_7479) {
            return;
        }
        boolean jumping = MinecraftClientInstance.options.field_1903.method_1434();
        boolean sneaking = MinecraftClientInstance.options.field_1832.method_1434();
        if (jumping && !sneaking) {
            double verticalMovement = verticalSpeedSetting.getCurrentValue().floatValue();
            event.setMotionVector(event.getMotionVector().add(0.0, verticalMovement, 0.0));
        } else if (sneaking && !jumping) {
            double verticalMovement = verticalSpeedSetting.getCurrentValue().floatValue();
            event.setMotionVector(event.getMotionVector().add(0.0, -verticalMovement, 0.0));
        }
        float speed = globalSpeedSetting.getCurrentValue().floatValue();
        updatePlayerVelocity(event, speed);
    };

    public DragonFlyMovementModule() {
        super("DragonFly", ru.melonity.h.c.ModuleCategory.MOVEMENT);
        registerSetting(globalSpeedSetting);
        registerSetting(verticalSpeedSetting);
    }

    private void updatePlayerVelocity(IllIllIlIIIllIIlIIllIIlllIIlllIIllIlllIIlIIllIIlllIlIllIIIllIllIIlllIlllIIlIIlIllIIllIlllIlllIIlIIlIIlIIlIIlllIllIIlIllIIIlllIIllIIIlIIIlllIlIIIlIlIlIIllIIIlIIllIlIIlIlIllIIlllIIllIIIlllIIllIIlIllIllIIllIllIIlllIIlIll velocityHolder, double speed) {
        double forwardMovement = MinecraftClientInstance.player.forwardSpeed;
        double sidewaysMovement = MinecraftClientInstance.player.sidewaysSpeed;
        float playerYaw = MinecraftClientInstance.player.getYaw();
        if (forwardMovement == 0.0 && sidewaysMovement == 0.0) {
            velocityHolder.setMotionVector(new class_243(0.0, velocityHolder.getMotionVector().field_1351, 0.0));
        } else {
            if (forwardMovement != 0.0) {
                if (sidewaysMovement > 0.0) {
                    playerYaw += (float) (forwardMovement > 0.0 ? -45 : 45);
                } else if (sidewaysMovement < 0.0) {
                    playerYaw += (float) (forwardMovement > 0.0 ? 45 : -45);
                }
                sidewaysMovement = 0.0;
                if (forwardMovement > 0.0) {
                    forwardMovement = 1.0;
                } else if (forwardMovement < 0.0) {
                    forwardMovement = -1.0;
                }
            }
            double radYaw = Math.toRadians(playerYaw + 90.0f);
            double cosYaw = class_3532.method_15362((float) radYaw);
            double sinYaw = class_3532.method_15374((float) radYaw);
            double motionX = (forwardMovement * speed * cosYaw) + (sidewaysMovement * speed * sinYaw);
            double motionZ = (forwardMovement * speed * sinYaw) - (sidewaysMovement * speed * cosYaw);
            velocityHolder.setMotionVector(new class_243(motionX, velocityHolder.getMotionVector().field_1351, motionZ));
        }
    }
}