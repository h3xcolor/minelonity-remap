// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.b.IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll;

@Environment(EnvType.CLIENT)
public interface ClientRegistry {
    public static int RESOURCE_REGISTRY_ID = 1215755690;

    Resource getResource(String id);

    List<Resource> getAllResources();
}