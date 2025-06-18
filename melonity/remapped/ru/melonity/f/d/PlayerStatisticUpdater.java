// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.d;

import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1657;
import net.minecraft.class_269;
import net.minecraft.class_310;
import net.minecraft.class_642;
import net.minecraft.class_9015;
import ru.melonity.f.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll;
import ru.melonity.f.b.IIlIIlIllIIllIIllIIIlIIIlllIIIlIIlllIIIlIIllIIlIIlIIllIIIllIlllIIIlllIIlIlIIIllIIlllIIlIlllIIllIIlIIllIIlIllIllIIlIIllIIllIIllIIlllIIlIIllIlIIllIlIIllIlllIllIlllIIIlIIllIIlllIlIllIIlIllIIlIIllIIIllIllIIlIIIlIllIllIlllIll;

@Environment(EnvType.CLIENT)
public class PlayerStatisticUpdater {
    private final IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll<IIlIIlIllIIllIIllIIIlIIIlllIIIlIIlllIIIlIIllIIlIIlIIllIIIllIlllIIIlllIIlIlIIIllIIlllIIlIlllIIllIIlIIllIIlIllIllIIlIIllIIllIIllIIlllIIlIIllIlIIllIlIIllIlllIllIlllIIIlIIllIIlllIlIllIIlIllIIlIIllIIIllIllIIlIIIlIllIllIlllIll> playerEventHandler = event -> {
        class_310 minecraftClient = class_310.method_1551();
        class_642 world = minecraftClient.field_1687;
        if (world != null) {
            List<class_1657> players = world.method_18456();
            for (class_1657 player : players) {
                GameProfile profile = player.method_7334();
                class_9015 statType = class_9015.method_55420(profile);
                class_269 statisticsManager = minecraftClient.field_1687.method_8428();
                Object2IntMap<?> playerStatMap = statisticsManager.method_1166(statType);
                IntCollection statValues = playerStatMap.values();
                statValues.intStream().forEach(statValue -> {
                    player.method_6033(statValue);
                });
            }
        }
    };
}