// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface StringSetting {
    int SETTING_ID = 452912316;

    String getValue();
    void setValue(String value);
}