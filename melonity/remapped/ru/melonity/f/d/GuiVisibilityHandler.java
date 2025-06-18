// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.d;

import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import ru.melonity.Melonity;
import ru.melonity.f.ActionEventHandler;
import ru.melonity.f.NamedScreenEvent;

@Environment(value = EnvType.CLIENT)
public class GuiVisibilityHandler {
    private final ActionEventHandler<NamedScreenEvent> eventHandler = event -> {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof ru.melonity.o.ExcludedScreen) {
            return;
        }
        ru.melonity.h.ScreenManager manager = Melonity.getScreenManager();
        List<ru.melonity.h.GuiElement> elements = manager.getElements();
        Iterator<ru.melonity.h.GuiElement> iterator = elements.iterator();
        while (iterator.hasNext()) {
            ru.melonity.h.GuiElement element = iterator.next();
            ru.melonity.b.ElementMetadata data = element.getMetadata();
            if (data == null) {
                continue;
            }
            int eventId = event.getElementId();
            int elementId = data.getElementId();
            if (eventId != elementId) {
                continue;
            }
            int elementSubId = data.getSubElementId();
            if (elementSubId == -1) {
                continue;
            }
            int eventSubId = event.getSubElementId();
            if (eventSubId == 301) {
                continue;
            }
            boolean visible = element.isVisible();
            element.setVisible(!visible);
        }
    };
}