// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public interface ClientDataProvider {
    public static int KEY = 1325012216;

    public List<DataModel> getDataModels();
}