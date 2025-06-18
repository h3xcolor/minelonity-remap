// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.f.b.MovementConfig;
import ru.melonity.h.b.MovementEvent;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    protected Vec3d velocity;
    @Shadow
    private float stepHeight;
    @Shadow
    private int stepHeightTimer;

    @Shadow
    protected int getStepHeight() {
        return 1;
    }

    @Shadow
    protected void incrementStepHeight() {
    }

    @Shadow
    protected static Vec3d adjustMovementForCollisions(Vec3d movement, float friction, float gravity) {
        return null;
    }

    @Shadow
    private boolean wouldCollideAt(BlockPos pos, BlockState state, boolean ignoreCobweb, boolean ignoreClimbable, Vec3d movement) {
        return false;
    }

    @Shadow
    protected void incrementFallDistance() {
    }

    @Shadow
    protected void resetFallDistance() {
    }

    @Shadow
    protected Vec3d adjustMovementForPiston(Vec3d movement) {
        return null;
    }

    @Shadow
    protected Vec3d adjustMovementForCollisions(MovementType movementType, Vec3d movement) {
        return null;
    }

    @Shadow
    private Vec3d adjustMovementForSneaking(Vec3d movement) {
        return null;
    }

    @Shadow
    protected boolean isOnGround() {
        return false;
    }

    @Shadow
    protected void updateFallState() {
    }

    @Shadow
    protected float getSlipperiness() {
        return 0.0f;
    }

    @Shadow
    protected void onLanding(double heightDifference, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Shadow
    private boolean isClimbableBlock(BlockState state) {
        return false;
    }

    @Shadow
    protected float getStepHeight() {
        return 0.0f;
    }

    @Shadow
    protected Entity.PositionFlag getPositionFlag() {
        return null;
    }

    @Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
    public void updateVelocity(float friction, Vec3d movement, CallbackInfo callbackInfo) {
        Entity entity = (Entity) (Object) this;
        MovementConfig movementConfig = Melonity.getClient().getModuleManager().getModule(MovementConfig.class).orElse(null);
        if (movementConfig != null && movementConfig.isEnabled() && movementConfig.getSpeedFactor() != null) {
            Vec3d adjustedMovement = adjustMovementForCollisions(movement, friction, movementConfig.getSpeedFactor());
            entity.setVelocity(entity.getVelocity().add(adjustedMovement));
            callbackInfo.cancel();
            return;
        }
        Vec3d adjustedMovement = adjustMovementForCollisions(movement, friction, entity.getStepHeight());
        entity.setVelocity(entity.getVelocity().add(adjustedMovement));
        callbackInfo.cancel();
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(MovementType movementType, Vec3d movement, CallbackInfo callbackInfo) {
        Entity entity = (Entity) (Object) this;
        if (entity.isLogicalSideForUpdatingMovement()) {
            entity.setPosition(entity.getPos().add(movement));
        } else {
            double heightDifference;
            MovementEvent movementEvent;
            Box entityBox;
            boolean horizontalCollision;
            boolean verticalCollision;
            Vec3d adjustedMovement;
            entity.lastRenderX = entity.getX();
            if (movementType == MovementType.SELF && (movement = this.adjustMovementForPiston(movement)).equals(Vec3d.ZERO)) {
                return;
            }
            entity.getWorld().getProfiler().push("move");
            if (this.velocity.lengthSquared() > 1.0E-7) {
                movement = movement.add(this.velocity);
                this.velocity = Vec3d.ZERO;
                entity.setVelocity(Vec3d.ZERO);
            }
            movement = this.adjustMovementForCollisions(movementType, movement);
            Vec3d movementAfterCollision = this.adjustMovementForCollisions(movementType, movement);
            Vec3d movementAfterSneaking = this.adjustMovementForSneaking(movementAfterCollision);
            if (entity instanceof PlayerEntity) {
                adjustedMovement = entity.getPos().add(movementAfterSneaking);
                Vec3d currentPos = entity.getPos();
                verticalCollision = movementAfterCollision.y != movementAfterSneaking.y;
                horizontalCollision = !MathHelper.approximatelyEquals(movementAfterCollision.x, movementAfterSneaking.x) || !MathHelper.approximatelyEquals(movementAfterCollision.z, movementAfterSneaking.z);
                boolean falling = verticalCollision && movementAfterCollision.y < 0.0;
                entityBox = entity.getBoundingBox();
                movementEvent = new MovementEvent(currentPos, adjustedMovement, movement, falling, horizontalCollision, verticalCollision, entityBox);
                Melonity.getEventBus().post(movementEvent);
                movement = movementEvent.getMovement();
            }
            if ((heightDifference = (adjustedMovement = this.adjustMovementForSneaking(movement)).lengthSquared()) > 1.0E-7) {
                VoxelShape collisionShape;
                if (entity.horizontalCollision && heightDifference >= 1.0 && (collisionShape = entity.getWorld().getCollisions(entity, new VoxelShape(entity.getBoundingBox(), entity.getBoundingBox().stretch(adjustedMovement), VoxelShapes.CollisionType.COLLIDER, VoxelShape.BlockType.FULL, entity))).getResult() != VoxelShapes.ShapeType.COLLIDE) {
                    entity.onSwappedCollision();
                }
                entity.setPosition(entity.getPos().add(adjustedMovement));
            }
            entity.getWorld().getProfiler().pop();
            entity.getWorld().getProfiler().push("rest");
            horizontalCollision = !MathHelper.approximatelyEquals(movement.x, adjustedMovement.x);
            verticalCollision = !MathHelper.approximatelyEquals(movement.z, adjustedMovement.z);
            entity.horizontalCollision = horizontalCollision || verticalCollision;
            entity.verticalCollision = movement.y != adjustedMovement.y;
            entity.onGround = entity.verticalCollision && movement.y < 0.0;
            entity.verticalCollisionBelow = entity.horizontalCollision ? this.isOnGround() : false;
            entity.checkFallState(entity.onGround, adjustedMovement);
            BlockPos blockPos = entity.getLandingPos();
            BlockState blockState = entity.getWorld().getBlockState(blockPos);
            this.onLanding(adjustedMovement.y, entity.isOnGround(), blockState, blockPos);
            if (entity.isLogicalSideForUpdatingMovement()) {
                entity.getWorld().getProfiler().pop();
            } else {
                Entity.PositionFlag positionFlag;
                Vec3d velocity;
                if (entity.horizontalCollision) {
                    velocity = entity.getVelocity();
                    entity.setVelocity(horizontalCollision ? 0.0 : velocity.x, velocity.y, verticalCollision ? 0.0 : velocity.z);
                }
                velocity = blockState.getCollisionShape(entity.getWorld(), entity);
                if (movement.y != adjustedMovement.y) {
                    velocity = velocity.offset(entity.getWorld(), entity);
                }
                if (entity.isOnGround()) {
                    velocity.onEntityLand(entity.getWorld(), blockPos, blockState, entity);
                }
                if ((positionFlag = this.getPositionFlag()).isSet() && !entity.isSpectator()) {
                    double deltaX = adjustedMovement.x;
                    double deltaY = adjustedMovement.y;
                    double deltaZ = adjustedMovement.z;
                    entity.fallDistance += (float) (adjustedMovement.horizontalLength() * 0.6);
                    BlockPos currentBlockPos = entity.getBlockPos();
                    BlockState currentBlockState = entity.getWorld().getBlockState(currentBlockPos);
                    boolean isClimbable = this.isClimbableBlock(currentBlockState);
                    if (!isClimbable) {
                        deltaY = 0.0;
                    }
                    entity.slideDistance += (float) adjustedMovement.getZ() * 0.6f;
                    entity.distanceTraveled += (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 0.6f;
                    if (entity.distanceTraveled > this.stepHeight && !currentBlockState.isAir()) {
                        boolean sameBlock = currentBlockPos.equals(blockPos);
                        boolean collided = this.wouldCollideAt(blockPos, blockState, positionFlag.ignoreCobweb(), sameBlock, movement);
                        if (!sameBlock) {
                            collided |= this.wouldCollideAt(currentBlockPos, currentBlockState, false, positionFlag.ignoreClimbable(), movement);
                        }
                        if (collided) {
                            this.stepHeight = this.getStepHeight();
                        } else if (entity.isOnGround()) {
                            this.stepHeight = this.getStepHeight();
                            if (positionFlag.ignoreCobweb()) {
                                this.incrementFallDistance();
                            }
                            if (positionFlag.ignoreClimbable()) {
                                entity.setStatus(EntityStatuses.STEP_SOUND);
                            }
                        }
                    } else if (currentBlockState.isAir()) {
                        this.resetFallDistance();
                    }
                }
                this.updateFallState();
                float slipperiness = this.getSlipperiness();
                entity.setVelocity(entity.getVelocity().multiply(slipperiness, 1.0, slipperiness));
                if (entity.getWorld().getStatesInBox(entity.getBoundingBox().expand(1.0E-6)).noneMatch(state -> state.isIn(BlockTags.FIRE) || state.isOf(Blocks.LAVA))) {
                    if (this.stepHeightTimer <= 0) {
                        entity.setStepHeight(-this.getStepHeight());
                    }
                    if (entity.lastRenderX != null && (entity.isTouchingWater() || entity.isInLava())) {
                        this.incrementStepHeight();
                    }
                }
                if (entity.isOnGround() && (entity.isTouchingWater() || entity.isInLava())) {
                    entity.setStepHeight(-this.getStepHeight());
                }
                entity.getWorld().getProfiler().pop();
            }
        }
        callbackInfo.cancel();
    }
}