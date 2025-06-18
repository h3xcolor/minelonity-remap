// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;

@Environment(value = EnvType.CLIENT)
public class PacketWrapper extends ru.melonity.f.PacketHandlerBase {
    private final class_2596<?> packet;
    private final PacketAction action;

    @Generated
    public PacketWrapper(class_2596<?> packet, PacketAction action) {
        this.packet = packet;
        this.action = action;
    }

    @Generated
    public class_2596<?> getPacket() {
        return this.packet;
    }

    @Generated
    public PacketAction getAction() {
        return this.action;
    }

    @Environment(value = EnvType.CLIENT)
    public static enum PacketAction {
        SEND,
        RECEIVE
    }
}