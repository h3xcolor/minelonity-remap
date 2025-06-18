// ремапили ребята из https://t.me/dno_rumine
package rut.melonity.h.b;

import com.mojang.blaze3d.platform.GlStateManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.util.shape.VoxelShapeCollision;
import net.minecraft.util.shape.VoxelShapeType;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import rut.melonity.f.Module;
import rut.melonity.f.ModuleSetting;
import rut.melonity.f.event.EntityHitEvent;
import rut.melonity.f.event.EntityTrackEvent;
import rut.melonity.f.event.PlayerKillEvent;
import rut.melonity.o.ModelTexture;
import rut.melonity.o.SettingCategory;
import rut.melonity.u.ModelShader;
import rut.melonity.u.ShaderProgram;

@Environment(EnvType.CLIENT)
public class ParticleFXSystem extends Module {
    private final ModuleSetting settings = new ModuleSetting("Pack", SettingCategory.EFFECTS, "MelonityVerse", "MelonityVerse", "Purple", "Swear words", "Weapons");
    private final List<ModelTexture> mcTextures = List.of(
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_mc.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/mc_particle.png")),
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_mlt.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/mlt_particle.png")),
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_rmpg.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/rmpg_particle.png"))
    );
    private final List<ModelTexture> purpleTextures = List.of(
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_heart.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/particle_regular.png")),
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_morgenstern.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/particle_regular.png")),
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_star.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/particle_regular.png"))
    );
    private final List<ModelTexture> swearTextures = List.of(
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_exclamation.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/particle_swearwords.png")),
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_hash.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/particle_swearwords.png")),
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_question.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/particle_swearwords.png")),
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_questionexsclam.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/particle_swearwords.png")),
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_strongword.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/particle_swearwords.png"))
    );
    private final List<ModelTexture> weaponTextures = List.of(
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_arrow.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/Particles_Weapon.png")),
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_spear.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/Particles_Weapon.png")),
        new ModelTexture(getClass().getResourceAsStream("/assets/minecraft/melonity/models/particle_sword.obj"), getClass().getResourceAsStream("/assets/minecraft/melonity/images/models/Particles_Weapon.png"))
    );
    private final List<ParticleInstance> particles = Collections.synchronizedList(new ArrayList());
    private ModelShader modelShader;
    private ShaderProgram particleShader;
    private int textureCounter;
    private final EntityHitEvent playerKillListener = event -> {
        if (!isActive()) return;
        createDeathEffects(event.getEntity().getPos().x, event.getEntity().getY(), event.getEntity().getPos().z);
    };
    private final PlayerKillEvent killTrackerListener = event -> {
        if (!isActive()) return;
        processKill(event.getVictim());
    };
    private final EntityTrackEvent entityTrackerListener = event -> {
        if (!isActive()) return;
        if (!MinecraftClient.getInstance().world.getTime() % 10 == 0) return;
        Entity target = event.getEntity();
        if (target instanceof LivingEntity living) {
            Vec3d pos = living.getPos();
            for (int i = 0; i < 12; i++) {
                createEffect(pos.x, living.getY() + living.getHeight(), pos.z);
            }
        }
    };

    public ParticleFXSystem() {
        super("3D Particles", Module.Category.VISUAL);
        addSetting(settings);
    }

    private void renderScene() {
        textureCounter = 0;
    }

    private void renderEffect(float partialTicks, Matrix4f viewMatrix) {
        Matrix4f matrix = new Matrix4f(viewMatrix);
        renderScene();
        MatrixStack matrixStack = MinecraftClient.getInstance().worldRenderer.getMatrices();
        Vec3d camPos = matrixStack.getPosition();
        Vec3d cameraPos = new Vec3d(-camPos.x, -camPos.y, -camPos.z);
        matrix.translate((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z);
        boolean restoreDepthMask = GL30.glIsEnabled(GL30.GL_DEPTH_TEST);
        boolean restoreCull = GL30.glIsEnabled(GL30.GL_CULL_FACE);
        boolean restoreClip = GL30.glIsEnabled(GL30.GL_DEPTH_CLAMP);
        int restoreVAO = GL30.glGenVertexArrays();
        GL30.glDepthMask(true);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDisable(GL30.GL_CULL_FACE);
        modelShader.bind();
        try {
            Iterator<ParticleInstance> iterator = particles.iterator();
            while (iterator.hasNext()) {
                ParticleInstance particle = iterator.next();
                BiConsumer<ShaderProgram, Integer> textureFunc = this::setActiveTexture;
                Matrix4f particleMatrix = new Matrix4f(matrix);
                String currentPack = settings.getFirstSelected();
                boolean isSwear = currentPack.equals("Swear words");
                particle.render(modelShader, textureFunc, partialTicks, particleMatrix, !isSwear);
                GlStateManager.bindTexture(0);
            }
        } catch (Throwable ignored) {}
        modelShader.unbind();
        GL30.glDepthMask(restoreDepthMask);
        setDepthState(GL30.GL_DEPTH_TEST, restoreDepthMask);
        setDepthState(GL30.GL_CULL_FACE, restoreCull);
        GL30.glBindVertexArray(restoreVAO);
    }

    public void setActiveTexture(ShaderProgram program, int sampler) {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, textureCounter++);
        program.setInt(sampler, textureCounter);
    }

    private void removeDeadEffects() {
        particles.remove(ParticleInstance::isDead);
        try {
            Iterator<ParticleInstance> iterator = particles.iterator();
            while (iterator.hasNext()) {
                ParticleInstance particle = iterator.next();
                particle.update();
            }
        } catch (Throwable ignored) {}
    }

    private void createDeathEffects(double x, double y, double z) {
        ModelTexture textureModel = getRandomTextureModel();
        float size = getRandomFloat();
        float sizeScale = size * 30.0f;
        float spiralSpeed = getRandomFloat() * 15.0f;
        float rotationRate = getRandomFloat() * 30.0f;
        float rotationAngle = getRandomFloat() * 360.0f;
        float spinAngle = getRandomFloat() * 360.0f;
        ParticleInstance particle = new ParticleInstance(textureModel, x, y, z, sizeScale, spiralSpeed, rotationRate, rotationAngle, -90.0f + 180.0f * spinAngle);
        particles.add(particle);
        particles.sort(Comparator.naturalOrder());
    }

    private ModelTexture getRandomTextureModel() {
        List<ModelTexture> textureModels = null;
        String selectedPack = settings.getFirstSelected();
        if (selectedPack.equals("MelonityVerse")) {
            textureModels = mcTextures;
        } else if (selectedPack.equals("Purple")) {
            textureModels = purpleTextures;
        } else if (selectedPack.equals("Swear words")) {
            textureModels = swearTextures;
        } else if (selectedPack.equals("Weapons")) {
            textureModels = weaponTextures;
        }
        return textureModels != null && !textureModels.isEmpty() ? 
               textureModels.get(ThreadLocalRandom.current().nextInt(textureModels.size())) : 
               null;
    }

    private void processKill(Entity victim) {
        if (victim instanceof LivingEntity living) {
            Vec3d pos = living.getPos();
            double height = living.getHeight();
            for (int i = 0; i < 12; i++) {
                createEffect(pos.x, pos.y + height, pos.z);
            }
        }
    }

    private float getRandomFloat() {
        return (float) Math.random();
    }

    private void setDepthState(int state, boolean enable) {
        if (enable) {
            GL20.glEnable(state);
        } else {
            GL20.glDisable(state);
        }
    }

    private void createEffect(double x, double y, double z) {
        createDeathEffects(x, y, z);
    }

    @Environment(EnvType.CLIENT)
    private class ParticleInstance implements Comparable<ParticleInstance> {
        private double initialX;
        private double initialY;
        private double initialZ;
        private double velocityX;
        private double velocityY;
        private double velocityZ;
        private double gravityX;
        private double gravityY;
        private double gravityZ;
        private final ModelShader model;
        private final ShaderProgram texture;
        private int age;
        private final float baseSize;
        private final float spiralRadius;
        private final float rotationRate;
        private float pitchAngle;
        private float yawAngle;
        private float rollAngle;
        private float initialRoll;
        private float speed;
        private float spiralDirection;
        private final float DEGREE = (float) (Math.PI / 180);
        public int stateControl = ThreadLocalRandom.current().nextInt();

        public ParticleInstance(ModelShader modelShader, double x, double y, double z,
            float sizeBase, float spiralVelocity, float rotationVelocity, float startRoll, float startAngle) {
            this.model = modelShader;
            this.texture = modelShader.getShader();
            resetPosition(x, y, z);
            calculateVelocity(startAngle, startRoll, sizeBase);
            baseSize = sizeBase;
            spiralRadius = spiralVelocity;
            rotationRate = rotationVelocity;
            initialRoll = startRoll;
            pitchAngle = initialRoll;
            yawAngle = initialRoll;
            rollAngle = initialRoll;
            spiralDirection = spiralVelocity;
        }

        public void render(ShaderProgram baseShader, BiConsumer<ShaderProgram, Integer> textureSetup,
            float partialTick, Matrix4f matrixStack, boolean applyRotation) {
            double interpolatedX = interpolate(initialX, initialX + velocityX, partialTick);
            double interpolatedY = interpolate(initialY, initialY + velocityY, partialTick);
            double interpolatedZ = interpolate(initialZ, initialZ + velocityZ, partialTick);
            float visibleScale = calculateVisibleScale();
            matrixStack.translate((float) interpolatedX, (float) interpolatedY, (float) interpolatedZ);
            if (applyRotation) {
                float currentRoll = interpolateRotation(pitchAngle, initialRoll, partialTick);
                Quaternionf pitchQuat = RotationAxis.POSITIVE_X.rotationDegrees(currentRoll);
                matrixStack.rotate(pitchQuat);
                float currentYaw = interpolateRotation(yawAngle, initialRoll, partialTick);
                Quaternionf yawQuat = RotationAxis.POSITIVE_Y.rotationDegrees(currentYaw);
                matrixStack.rotate(yawQuat);
                float currentSpin = interpolateRotation(rollAngle, spiralDirection, partialTick);
                Quaternionf rollQuat = RotationAxis.POSITIVE_Z.rotationDegrees(currentSpin);
                matrixStack.rotate(rollQuat);
            }
            matrixStack.scale(visibleScale, visibleScale, visibleScale);
            baseShader.setMatrix(matrixStack);
            ModelShader.setUniformTexture(0);
            textureSetup.accept(texture, 0);
            model.render();
        }

        public void update() {
            savePreviousState();
            velocityX *= 0.75;
            velocityY *= 0.75;
            velocityZ *= 0.75;
            initialRoll += baseSize;
            pitchAngle += spiralRadius;
            yawAngle += rotationRate;
            double nextX = initialX + velocityX;
            double nextY = initialY + velocityY;
            double nextZ = initialZ + velocityZ;
            MinecraftClient mc = MinecraftClient.getInstance();
            World world = mc.world;
            Vec3d startPos = new Vec3d(initialX, initialY, initialZ);
            Vec3d nextPos = new Vec3d(nextX, nextY, nextZ);
            Box entityBox = Box.fromPositions(startPos, nextPos);
            HitResult collision = world.collidesWith(entityBox);
            if (collision.getType() != HitResult.Type.MISS) {
                float reflectStrength = bounceStrength();
                Direction face = ((HitResult) collision).getSide();
                Vec3d normal = face.getUnitVector();
                Vec3d currentVector = new Vec3d(velocityX, velocityY, velocityZ);
                Vec3d reflection = currentVector.lerp(reflect(currentVector, normal), 0.5).multiply(reflectStrength);
                velocityX = reflection.x;
                velocityY = reflection.y;
                velocityZ = reflection.z;
                Vec3d adjustedPos = ((HitResult) collision).getPos();
                initialX = adjustedPos.x;
                initialY = adjustedPos.y;
                initialZ = adjustedPos.z;
            }
            initialX = nextX;
            initialY = nextY;
            initialZ = nextZ;
            age++;
        }

        public boolean isDead() {
            float alpha = alphaValue(1.0f);
            return alpha <= 0.0f;
        }

        private float bounceStrength() {
            return 1.0f;
        }

        private void resetPosition(double x, double y, double z) {
            initialX = x;
            initialY = y;
            initialZ = z;
        }

        private void savePreviousState() {
            gravityX = initialX;
            gravityY = initialY;
            gravityZ = initialZ;
        }

        private void saveRotationState() {
            spiralDirection = initialRoll;
        }

        private double interpolate(double start, double end, double progress) {
            return start + (end - start) * progress;
        }

        private float interpolateRotation(float past, float target, float partial) {
            return past + (target - past) * partial;
        }

        private float calculateVisibleScale() {
            if (age > 10) {
                float valueA = fadeVal(age, 1.0f);
                float valueEase = Math.min(fadeVal(age - 1, 1.0f), 1.0f);
                float easedValue = interpolateRotation(valueEase, valueA, 1.0f);
                return easedValue * 1.0f;
            }
            return 1.0f;
        }

        private float fadeVal(int tickAge, float partialT) {
            return (20.0f - tickAge) / 10.0f;
        }

        private Vec3d reflect(Vec3d incidentVec, Vec3d normalVec) {
            return incidentVec.subtract(normalVec.multiply(2.0 * incidentVec.dot(normalVec)));
        }

        private void calculateVelocity(float rotationAngle, float initialAngle, float speedFactor) {
            float angleRad = (float) (Math.PI / 180) * rotationAngle + 1.5707964f;
            float angleRadZ = (float) (Math.PI / 180) * initialAngle;
            float cosYaw = MathHelper.cos(angleRad);
            velocityX = MathHelper.cos(angleRad) * cosYaw * speedFactor;
            velocityZ = MathHelper.sin(angleRad) * cosYaw * speedFactor;
            velocityY = -MathHelper.sin(angleRadZ) * speedFactor;
        }

        public int compareTo(ParticleInstance other) {
            return Integer.compare(other.texture.hashCode(), texture.hashCode());
        }
    }
}