// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.RaycastContext;
import ru.melonity.f.event.EventCallback;
import ru.melonity.f.system.Module;
import ru.melonity.f.system.Settings;
import ru.melonity.f.utils.MathUtils;

@Environment(value=EnvType.CLIENT)
public class AutoFishModule extends Module {
    private final EventCallback<Settings.TickSetting> updateCallback = setting -> {
        boolean shouldActivate = this.shouldActivate();
        if (!shouldActivate) {
            return;
        }
        float closestDistance = -1.0f;
        PlayerEntity targetPlayer = null;
        ClientWorld world = mc.world;
        List<Entity> entities = world.getEntities();
        Iterator<Entity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            Entity entity = entityIterator.next();
            PlayerEntity otherPlayer = (PlayerEntity) entity;
            if (otherPlayer == mc.player) {
                continue;
            }
            if (closestDistance != -1.0f) {
                float currentDistance = otherPlayer.distanceTo(mc.player);
                if (currentDistance >= closestDistance) {
                    continue;
                }
            }
            float distance = otherPlayer.distanceTo(mc.player);
            closestDistance = distance;
            targetPlayer = otherPlayer;
        }
        if (targetPlayer != null) {
            int fishingRodSlot = MathUtils.getSlotForItem(Items.FISHING_ROD);
            if (fishingRodSlot != -1 && closestDistance <= 3.6f) {
                ClientPlayNetworkHandler networkHandler = mc.player.networkHandler;
                PlayerActionC2SPacket castPacket = new PlayerActionC2SPacket(fishingRodSlot);
                networkHandler.sendPacket(castPacket);
                
                ClientWorld clientWorld = mc.world;
                double viewX = mc.player.getX();
                double eyeY = mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose());
                ClientPlayerEntity player = mc.player;
                Vec3f pitchYawVector = player.getRotationVecClient();
                float clientPitch = player.getPitch(pitchYawVector);
                double viewZ = mc.player.getZ();
                Vec3d viewPos = new Vec3d(viewX, eyeY, viewZ);
                
                double targetX = targetPlayer.getX();
                double targetY = targetPlayer.getY() - 0.2;
                double targetZ = targetPlayer.getZ();
                Vec3d targetPos = new Vec3d(targetX, targetY, targetZ);
                
                RaycastContext raycastContext = new RaycastContext(viewPos, targetPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player);
                HitResult hitResult = clientWorld.raycast(raycastContext);
                if (hitResult instanceof EntityHitResult entityHitResult) {
                    Entity hitEntity = entityHitResult.getEntity();
                    if (hitEntity instanceof PlayerEntity) {
                        player.setCurrentHand(Hand.MAIN_HAND);
                        mc.interactionManager.interactBlock(player, Hand.MAIN_HAND, (BlockHitResult) hitResult);
                        
                        BlockPos hitBlockPos = ((BlockHitResult) hitResult).getBlockPos();
                        BlockPos adjacentPos = hitBlockPos.up();
                        BlockState adjacentState = clientWorld.getBlockState(adjacentPos);
                        Block adjacentBlock = adjacentState.getBlock();
                        if (adjacentBlock != Blocks.AIR) {
                            player.setCurrentHand(Hand.OFF_HAND);
                            mc.interactionManager.interactBlock(player, Hand.OFF_HAND, (BlockHitResult) hitResult);
                            
                            int hitX = hitBlockPos.getX();
                            int hitZ = hitBlockPos.getZ();
                            double angleYaw = Math.atan2(hitX, hitZ);
                            double yawDegrees = Math.toDegrees(angleYaw);
                            float normalizedYaw = (float) MathHelper.wrapDegrees(yawDegrees - 90.0);
                            
                            int hitY = hitBlockPos.getY();
                            double hitYPos = hitY + 1;
                            double hitXDist = hitBlockPos.getX();
                            double hitZDist = Math.hypot(hitYPos, hitXDist);
                            double anglePitch = Math.atan2(hitBlockPos.getY(), hitZDist);
                            double pitchDegrees = Math.toDegrees(anglePitch);
                            float normalizedPitch = (float) (-pitchDegrees);
                            
                            ClientPlayNetworkHandler network = mc.player.networkHandler;
                            boolean isOnGround = mc.player.isOnGround();
                            PlayerMoveC2SPacket.LookOnly lookPacket = new PlayerMoveC2SPacket.LookOnly(normalizedYaw, normalizedPitch, isOnGround);
                            network.sendPacket(lookPacket);
                            
                            double eyePosY = eyeY;
                            Vec3d eyePos = new Vec3d(viewX, eyePosY, viewZ);
                            double playerTopY = targetY + 1.0 - 0.2;
                            Vec3d hitPoint = new Vec3d(targetX, playerTopY, targetZ + 1.0);
                            RaycastContext recheckContext = new RaycastContext(eyePos, hitPoint, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player);
                            HitResult recheckHit = clientWorld.raycast(recheckContext);
                            
                            player.setCurrentHand(Hand.MAIN_HAND);
                            mc.interactionManager.interactBlock(player, Hand.MAIN_HAND, (BlockHitResult) recheckHit);
                            
                            int newHitX = hitBlockPos.getX();
                            int newHitZ = hitBlockPos.getZ();
                            double newYawAngle = Math.atan2(newHitX, newHitZ);
                            double newYawDegrees = Math.toDegrees(newYawAngle);
                            normalizedYaw = (float) MathHelper.wrapDegrees(newYawDegrees - 90.0);
                            
                            int newHitY = hitBlockPos.getY();
                            double newYPos = newHitY + 2;
                            double xDist = hitBlockPos.getX();
                            double zDist = Math.hypot(newYPos, xDist);
                            double newPitchAngle = Math.atan2(newHitY, zDist);
                            double newPitchDegrees = Math.toDegrees(newPitchAngle);
                            normalizedPitch = (float) (-newPitchDegrees);
                            
                            ClientPlayNetworkHandler networkHandlerRe = mc.player.networkHandler;
                            boolean onGroundStatus = mc.player.isOnGround();
                            PlayerMoveC2SPacket.LookOnly newLookPacket = new PlayerMoveC2SPacket.LookOnly(normalizedYaw, normalizedPitch, onGroundStatus);
                            networkHandlerRe.sendPacket(newLookPacket);
                        }
                    }
                }
                FishingBobberEntity fishingBobber = player.getFishingBobber();
                PlayerActionC2SPacket reelPacket = new PlayerActionC2SPacket(fishingBobber.getId());
                networkHandler.sendPacket(reelPacket);
            }
        }
    };
    
    public AutoFishModule() {
        super("Surround", Module.Categories.COMBAT);
    }
}