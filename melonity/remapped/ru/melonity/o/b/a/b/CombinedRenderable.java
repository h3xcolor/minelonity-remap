// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b.a.b;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import ru.melonity.o.b.a.b.RenderableComponent;
import ru.melonity.s.c.RenderContext;

@Environment(value = EnvType.CLIENT)
public class CombinedRenderable implements ru.melonity.o.b.a.Renderable {
    private final RenderableComponent firstRenderable;
    private final RenderableComponent secondRenderable;
    public static int GENERATED_ID = 1950498726;

    public CombinedRenderable(RenderableComponent firstRenderable, RenderableComponent secondRenderable) {
        this.firstRenderable = firstRenderable;
        this.secondRenderable = secondRenderable;
    }

    @Override
    public void render(Renderable entity, float x, float y, float z, double partialTicks, double scale, RenderContext context, class_4587 matrixStack, class_332 vertexConsumerProvider) {
        this.firstRenderable.render(entity, x, y, z, partialTicks, scale, context, matrixStack, vertexConsumerProvider);
        this.secondRenderable.render(entity, x + 75.0f, y, z, partialTicks, scale, context, matrixStack, vertexConsumerProvider);
    }

    @Override
    public void updatePosition(float x, float y, float z, double partialTicks, double scale, int light) {
        this.firstRenderable.updatePosition(x, y, z, partialTicks, scale, light);
        this.secondRenderable.updatePosition(x + 75.0f, y, z, partialTicks, scale, light);
    }

    @Override
    public boolean handleClick(int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    @Override
    public void resetState() {
        this.firstRenderable.resetState();
        this.secondRenderable.resetState();
    }

    @Override
    public float getRenderWidth() {
        return this.firstRenderable.getRenderWidth();
    }

    @Override
    public void cleanup() {
        this.firstRenderable.cleanup();
        this.secondRenderable.cleanup();
    }

    @Generated
    public RenderableComponent getFirstRenderable() {
        return this.firstRenderable;
    }

    @Generated
    public RenderableComponent getSecondRenderable() {
        return this.secondRenderable;
    }
}