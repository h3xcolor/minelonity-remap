// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ResourceProvider {
    int API_VERSION = 1740602256;

    Resource loadResource(String resourceName);

    interface Resource {
    }
}