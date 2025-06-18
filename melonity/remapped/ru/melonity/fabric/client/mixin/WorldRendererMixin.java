// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.fabric.client.model.ILevelRenderer;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin implements ILevelRenderer {
    @Unique
    private Frustum customFrustum;
    @Unique
    private Framebuffer entitiesFramebuffer;
    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;

    @Shadow
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void onRenderStart(GameRenderer gameRenderer, boolean renderBlockOutline, Camera camera, BuiltChunkStorage builtChunkStorage, Frustum frustum, Matrix4f projectionMatrix, Matrix4f modelViewMatrix, CallbackInfo ci) {
        this.customFrustum = new Frustum(projectionMatrix, modelViewMatrix);
        Vec3d cameraPos = camera.getPos();
        this.customFrustum.setPosition(cameraPos.x, cameraPos.y, cameraPos.z);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;"))
    public void onRenderEntities(GameRenderer gameRenderer, boolean renderBlockOutline, Camera camera, BuiltChunkStorage builtChunkStorage, Frustum frustum, Matrix4f projectionMatrix, Matrix4f modelViewMatrix, CallbackInfo ci) {
        Melonity.getInstance().getEventBus().post(new RenderEventStart());
        MatrixStack matrixStack = new MatrixStack();
        Object espModule = Melonity.getInstance().getModuleManager().getModule("ESP").orElse(null);
        Object chamsModule = Melonity.getInstance().getModuleManager().getModule("Chams").orElse(null);
        if ((espModule != null && ((Module) espModule).isEnabled()) || (chamsModule != null && ((Module) chamsModule).isEnabled())) {
            this.entitiesFramebuffer = ((Module) espModule).updateFramebuffer(this.entitiesFramebuffer);
            Melonity.entityFramebufferState = ((Module) espModule).getFramebufferState(Melonity.entityFramebufferState);
            this.entitiesFramebuffer.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
            this.entitiesFramebuffer.setClearAlpha(0.0f);
            this.entitiesFramebuffer.setDrawToScreen(false);
            boolean wasOutlineEnabled = MinecraftClient.getInstance().gameRenderer.getRenderOutlines();
            MinecraftClient.getInstance().gameRenderer.setRenderOutlines(false);
            Vec3d cameraPos = camera.getPos();
            double cameraX = cameraPos.x;
            double cameraY = cameraPos.y;
            double cameraZ = cameraPos.z;
            EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
            Melonity.isRenderingEntities = false;
            for (PlayerEntity player : MinecraftClient.getInstance().world.getPlayers()) {
                if (player.age == 0) {
                    player.lastRenderX = player.getX();
                    player.lastRenderY = player.getY();
                    player.lastRenderZ = player.getZ();
                }
                boolean wasInvisible = player.isInvisible();
                player.setInvisible(false);
                if (MinecraftClient.getInstance().player != player) {
                    Vec3d playerPos = new Vec3d(MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player.getZ());
                    Vec3d targetPos = new Vec3d(player.getX(), player.getY(), player.getZ());
                    boolean hasLineOfSight = MinecraftClient.getInstance().world.raycast(new RaycastContext(playerPos, targetPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, MinecraftClient.getInstance().player)).getType() == net.minecraft.util.hit.HitResult.Type.MISS;
                    float brightness = gameRenderer.getBrightness(player, MinecraftClient.getInstance().getTickDelta(), !entityRenderDispatcher.shouldRenderShadow(player));
                    GL30.glBindFramebuffer(36160, this.entitiesFramebuffer.fbo);
                    this.renderEntity(player, cameraX, cameraY, cameraZ, MinecraftClient.getInstance().getTickDelta(), matrixStack, this.bufferBuilders.getEntityVertexConsumers());
                }
                player.setInvisible(wasInvisible);
            }
            Melonity.isRenderingEntities = true;
            MinecraftClient.getInstance().gameRenderer.setRenderOutlines(wasOutlineEnabled);
            this.entitiesFramebuffer.endWrite();
            MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void onRenderEnd(GameRenderer gameRenderer, boolean renderBlockOutline, Camera camera, BuiltChunkStorage builtChunkStorage, Frustum frustum, Matrix4f projectionMatrix, Matrix4f modelViewMatrix, CallbackInfo ci) {
        Matrix4f combinedMatrix = new Matrix4f(modelViewMatrix).mul(projectionMatrix);
        Melonity.getInstance().getEventBus().post(new RenderEventEnd(combinedMatrix, gameRenderer.getBrightness(MinecraftClient.getInstance().player, MinecraftClient.getInstance().getTickDelta(), true)));
    }

    @Override
    public Frustum getFrustum() {
        return this.customFrustum;
    }

    @Override
    public Framebuffer entitiesFramebuffer() {
        return this.entitiesFramebuffer;
    }

    private static class RenderEventStart {}
    private static class RenderEventEnd {
        private final Matrix4f projectionMatrix;
        private final float brightness;

        public RenderEventEnd(Matrix4f projectionMatrix, float brightness) {
            this.projectionMatrix = projectionMatrix;
            this.brightness = brightness;
        }
    }

    private interface Module {
        boolean isEnabled();
        Framebuffer updateFramebuffer(Framebuffer current);
        Object getFramebufferState(Object current);
    }
}