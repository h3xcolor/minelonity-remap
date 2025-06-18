// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import ru.melonity.f.InventoryTickListener;
import ru.melonity.f.Setting;
import ru.melonity.f.Timer;
import ru.melonity.f.PacketEventListener;
import ru.melonity.h.c.Module;

@Environment(value=EnvType.CLIENT)
public class RussianGriefJoinerModule extends Module {
    private final Setting<Integer> griefSetting = new Setting.IntegerSetting("rwjoiner.grief", number -> String.format("%d", number.intValue()), 1, 28, 1);
    private final Timer interactionTimer = new Timer();
    private final InventoryTickListener containerSlotEventListener = event -> {
        if (!shouldAct()) {
            return;
        }
        Player player = mc.player;
        if (player == null) {
            return;
        }
        if (player.containerMenu == null) {
            if (player.getHealth() < 5) {
                mc.gameMode.handleInventoryButtonClick(player.containerMenu.containerId, 0);
            }
        } else {
            AbstractContainerMenu container = player.containerMenu;
            if (container instanceof ShulkerBoxMenu) {
                ShulkerBoxMenu shulkerBox = (ShulkerBoxMenu)container;
                try {
                    int griefNumber = griefSetting.getValue().intValue();
                    int slotCount = shulkerBox.container.getContainerSize();
                    for (int slotIndex = 0; slotIndex < slotCount; slotIndex++) {
                        ItemStack stack = shulkerBox.slots.get(slotIndex).getItem();
                        Item item = stack.getItem();
                        Component displayName = item.getName(stack);
                        String itemName = displayName.getString();
                        if (itemName.contains("ГРИФЕРСКОЕ ВЫЖИВАНИЕ")) {
                            if (interactionTimer.hasPassed(50)) {
                                mc.gameMode.handleInventoryMouseClick(shulkerBox.containerId, slotIndex, 0, ClickType.PICKUP, player);
                                interactionTimer.reset();
                            }
                        }
                        if (itemName.contains("ГРИФ #" + griefNumber)) {
                            if (interactionTimer.hasPassed(50)) {
                                mc.gameMode.handleInventoryMouseClick(shulkerBox.containerId, slotIndex, 0, ClickType.PICKUP, player);
                                interactionTimer.reset();
                            }
                        }
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    };
    private final PacketEventListener packetEventListener = event -> {
        if (!shouldAct()) {
            return;
        }
        Player player = mc.player;
        if (player == null) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (packet instanceof ClientboundSystemChatPacket) {
            ClientboundSystemChatPacket chatPacket = (ClientboundSystemChatPacket)packet;
            String message = chatPacket.content().getString();
            if (message.contains("К сожалению сервер переполнен") || 
                message.contains("Подождите 20 секунд!") || 
                message.contains("большой поток игроков")) {
                resetHotbarSelection();
                mc.gameMode.handleInventoryButtonClick(player.containerMenu.containerId, 0);
            }
        }
    };

    public RussianGriefJoinerModule() {
        super("RWJoiner", Module.Category.MISC);
        addSetting(griefSetting);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player == null) {
            return;
        }
        interactionTimer.reset();
        if (isEnabled()) {
            resetHotbarSelection();
            mc.gameMode.handleInventoryButtonClick(mc.player.containerMenu.containerId, 0);
        }
    }

    private void resetHotbarSelection() {
        Player player = mc.player;
        if (player == null) {
            return;
        }
        int airSlot = ru.melonity.w.InventoryUtilities.findItemSlot(player, Items.AIR, false);
        if (airSlot == -1) {
            return;
        }
        int hotbarSlot = airSlot - 36;
        if (hotbarSlot < 0 || hotbarSlot > 8) {
            return;
        }
        player.inventory.selected = hotbarSlot;
        mc.getConnection().send(new ClientboundContainerSetSlotPacket(-2, 0, hotbarSlot, player.inventory.getItem((hotbarSlot))));
        mc.getConnection().send(new ClientboundContainerSetSlotPacket(-1, 0, hotbarSlot, player.inventory.getItem((hotbarSlot))));
    }

    private boolean shouldAct() {
        return isEnabled() && mc.player != null && mc.getConnection() != null;
    }
}