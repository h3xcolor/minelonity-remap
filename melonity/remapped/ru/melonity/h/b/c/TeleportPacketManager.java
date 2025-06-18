// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.c;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import ru.melonity.f.EventHandler;
import ru.melonity.f.IncomingPacketEvent;
import ru.melonity.f.PacketEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class TeleportPacketManager {
    public static boolean enabled = false;
    private static boolean isSending;
    private static final List<double[]> recordedPositions = new LinkedList<>();
    public static final List<Packet<?>> storedPackets = new ArrayList<>();
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final EventHandler<IncomingPacketEvent> packetReceiveHandler = event -> {
        if (event.getPlayer() == null || event.getPlayer() != this.client.player || !enabled) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (packet instanceof PlayerPositionLookS2CPacket && !isSending) {
            event.cancel();
            return;
        }
        if (packet instanceof PlayerPositionLookS2CPacket || packet instanceof PlayerPositionS2CPacket) {
            storedPackets.add(packet);
        }
    };
    private final EventHandler<PacketEvent> packetProcessHandler = event -> {};

    private void flushPackets() {
        try {
            isSending = true;
            Iterator<Packet<?>> iterator = storedPackets.iterator();
            while (iterator.hasNext()) {
                ClientPlayNetworkHandler connection = this.client.player.networkHandler;
                Packet<?> packet = iterator.next();
                connection.sendPacket(packet);
                iterator.remove();
            }
            isSending = false;
        } catch (Exception e) {
            e.printStackTrace();
            isSending = false;
        }
        synchronized (recordedPositions) {
            recordedPositions.clear();
        }
    }

    public void startRecording() {
        flushPackets();
        enabled = true;
        synchronized (recordedPositions) {
            double[] feetPosition = new double[3];
            feetPosition[0] = this.client.player.getX();
            Box boundingBox = this.client.player.getBoundingBox();
            Direction direction = this.client.player.getHorizontalFacing();
            float eyeHeight = this.client.player.getEyeHeight(direction);
            feetPosition[1] = boundingBox.minY + (double) (eyeHeight / 2.0f);
            feetPosition[2] = this.client.player.getZ();
            recordedPositions.add(feetPosition);
            double[] headPosition = new double[3];
            headPosition[0] = this.client.player.getX();
            headPosition[1] = boundingBox.minY;
            headPosition[2] = this.client.player.getZ();
            recordedPositions.add(headPosition);
        }
    }

    public void stopRecording() {
        flushPackets();
        enabled = false;
    }

    public void reset() {
        if (this.client.player == null) {
            return;
        }
    }

    static {
    }
}