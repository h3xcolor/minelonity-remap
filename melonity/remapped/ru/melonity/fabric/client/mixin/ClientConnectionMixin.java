// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;
import net.minecraft.class_7648;
import net.minecraft.network.ClientConnection;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.f.b.PacketEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Shadow
    private Channel channel;

    @Inject(method={"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    protected void onChannelRead(ChannelHandlerContext context, class_2596<?> packet, CallbackInfo callbackInfo) {
        if (this.channel.isOpen()) {
            PacketEvent packetEvent = new PacketEvent(packet, PacketEvent.Type.INCOMING);
            Melonity.MOD_EVENT_BUS.post(packetEvent);
            if (packetEvent.isCanceled()) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method={"send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void onSendPacket(class_2596<?> packet, @Nullable class_7648 callbacks, boolean flush, CallbackInfo callbackInfo) {
        PacketEvent packetEvent = new PacketEvent(packet, PacketEvent.Type.OUTGOING);
        if (Melonity.PROCESS_OUTGOING_EVENTS) {
            Melonity.MOD_EVENT_BUS.post(packetEvent);
        }
        if (packetEvent.isCanceled()) {
            callbackInfo.cancel();
        }
    }
}