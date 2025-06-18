// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.a;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.joml.Vector3f;
import ru.melonity.Melonity;
import ru.melonity.h.b.IllIlIIllIIllIIllIIIlllIIIlIllIllIl极
import ru.melonity.j.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll;
import ru.melonity.o.b.a.b.IlIIIlIIllIlllIlIIIlIIlllIIlIllIIlIIllIIIlllIIlllIIIlIIIlIIllIlllIIllIllIIIlllIlIllIIIlllIIlllIlIIllIIIllIlIIlIlllIllIIlIIllIllIlIlIIlIIIllIIllIIlIllIIllIIlIIlllIllIIIllIlllIIllIlllIlIIlIllIIlllIIIllIlllIIIllIIlIIllIlllIIll;
import ru.melonity.w.IIllIllIlIlllIIlllIIlllIlIIIllIIlIIllIIllIIlIIIlllIIIlIIlllIIlllIIIlIlIIlIIllIlIIIllIIlIIllIIlIIlIIlllIIIllIlllIIlIIlllIllIllIIlllIlllIIlllIlIIIllIIllIlIlIIIllIlIIIlIIIllIIllIIlIlllIllIllIIIlIIIlIIllIIllIlIIIlIIIlIIllIIIllIIlll;

@Environment(value = EnvType.CLIENT)
public final class CombatUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    public static int f0c412f520c914574811e561d7a8ec0b5 = 797377299;

    public static float normalizeAngle(float angle) {
        float step = getRotationStep();
        angle -= angle % step;
        return angle;
    }

    public static float roundToRotationStep(float angle) {
        return getRoundedRotationStepCount(angle) * getRotationStep();
    }

    public static float getRoundedRotationStepCount(float angle) {
        return Math.round(angle / getRotationStep());
    }

    public static float adjustAngleForAimAssist(float angle) {
        IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIII极
        Optional<IllIlIIllIIllIIllIIIlllIIIlIllIllIlIIlIIllIlllIIIllIlllIIlIllIIlllIIlllIlllIllIIIlllIllIllIlIIllIIllIIllIllIIIllIIIlIIIllIIlIllIlIIIlIIllIllIIIllIllIIlllIIlIllIIlIIIlIIlIllIllIlIlllIIlllIIlIIlllIllIIllIlIIllIlIIllIIIll> optional = aimAssistModule.getModule(IllIlIIllIIllIIllIIIlllIIIlIllIllIlIIlIIllIlllIIIllIlllIIlIllIIlllIIlllIlllIllIIIlllIllIllIlIIllIIllIIllIllIIIllIIIlIIIllIIlIllIlIIIlIIllIllIIIllIllIIlllIIlIllIIlIIIlIIlIllIllIlIlllIIlllIIlIIlllIllIIllIlIIllIlIIllIIIll.class);
        IllIlIIllIIll极
        if (aimAssistEnabled) {
            float adjustedAngle = roundToRotationStep(angle);
            angle = adjustedAngle;
        } else {
            float step = getRotationStep();
            angle -= angle % step;
        }
        return angle;
    }

    public static float adjustYawForAimAssist(float yaw, float pitch) {
        return adjustAngleForAimAssist(MathHelper.wrapDegrees(MathHelper.atan2(pitch, yaw) * 57.2957763671875f - 90.0f - mc.player.getYaw()));
    }

    public static float adjustPitchForAimAssist(float yaw, float pitch) {
        return adjustAngleForAimAssist(MathHelper.wrapDegrees(MathHelper.atan2(pitch, yaw) * 57.2957763671875f - 90.0f - mc.player.getPitch()));
    }

    public static float getRotationStep() {
        double sensitivity = (Double) mc.options.getMouseSensitivity().getValue();
        double adjustedSensitivity = sensitivity * 0.6 + 0.2;
        return (float) (adjustedSensitivity * adjustedSensitivity * adjustedSensitivity * 8.0 * 0.15);
    }

    public static Vec2f addJitterToPosition(float x, float y) {
        float timeFactor = (float) Math.cos(System.currentTimeMillis() / 90.0);
        timeFactor -= (float) Math.sin(System.currentTimeMillis() / 90.0);
        timeFactor = (float) Math.tan(timeFactor);
        float xOffset = (float) Math.ceil(2.0f * timeFactor) * 2.0f;
        float yOffset = (float) Math.ceil(2.0f * timeFactor);
        xOffset = (float) IIllIllIlIlllIIlllIIlllIlIIIllIIlIIllIIllIIlIIIlllIIIlIIlllIIlllIIIlIlIIlIIllIlIIIllIIlIIllIIlIIlIIlllIIIllIlllIIlIIlllIllIllIIlllIlllIIlllIlIIIllIIllIlIlIIIllIlIIIlIIIllIIllIIlIlllIllIllIIIlIIIlIIllIIllIlIIIlIIIlIIllIIIllIIlll.randomRange(-xOffset, xOffset, Math.random());
        yOffset = (float) IIllIllIlIlllIIlllIIlllIlIIIllIIlIIllIIllIIlIIIlllIIIlIIlllIIlllIIIlIlIIlIIllIlIIIllIIlIIllIIlIIlIIlllIIIllIlllIIlIIlllIllIllIIlllIlllIIlllIlIIIllIIllIlIlIIIllIlIIIlIIIllIIllIIlIlllIllIllIIIlIIIlIIllIIllIlIIIlIIIlIIllIIIllIIlll.randomRange(-yOffset, yOffset, Math.random());
        return new Vec2f(x + xOffset * 0.15f, y + yOffset * 0.15f);
    }

    public static float adjustAngleForJitter(float angle, boolean reduce) {
        float absAngle = Math.abs(angle);
        float jitterAmount = IIllIllIlIlllIIlllIIlllIlIIIllIIlIIllIIllIIlIIIlllIIIlIIlllIIlllIIIlIlIIlIIllIlIIIllIIlIIllIIlIIlIIlllIIIllIlllIIlIIlllIllIllIIlllIlllIIlllIlIIIllIIllIlIlIIIllIlIIIlIIIllIIllIIlIlllIllIllIIIlIIIlIIllIIllIlIIIlIIIlIIllIIIllIIlll.randomRange(6.0f, 7.0f);
        float maxAdjustment = Math.min(absAngle, 0.0013888889f * jitterAmount * 150.0f * 120.0f / (reduce ? 1 : 5));
        float adjustedAngle = maxAdjustment;
        angle = angle > 0.0f ? adjustedAngle : -adjustedAngle;
        return angle;
    }

    public static Vec2f addRandomOffsetToAngles(float yaw, float pitch) {
        float baseOffset = IIllIllIlIlllIIlllIIlllIlIIIllIIlIIllIIllIIlIIIlllIIIlIIlllIIlllIIIlIlIIlIIllIlIIIllIIlIIllIIlIIlIIlllIIIllIlllIIlIIlllIllIllIIlllIlllIIlllIlIIIllIIllIlIlIIIllIlIIIlIIIllIIllIIlIlllIllIllIIIlIIIlIIllIIllIlIIIlIIIlIIllIIIllIIlll.randomRange(0.1f, 0.8f);
        double offsetVariation = Math.pow(IIllIllIlIlllIIlllIIlllIlIIIllIIlIIllIIllIIlIIIlllIIIlIIlllIIlllIIIlIlIIlIIllIlIIIllIIlIIllIIlIIlIIlllIIIllIlllIIlIIlllIllIllIIlllIlllIIlllIlIIIllIIllIlIlIIIllIlIIIlIIIllIIllIIlIlllIllIllIIIlIIIlIIllIIllIlIIIlIIIlIIllIIIllIIlll.randomRange(-0.3f, 0.3f), 3.0);
        float totalOffset = (float) (baseOffset + offsetVariation);
        if (Math.abs(yaw) > 0.0f && Math.abs(pitch) == 0.0f) {
            pitch += totalOffset;
        }
        if (Math.abs(pitch) > 0.0f && Math.abs(yaw) == 0.0f) {
            yaw += totalOffset;
        }
        return new Vec2f(yaw, pitch);
    }

    public static float clampAngle(float angle) {
        float range = 50.0f * (1.0f - mc.player.getMovementSpeed(1.0f));
        return MathHelper.clamp(angle, angle - range, angle + range);
    }

    public static float getAngleToEntity(Entity entity) {
        double deltaX = entity.getX() - mc.player.getX();
        double deltaZ = entity.getZ() - mc.player.getZ();
        return (float) Math.abs(MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0 - mc.player.getYaw()));
    }

    public static float getYawAngleToEntity(Entity entity) {
        double deltaX = entity.getX() - mc.player.getX();
        double deltaZ = entity.getZ() - mc.player.getZ();
        return (float) Math.abs(MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0 - mc.player.getHeadYaw()));
    }

    public static boolean canSeePosition(LivingEntity entity, Vector3f position, float range) {
        if (entity == null) return false;
        Entity hitEntity = raycastEntity(entity, position.x, position.y, range);
        return hitEntity != null;
    }

    public static boolean canSeeBlock(Vec3d targetPos) {
        Vec3d eyePos = mc.player.getEyePos();
        PlayerEntity player = mc.player;
        float eyeHeight = player.getEyeHeight(player.getPose());
        Vec3d adjustedEyePos = eyePos.add(0.0, eyeHeight, 0.0);
        World world = mc.world;
        RaycastContext context = new RaycastContext(adjustedEyePos, targetPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player);
        HitResult hitResult = world.raycast(context);
        return hitResult.getType() == HitResult.Type.MISS;
    }

    public static float clampAngleToTarget(float angle, float target) {
        float range = 50.0f * (1.0f - mc.player.getMovementSpeed(1.0f));
        return MathHelper.clamp(target, angle - range, angle + range);
    }

    public static boolean canSeeEntity(Entity entity) {
        World world = mc.world;
        Vec3d eyePos = mc.player.getEyePos();
        PlayerEntity player = mc.player;
        float eyeHeight = player.getEyeHeight(player.getPose());
        Vec3d adjustedEyePos = eyePos.add(0.0, eyeHeight, 0.0);
        Vec3d entityPos = entity.getPos();
        float entityEyeHeight = entity.getEyeHeight(entity.getPose());
        Vec3d adjustedEntityPos = entityPos.add(0.0, entityEyeHeight, 0.0);
        RaycastContext context = new RaycastContext(adjustedEyePos, adjustedEntityPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player);
        HitResult hitResult = world.raycast(context);
        return hitResult.getType() == HitResult.Type.MISS;
    }

    public static boolean isValidTarget(LivingEntity entity, float maxDistance, TargetFilter filter) {
        if (entity == null) return false;
        if (entity instanceof PlayerEntity) return false;
        if (entity.age < 3) return false;
        float distance = mc.player.distanceTo(entity);
        if (distance >= maxDistance) return false;
        if (!entity.isAlive()) return false;
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            if (filter.isFriend(playerEntity.getEntityName())) return false;
            if (playerEntity.getEntityName().equalsIgnoreCase(mc.player.getEntityName())) return false;
            if (!filter.targetPlayers()) return false;
            if (playerEntity.getInventory().armor.get(3).isEmpty() && !filter.targetNaked()) return false;
            if (playerEntity.isInvisible() && !filter.targetInvisibles()) return false;
        }
        if (entity instanceof HostileEntity && !filter.targetMobs()) return false;
        if (entity instanceof AnimalEntity && !filter.targetAnimals()) return false;
        if (entity.isSpectator()) return false;
        if (!entity.isAlive()) return false;
        if (entity instanceof ArmorStandEntity) return false;
        return true;
    }

    @Generated
    private CombatUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}