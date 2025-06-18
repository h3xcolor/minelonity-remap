// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.g;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.g.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;

@Environment(EnvType.CLIENT)
public interface MouseInputHandler {
    public static int MOUSE_HANDLER_ID = 753477718;

    public void handleMouseEvent(IIllIllIIllIlIIlIl极极lllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIl极极IlIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll var1);

    public void handleMouseInput();

    public static MouseInputHandler createMouseInputHandler() {
        return new ru.melonity.g.a.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll();
    }
}