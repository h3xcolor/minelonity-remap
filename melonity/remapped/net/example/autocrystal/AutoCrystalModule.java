// ремапили ребята из https://t.me/dno_rumine
package net.example.autocrystal;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.melonity.framework.settings.BooleanSetting;
import ru.melonity.framework.settings.SettingCallback;
import ru.melonity.framework.timer.TimerUtil;
import ru.melonity.framework.util.EntityUtil;
import ru.melonity.framework.util.InventoryUtil;
import ru.melonity.framework.util.RotationUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class AutoCrystalModule extends ru.melonity.framework.modules.Module {
    private BlockPos targetPlacePos = null;
    private Entity targetCrystal = null;
    private int currentSlot = -1;
    private final TimerUtil placeTimer = new TimerUtil();
    private final BooleanSetting saveSelf = new BooleanSetting("autocrystal.saveself", true);
    public final BooleanSetting movementCorrection = new BooleanSetting("global.movecorrect", true);
    private Vec3d rotationTarget;
    private final SettingCallback<BooleanSetting> saveSelfCallback = setting -> {
        if (!isActive()) return;
        boolean shouldAttack = checkAttackCondition();
        if (!shouldAttack) return;
        boolean canPlace = placeTimer.hasPassed(400L);
        if (canPlace) {
            RotationUtil.setRotation(rotationTarget, setting.getValue());
            placePlacement();
        }
    };
    private final SettingCallback<AutoCrystalModule> placeCallback = module -> {
        if (!isActive()) return;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                module.placeCrystal(targetPlacePos);
            }
        }, 400L);
    };
    private final SettingCallback<TimerUtil> timerCallback = timer -> {
        if (!isActive()) return;
        processPlacement();
    };

    public AutoCrystalModule() {
        super("AutoCrystal", ru.melonity.framework.Category.COMBAT);
        registerSettingCallback(saveSelf, saveSelfCallback);
        registerSettingCallback(this, placeCallback);
    }

    public boolean isPositionValid() {
        if (rotationTarget == null) return false;
        if (!movementCorrection.getValue()) return false;
        if (targetCrystal == null) return false;
        if (targetPlacePos == null) return false;
        return true;
    }

    private void processPlacement() {
        if (targetPlacePos != null) {
            ClientWorld world = ClientPlayerEntity.world;
            int baseX = targetPlacePos.getX();
            int baseY = targetPlacePos.getY();
            int baseZ = targetPlacePos.getZ();
            Box box = new Box(baseX, baseY, baseZ, baseX + 1.0, baseY + 2.0, baseZ + 1.0);
            List<Entity> entities = world.getEntities(null, box);
            Stream<Entity> stream = entities.stream();
            Stream<EndCrystalEntity> crystalStream = stream.filter(entity -> entity instanceof EndCrystalEntity);
            List<Entity> crystals = crystalStream.toList();
            crystals.forEach(this::attackTarget);
        }
        if (targetCrystal != null) {
            boolean crystalDead = targetCrystal.isRemoved();
            if (!crystalDead) {
                targetCrystal = null;
                targetPlacePos = null;
                rotationTarget = null;
            }
        }
    }

    private void placeCrystal(BlockPos pos) {
        if (pos == null) return;
        int crystalSlot = InventoryUtil.findItemSlot(ClientPlayerEntity.player, Items.END_CRYSTAL);
        PlayerInventory inventory = ClientPlayerEntity.player.getInventory();
        int previousSlot = inventory.selectedSlot;
        if (crystalSlot == -1 || pos == null) {
            return;
        }
        ClientPlayerEntity player = ClientPlayerEntity.playerNetworkHandler;
        PlayerInteractBlockC2SPacket packet = new PlayerInteractBlockC2SPacket();
        player.sendPacket(packet);
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        Vec3d hitPos = new Vec3d(x, y, z);
        BlockHitResult hitResult = new BlockHitResult(hitPos, Direction.UP, pos, false);
        ItemUsageContext context = new ItemUsageContext(ClientPlayerEntity.player, Hand.MAIN_HAND, hitResult);
        ActionResult result = ClientPlayerEntity.player.getStackInHand(Hand.MAIN_HAND).useOnBlock(context);
        if (result == ActionResult.SUCCESS) {
            ClientPlayerEntity.player.swingHand(Hand.MAIN_HAND);
        }
        if (currentSlot != -1) {
            ClientPlayerEntity playerNetwork = ClientPlayerEntity.playerNetworkHandler;
            PlayerInteractBlockC2SPacket slotPacket = new PlayerInteractBlockC2SPacket(currentSlot);
            playerNetwork.sendPacket(slotPacket);
        }
        currentSlot = -1;
        targetPlacePos = pos;
    }

    private void processSetting(BooleanSetting setting) {
        boolean active = isActive();
        if (!active) return;
        boolean shouldAttack = checkAttackCondition();
        if (!shouldAttack) return;
        setRotationTarget(setting);
        placePlacement();
    }

    private void attackTarget(Entity entity) {
        if (!canAttack(entity)) return;
        float attackCooldown = ClientPlayerEntity.player.getAttackCooldownProgress(1.0f);
        if (attackCooldown < 1.0f) return;
        boolean canAttack = placeTimer.hasPassed(400L);
        if (canAttack) {
            ClientPlayerEntity.player.attack(entity);
            ClientPlayerEntity.player.swingHand(Hand.MAIN_HAND);
            placeTimer.reset();
        }
        targetCrystal = entity;
    }

    private boolean canAttack(Entity entity) {
        if (entity == null) return false;
        if (targetPlacePos == null) return false;
        boolean saveSettingActive = saveSelf.getValue();
        if (saveSettingActive) {
            Vec3d playerPos = ClientPlayerEntity.player.getPos();
            Vec3d entityPos = entity.getPos();
            if (entityPos.y > playerPos.y) {
                return false;
            }
        }
        return isWithinAttackRange();
    }

    private boolean isWithinAttackRange() {
        if (targetPlacePos == null) return false;
        Vec3d playerPos = ClientPlayerEntity.player.getPos();
        BlockPos pos = targetPlacePos;
        double crystalX = pos.getX();
        double crystalY = pos.getY();
        double crystalZ = pos.getZ();
        Vec3d crystalPos = new Vec3d(crystalX, crystalY, crystalZ);
        double distance = playerPos.distanceTo(crystalPos);
        return distance <= 3.6;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) {
            rotationTarget = null;
            targetPlacePos = null;
            targetCrystal = null;
            currentSlot = -1;
        }
    }
}