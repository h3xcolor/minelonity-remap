// ремапили ребята из https://t.me/dno_rumine
package net.example.chinahat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.function.BiConsumer;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class ChinahatRenderer extends CosmeticRenderer {
    private final Model model = new Model(
        ChinahatRenderer.class.getResourceAsStream("/assets/minecraft/melonity/models/nimb.obj"),
        ChinahatRenderer.class.getResourceAsStream("/assets/minecraft/melonity/images/models/nimb.png")
    );
    private final BiConsumer<MatrixStack, Integer> renderer = matrixStack -> {
        MinecraftClient client = MinecraftClient.getInstance();
        boolean depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        boolean depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        boolean cullFace = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        int boundVAO = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);

        Matrix4f modelMatrix = new Matrix4f(matrixStack.peek().getPositionMatrix());
        Camera camera = client.gameRenderer.getCamera();
        Vec3d cameraPos = camera.getPos();
        modelMatrix.translate((float) -cameraPos.x, (float) -cameraPos.y, (float) -cameraPos.z);

        RenderUtils.bindModel();
        renderModel(this.model, modelMatrix);
        RenderUtils.unbindModel();

        GL11.glDepthMask(depthMask);
        setGlCapability(GL11.GL_DEPTH_TEST, depthTest);
        setGlCapability(GL11.GL_CULL_FACE, cullFace);
        GL30.glBindVertexArray(boundVAO);
    };

    public ChinahatRenderer() {
        super("Chinahat", renderer);
    }

    private void bindModel(Model model, int textureUnit) {
        model.bindTexture(textureUnit);
    }

    private void renderModel(Model model, Matrix4f matrix) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        ClientPlayerEntity player = client.player;

        if (camera.getSubmersionType() != Camera.SubmersionType.NONE) {
            return;
        }

        Vec3d headPos = player.getPos().add(0, player.getStandingEyeHeight(), 0);
        double headX = headPos.x;
        double headY = headPos.y;
        double headZ = headPos.z;

        double cameraX = MathHelper.hypot(headX - camera.getPos().x, headZ - camera.getPos().z);
        double cameraY = MathHelper.hypot(headY - camera.getPos().y, headZ - camera.getPos().z) + 1.2;
        double cameraZ = MathHelper.hypot(headX - camera.getPos().x, headY - camera.getPos().y);

        matrix.translate((float) cameraX, (float) cameraY, (float) cameraZ);
        matrix.scale(0.26f, 0.46f, 0.26f);

        float yaw = player.getYaw();
        Quaternionf yawRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(360.0f - yaw);
        matrix.rotate(yawRotation);

        Quaternionf pitchRotation = Vec3f.POSITIVE_Z.getDegreesQuaternion(-15.0f);
        matrix.rotate(pitchRotation);

        RenderUtils.renderModel(model, matrix);
        RenderUtils.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        model.render();
    }

    private static void setGlCapability(int capability, boolean enable) {
        if (enable) {
            GL20.glEnable(capability);
        } else {
            GL20.glDisable(capability);
        }
    }

    public Model getModel() {
        return model;
    }

    public BiConsumer<MatrixStack, Integer> getRenderer() {
        return renderer;
    }
}