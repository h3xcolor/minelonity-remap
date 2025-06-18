// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.b.EventData;
import ru.melonity.d.ContextData;

@Environment(value = EnvType.CLIENT)
public interface ClientEventHandler {
    public static int DEFAULT_VALUE = 1943303282;

    public void handleEvent(EventData eventData, ContextData contextData);

    public String getEventName();

    public String getEventDescription();
}