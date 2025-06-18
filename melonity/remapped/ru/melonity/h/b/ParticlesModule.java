// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import ru.melonity.Melonity;
import ru.melonity.f.events.RenderEvent;
import ru.melonity.f.settings.ColorSetting;
import ru.melonity.fabric.client.model.ILevelRenderer;
import ru.melonity.hud.module.Module;
import ru.melonity.s.c.RenderUtils;
import ru.melonity.w.WorldUtils;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class ParticlesModule extends Module {
    private final Color defaultColor = Melonity.getDefaultColor(1, 1);
    private final ColorSetting colorSetting = new ColorSetting("global.color", this.defaultColor);
    private final List<Particle> particles = Lists.newLinkedList();
    private final RenderEvent onRenderListener = this::onRender;

    public ParticlesModule() {
        super("Particles", colorSetting);
        this.registerSetting(this.colorSetting);
    }

    @Override
    public void onEnable(boolean state) {
        super.onEnable(state);
        if (!state) {
            this.particles.clear();
        }
    }

    private void onRender(RenderEvent event) {
        if (!isEnabled()) {
            return;
        }
        if (MinecraftClient.getInstance().player.age % 4 == 0) {
            for (int i = 0; i < 2; i++) {
                Vec3d playerPos = MinecraftClient.getInstance().player.getPos();
                Particle particle = new Particle(playerPos);
                this.particles.add(particle);
            }
        }
        this.particles.removeIf(Particle::shouldRemove);
        Camera camera = event.getCamera();
        for (Particle particle : particles) {
            if (!camera.getFrustum().isVisible(new Box(particle.position.x, particle.position.y, particle.position.z, particle.position.x + 1.0, particle.position.y + 1.0, particle.position.z + 1.0))) {
                continue;
            }
            float[] screenPos = WorldUtils.toScreenPos(particle.position.x, particle.position.y, particle.position.z);
            if (screenPos == null) {
                continue;
            }
            long currentTime = System.currentTimeMillis();
            float lifeFactor = 1.0f - (currentTime - particle.creationTime) / 5000.0f;
            lifeFactor *= 12.0f;
            Color particleColor = colorSetting.getValue();
            boolean useGlobalColor = particleColor.equals(defaultColor);
            Color renderColor = useGlobalColor ? Melonity.getColorScheme().getColor() : particleColor;
            int alpha = RenderUtils.applyAlpha(renderColor, (int) (particle.scale * 255.0f));
            MatrixStack matrixStack = Melonity.getMatrixStack();
            float x = screenPos[0] - 3.0f * lifeFactor - 2.0f;
            float y = screenPos[1] - 3.0f * lifeFactor - 2.0f;
            RenderUtils.drawRoundedRect(matrixStack, x, y, lifeFactor + 4.0f, lifeFactor + 4.0f, 10.0f, 7.0f, alpha, event.getTickDelta());
            Identifier logo = new Identifier("melonity/images/ui/mainmenu/main_menu_logo.png");
            float logoX = screenPos[0] - 3.0f * lifeFactor;
            float logoY = screenPos[1] - 3.0f * lifeFactor;
            Color logoColor = useGlobalColor ? Melonity.getColorScheme().getColor() : particleColor;
            int logoAlpha = RenderUtils.applyAlpha(logoColor, (int) (particle.scale * 255.0f));
            RenderUtils.drawTexture(matrixStack, logo, logoX, logoY, lifeFactor, lifeFactor, logoAlpha, event.getTickDelta());
        }
    }

    @Environment(EnvType.CLIENT)
    private static class Particle {
        private Vec3d position;
        private final long creationTime = System.currentTimeMillis();
        private float scale;

        public Particle(Vec3d startPos) {
            Random random = new Random();
            float offsetX = (random.nextFloat() - 0.5f) * 20.0f * 2.0f;
            float horizontalVelocity = MathHelper.sqrt(400.0f - offsetX * offsetX) * 2.0f;
            float offsetY = -1.0f;
            while (offsetY < 0.0f) {
                offsetY = (random.nextFloat() - 0.5f) * horizontalVelocity;
            }
            float offsetZ = (random.nextFloat() - 0.5f) * horizontalVelocity;
            this.position = startPos.add(offsetX, offsetY, offsetZ);
        }

        public boolean shouldRemove() {
            Vec3d gravityEffect = this.position.add(0.0, -5.0, 0.0);
            this.position = interpolate(this.position, gravityEffect, 0.75f);
            this.scale = interpolateScale(this.scale, 1.0f, 10.0f);
            if (System.currentTimeMillis() - creationTime > 5000) {
                return true;
            }
            Vec3d cameraPos = MinecraftClient.getInstance().player.getCameraPosVec(1.0f);
            ClientWorld world = MinecraftClient.getInstance().world;
            BlockHitResult hitResult = world.raycast(new BlockHitResult(cameraPos, null, this.position));
            return hitResult.getType() != HitResult.Type.MISS;
        }

        private float interpolateScale(float start, float end, float factor) {
            double deltaTimeFactor = getDeltaTimeFactor() * factor;
            float t = MathHelper.clamp((float) deltaTimeFactor, 0.0f, 1.0f);
            return (1.0f - t) * start + t * end;
        }

        private Vec3d interpolate(Vec3d start, Vec3d end, float factor) {
            double x = interpolateComponent((float) start.x, (float) end.x, factor);
            double y = interpolateComponent((float) start.y, (float) end.y, factor);
            double z = interpolateComponent((float) start.z, (float) end.z, factor);
            return new Vec3d(x, y, z);
        }

        private double interpolateComponent(float start, float end, float factor) {
            double deltaTimeFactor = getDeltaTimeFactor() * factor;
            float t = MathHelper.clamp((float) deltaTimeFactor, 0.0f, 1.0f);
            return (1.0f - t) * start + t * end;
        }

        private double getDeltaTimeFactor() {
            int fps = MinecraftClient.getInstance().getCurrentFps();
            return fps > 0 ? 1.0 / fps : 1.0;
        }
    }
}