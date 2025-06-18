// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_1657;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_4604;
import net.minecraft.class_746;
import ru.melonity.f.NumberSetting;
import ru.melonity.f.BooleanSetting;
import ru.melonity.f.Callback;
import ru.melonity.fabric.client.model.ILevelRenderer;
import ru.melonity.o.Module;
import ru.melonity.o.b.a.b.RotationUpdateEvent;

@Environment(value=EnvType.CLIENT)
public class ProjectileHelper extends Module {
    private final NumberSetting rangeSetting = new NumberSetting("global.range", number -> String.format("%.1f", Float.valueOf(number.floatValue())), 2.8, 200, 10);
    private final BooleanSetting clientLookSetting = new BooleanSetting("global.clientlook", false);
    private final Callback<RotationUpdateEvent> rotationCallback = this::onRotationUpdate;
    public static int dummyConstant = 871819157;

    public ProjectileHelper() {
        super("ProjectileHelper", rotationCallback);
        registerSetting(rangeSetting);
        registerSetting(clientLookSetting);
    }

    private class_1309 findNearestEntity() {
        class_243 playerPos = Module.mc.field_1724.method_33571();
        class_243 playerViewVector = Module.mc.field_1724.method_5663().method_1029();
        return Lists.newArrayList(Module.mc.field_1687.method_18112()).stream()
            .filter(entity -> entity instanceof class_1309)
            .map(entity -> (class_1309) entity)
            .filter(this::isValidTarget)
            .min(Comparator.comparingDouble(entity -> {
                class_243 relativePos = entity.method_33571().method_1020(playerPos).method_1029();
                return 1.0 - playerViewVector.method_1026(relativePos);
            }).thenComparingDouble(entity -> Module.mc.field_1724.method_5739(entity)))
            .orElse(null);
    }

    private boolean isValidTarget(class_1309 entity) {
        if (entity == Module.mc.field_1724) {
            return false;
        }
        boolean isAlive = entity.method_5805();
        if (!isAlive) {
            return false;
        }
        if (entity instanceof class_1531) {
            return false;
        }
        if (!(entity instanceof class_1657)) {
            return false;
        }
        class_4604 frustum = ((ILevelRenderer) Module.mc.field_1769).getFrustum();
        class_238 boundingBox = entity.method_5829();
        boolean inFrustum = frustum.method_23093(boundingBox);
        if (!inFrustum) {
            return false;
        }
        float distance = Module.mc.field_1724.method_5739(entity);
        float maxRange = rangeSetting.getValue().floatValue();
        return distance < maxRange;
    }

    private void onRotationUpdate(RotationUpdateEvent event) {
        if (!isEnabled()) {
            return;
        }
        class_1799 heldItem = Module.mc.field_1724.method_6030();
        if (heldItem.method_7960()) {
            return;
        }
        boolean isThrowable = heldItem.method_7909();
        class_1802 itemType = heldItem.method_7909();
        boolean isBow = itemType instanceof class_1753;
        boolean isCrossbow = false;
        if (itemType instanceof class_1764) {
            isCrossbow = class_1764.method_7781(heldItem);
        }
        boolean isTrident = itemType == class_1802.field_8547;
        if (!(isBow || isCrossbow || isTrident)) {
            return;
        }
        class_1309 targetEntity = findNearestEntity();
        if (targetEntity == null) {
            return;
        }
        double targetX = targetEntity.method_23317();
        double playerX = Module.mc.field_1724.method_23317();
        float deltaX = (float)(targetX - playerX);
        double targetZ = targetEntity.method_23321();
        double playerZ = Module.mc.field_1724.method_23321();
        float deltaZ = (float)(targetZ - playerZ);
        double targetY = targetEntity.method_23318();
        double playerY = Module.mc.field_1724.method_23318();
        float deltaY = (float)(targetY - playerY);
        double horizontalDistance = Math.hypot(deltaX, deltaZ);
        double yawAngle = Math.atan2(deltaZ, deltaX);
        float yaw = (float) Math.toDegrees(yawAngle) - 90.0f;
        double pitchAngle = Math.atan2(deltaY, horizontalDistance);
        float pitch = (float) Math.toDegrees(-pitchAngle);
        if (clientLookSetting.getValue()) {
            Module.mc.field_1724.method_36456(yaw);
            Module.mc.field_1724.method_5636(yaw);
            Module.mc.field_1724.method_36457(pitch);
        }
        Module.mc.field_1724.method_5847(yaw);
        event.setYaw(yaw);
        event.setPitch(pitch);
    }
}