// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.h.c.Module;
import ru.melonity.o.b.a.b.ModeSetting;

@Environment(value = EnvType.CLIENT)
public class ShaderESPMod extends Module {
    private final ModeSetting modeSetting = new ModeSetting("global.mode", ModeSetting.SettingType.MODE, "Galaxy 1", "Galaxy 1", "Galaxy 2", "Galaxy 3");
    public static int VERSION_HASH = 500249560;

    public ShaderESPMod() {
        super("ShaderESP", ru.melonity.o.ModuleCategory.RENDER);
        this.addSetting(this.modeSetting);
    }

    public ModeSetting getModeSetting() {
        return this.modeSetting;
    }
}