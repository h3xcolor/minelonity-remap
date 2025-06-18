// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h;

import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.h.Element;
import ru.melonity.o.Category;

@Environment(value = EnvType.CLIENT)
public interface ElementManager {
    public static int DEFAULT_COLOR = 13946823;

    void addElement(Element element);

    <T extends Element> Optional<T> getElement(Class<T> clazz);

    Optional<Element> getElement(String id);

    List<Element> getAllElements();

    List<Element> getElementsByCategory(Category category);

    void clear();
}