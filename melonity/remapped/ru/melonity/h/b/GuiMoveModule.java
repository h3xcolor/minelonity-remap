// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import ru.melonity.f.b.TickEvent;
import ru.melonity.f.b.PacketReceiveEvent;
import ru.melonity.f.b.PacketSendEvent;
import ru.melonity.o.Module;
import ru.melonity.o.b.a.b.ModeSetting;
import ru.melonity.w.EventBus;
import ru.melonity.w.PacketInterceptManager;

@Environment(value=EnvType.CLIENT)
public class GuiMoveModule extends Module {
    private int packetCounter;
    private final List<Packet<?>> interceptedPackets = new ArrayList();
    private final PacketInterceptManager packetInterceptManager = new PacketInterceptManager();
    private final ModeSetting bypassModeSetting = new ModeSetting("BypassMode", ModeSetting.Mode.NORMAL, "Normal", "Normal", "FunTime");
    private final EventBus.EventHandler<TickEvent> tickEventListener = event -> {
        if (!isEnabled()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }
        KeyBinding[] movementKeys = new KeyBinding[]{client.options.forwardKey, client.options.backKey, client.options.leftKey, client.options.rightKey, client.options.jumpKey, client.options.sprintKey};
        List<String> modes = bypassModeSetting.getModes();
        String currentMode = modes.get(0);
        if (!currentMode.equals("FunTime")) {
            return;
        }
        boolean canPress = packetInterceptManager.canPress(100L);
        if (!canPress) {
            for (KeyBinding key : movementKeys) {
                key.setPressed(false);
            }
            return;
        }
        if (client.currentScreen instanceof AbstractInventoryScreen || client.currentScreen instanceof RecipeBookScreen) {
            return;
        }
        processMovementKeys(movementKeys);
    };
    private boolean isResendingPackets = false;
    private final EventBus.EventHandler<PacketReceiveEvent> packetReceiveListener = event -> {
        if (!isEnabled() || isResendingPackets) {
            return;
        }
        List<String> modes = bypassModeSetting.getModes();
        String currentMode = modes.get(0);
        if (!currentMode.equals("FunTime")) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (packet instanceof PlayerMoveC2SPacket) {
            PlayerMoveC2SPacket movePacket = (PlayerMoveC2SPacket)packet;
            boolean interceptEnabled = PacketInterceptManager.isInterceptEnabled();
            if (interceptEnabled && client.currentScreen instanceof InventoryScreen) {
                interceptedPackets.add(movePacket);
                event.cancel();
            }
        }
    };
    private final EventBus.EventHandler<PacketSendEvent> packetSendListener = event -> {
        if (!isEnabled()) {
            return;
        }
        List<String> modes = bypassModeSetting.getModes();
        String currentMode = modes.get(0);
        if (currentMode.equals("FunTime") && client.currentScreen instanceof InventoryScreen) {
            boolean hasPackets = !interceptedPackets.isEmpty();
            if (hasPackets) {
                boolean interceptEnabled = PacketInterceptManager.isInterceptEnabled();
                if (interceptEnabled) {
                    Thread resendThread = new Thread(() -> {
                        packetInterceptManager.reset();
                        try {
                            Thread.sleep(100L);
                        }
                        catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        isResendingPackets = true;
                        Iterator<Packet<?>> iterator = interceptedPackets.iterator();
                        while (iterator.hasNext()) {
                            Packet<?> packet = iterator.next();
                            client.player.networkHandler.sendPacket(packet);
                        }
                        isResendingPackets = false;
                        interceptedPackets.clear();
                    });
                    resendThread.start();
                }
            }
        }
    };
    private MinecraftClient client = MinecraftClient.getInstance();

    public GuiMoveModule() {
        super("GuiMove", Module.Category.MOVEMENT);
        initializeSettings();
        registerEventHandlers();
    }

    private void initializeSettings() {
        addSetting(bypassModeSetting);
    }

    private void registerEventHandlers() {
        EventBus.subscribe(TickEvent.class, tickEventListener);
        EventBus.subscribe(PacketReceiveEvent.class, packetReceiveListener);
        EventBus.subscribe(PacketSendEvent.class, packetSendListener);
    }

    private void processMovementKeys(KeyBinding[] keys) {
        for (KeyBinding key : keys) {
            InputUtil.Key inputKey = key.getBoundKey();
            long currentTime = System.currentTimeMillis();
            int keyCode = inputKey.getCode();
            boolean pressed = InputUtil.isKeyPressed(currentTime, keyCode);
            key.setPressed(pressed);
        }
    }
}