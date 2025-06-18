// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import ru.melonity.f.Setting;
import ru.melonity.o.Module;

@Environment(EnvType.CLIENT)
public class HitboxModifier extends Module {
    private final Setting<Float> sizeSetting = new Setting<>("global.size", number -> String.format("%.1f", number), 0.1f, 1.0f, 1.0f);
    private final Runnable hitboxUpdater = () -> {
        List<PlayerEntity> players = mc.world.getPlayers();
        Iterator<PlayerEntity> iterator = players.iterator();
        while (iterator.hasNext()) {
            PlayerEntity player = iterator.next();
            if (mc.player == player) continue;
            boolean expandMode = shouldExpandHitbox();
            if (expandMode) {
                double playerX = player.getX();
                Box playerBox = player.getBoundingBox();
                double minY = playerBox.minY;
                double playerZ = player.getZ();
                float size = sizeSetting.getValue();
                double minX = playerX - size;
                double minZ = playerZ - size;
                double maxX = playerX + size;
                double maxY = playerBox.maxY;
                double maxZ = playerZ + size;
                Box newBox = new Box(minX, minY, minZ, maxX, maxY, maxZ);
                player.setBoundingBox(newBox);
            } else {
                EntityPose pose = player.getPose();
                EntityDimensions dimensions = player.getDimensions(pose);
                float width = dimensions.width;
                float halfWidth = width / 2.0f;
                double minX = player.getX() - halfWidth;
                double minY = player.getY();
                double minZ = player.getZ() - halfWidth;
                Vec3d minCorner = new Vec3d(minX, minY, minZ);
                double maxX = player.getX() + halfWidth;
                float height = dimensions.height;
                double maxY = player.getY() + height;
                double maxZ = player.getZ() + halfWidth;
                Vec3d maxCorner = new Vec3d(maxX, maxY, maxZ);
                Box newBox = new Box(minCorner, maxCorner);
                player.setBoundingBox(newBox);
            }
        }
    };

    public HitboxModifier() {
        super("Hitbox", Category.COMBAT);
        this.addSetting(sizeSetting);
    }

    private boolean shouldExpandHitbox() {
        return true;
    }
}