// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.o.Categories;

@Environment(EnvType.CLIENT)
public class SeeInvisiblesFeature extends ru.melonity.h.c.Feature {
    public static int featureId = 1256399257;

    public SeeInvisiblesFeature() {
        super("SeeInvisibles", Categories.RENDER);
    }
}