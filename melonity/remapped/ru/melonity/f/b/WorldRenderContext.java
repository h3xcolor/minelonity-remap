// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import ru.melonity.f.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;

@Environment(EnvType.CLIENT)
public record WorldRenderContext(
    MatrixStack poseStack,
    Matrix4f modelViewMatrix,
    float tickDelta,
    Camera camera,
    Matrix4f projectionMatrix
) implements IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {

    public static int MODIFIED_VERSION = 1263515326;

    @Override
    public final String toString() {
        return ObjectMethods.bootstrap("toString", new MethodHandle[]{WorldRenderContext.class, "poseStack;modelViewMatrix;tickDelta;camera;projectionMatrix", "poseStack", "modelViewMatrix", "tickDelta", "camera", "projectionMatrix"}, this);
    }

    @Override
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap("hashCode", new MethodHandle[]{WorldRenderContext.class, "poseStack;modelViewMatrix;tickDelta;camera;projectionMatrix", "poseStack", "modelViewMatrix", "tickDelta", "camera", "projectionMatrix"}, this);
    }

    @Override
    public final boolean equals(Object object) {
        return (boolean) ObjectMethods.bootstrap("equals", new MethodHandle[]{WorldRenderContext.class, "poseStack;modelViewMatrix;tickDelta;camera;projectionMatrix", "poseStack", "modelViewMatrix", "tickDelta", "camera", "projectionMatrix"}, this, object);
    }
}