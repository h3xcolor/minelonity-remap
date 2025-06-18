// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import ru.melonity.f.d;
import ru.melonity.f.e;
import ru.melonity.f.b;
import ru.melonity.o.a;
import ru.melonity.w.c;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class AutoFish extends ru.melonity.h.c.a {
    private final d timer = new d();
    private boolean shouldRecast = false;
    private boolean shouldReel = false;
    private final e<b> packetListener = packet -> {
        if (!isEnabled()) {
            return;
        }
        boolean flag1 = timer.elapsed(600L);
        if (flag1 && shouldRecast) {
            AutoFish.access$getInteractionManager().interact((PlayerEntity) AutoFish.access$getPlayer(), Hand.MAIN_HAND);
            shouldRecast = false;
            shouldReel = true;
            timer.reset();
        }
        boolean flag2 = timer.elapsed(300L);
        if (flag2 && shouldReel) {
            AutoFish.access$getInteractionManager().interact((PlayerEntity) AutoFish.access$getPlayer(), Hand.MAIN_HAND);
            shouldReel = false;
            timer.reset();
        }
    };
    private final e<c> soundListener = event -> {
        if (!isEnabled()) {
            return;
        }
        Packet<?> packet = event.getPacket();
        Packet<?> soundPacket = packet;
        if (soundPacket instanceof PlaySoundS2CPacket) {
            PlaySoundS2CPacket soundPacket2 = (PlaySoundS2CPacket) soundPacket;
            TypedActionResult<SoundEvent> actionResult = soundPacket2.getSound();
            Optional<SoundEvent> optional = actionResult.getValue();
            if (optional.isPresent()) {
                SoundEvent soundEvent = optional.get();
                Identifier identifier = soundEvent.getId();
                String soundName = identifier.toString();
                if (soundName.equals("entity.fishing_bobber.splash")) {
                    shouldRecast = true;
                    timer.reset();
                }
            }
        }
    };
    public static int unusedField = 580883015;

    public AutoFish() {
        super("AutoFish", ru.melonity.o.a.AUTOFISH);
    }
}