// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.g.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.g.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll as Expirable;

@Environment(value=EnvType.CLIENT)
public record TimedExpiration(long expirationTime) implements Expirable {
    public TimedExpiration(long delay) {
        this(System.currentTimeMillis() + delay);
    }

    @Override
    public void IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll() {
    }

    @Override
    public boolean IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll() {
        return System.currentTimeMillis() >= this.expirationTime;
    }
}