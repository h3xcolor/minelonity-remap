// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_638;
import net.minecraft.class_746;
import ru.melonity.f.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll;
import ru.melonity.f.b.IllIllIlIIIllIIlIIllIIlllIIlllIIllIlllIIlIIllIIlllIlIllIIIllIllIIlllIlllIIlIIlIllIIllIlllIlllIIlIIlIIlIIlIIlllIllIIlIllIIIlllIIllIIIlIIIlllIlIIIlIlIlIIllIIIlIIllIlIIlIlIllIIlllIIllIIIlllIIllIIlIllIllIIllIllIIlllIIlIll;
import ru.melonity.o.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;

@Environment(EnvType.CLIENT)
public class NoClipMod extends ru.melonity.h.c.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    private final IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll<IllIllIlIIIllIIlIIllIIlllIIlllIIllIlllIIlIIllIIlllIlIllIIIllIllIIlllIlllIIlIIlIllIIllIlllIlllIIlIIlIIlIIlIIlllIllIIlIllIIIlllIIllIIIlIIIlllIlIIIlIlIlIIllIIIlIIllIlIIlIlIllIIlllIIllIIIlllIIllIIlIllIllIIllIllIIlllIIlIll> movementListener = event -> {
        if (isColliding(event.getPosition())) {
            return;
        }
        if (event.isSneaking()) {
            event.cancelMovement();
        }
        if (event.getY() <= 0.0) {
            if (mc.player.isOnGround()) {
                event.setOnGround();
            }
        }
        double x = event.getX();
        double y = Math.min(event.getY(), 99999.0);
        double z = event.getZ();
        class_243 newPosition = new class_243(x, y, z);
        event.setPosition(newPosition);
    };

    public NoClipMod() {
        super("NoClip", IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll);
    }

    private boolean isColliding(class_243 position) {
        class_638 world = mc.world;
        class_746 player = mc.player;
        class_238 playerBoundingBox = player.method_5829();
        class_238 adjustedBoundingBox = playerBoundingBox.method_1014(-0.0625);
        boolean collisionBefore = world.method_39454(player, adjustedBoundingBox);
        double originalX = player.method_23317();
        double originalY = player.method_23318();
        double originalZ = player.method_23321();
        class_243 originalPosition = new class_243(originalX, originalY, originalZ);
        player.method_23327(position.field_1352, position.field_1351, position.field_1350);
        class_238 newBoundingBox = player.method_5829();
        class_238 newAdjustedBoundingBox = newBoundingBox.method_1014(-0.0625);
        boolean collisionAfter = world.method_39454(player, newAdjustedBoundingBox) && collisionBefore;
        player.method_23327(originalPosition.field_1352, originalPosition.field_1351, originalPosition.field_1350);
        return collisionAfter;
    }
}