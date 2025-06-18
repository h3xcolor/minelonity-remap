// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.TimeUpdateS2CPacket;
import ru.melonity.f.EventHandler;
import ru.melonity.f.b.ModeChangedEvent;
import ru.melonity.f.b.PacketReceivedEvent;
import ru.melonity.o.EnumSetting;
import ru.melonity.o.ModuleBase;
import ru.melonity.fabric.client.model.IClientWorld;
import java.util.List;

@Environment(EnvType.CLIENT)
public class WorldTimeModule extends ModuleBase {
    private final EnumSetting timeSetting = new EnumSetting("Time", EnumSetting.Mode.DROPDOWN, "Day", "Afternoon", "Night", "Sunset");
    private final EventHandler<ModeChangedEvent> timeChangeHandler = event -> {
        if (isEnabled()) {
            long time = 0L;
            try {
                List<String> options = timeSetting.getOptions();
                String selected = options.get(timeSetting.getSelectedIndex());
                String mode = selected;
                int selectedIndex = -1;
                switch (mode) {
                    case "Day":
                        selectedIndex = 0;
                        break;
                    case "Afternoon":
                        selectedIndex = 1;
                        break;
                    case "Night":
                        selectedIndex = 2;
                        break;
                    case "Sunset":
                        selectedIndex = 3;
                        break;
                }
                switch (selectedIndex) {
                    case 0:
                        time = 18L;
                        break;
                    case 1:
                        time = 127L;
                        break;
                    case 2:
                        time = 214L;
                        break;
                    case 3:
                        time = 230L;
                        break;
                }
            } catch (Throwable ignored) {
            }
            ((IClientWorld) this.mc.world).setTimeOfDay(time * 100L);
        }
    };
    private final EventHandler<PacketReceivedEvent> packetHandler = event -> {
        if (isEnabled()) {
            Packet<?> packet = event.getPacket();
            if (packet instanceof TimeUpdateS2CPacket) {
                event.setCancelled(true);
            }
        }
    };

    public WorldTimeModule() {
        super("WorldTime", ModuleBase.Category.WORLD);
        addSetting(timeSetting);
    }
}