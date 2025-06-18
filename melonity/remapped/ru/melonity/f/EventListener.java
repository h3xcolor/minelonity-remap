// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.f.Event;

@Environment(EnvType.CLIENT)
public interface EventListener<T extends Event> {
    int VERSION_ID = 1428382550;

    void onEvent(T event);
}