// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s.e;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import ru.melonity.o.a.RenderContext;
import ru.melonity.s.c.RenderableState;

@Environment(value = EnvType.CLIENT)
public abstract class AbstractRenderable {
    protected float x;
    protected float y;
    public static int DEFAULT_COLOR = 194567429;

    public abstract void render(RenderContext context, float mouseX, float mouseY, double renderX, double renderY, RenderableState state, MatrixStack matrixStack);

    public abstract void onMouseClick(float mouseX, float mouseY, double componentX, double componentY, int button);

    public abstract boolean isMouseOver(int mouseX, int mouseY, int button);

    public abstract void close();

    @Generated
    public AbstractRenderable(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Generated
    public float getX() {
        return this.x;
    }

    @Generated
    public float getY() {
        return this.y;
    }

    @Generated
    public void setX(float x) {
        this.x = x;
    }

    @Generated
    public void setY(float y) {
        this.y = y;
    }
}