// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_7439;
import ru.melonity.Melonity;
import ru.melonity.f.b.IllIllIIIlllIllIIlllIlIllIIllIllIIlIIIlIIlllIIllIlllIIlIlIlIIlIIIllIIlIIIllIIIllIIlIIlIIIllIIlIllIIlllIlIIlllIllIIIllIIllIIlllIIIllIIllIIIllIIIllIIIlllIIlIlIIIlIlllIIIlIIllIlIIlllIlIIllIllIllIlllIIllIIIllIIIllIIlIIlIIIlIIllIll;
import ru.melonity.j.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll;
import ru.melonity.o.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;
import ru.melonity.o.b.a.b.IlllIIlIIIlllIIIlIIllIlIllIIIlIIIllIlllIIIllIIlIIllIIllIlllIIllIIllIIllIIllIIlllIIlllIllIIllIlllIlIllIIlllIllIllIIlllIIllIIIlllIIIllIIIllIlllIIlllIIlIlIlIlIllIIIlllIIlIllIIllIIlllIIlIIIllIIIllIIlIIlllIlIllIllIIlIIIllIllIIlIIllIll;
import ru.melonity.w.IllIIllIlIlIllIIllIIIllIIIllIIIllIIIlIlIlIllIlIIllIIlllIlllIIlIIIllIIlIllIIlllIIllIllIIIlIIlIlIlllIIIlllIIlIIIllIIIllIIIllIlIIlllIIlIlIIIlllIIlIlIIIlllIIlllIIlIllIllIIlIIIllIIllIlllIIllIIllIllIIllIIllIIlIIllIllIIllIIlIIl;

@Environment(EnvType.CLIENT)
public class AutoAcceptModule extends IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    private final IlllIIlIIIlllIIIlIIllIlIllIIIlIIIllIlllIIIllIIlIIllIIllIlllIIllIIllIIllIIllIIlllIIlllIllIIllIlllIlIllIIlllIllIllIIlllIIllIIIlllIIIllIIIllIlllIIlllIIlIlIlIlIllIIIlllIIlIllIIllIIlllIIlIIIllIIIllIIlIIlllIlIllIllIIlIIIllIllIIlIIllIll onlyFriendsSetting = new IlllIIlIIIlllIIIlIIllIlIllIIIlIIIllIlllIIIllIIlIIllIIllIlllIIllIIllIIllIIllIIlllIIlllIllIIllIlllIlIllIIlllIllIllIIlllIIllIIIlllIIIllIIIllIlllIIlllIIlIlIlIlIllIIIlllIIlIllIIllIIlllIIlIIIllIIIllIIlIIlllIlIllIllIIlIIIllIllIIlIIllIll("Only friends", true);
    private final ru.melonity.f.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll<IllIllIIIlllIllIIlllIlIllIIllIllIIlIIIlIIlllIIllIlllIIlIlIlIIlIIIllIIlIIIllIIIllIIlIIlIIIllIIlIllIIlllIlIIlllIllIIIllIIllIIlllIIIllIIllIIIllIIIllIIIlllIIlIlIIIlIlllIIIlIIllIlIIlllIlIIllIllIllIlllIIllIIIllIIIllIIlIIlIIIlIIllIll> teleportRequestHandler = packetEvent -> {
        if (!this.isEnabled()) {
            return;
        }
        class_2596<?> packet = packetEvent.getPacket();
        if (packet instanceof class_7439) {
            class_7439 teleportPacket = (class_7439) packet;
            class_2561 text = teleportPacket.comp_763();
            String message = text.getString().toLowerCase();
            if (message.contains("\u043f\u0440\u043e\u0441\u0438\u0442 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442") ||
                message.contains("\u0445\u043e\u0447\u0435\u0442 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f") ||
                message.contains("/tpaccept")) {
                if (onlyFriendsSetting.isEnabled()) {
                    IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll friendManager = Melonity.getFriendManager();
                    List<String> friends = friendManager.getFriends();
                    boolean isFriend = false;
                    for (String friend : friends) {
                        if (message.contains(friend.toLowerCase())) {
                            isFriend = true;
                            break;
                        }
                    }
                    if (!isFriend) {
                        return;
                    }
                }
                IllIIllIlIlIllIIllIIIllIIIllIIIllIIIlIlIlIllIlIIllIIlllIlllIIlIIIllIIlIllIIlllIIllIllIIIlIIlIlIlllIIIlllIIlIIIllIIIllIIIllIlIIlllIIlIlIIIlllIIlIlIIIlllIIlllIIlIllIllIIlIIIllIIllIlllIIllIIllIllIIllIIllIIlIIllIllIIllIIlIIl.sendCommand("/tpaccept");
            }
        }
    };

    public AutoAcceptModule() {
        super("AutoAccept", IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll.MISC);
        this.addSetting(onlyFriendsSetting);
        this.addListener(teleportRequestHandler);
    }
}