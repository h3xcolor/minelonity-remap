// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientModule implements ClientModule.ClientFeature {
    public static int MODULE_ID = 1982486922;

    public interface ClientFeature {
    }
}