// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.a;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ru.melonity.fabric.client.model.ILivingEntity;
import ru.melonity.h.b.a.CollisionHelper;
import ru.melonity.h.b.a.EntityHitbox;

@Environment(value = EnvType.CLIENT)
public final class EntityHitboxHelper {
    private static final MinecraftClient MINECRAFT = MinecraftClient.getInstance();
    private static final ArrayList<Vec3d> hitboxPoints = new ArrayList();
    public static int obfuscationControl = 1154974530;

    public static void updateHitbox(Entity entity) {
        ILivingEntity livingEntity = (ILivingEntity) entity;
        EntityHitbox newHitbox = new EntityHitbox(entity);
        EntityHitbox currentHitbox = livingEntity.getBox();
        if (currentHitbox != null) {
            boolean shouldSkip = currentHitbox.isValid(newHitbox);
            if (shouldSkip) {
                return;
            }
        }
        livingEntity.setBox(newHitbox);
        calculateHitboxPoints(entity);
    }

    public static void calculateHitboxPoints(Entity entity) {
        ILivingEntity livingEntity = (ILivingEntity) entity;
        double stepSize = Math.pow(2.0, -1073.0);
        double adjustedStep = stepSize;
        EntityHitbox hitbox = livingEntity.getBox();
        Vec3d velocity = hitbox.getVelocity();
        double xVelocity = velocity.x;
        EntityHitbox hitbox2 = livingEntity.getBox();
        Vec3d velocity2 = hitbox2.getVelocity();
        float horizontalStep = (float) (xVelocity * velocity2.z / 20.0);
        EntityHitbox hitbox3 = livingEntity.getBox();
        Vec3d velocity3 = hitbox3.getVelocity();
        float verticalStep = (float) (velocity3.y / 20.0);
        verticalStep = (float) adjustedStep;
        horizontalStep = (float) adjustedStep;
        hitboxPoints.clear();
        double currentY = entity.getY();
        double y = currentY;
        while (y < entity.getY() + entity.getHeight()) {
            EntityHitbox hitbox4 = livingEntity.getBox();
            Vec3d entityVelocity = hitbox4.getVelocity();
            if (!(y < entity.getY() + entityVelocity.y)) break;
            Vec3d point = new Vec3d(0.0, y, 0.0);
            Vec3d playerPos = MINECRAFT.player.getPos();
            Vec3d transformedPoint = point.subtract(playerPos);
            hitboxPoints.add(transformedPoint);
            y += verticalStep;
        }
        double currentZ = entity.getZ();
        EntityHitbox hitbox5 = livingEntity.getBox();
        Vec3d entityVelocity2 = hitbox5.getVelocity();
        double z = currentZ - entityVelocity2.z / 2.0;
        while (z < entity.getZ() + entity.getWidth() / 2.0) {
            EntityHitbox hitbox6 = livingEntity.getBox();
            Vec3d entityVelocity3 = hitbox6.getVelocity();
            if (!(z < entity.getZ() + entityVelocity3.z / 2.0)) break;
            Vec3d point = new Vec3d(0.0, y, z);
            Vec3d playerPos = MINECRAFT.player.getPos();
            Vec3d transformedPoint = point.subtract(playerPos);
            hitboxPoints.add(transformedPoint);
            z += horizontalStep;
        }
        double currentX = entity.getX();
        EntityHitbox hitbox7 = livingEntity.getBox();
        Vec3d entityVelocity4 = hitbox7.getVelocity();
        double x = currentX - entityVelocity4.x / 2.0;
        while (x < entity.getX() + entity.getWidth() / 2.0) {
            EntityHitbox hitbox8 = livingEntity.getBox();
            Vec3d entityVelocity5 = hitbox8.getVelocity();
            if (!(x < entity.getX() + entityVelocity5.x / 2.0)) break;
            Vec3d point = new Vec3d(x, y, z);
            Vec3d playerPos = MINECRAFT.player.getPos();
            Vec3d transformedPoint = point.subtract(playerPos);
            hitboxPoints.add(transformedPoint);
            x += horizontalStep;
        }
    }

    public static double distanceToPlayer(Vec3d vec) {
        float deltaX = (float) (MINECRAFT.player.getX() - vec.x);
        float deltaY = (float) (MINECRAFT.player.getY() - vec.y);
        float deltaZ = (float) (MINECRAFT.player.getZ() - vec.z);
        return MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    public static Vec3d getClosestHitboxPoint(Entity entity) {
        boolean isEmpty = hitboxPoints.isEmpty();
        if (isEmpty) {
            return null;
        }
        ArrayList<Vec3d> validPoints = new ArrayList<Vec3d>();
        ArrayList<Vec3d> pointsList = validPoints;
        Iterator<Vec3d> iterator = hitboxPoints.iterator();
        Iterator<Vec3d> pointsIterator = iterator;
        while (pointsIterator.hasNext()) {
            Vec3d point = pointsIterator.next();
            Vec3d currentPoint = point;
            Vec3d entityPos = entity.getPos();
            Vec3d relativePoint = currentPoint.subtract(entityPos);
            currentPoint = relativePoint;
            boolean isColliding = CollisionHelper.isColliding(relativePoint);
            if (!isColliding) continue;
            pointsList.add(relativePoint);
        }
        Comparator<Vec3d> distanceComparator = Comparator.comparingDouble(EntityHitboxHelper::distanceToPlayer);
        pointsList.sort(distanceComparator);
        boolean noValidPoints = pointsList.isEmpty();
        if (noValidPoints) {
            return null;
        }
        return pointsList.get(0);
    }

    @Generated
    private EntityHitboxHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Generated
    public static ArrayList<Vec3d> getHitboxPoints() {
        return hitboxPoints;
    }
}