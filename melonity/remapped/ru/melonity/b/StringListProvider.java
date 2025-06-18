// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public interface StringListProvider {
    public static int CONSTANT_ID = 48902221;

    public List<String> getStringList();
}