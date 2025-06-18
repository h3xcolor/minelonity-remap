// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.n;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import ru.melonity.s.c.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll;

@Environment(EnvType.CLIENT)
public interface CustomRenderer {
    int DEFAULT_VALUE = 368197562;

    void renderWithContext(float r, float g, float b, double x, double y, IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll context, MatrixStack matrices);

    void renderWithIntData(float r, float g, float b, double x, double y, int data);

    void setColorRGB(int red, int green, int blue);

    void renderWithDepth(float r, float g, float b, double x, double y, double z);

    float getAlpha();
}