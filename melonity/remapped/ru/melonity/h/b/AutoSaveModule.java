// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HeldItemChangeC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import ru.melonity.o.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIll极lIlllIllIIIllIIlIIlllIllIIlIIIlIIllII极IIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;
import ru.melonity.w.IlIIIlIIllIlllIlIIIlIIlllIIlIllIIlIIllIIIlllIIlllIIIlIIIlIIllIlllIIllIllIIIlllIlIllIIIlllIIlllIlIIllIIIllIlIIlIlllIllIIlIIllIllIlIlIIlIIIllIIllIIlIllIIllIIlIIlllIllIIIllIlllIIllIlllIlIIlIllIIlllIIIllIlllIIIllIIlIIllIlllIIll;

@Environment(EnvType.CLIENT)
public class AutoSaveModule extends ru.melonity.h.c.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    private final IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll<IIlllIIllIlllIlllIIlllIIIlIllIIIlllIIlIIIlIlllIlllIIllIIIlIIllIIlIIlIllIIIllIIlllIIIllIIIllIIlllIIllIIllIIllIlllIlllIIIllIllIlIllIIlllIlllIIIlllIlllIlllIllIIllIIIllIIllIlllIIllIIllIIlllIIlIIlllIIIlIlIllIIlIIllIIIlIIlIIlIIIlllIIIllIIIllIIlIl> repairEventHandler = event -> {
        if (!this.shouldRepair()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        ClientPlayerEntity player = client.player;
        if (world == null || player == null) {
            return;
        }
        Vec3d eyePosition = player.getEyePos();
        Vec3d targetPosition = eyePosition.add(0.0, -3.0, 0.0);
        RaycastContext raycastContext = new RaycastContext(eyePosition, targetPosition, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player);
        BlockHitResult raycastResult = world.raycast(raycastContext);
        if (raycastResult.getType() == HitResult.Type.MISS) {
            return;
        }
        BlockPos blockPos = raycastResult.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block != Blocks.ANVIL) {
            return;
        }
        if (player.experienceLevel <= 4.4f) {
            return;
        }
        Item[] repairItems = new Item[]{Items.BOOK, Items.DIAMOND, Items.IRON_INGOT, Items.GOLD_INGOT, Items.LAPIS_LAZULI};
        for (Item repairItem : repairItems) {
            int slot = IlIIIlIIllIlllIlIIIlIIlllIIlIllIIlIIllIIIlllIIlllIIIlIIIlIIllIlllIIllIllIIIlllIlIllIIIlllIIlllIlIIllIIIllIlIIlIlllIllIIlIIllIllIlIlIIlIIIllIIllIIlIllIIllIIlIIlllIllIIIllIlllIIllIlllIlIIlIllIIlllIIIllIlllIIIllIIlIIllIlllIIll.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll(player, repairItem);
            if (slot == -1) {
                continue;
            }
            IIlllIIllIlllIlllIIlllIIIlIllIIIlllIIlIIIlIlllIlllIIllIIIlIIllIIlIIlIllIIIllIIlllIIIllIIIllIIlllIIllIIllIIllIlllIlllIIIllIllIlIllIIlllIlllIIIlllIlllIlllIllIIllIIIllIIllIlllIIllIIllIIlllIIlIIlllIIIlIlIllIIlIIllIIIlIIlIIlIIIlllIIIllIIIllIIlIl.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll(89.0f);
            player.networkHandler.sendPacket(new HeldItemChangeC2SPacket(slot));
            double entityX = raycastResult.getBlockPos().getX();
            double entityY = raycastResult.getBlockPos().getY();
            double entityZ = raycastResult.getBlockPos().getZ();
            double zDiff = entityZ - player.getZ();
            double xDiff = entityX - player.getX();
            double angle = Math.atan2(zDiff, xDiff);
            double degrees = Math.toDegrees(angle);
            float yaw = (float)MathHelper.wrapDegrees(degrees - 90.0);
            double entityYOffset = entityY + 1.0 - player.getY();
            double horizontalDistance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
            double pitchAngle = Math.atan2(entityYOffset, horizontalDistance);
            double pitchDegrees = Math.toDegrees(pitchAngle);
            float pitch = (float)(-pitchDegrees);
            boolean onGround = player.isOnGround();
            PlayerMoveC2SPacket.LookAndOnGround lookPacket = new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, onGround);
            player.networkHandler.sendPacket(lookPacket);
            player.swingHand(Hand.MAIN_HAND);
            if (repairItem == Items.GOLD_INGOT) {
                player.interact(Hand.MAIN_HAND);
            }
            else {
                player.interact(Hand.MAIN_HAND);
            }
            player.networkHandler.sendPacket(new HeldItemChangeC2SPacket(player.getInventory().selectedSlot));
        }
    };

    public AutoSaveModule() {
        super("AutoSave", IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll);
    }

    private boolean shouldRepair() {
        return IlIIIlIIllIlllIlIIIlIIlllIIlIllIIlIIllIIIlllIIlllIIIlIIIlIIllIlllIIllIllIIIlllIlIllIIIlllIIlllIlIIllIIIllIlIIlIlllIllIIlIIllIllIlIlIIlIIIllIIllIIlIllIIllIIlIIlllIllIIIllIlllIIllIlllIlIIlIllIIlllIIIllIlllIIIllIIlIIllIlllIIll.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll();
    }
}