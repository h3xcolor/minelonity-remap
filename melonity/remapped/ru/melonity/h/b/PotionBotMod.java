// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import ru.melonity.Melonity;
import ru.melonity.f.Setting;
import ru.melonity.f.ToggleSetting;
import ru.melonity.fabric.client.model.IBrewingStandScreenHandler;
import ru.melonity.h.c.Module;
import ru.melonity.o.ModuleCategory;

@Environment(EnvType.CLIENT)
public class PotionBotMod extends Module {
    private final ToggleSetting splashSetting = new ToggleSetting("global.splash", true);
    private final Setting modeSetting = new Setting("global.mode", Setting.Mode.IDLE, "Сила I", "Сила II", "Скорость I", "Скорость II", "Огнестойкость I", "Огнестойкость II", "Невидимость I", "Невидимость II");
    private BlockPos chestPos = null;
    private BlockPos brewingStandPos = null;
    private final ModuleCategory category = new ModuleCategory();
    private final BotRunnable botRunnable = new BotRunnable();

    public PotionBotMod() {
        super("PotionBot", category);
        this.registerSetting(splashSetting);
        this.registerSetting(modeSetting);
        new Thread(() -> {
            while (true) {
                botRunnable.run();
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Environment(EnvType.CLIENT)
    private class BotRunnable implements Runnable {
        private Setting.Mode currentMode = Setting.Mode.IDLE;
        private long lastActionTime = 0L;

        @Override
        public void run() {
            MinecraftClient client = MinecraftClient.getInstance();
            PotionBotMod mod = Melonity.getModuleManager().getModule(PotionBotMod.class).orElse(null);
            if (mod == null || !mod.isEnabled()) {
                currentMode = Setting.Mode.IDLE;
                return;
            }

            if (chestPos == null) {
                ChatHud chat = client.inGameHud.getChatHud();
                chat.addMessage(Text.of("Установите позицию сундука, нажав колёсиком мыши на сундук"));
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return;
            }

            if (brewingStandPos == null) {
                ChatHud chat = client.inGameHud.getChatHud();
                chat.addMessage(Text.of("Установите позицию зельеварки, нажав колёсиком мыши на зельеварку"));
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return;
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastActionTime < 500L) {
                return;
            }

            switch (currentMode) {
                case IDLE:
                    currentMode = Setting.Mode.BREWING;
                    break;
                case BREWING:
                    ScreenHandler screenHandler = client.player.currentScreenHandler;
                    if (!(screenHandler instanceof BrewingStandScreenHandler)) {
                        interactWithBlock(client, chestPos);
                        break;
                    }
                    BrewingStandScreenHandler brewingHandler = (BrewingStandScreenHandler) screenHandler;
                    brewPotions(client, brewingHandler, mod);
                    RenderSystem.recordRenderCall(() -> client.interactionManager.clickSlot(brewingHandler.syncId, 0, 0, SlotActionType.PICKUP, client.player));
                    currentMode = Setting.Mode.FILLING;
                    lastActionTime = System.currentTimeMillis();
                    break;
                case FILLING:
                    screenHandler = client.player.currentScreenHandler;
                    if (screenHandler instanceof GenericContainerScreenHandler) {
                        fillPotions(client, (GenericContainerScreenHandler) screenHandler, mod);
                        currentMode = Setting.Mode.COLLECTING;
                        lastActionTime = System.currentTimeMillis();
                    } else {
                        interactWithBlock(client, brewingStandPos);
                    }
                    break;
                case COLLECTING:
                    screenHandler = client.player.currentScreenHandler;
                    if (screenHandler instanceof BrewingStandScreenHandler) {
                        collectPotions(client, (BrewingStandScreenHandler) screenHandler, mod);
                        currentMode = Setting.Mode.DONE;
                        lastActionTime = System.currentTimeMillis();
                    } else {
                        interactWithBlock(client, chestPos);
                    }
                    break;
                case DONE:
                    ChatHud chat = client.inGameHud.getChatHud();
                    chat.addMessage(Text.of("Зелья готовы!"));
                    currentMode = Setting.Mode.IDLE;
                    break;
            }
        }

        private void interactWithBlock(MinecraftClient client, BlockPos pos) {
            Vec3d playerPos = new Vec3d(client.player.getX(), client.player.getY() + client.player.getEyeHeight(client.player.getPose()), client.player.getZ());
            Vec3d blockPos = Vec3d.ofCenter(pos);
            Vec3f pitchYaw = calculatePitchYaw(playerPos, blockPos);
            client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(pitchYaw.getX(), pitchYaw.getY(), client.player.isOnGround()));
            BlockHitResult hitResult = client.world.raycast(new RaycastContext(playerPos, blockPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, client.player));
            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
            client.player.swingHand(Hand.MAIN_HAND);
        }

        private void brewPotions(MinecraftClient client, BrewingStandScreenHandler handler, PotionBotMod mod) {
            int netherWartSlot = findItemSlot(handler, Items.NETHER_WART);
            if (netherWartSlot != -1) {
                client.interactionManager.clickSlot(handler.syncId, netherWartSlot, 0, SlotActionType.PICKUP, client.player);
            }

            int glowstoneSlot = findItemSlot(handler, Items.GLOWSTONE_DUST);
            if (glowstoneSlot != -1) {
                client.interactionManager.clickSlot(handler.syncId, glowstoneSlot, 0, SlotActionType.PICKUP, client.player);
            }

            int redstoneSlot = findItemSlot(handler, Items.REDSTONE);
            if (redstoneSlot != -1) {
                client.interactionManager.clickSlot(handler.syncId, redstoneSlot, 0, SlotActionType.PICKUP, client.player);
            }

            if (mod.modeSetting.isModeSelected("Сила I")) {
                int blazePowderSlot = findItemSlot(handler, Items.BLAZE_POWDER);
                if (blazePowderSlot != -1) {
                    client.interactionManager.clickSlot(handler.syncId, blazePowderSlot, 0, SlotActionType.PICKUP, client.player);
                }
                if (mod.modeSetting.isModeSelected("Сила II")) {
                    int glowstoneSlot2 = findItemSlot(handler, Items.GLOWSTONE_DUST);
                    if (glowstoneSlot2 != -1) {
                        client.interactionManager.clickSlot(handler.syncId, glowstoneSlot2, 0, SlotActionType.PICKUP, client.player);
                    }
                }
            }

            if (mod.modeSetting.isModeSelected("Скорость I")) {
                int sugarSlot = findItemSlot(handler, Items.SUGAR);
                if (sugarSlot != -1) {
                    client.interactionManager.clickSlot(handler.syncId, sugarSlot, 0, SlotActionType.PICKUP, client.player);
                }
                if (mod.modeSetting.isModeSelected("Скорость II")) {
                    int redstoneSlot2 = findItemSlot(handler, Items.REDSTONE);
                    if (redstoneSlot2 != -1) {
                        client.interactionManager.clickSlot(handler.syncId, redstoneSlot2, 0, SlotActionType.PICKUP, client.player);
                    }
                }
            }

            if (mod.modeSetting.isModeSelected("Огнестойкость I")) {
                int magmaCreamSlot = findItemSlot(handler, Items.MAGMA_CREAM);
                if (magmaCreamSlot != -1) {
                    client.interactionManager.clickSlot(handler.syncId, magmaCreamSlot, 0, SlotActionType.PICKUP, client.player);
                }
                if (mod.modeSetting.isModeSelected("Огнестойкость II")) {
                    int redstoneSlot2 = findItemSlot(handler, Items.REDSTONE);
                    if (redstoneSlot2 != -1) {
                        client.interactionManager.clickSlot(handler.syncId, redstoneSlot2, 0, SlotActionType.PICKUP, client.player);
                    }
                }
            }

            if (mod.modeSetting.isModeSelected("Невидимость I")) {
                int goldenCarrotSlot = findItemSlot(handler, Items.GOLDEN_CARROT);
                if (goldenCarrotSlot != -1) {
                    client.interactionManager.clickSlot(handler.syncId, goldenCarrotSlot, 0, SlotActionType.PICKUP, client.player);
                }
                if (mod.modeSetting.isModeSelected("Невидимость II")) {
                    int redstoneSlot2 = findItemSlot(handler, Items.REDSTONE);
                    if (redstoneSlot2 != -1) {
                        client.interactionManager.clickSlot(handler.syncId, redstoneSlot2, 0, SlotActionType.PICKUP, client.player);
                    }
                }
            }

            if (mod.splashSetting.isEnabled()) {
                int gunpowderSlot = findItemSlot(handler, Items.GUNPOWDER);
                if (gunpowderSlot != -1) {
                    client.interactionManager.clickSlot(handler.syncId, gunpowderSlot, 0, SlotActionType.PICKUP, client.player);
                }
            }
        }

        private void fillPotions(MinecraftClient client, GenericContainerScreenHandler handler, PotionBotMod mod) {
            moveItemToSlot(client, handler, Items.NETHER_WART, 0);
            moveItemToSlot(client, handler, Items.GLOWSTONE_DUST, 1);
            moveItemToSlot(client, handler, Items.REDSTONE, 2);

            if (mod.modeSetting.isModeSelected("Сила I") || mod.modeSetting.isModeSelected("Сила II")) {
                moveItemToSlot(client, handler, Items.BLAZE_POWDER, 3);
            }

            if (mod.modeSetting.isModeSelected("Скорость I") || mod.modeSetting.isModeSelected("Скорость II")) {
                moveItemToSlot(client, handler, Items.SUGAR, 4);
            }

            if (mod.modeSetting.isModeSelected("Огнестойкость I") || mod.modeSetting.isModeSelected("Огнестойкость II")) {
                moveItemToSlot(client, handler, Items.MAGMA_CREAM, 5);
            }

            if (mod.modeSetting.isModeSelected("Невидимость I") || mod.modeSetting.isModeSelected("Невидимость II")) {
                moveItemToSlot(client, handler, Items.GOLDEN_CARROT, 6);
            }

            if (mod.splashSetting.isEnabled()) {
                moveItemToSlot(client, handler, Items.GUNPOWDER, 7);
            }
        }

        private void collectPotions(MinecraftClient client, BrewingStandScreenHandler handler, PotionBotMod mod) {
            IBrewingStandScreenHandler brewingHandler = (IBrewingStandScreenHandler) handler;
            moveItemToSlot(client, handler, Items.POTION, 3);
            while (brewingHandler.propertyDelegate().get(0) != 0) {
                if (!(client.player.currentScreenHandler instanceof BrewingStandScreenHandler)) {
                    return;
                }
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            while (brewingHandler.propertyDelegate().get(0) > 0) {
                if (!(client.player.currentScreenHandler instanceof BrewingStandScreenHandler)) {
                    return;
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void moveItemToSlot(MinecraftClient client, ScreenHandler handler, Item item, int targetSlot) {
            int sourceSlot = findItemSlot(handler, item);
            if (sourceSlot != -1) {
                client.interactionManager.clickSlot(handler.syncId, sourceSlot, 0, SlotActionType.PICKUP, client.player);
                client.interactionManager.clickSlot(handler.syncId, targetSlot, 0, SlotActionType.PICKUP, client.player);
                client.interactionManager.clickSlot(handler.syncId, sourceSlot, 0, SlotActionType.PICKUP, client.player);
            }
        }

        private int findItemSlot(ScreenHandler handler, Item item) {
            for (int i = 5; i < handler.slots.size(); i++) {
                if (!handler.getSlot(i).hasStack()) continue;
                ItemStack stack = handler.getSlot(i).getStack();
                if (stack.getItem() == item) {
                    return i;
                }
            }
            return -1;
        }

        private int findEmptySlot(ScreenHandler handler) {
            for (int i = 5; i < handler.slots.size(); i++) {
                if (!handler.getSlot(i).hasStack()) {
                    return i;
                }
            }
            return -1;
        }

        private Vec3f calculatePitchYaw(Vec3d from, Vec3d to) {
            double diffX = to.x - from.x;
            double diffY = to.y - from.y;
            double diffZ = to.z - from.z;
            double horizontalDist = Math.sqrt(diffX * diffX + diffZ * diffZ);
            float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
            float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, horizontalDist)));
            return new Vec3f(pitch, yaw, 0);
        }

        @Environment(EnvType.CLIENT)
        private enum Mode {
            IDLE,
            BREWING,
            FILLING,
            COLLECTING,
            DONE
        }
    }
}