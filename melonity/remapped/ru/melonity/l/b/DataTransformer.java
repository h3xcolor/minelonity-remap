// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.l.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.l.a.TransformedResult;
import ru.melonity.l.InputObject;

@Environment(EnvType.CLIENT)
public interface DataTransformer {
    public static int DEFAULT_KEY = 981767901;

    public TransformedResult transform(InputObject input);
}