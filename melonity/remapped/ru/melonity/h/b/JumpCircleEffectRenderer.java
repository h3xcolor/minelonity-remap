// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.f.event.EventHandler;
import ru.melonity.f.event.EventState;
import ru.melonity.f.event.impl.PlayerJumpEvent;
import ru.melonity.f.event.impl.RenderEvent;
import ru.melonity.f.render.VAO;
import ru.melonity.h.c.RenderProcessor;
import ru.melonity.o.EventPriority;
import ru.melonity.y.Model;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public class JumpCircleEffectRenderer extends RenderProcessor {
    private final CopyOnWriteArrayList<JumpCircleInstance> jumpCircleInstances = Lists.newCopyOnWriteArrayList();
    private boolean isJumping;
    private final EventHandler<PlayerJumpEvent> onPlayerJump = event -> {
        if (this.isJumping) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player.isOnGround()) {
                Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
                float tickDelta = camera.getSubmersionTickDelta();
                Vec3d playerPos = player.getPos();
                Vec3d adjustedPos = playerPos.add(0.0, 0.05, 0.0);
                JumpCircleInstance jumpCircle = new JumpCircleInstance(adjustedPos);
                this.jumpCircleInstances.add(jumpCircle);
            }
        }
    };
    private final EventHandler<RenderEvent> renderHandler = event -> {
        if (!this.isActive()) {
            return;
        }
        this.isJumping = true;
    };
    private final Model largeWaveModel = new Model(this.getClass().getResourceAsStream("/assets/minecraft/melonity/models/jumpc_wave_l.obj"), this.getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/jumpcircle_waves.png"));
    private final Model mediumWaveModel = new Model(this.getClass().getResourceAsStream("/assets/minecraft/melonity/models/jumpc_wave_m.obj"), this.getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/jumpcircle_waves.png"));
    private final Model smallWaveModel = new Model(this.getClass().getResourceAsStream("/assets/minecraft/melonity/models/jumpc_wave_s.obj"), this.getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/jumpcircle_waves.png"));
    private final Model floorModel = new Model(this.getClass().getResourceAsStream("/assets/minecraft/melonity/models/jumpc_floor.obj"), this.getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/jumpcircle_floor.png"));
    private final EventHandler<RenderEvent> afterWorldRenderHandler = event -> {
        if (!this.isActive()) {
            return;
        }
        Matrix4f projectionMatrix = event.getProjectionMatrix();
        Matrix4f viewMatrix = new Matrix4f(projectionMatrix);
        MinecraftClient client = MinecraftClient.getInstance();
        GameRenderer gameRenderer = client.gameRenderer;
        Vec3d cameraPos = gameRenderer.getCamera().getPos();
        viewMatrix.translate((float) (-cameraPos.x), (float) (-cameraPos.y), (float) (-cameraPos.z));
        boolean depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        boolean depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        boolean cullFace = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        int vertexArray = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        VAO.INSTANCE.bind();
        this.jumpCircleInstances.removeIf(jumpCircle -> {
            if (!jumpCircle.isActive()) return false;
            float progress = jumpCircle.getOverallProgress();
            return progress <= 0.0f;
        });
        Iterator<JumpCircleInstance> iterator = this.jumpCircleInstances.iterator();
        while (iterator.hasNext()) {
            JumpCircleInstance jumpCircle = iterator.next();
            Matrix4f largeWaveMatrix = new Matrix4f(viewMatrix);
            this.renderModel(largeWaveModel, largeWaveMatrix, jumpCircle.position, jumpCircle.waveAnimation, 1);
            Matrix4f mediumWaveMatrix = new Matrix4f(viewMatrix);
            this.renderModel(mediumWaveModel, mediumWaveMatrix, jumpCircle.position, jumpCircle.waveAnimation2, 2);
            Matrix4f smallWaveMatrix = new Matrix4f(viewMatrix);
            this.renderModel(smallWaveModel, smallWaveMatrix, jumpCircle.position, jumpCircle.waveAnimation3, 3);
            if (!jumpCircle.isActive()) {
                float progress = jumpCircle.getOverallProgress();
                if (progress >= 0.2f) {
                    Matrix4f floorMatrix = new Matrix4f(viewMatrix);
                    this.renderModel(floorModel, floorMatrix, jumpCircle.position, jumpCircle.floorAnimation, 4);
                }
            }
        }
        GlStateManager._bindTexture(0);
        VAO.INSTANCE.unbind();
        GL11.glDepthMask(depthMask);
        setGlState(GL11.GL_DEPTH_TEST, depthTest);
        setGlState(GL11.GL_CULL_FACE, cullFace);
        GL30.glBindVertexArray(vertexArray);
    };

    public JumpCircleEffectRenderer() {
        super("Circles", EventPriority.HIGH);
    }

    private void bindTexture(VAO modelVAO, int textureUnit) {
        modelVAO.bindTexture(textureUnit);
    }

    private void renderModel(Model model, Matrix4f matrix, Vec3d position, JumpCircleAnimation animation, int layer) {
        float scale;
        switch (layer) {
            case 1: {
                float progress = animation.getProgress(!animation.isFinished);
                scale = progress * 1.1f;
                if (scale == 1.1f) {
                    animation.isFinished = true;
                }
                break;
            }
            case 2: {
                float progress = animation.getProgress(!animation.isFinished);
                scale = progress * 0.7f;
                if (scale == 0.7f) {
                    animation.isFinished = true;
                }
                break;
            }
            case 3: {
                float progress = animation.getProgress(!animation.isFinished);
                scale = progress * 0.62f;
                if (scale == 0.62f) {
                    animation.isFinished = true;
                }
                break;
            }
            case 4: {
                float progress = animation.getProgress(!animation.isFinished);
                scale = progress * 1.0f;
                if (scale == 1.0f) {
                    animation.isFinished = true;
                }
                break;
            }
            default:
                scale = 1.0f;
        }
        float x = (float) position.x;
        float y = (float) position.y;
        float z = (float) position.z;
        float scaleY = 1.0f;
        matrix.translate(x, y, z);
        if (layer == 1 || layer == 3) {
            matrix.scale(scale, scaleY, scale);
        } else {
            matrix.scale(scale, scaleY, scale);
        }
        VAO.INSTANCE.uploadMatrix(matrix);
        BiConsumer<VAO, Integer> bindFunc = this::bindTexture;
        bindFunc.accept(model.getVAO(), 0);
        VAO.INSTANCE.setOpacity(0.0f);
        model.getVAO().render();
    }

    private static void setGlState(int capability, boolean enable) {
        if (enable) {
            GL20.glEnable(capability);
        } else {
            GL20.glDisable(capability);
        }
    }

    @Environment(EnvType.CLIENT)
    private static class JumpCircleAnimation {
        private final FrameWeightCalculator weightCalculator;
        private final StateAnimation stateAnimation;
        public boolean isFinished = false;

        public JumpCircleAnimation(long duration) {
            this.weightCalculator = FrameWeightCalculator.milliseconds(duration);
            this.stateAnimation = new StateAnimation();
        }

        public float getProgress(boolean active) {
            this.stateAnimation.state(active);
            this.stateAnimation.update(this.weightCalculator.elapsedUnits());
            return this.stateAnimation.animation();
        }

        public float getProgress() {
            return this.stateAnimation.animation();
        }

        public float getDropProgress() {
            return this.stateAnimation.dropAnimation();
        }
    }

    @Environment(EnvType.CLIENT)
    private static class JumpCircleInstance {
        private final Vec3d position;
        private final JumpCircleAnimation waveAnimation = new JumpCircleAnimation(350L);
        private final JumpCircleAnimation waveAnimation2 = new JumpCircleAnimation(560L);
        private final JumpCircleAnimation waveAnimation3 = new JumpCircleAnimation(720L);
        private final JumpCircleAnimation floorAnimation = new JumpCircleAnimation(1000L);

        public JumpCircleInstance(Vec3d position) {
            this.position = position;
        }

        public boolean isActive() {
            return !floorAnimation.isFinished;
        }

        public float getOverallProgress() {
            return floorAnimation.getProgress();
        }
    }
}