// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.compress.utils.Lists;
import ru.melonity.f.events.MotionUpdateEvent;
import ru.melonity.f.events.PreEvent;
import ru.melonity.f.events.UseItemEvent;
import ru.melonity.f.settings.ModeSetting;
import ru.melonity.f.settings.Setting;
import ru.melonity.o.Module;
import ru.melonity.o.Category;
import ru.melonity.w.Counter;
import ru.melonity.w.RandomUtil;

@Environment(EnvType.CLIENT)
public class NoSlow extends Module {
    private final ModeSetting motionMpType = new ModeSetting("Motion MP type", new Setting("Type of motion multiplier", "Defines the method used for motion slowdown resistance"),
            "FunTime", "Grim Latest", "Full (Cancel)", "Matrix (Cancel)", "Lag 1", "FunTime");
    private final ModeSetting packetType = new ModeSetting("Addable packet type", new Setting("Additional packet types", "Defines which extra packets to send for bypass"),
            (List<String>)Lists.newArrayList(), "Sneak", "SpamSneak", "BlockSpoof", "Abort BlockSpoof", "Hand swapper", "Pitch settable");
    private final Counter packetSendCounter = new Counter();
    private final Counter packetCooldownCounter = new Counter();
    private int jumpTicks;
    private boolean itemInUse;
    private final Consumer<MotionUpdateEvent> onMotionUpdate = event -> {
        if (!motionMpType.isEnabled()) {
            return;
        }
        if (itemInUse && !packetType.isMode("Pitch settable")) {
            return;
        }
        event.setPitch(90.0f);
    };
    private final Consumer<UseItemEvent> onItemUse = event -> {
        if (!motionMpType.isEnabled()) {
            return;
        }
        boolean usingSlowItem = isUsingSlowItem();
        if (!usingSlowItem) {
            return;
        }
        if (packetCooldownCounter.hasPassed(100L) && !mc.player.getMainHandStack().isEmpty()) {
            packetCooldownCounter.reset();
        } else if (itemInUse) {
            packetCooldownCounter.reset();
            itemInUse = false;
        }
    };
    private final Consumer<PreEvent> onPacketSend = event -> {
        if (!motionMpType.isEnabled()) {
            return;
        }
        if (!isUsingSlowItem()) {
            return;
        }
        if (!itemInUse) {
            ++jumpTicks;
        } else {
            jumpTicks = 0;
            packetSendCounter.reset();
        }
        if (packetType.isMode("Sneak")) {
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.PRESS_SHIFT_KEY, BlockPos.ORIGIN, Direction.DOWN));
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_SHIFT_KEY, BlockPos.ORIGIN, Direction.DOWN));
        }
        if (packetType.isMode("Hand swapper")) {
            Hand hand = mc.player.getMainHand();
            mc.interactionManager.interact(mc.player, hand);
            mc.interactionManager.interact(mc.player, hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);
        }
        if (packetType.isMode("SpamSneak")) {
            for (int i = 0; i < 10; ++i) {
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.PRESS_SHIFT_KEY, BlockPos.ORIGIN, Direction.DOWN));
            }
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_SHIFT_KEY, BlockPos.ORIGIN, Direction.DOWN));
        }
        if (packetType.isMode("BlockSpoof")) {
            Vec3d eyesPos = mc.player.getRotationVec(1.0f);
            Vec3d extendedPos = new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).add(eyesPos.x, 0.0, eyesPos.z);
            BlockPos blockPos = new BlockPos(extendedPos);
            ThreadLocalRandom random = ThreadLocalRandom.current();
            boolean random1 = random.nextBoolean();
            Direction direction = random1 ? Direction.WEST : Direction.EAST;
            ThreadLocalRandom random2 = ThreadLocalRandom.current();
            boolean random3 = random2.nextBoolean();
            Hand hand = random3 ? Hand.MAIN_HAND : Hand.OFF_HAND;
            float offsetX = RandomUtil.randomFloat(-0.05f, 0.05f);
            double hitX = 0.5 + (double)offsetX;
            float offsetY = RandomUtil.randomFloat(-0.05f, 0.05f);
            double hitY = 0.5 + (double)offsetY;
            float offsetZ = RandomUtil.randomFloat(-0.05f, 0.05f);
            Vec3d hitPos = new Vec3d(hitX, hitY, 0.5 + (double)offsetZ);
            ThreadLocalRandom random4 = ThreadLocalRandom.current();
            boolean random5 = random4.nextBoolean();
            BlockHitResult blockHitResult = new BlockHitResult(hitPos, direction, blockPos, random5);
            mc.interactionHandler.interactBlock(mc.player, hand, blockHitResult);
        }
        if (packetType.isMode("Abort BlockSpoof")) {
            Direction placedDirection = mc.player.getHorizontalFacing();
            double posX = mc.player.getX();
            double posY = mc.player.getY();
            double posZ = mc.player.getZ();
            BlockPos blockPos = new BlockPos(posX, posY, posZ).offset(placedDirection);
            int offsetX = placedDirection.getOffsetX();
            int offsetY = placedDirection.getOffsetY();
            int offsetZ = placedDirection.getOffsetZ();
            BlockPos offsetPos = blockPos.add(offsetX, offsetY, offsetZ);
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, offsetPos, placedDirection));
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, offsetPos, placedDirection));
        }
        String selectedMode = motionMpType.getSelectedMode();
        switch (selectedMode) {
            case "Full (Cancel)":
            case "Matrix (Cancel)":
                event.cancel();
                if (!motionMpType.isMode("Matrix (Cancel)")) return;
                if (mc.player.age > 28) return;
                boolean movingSlow = mc.player.forwardSpeed > 0.4f;
                int age = mc.player.age;
                if (age <= 3) return;
                boolean isSneaking = mc.player.input.sneaking;
                if (isSneaking && !mc.player.input.jumping) {
                    if (mc.player.age % 2 != 0) return;
                    float multiplier = mc.player.forwardSpeed < 0.01f ? 0.5f : 0.4f;
                    Vec3d velocity = mc.player.getVelocity();
                    mc.player.setVelocity(velocity.multiply(multiplier, 0.0, multiplier));
                    return;
                }
                float multiplier = (mc.player.forwardSpeed > 1.0F) ? 0.95f : 0.97f;
                Vec3d velocity = mc.player.getVelocity();
                mc.player.setVelocity(velocity.multiply(multiplier, 0.0, multiplier));
                break;
            case "Grim Latest":
            case "Lag 1":
                boolean isGrim = motionMpType.isMode("Grim Latest");
                if (!packetSendCounter.hasPassed(isGrim ? 55L : 110L)) return;
                int currentAge = mc.player.age;
                if (currentAge > 27) return;
                event.cancel();
                packetSendCounter.reset();
                break;
            case "FunTime":
                int playerAge = mc.player.age;
                if (playerAge > 27) return;
                Random random = new Random();
                int threshold = random.nextInt(2);
                if (jumpTicks > threshold) return;
                event.cancel();
                jumpTicks = 0;
        }
    };

    public NoSlow() {
        super("NoSlow", Category.MOVEMENT);
        addSetting(motionMpType);
        addSetting(packetType);
    }

    private boolean isUsingSlowItem() {
        ItemStack usingItem = mc.player.getActiveItem();
        if (usingItem.isEmpty()) return false;
        float useTime = (float)usingItem.getMaxUseTime();
        if ((float)usingItem.getMaxUseTime() < 6.0f) return true;
        if (!mc.player.isUsingItem()) return false;
        return !mc.player.isOnGround();
    }
}