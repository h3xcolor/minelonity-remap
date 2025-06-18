// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import ru.melonity.Melonity;
import ru.melonity.f.IPlayerMovementHelper;
import ru.melonity.f.IScreenInteractionHelper;
import ru.melonity.f.ITimer;
import ru.melonity.f.events.PlayerStateEvent;
import ru.melonity.f.events.PlayerWorldChangeEvent;
import ru.melonity.o.IFeatureHolder;
import ru.melonity.o.b.a.b.BooleanSetting;
import ru.melonity.o.b.a.b.IntSliderSetting;
import ru.melonity.w.IEventHandler;

@Environment(value = EnvType.CLIENT)
public class ElytraHelperModule extends IFeatureHolder {
    private final IntSliderSetting swapSetting = new IntSliderSetting("Swap", 0, 0, 10);
    private final IntSliderSetting fireworkSetting = new IntSliderSetting("Firework", 0, 0, 10);
    private final BooleanSetting autoPilotEnabled = new BooleanSetting("elytrahelper.autopilot", false);
    private final ITimer messageCooldownTimer = new ITimer();
    private final IPlayerMovementHelper playerMovementHelper = playerStateEvent -> {
        boolean isUsingElytra = isPlayerUsingElytra();
        if (!isUsingElytra) {
            return;
        }
        boolean autoPilotActive = this.autoPilotEnabled.get();
        if (!autoPilotActive) {
            return;
        }
        ClientPlayerEntity player = Melonity.getPlayer();
        if (player == null) {
            return;
        }
        boolean hasFirework = hasFireworkRocket();
        boolean onCooldown = this.messageCooldownTimer.hasPassed(3200L);
        if (!onCooldown) {
            if (hasFirework) {
                int targetX = Melonity.getCoordinateManager().getXCoordinate();
                double posX = player.getX();
                double dx = targetX - posX;
                int targetZ = Melonity.getCoordinateManager().getZCoordinate();
                double posZ = player.getZ();
                double dz = targetZ - posZ;
                double directionRad = Math.atan2(dz, dx);
                double directionDeg = Math.toDegrees(directionRad);
                double normalizedYaw = MathHelper.wrapDegrees(directionDeg - 90.0);
                float targetYaw = (float) normalizedYaw;
                float currentYaw = player.getYaw();
                float yawDifference = targetYaw - currentYaw;
                float maxTurn = player.getMovementSpeed();
                player.setYaw(currentYaw + yawDifference * 0.06f);
                double elevation = player.getY();
                if (elevation < 160.0) {
                    player.setPitch(-15.0f);
                } else {
                    float currentPitch = player.getPitch();
                    float adjustedPitch = Math.max(currentPitch, -2.0f);
                    player.setPitch(adjustedPitch);
                }
            }
            return;
        }
        if (!hasFirework) {
            player.sendMessage(Text.of("Включите GPS (.help with gps)"), true);
            this.messageCooldownTimer.reset();
            return;
        }
        useFirework();
        this.messageCooldownTimer.reset();
    };
    private final IEventHandler<PlayerWorldChangeEvent> worldChangeHandler = event -> {
        int swapSettingValue = this.swapSetting.get();
        if (swapSettingValue == swapSetting.get()) {
            attemptFireworkSwap();
        }
        int fireworkSettingValue = this.fireworkSetting.get();
        if (fireworkSettingValue == fireworkSetting.get()) {
            attemptChestplateToElytraSwap();
        }
    };
    public static int randomObfuscationCounter = 527470890;

    public ElytraHelperModule() {
        super("ElytraHelper", IFeatureHolder.Category.MOVEMENT);
        setupSettings();
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        Melonity.getEventManager().registerEventHandler(PlayerStateEvent.class, playerMovementHelper);
        Melonity.getEventManager().registerEventHandler(PlayerWorldChangeEvent.class, worldChangeHandler);
    }

    private void setupSettings() {
        addSetting(swapSetting);
        addSetting(fireworkSetting);
        addSetting(autoPilotEnabled);
    }

    private boolean isPlayerUsingElytra() {
        return Melonity.getPlayer() != null && Melonity.getPlayer().isFallFlying();
    }

    private boolean hasFireworkRocket() {
        return findFireworkRocket() >= 0;
    }

    private void attemptFireworkSwap() {
        int chestplateSlot = -1;
        int elytraSlot = -1;
        int fireworkSlot = -1;
        PlayerInventory inventory = Melonity.getPlayer().getInventory();
        for (int i = 0; i < 36; i++) {
            int slotIndex = i;
            if (i < 9) {
                slotIndex = 36 + i;
            }
            ItemStack item = inventory.getStack(i);
            if (item.isEmpty()) {
                continue;
            }
            if (item.isStackable()) {
                chestplateSlot = slotIndex;
                continue;
            }
            if (item.getItem() == Items.ELYTRA) {
                elytraSlot = slotIndex;
                continue;
            }
            if (item.getItem() instanceof FireworkRocketItem) {
                if (EnchantmentHelper.getLevel(Enchantments.POWER, item) > 0) {
                    fireworkSlot = slotIndex;
                }
            }
        }
        ClientPlayerEntity player = Melonity.getPlayer();
        ItemStack chestItem = inventory.getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
        if (chestItem.getItem() == Items.ELYTRA && fireworkSlot != -1) {
            if (chestplateSlot == -1) {
                return;
            }
            interactWithSlot(chestplateSlot);
            interactWithSlot(chestplateSlot);
            interactWithSlot(fireworkSlot);
            interactWithSlot(chestplateSlot);
            return;
        }
        if (chestItem.getItem() instanceof FireworkRocketItem) {
            if (EnchantmentHelper.getLevel(Enchantments.POWER, chestItem) > 0 && chestplateSlot != -1) {
                if (elytraSlot == -1) {
                    return;
                }
                interactWithSlot(chestplateSlot);
                interactWithSlot(elytraSlot);
                interactWithSlot(fireworkSlot);
                interactWithSlot(chestplateSlot);
            }
        }
    }

    private void interactWithSlot(int slot) {
        Melonity.getInteractionManager().interactInventorySlot(0, slot, 0, SlotActionType.PICKUP, Melonity.getPlayer());
    }

    private void attemptChestplateToElytraSwap() {
        PlayerInventory inventory = Melonity.getPlayer().getInventory();
        ItemStack chestItem = inventory.getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
        if (chestItem.getItem() == Items.ELYTRA) {
            return;
        }
        int elytraSlot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack item = inventory.getStack(i);
            if (item.isEmpty()) {
                continue;
            }
            if (item.getItem() != Items.ELYTRA) {
                continue;
            }
            elytraSlot = i;
        }
        if (elytraSlot == -1) {
            return;
        }
        for (int i = 9; i < 36; i++) {
            ItemStack item = inventory.getStack(i);
            if (item.getItem() != Items.ELYTRA) {
                continue;
            }
            ItemStack chestSlotItem = inventory.getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
            if (!chestSlotItem.isEmpty()) {
                interactWithSlot(EquipmentSlot.CHEST.getEntitySlotId() + 36);
                interactWithSlot(elytraSlot);
                return;
            }
            interactWithSlot(i);
            interactWithSlot(EquipmentSlot.CHEST.getEntitySlotId() + 36);
            break;
        }
    }

    private void useFirework() {
        attemptChestplateToElytraSwap();
        ClientPlayerEntity player = Melonity.getPlayer();
        player.networkHandler.sendPacket(new ClientCommandC2SPacket(player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(player.preferredHand));
        player.swingHand(player.preferredHand);
        player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, player.getBlockPos(), player.getHorizontalFacing()));
    }

    @Generated
    public BooleanSetting getAutoPilotSetting() {
        return autoPilotEnabled;
    }
}