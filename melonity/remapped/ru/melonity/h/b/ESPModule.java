// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.lang.invoke.LambdaMetafactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector4d;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.f.b.Feature;
import ru.melonity.f.b.Setting;
import ru.melonity.f.b.SettingGroup;
import ru.melonity.fabric.client.model.ILevelRenderer;
import ru.melonity.fabric.client.model.IPlayer;
import ru.melonity.o.a.Category;
import ru.melonity.o.b.a.b.MultiSelectSetting;
import ru.melonity.o.b.a.b.SelectSetting;
import ru.melonity.s.a.FontRenderer;
import ru.melonity.w.ArrowIndicator;

@Environment(EnvType.CLIENT)
public class ESPModule extends Feature {
    private final Map<StatusEffect, String> effectAbbreviations = Maps.newHashMap();
    private final StateAnimation arrowAnimation = new StateAnimation();
    private final FrameWeightCalculator animationCalculator = FrameWeightCalculator.milliseconds(400L);
    private final MultiSelectSetting elementsSetting = new MultiSelectSetting("esp.elements", SettingGroup.VISUAL, Arrays.asList("esp.elements.box", "esp.elements.health", "esp.elements.nametag"), "esp.elements.box", "esp.elements.health", "esp.elements.nametag", "esp.elements.armor", "esp.elements.potions", "esp.elements.items");
    private final MultiSelectSetting targetsSetting = new MultiSelectSetting("esp.targets", SettingGroup.VISUAL, "Naked", "Invisibles", "Naked", "Friends");
    private final Setting<Boolean> oofArrows = new Setting<>("OOF Arrows", true);
    private final Feature.EventHandler<PlayerEntity> playerResetHandler = player -> {
        List<PlayerEntity> players = MinecraftClient.getInstance().world.getPlayers();
        Iterator<PlayerEntity> iterator = players.iterator();
        while (iterator.hasNext()) {
            PlayerEntity entity = iterator.next();
            IPlayer iPlayer = (IPlayer) entity;
            iPlayer.setEspPosition(null);
            if (!isValidTarget(entity)) continue;
            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
            float tickDelta = camera.getTickDelta(true);
            Vector4d position = calculateEntityPosition(entity, tickDelta);
            if (!isEntityVisible(entity)) continue;
            if (MinecraftClient.getInstance().options.getPerspective().isFirstPerson() && entity == MinecraftClient.getInstance().player) continue;
            iPlayer.setEspPosition(position);
        }
    };
    private final Feature.EventHandler<DrawContext> renderHandler = this::renderESP;
    private final Feature.EventHandler<ArrowIndicator> arrowRenderHandler = this::renderArrows;

    public ESPModule() {
        super("ESP", Category.VISUAL);
        this.registerSetting(this.elementsSetting);
        this.registerSetting(this.targetsSetting);
        this.registerSetting(this.oofArrows);
        this.effectAbbreviations.put(StatusEffect.byRawId(1), "Pr");
        this.effectAbbreviations.put(StatusEffect.byRawId(2), "Sh");
        this.effectAbbreviations.put(StatusEffect.byRawId(3), "Ef");
        this.effectAbbreviations.put(StatusEffect.byRawId(4), "Un");
        this.effectAbbreviations.put(StatusEffect.byRawId(5), "Po");
    }

    private String formatDuration(int ticks) {
        int totalSeconds = ticks / 20;
        int hours = totalSeconds / 3600;
        int minutes = totalSeconds % 3600 / 60;
        int seconds = totalSeconds % 60;
        if (hours > 0) {
            return String.format("%d:%d:%02d", hours, minutes, seconds);
        }
        return String.format("%d:%02d", minutes, seconds);
    }

    private Vec3d calculateEntityPosition(Entity entity, float tickDelta) {
        double x = entity.prevX + (entity.getX() - entity.prevX) * tickDelta;
        double y = entity.prevY + (entity.getY() - entity.prevY) * tickDelta;
        double z = entity.prevZ + (entity.getZ() - entity.prevZ) * tickDelta;
        return new Vec3d(x, y, z);
    }

    private float calculateYaw(Entity entity) {
        if (MinecraftClient.getInstance().player == null) {
            return 0.0f;
        }
        double deltaX = interpolate(entity.getX(), entity.prevX) - interpolate(MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.prevX);
        double deltaZ = interpolate(entity.getZ(), entity.prevZ) - interpolate(MinecraftClient.getInstance().player.getZ(), MinecraftClient.getInstance().player.prevZ);
        return (float) (-Math.atan2(deltaX, deltaZ) * 57.29577951308232);
    }

    private double interpolate(double current, double previous) {
        return previous + (current - previous) * MinecraftClient.getInstance().getTickDelta();
    }

    private boolean isValidTarget(PlayerEntity player) {
        List<String> targetOptions = this.targetsSetting.getSelectedOptions();
        if (targetOptions.contains("Naked")) {
            if (player.getInventory().armor.size() == 0) {
                return false;
            }
        }
        if (targetOptions.contains("Friends")) {
            if (Melonity.getInstance().getFriendManager().isFriend(player.getName().getString())) {
                return false;
            }
        }
        if (targetOptions.contains("Invisibles")) {
            if (player.isInvisible()) {
                return false;
            }
        }
        return player.isAlive() && player.getHealth() > 0;
    }

    private boolean isEntityVisible(Entity entity) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Frustum frustum = ((ILevelRenderer) MinecraftClient.getInstance().worldRenderer).getFrustum();
        Box boundingBox = entity.getBoundingBox();
        return frustum.isVisible(boundingBox);
    }

    private void renderESP(DrawContext context) {
        if (!this.isEnabled()) return;
        if (!this.oofArrows.getValue()) return;
        boolean shouldRender = ArrowIndicator.shouldRender();
        this.arrowAnimation.state(shouldRender);
        float elapsed = this.getElapsedTime();
        this.arrowAnimation.update(elapsed);
        float animationProgress = this.arrowAnimation.animation() * -12.0f;
        MatrixStack matrices = context.getMatrices();
        List<PlayerEntity> players = MinecraftClient.getInstance().world.getPlayers();
        Iterator<PlayerEntity> iterator = players.iterator();
        while (iterator.hasNext()) {
            PlayerEntity entity = iterator.next();
            if (entity == MinecraftClient.getInstance().player) continue;
            Frustum frustum = ((ILevelRenderer) MinecraftClient.getInstance().worldRenderer).getFrustum();
            Box boundingBox = entity.getBoundingBox();
            if (frustum.isVisible(boundingBox)) continue;
            IPlayer iPlayer = (IPlayer) entity;
            Vector4d position = iPlayer.espPosition();
            if (position == null) continue;
            RenderSystem.depthMask(false);
            float x = (float) position.x;
            float y = (float) position.y;
            float width = (float) (position.z - position.x);
            float height = (float) (position.w - position.y);
            if (entity == MinecraftClient.getInstance().player) continue;
            boolean isFriend = Melonity.getInstance().getFriendManager().isFriend(entity.getName().getString());
            boolean isTarget = Melonity.getInstance().getTargetManager().isTarget(entity);
            Color color = isFriend ? Color.ORANGE : (isTarget ? Color.RED : Color.GREEN);
            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
            float tickDelta = camera.getTickDelta(true);
            float yaw = calculateYaw(entity);
            matrices.push();
            double screenWidth = 40 + (MinecraftClient.getInstance().currentScreen instanceof net.minecraft.client.gui.screen.ingame.InventoryScreen ? 95 : 0);
            int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
            int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();
            double centerX = windowWidth / 2.0;
            double centerY = windowHeight / 2.0;
            double angle = Math.atan2(centerY - y, centerX - x);
            double rotation = Math.toRadians(yaw + 90.0f);
            double angleDiff = angle - rotation + Math.toRadians(180.0);
            matrices.translate(centerX, centerY, 0.0);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) Math.toDegrees(angleDiff) + 90.0f));
            double arrowX = centerX + screenWidth * Math.cos(angleDiff);
            double arrowY = centerY + screenWidth * Math.sin(angleDiff);
            matrices.translate(arrowX, arrowY, 0.0);
            Identifier arrowTexture = new Identifier("melonity", "textures/features/arrow.png");
            FontRenderer fontRenderer = Melonity.getInstance().getFontRenderer();
            context.drawTexture(arrowTexture, (int) (arrowX - 8), (int) (arrowY - 8), 16, 16, animationProgress, animationProgress, 16, 16, 16, 16);
            matrices.pop();
            RenderSystem.depthMask(true);
        }
    }

    private void renderArrows(ArrowIndicator indicator) {
        if (!this.isEnabled()) return;
        if (!this.oofArrows.getValue()) return;
        boolean shouldRender = ArrowIndicator.shouldRender();
        this.arrowAnimation.state(shouldRender);
        float elapsed = this.getElapsedTime();
        this.arrowAnimation.update(elapsed);
        float animationProgress = this.arrowAnimation.animation() * -12.0f;
        MatrixStack matrices = indicator.getMatrices();
        List<PlayerEntity> players = MinecraftClient.getInstance().world.getPlayers();
        Iterator<PlayerEntity> iterator = players.iterator();
        while (iterator.hasNext()) {
            PlayerEntity entity = iterator.next();
            if (entity == MinecraftClient.getInstance().player) continue;
            Frustum frustum = ((ILevelRenderer) MinecraftClient.getInstance().worldRenderer).getFrustum();
            Box boundingBox = entity.getBoundingBox();
            if (frustum.isVisible(boundingBox)) continue;
            IPlayer iPlayer = (IPlayer) entity;
            Vector4d position = iPlayer.espPosition();
            if (position == null) continue;
            RenderSystem.depthMask(false);
            float x = (float) position.x;
            float y = (float) position.y;
            float width = (float) (position.z - position.x);
            float height = (float) (position.w - position.y);
            if (entity == MinecraftClient.getInstance().player) continue;
            boolean isFriend = Melonity.getInstance().getFriendManager().isFriend(entity.getName().getString());
            boolean isTarget = Melonity.getInstance().getTargetManager().isTarget(entity);
            Color color = isFriend ? Color.ORANGE : (isTarget ? Color.RED : Color.GREEN);
            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
            float tickDelta = camera.getTickDelta(true);
            float yaw = calculateYaw(entity);
            matrices.push();
            double screenWidth = 40 + (MinecraftClient.getInstance().currentScreen instanceof net.minecraft.client.gui.screen.ingame.InventoryScreen ? 95 : 0);
            int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
            int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();
            double centerX = windowWidth / 2.0;
            double centerY = windowHeight / 2.0;
            double angle = Math.atan2(centerY - y, centerX - x);
            double rotation = Math.toRadians(yaw + 90.0f);
            double angleDiff = angle - rotation + Math.toRadians(180.0);
            matrices.translate(centerX, centerY, 0.0);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) Math.toDegrees(angleDiff) + 90.0f));
            double arrowX = centerX + screenWidth * Math.cos(angleDiff);
            double arrowY = centerY + screenWidth * Math.sin(angleDiff);
            matrices.translate(arrowX, arrowY, 0.0);
            Identifier arrowTexture = new Identifier("melonity", "textures/features/arrow.png");
            FontRenderer fontRenderer = Melonity.getInstance().getFontRenderer();
            indicator.drawTexture(arrowTexture, (int) (arrowX - 8), (int) (arrowY - 8), 16, 16, animationProgress, animationProgress, 16, 16, 16, 16);
            matrices.pop();
            RenderSystem.depthMask(true);
        }
    }
}