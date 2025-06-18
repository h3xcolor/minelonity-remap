// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.SequencedPacket;

@Environment(EnvType.CLIENT)
public interface IClientPlayerInteractionManager {
    public static final int SEQUENCED_PACKET_ID = 1851253779;

    void trySendSequencedPacket(ClientWorld world, SequencedPacket packet);
}