// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.e;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ComponentContainer {
    int CONTAINER_ID = 268787505;

    List<Component> getComponents();
    void addComponent(Component component);
    <T extends Component> T getComponent(Class<T> componentClass);
    Component getComponent(int index);
}