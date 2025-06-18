// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.d;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import ru.melonity.Melonity;
import ru.melonity.f.RenderListener;
import ru.melonity.f.b.RenderEvent;
import ru.melonity.h.b.Feature;
import ru.melonity.h.b.FeatureManager;
import ru.melonity.h.b.FeatureType;
import ru.melonity.p.Notification;
import ru.melonity.p.NotificationManager;
import ru.melonity.s.c.TextureRenderer;
import ru.melonity.s.c.TextureRendererManager;

import java.awt.Color;
import java.util.Optional;

@Environment(EnvType.CLIENT)
@Feature(name = "GPS", description = "Shows direction to target", icon = "gps.png", type = FeatureType.RENDER)
public class GpsRenderer {
    private boolean enabled;
    private int targetX;
    private int targetZ;
    private final RenderListener<RenderEvent> renderListener = event -> {
        if (!enabled) {
            return;
        }
        MatrixStack matrixStack = event.getMatrixStack();
        MinecraftClient client = MinecraftClient.getInstance();
        Perspective cameraSubmersionType = client.options.getPerspective();
        float fov = cameraSubmersionType.getFov(client.options, client.getTickDelta());
        float halfFov = fov * 0.5f;
        float yaw = client.player.getYaw();
        float pitch = client.player.getPitch();
        float adjustedYaw = MathHelper.wrapDegrees(yaw);
        matrixStack.push();
        RenderSystem.disableBlend();
        TextRenderer textRenderer = client.textRenderer;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        double centerX = (double) screenWidth / 2.0;
        double centerY = (double) screenHeight / 2.0;
        Camera camera = client.gameRenderer.getCamera();
        Vec3d cameraPos = camera.getPos();
        double deltaX = targetX - cameraPos.x;
        double deltaZ = targetZ - cameraPos.z;
        double angle = Math.atan2(deltaZ, deltaX);
        double angleDeg = Math.toDegrees(angle);
        double adjustedAngle = angleDeg - adjustedYaw + 180.0;
        double cos = Math.cos(Math.toRadians(adjustedAngle));
        double sin = Math.sin(Math.toRadians(adjustedAngle));
        double arrowX = centerX + 40.0 * cos;
        double arrowY = centerY + 40.0 * sin;
        matrixStack.translate(arrowX, arrowY, 0.0);
        Quaternionf rotation = RotationAxis.POSITIVE_Z.rotationDegrees((float) adjustedAngle + 100.0f);
        matrixStack.multiply(rotation);
        double distance = client.player.squaredDistanceTo(targetX, client.player.getY(), targetZ);
        double distanceSqrt = Math.sqrt(distance);
        TextureRenderer textureRenderer = TextureRendererManager.getInstance();
        Identifier arrowTexture = new Identifier("melonity", "images/features/arrow.png");
        textureRenderer.drawTexture(arrowTexture, 0.0f, 0.0f, 16.0f, 16.0f, Color.WHITE, matrixStack);
        TextureRenderer textRender = TextureRendererManager.getInstance();
        String distanceText = String.format("%dM", (int) distanceSqrt);
        textRender.drawText(TextRenderer.TextLayerType.NORMAL, distanceText, 0.0f, 20.0f, matrixStack);
        if (distanceSqrt <= 15.0) {
            NotificationManager notificationManager = Melonity.getNotificationManager();
            Notification notification = new Notification("GPS", "You have arrived", "feature-disabled.png");
            notificationManager.addNotification(notification);
            FeatureManager featureManager = Melonity.getFeatureManager();
            Optional<Feature> optionalFeature = featureManager.getFeature(Feature.class);
            Feature feature = optionalFeature.get();
            GpsRenderer gpsFeature = (GpsRenderer) feature.getFeatureInstance();
            gpsFeature.setEnabled(false);
        }
        matrixStack.pop();
        RenderSystem.enableBlend();
    };

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public void setTargetZ(int targetZ) {
        this.targetZ = targetZ;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetZ() {
        return targetZ;
    }

    public RenderListener<RenderEvent> getRenderListener() {
        return renderListener;
    }
}