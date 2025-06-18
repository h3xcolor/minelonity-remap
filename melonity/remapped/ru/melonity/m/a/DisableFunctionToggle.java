// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.m.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.m.a.BooleanSetting;

@Environment(value = EnvType.CLIENT)
public class DisableFunctionToggle extends BooleanSetting {
    public static int SETTING_ID = 1924764719;

    public DisableFunctionToggle() {
        super(false);
    }

    @Override
    public String getDescription() {
        return "Disable function";
    }
}