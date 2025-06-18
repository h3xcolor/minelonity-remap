// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.n.Stage;

@Environment(EnvType.CLIENT)
public interface IMainMenuStage {
    int BUILD_NUMBER = 1590443025;

    Stage getStage();

    void setStage(Stage stage);
}