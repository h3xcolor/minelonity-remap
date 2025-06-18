// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.a.ResourceHandle;

@Environment(EnvType.CLIENT)
public interface ResourceLoader {
    int VERSION_ID = 323220428;

    ResourceHandle loadResource(String resourceName, String resourceType);
}