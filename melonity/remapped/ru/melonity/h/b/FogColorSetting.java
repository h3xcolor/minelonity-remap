// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.awt.Color;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.h.c.BaseSettingsContainer;
import ru.melonity.o.CategoryType;
import ru.melonity.o.b.a.b.ColorSetting;

@Environment(EnvType.CLIENT)
public class FogColorSetting extends BaseSettingsContainer {
    private final ColorSetting colorSetting = new ColorSetting("global.color", Color.MAGENTA);
    public static int constantId = 919993731;

    public FogColorSetting() {
        super("FogColor", CategoryType.RENDER);
        this.addSetting(this.colorSetting);
    }

    @Generated
    public ColorSetting getColorSetting() {
        return this.colorSetting;
    }
}