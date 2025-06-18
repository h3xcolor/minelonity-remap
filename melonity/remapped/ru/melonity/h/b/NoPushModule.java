// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.h.c.Module;
import ru.melonity.o.b.a.b.EnumSetting;
import ru.melonity.o.b.a.b.EnumSettingType;

@Environment(EnvType.CLIENT)
public class NoPushModule extends Module {
    private final EnumSetting modeSetting = new EnumSetting("global.mode", EnumSettingType.MODE, "Players", "Water", "Blocks");
    public static int unusedStaticField = 1113893236;

    public NoPushModule() {
        super("NoPush", Module.Category.MOVEMENT);
        this.addSetting(this.modeSetting);
    }

    public EnumSetting getModeSetting() {
        return this.modeSetting;
    }
}