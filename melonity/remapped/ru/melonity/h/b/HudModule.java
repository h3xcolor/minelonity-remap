// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.HotbarStorage;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.fabric.client.model.IMobEffectInstance;
import ru.melonity.h.c.HudElement;
import ru.melonity.o.b.a.b.BooleanSetting;
import ru.melonity.o.b.a.b.EnumSetting;
import ru.melonity.o.b.a.b.FloatSetting;
import ru.melonity.s.c.ColorUtil;
import ru.melonity.s.c.RenderUtil;

import java.awt.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class HudModule extends HudElement {
    private final EnumSetting<HudElementType> hudElementsSetting = new EnumSetting<>("hud.elements", HudElementType.class, "Watermark", "Watermark", "StaffList", "KeyBinds", "Armor Alert", "Potions", "Hotbar", "Tips", "Minimap");
    private final BooleanSetting shadowSetting = new BooleanSetting("hud.shadow", true);
    private final FloatSetting scaleSetting = new FloatSetting(17.5f);
    private final StateAnimation watermarkAnimation = new StateAnimation();
    private final FrameWeightCalculator watermarkAnimationTimer = FrameWeightCalculator.milliseconds(320L);
    private final StateAnimation tipsAnimation = new StateAnimation();
    private final FrameWeightCalculator tipsAnimationTimer = FrameWeightCalculator.seconds(1L);
    private final StateAnimation staffListAnimation = new StateAnimation();
    public static long lastUpdateTime = System.currentTimeMillis();
    private final StateAnimation keyBindsAnimation = new StateAnimation();
    private final FrameWeightCalculator keyBindsAnimationTimer = FrameWeightCalculator.seconds(1L);
    private final StateAnimation armorAlertAnimation = new StateAnimation();
    private final Map<StatusEffectInstance, Integer> potionEffects = Maps.newLinkedHashMap();
    private final FrameWeightCalculator potionAnimationTimer = FrameWeightCalculator.milliseconds(320L);
    private final StateAnimation potionAnimation = new StateAnimation();
    private final FrameWeightCalculator hotbarAnimationTimer = FrameWeightCalculator.seconds(1L);
    private final StateAnimation hotbarAnimation = new StateAnimation();
    private final FrameWeightCalculator minimapAnimationTimer = FrameWeightCalculator.seconds(1L);
    private final StateAnimation minimapAnimation = new StateAnimation();

    public HudModule() {
        super("HUD", HudElementType.WATERMARK);
        this.registerSetting(this.hudElementsSetting);
    }

    @Generated
    public EnumSetting<HudElementType> getHudElementsSetting() {
        return this.hudElementsSetting;
    }

    @Generated
    public BooleanSetting getShadowSetting() {
        return this.shadowSetting;
    }

    @Generated
    public FloatSetting getScaleSetting() {
        return this.scaleSetting;
    }

    @Generated
    public StateAnimation getWatermarkAnimation() {
        return this.watermarkAnimation;
    }

    @Generated
    public FrameWeightCalculator getWatermarkAnimationTimer() {
        return this.watermarkAnimationTimer;
    }

    @Generated
    public StateAnimation getTipsAnimation() {
        return this.tipsAnimation;
    }

    @Generated
    public FrameWeightCalculator getTipsAnimationTimer() {
        return this.tipsAnimationTimer;
    }

    @Generated
    public StateAnimation getStaffListAnimation() {
        return this.staffListAnimation;
    }

    @Generated
    public StateAnimation getKeyBindsAnimation() {
        return this.keyBindsAnimation;
    }

    @Generated
    public FrameWeightCalculator getKeyBindsAnimationTimer() {
        return this.keyBindsAnimationTimer;
    }

    @Generated
    public StateAnimation getArmorAlertAnimation() {
        return this.armorAlertAnimation;
    }

    @Generated
    public Map<StatusEffectInstance, Integer> getPotionEffects() {
        return this.potionEffects;
    }

    @Generated
    public FrameWeightCalculator getPotionAnimationTimer() {
        return this.potionAnimationTimer;
    }

    @Generated
    public StateAnimation getPotionAnimation() {
        return this.potionAnimation;
    }

    @Generated
    public FrameWeightCalculator getHotbarAnimationTimer() {
        return this.hotbarAnimationTimer;
    }

    @Generated
    public StateAnimation getHotbarAnimation() {
        return this.hotbarAnimation;
    }

    @Generated
    public FrameWeightCalculator getMinimapAnimationTimer() {
        return this.minimapAnimationTimer;
    }

    @Generated
    public StateAnimation getMinimapAnimation() {
        return this.minimapAnimation;
    }

    @Override
    public void onRender(DrawContext context, float partialTicks) {
        MatrixStack matrixStack = context.getMatrices();
        MinecraftClient client = MinecraftClient.getInstance();
        long currentTime = System.currentTimeMillis();

        boolean showTips = currentTime - lastUpdateTime <= 15000L && this.hudElementsSetting.getValue().contains(HudElementType.TIPS);
        this.tipsAnimation.state(showTips);
        this.tipsAnimation.update(this.tipsAnimationTimer.elapsedUnits());
        float tipsAlpha = this.tipsAnimation.animation();

        if (tipsAlpha > 0.0f) {
            Map<String, String> keyBindTips = Maps.newLinkedHashMap();
            keyBindTips.put("R SHIFT", "Open/close menu");
            keyBindTips.put("T", "Configure HUD elements");

            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();
            float centerX = screenWidth / 2.0f;
            float startY = centerX - (keyBindTips.size() * 16) / 2.0f;
            Iterator<Map.Entry<String, String>> iterator = keyBindTips.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String key = entry.getKey();
                String description = entry.getValue();
                float keyWidth = RenderUtil.getStringWidth(key);
                float totalWidth = 3.0f + keyWidth + 5.0f + RenderUtil.getStringWidth(description) + 4.0f;
                Color bgColor = new Color(0.0f, 0.0f, 0.0f, 0.41568628f * tipsAlpha);
                RenderUtil.drawRoundedRect(10.0f, startY, totalWidth, 18.0f, 6.0f, bgColor, matrixStack);
                Color textColor = ColorUtil.applyAlpha(ColorUtil.fromRGB("#181818"), (int) (tipsAlpha * 255.0f));
                RenderUtil.drawRoundedRect(12.0f, startY + 2.0f, keyWidth + 4.0f, 14.0f, 4.0f, textColor, matrixStack);
                RenderUtil.drawString(RenderUtil.getFontRenderer(), key, 11.0f + keyWidth / 2.0f - keyWidth / 2.0f, startY + 7.5f, ColorUtil.applyAlpha(Color.WHITE, (int) (tipsAlpha * 255.0f)), matrixStack);
                RenderUtil.drawString(RenderUtil.getFontRenderer(), description, 11.0f + keyWidth + 5.0f, startY + 7.5f, ColorUtil.applyAlpha(Color.WHITE, (int) (tipsAlpha * 255.0f)), matrixStack);
                startY += 23.0f;
            }
        }

        if (this.hudElementsSetting.getValue().contains(HudElementType.WATERMARK)) {
            this.watermarkAnimation.update(this.watermarkAnimationTimer.elapsedUnits());
        }

        if (this.hudElementsSetting.getValue().contains(HudElementType.STAFF_LIST)) {
            this.staffListAnimation.update(this.staffListAnimationTimer.elapsedUnits());
        }

        if (this.hudElementsSetting.getValue().contains(HudElementType.KEY_BINDS)) {
            this.keyBindsAnimation.update(this.keyBindsAnimationTimer.elapsedUnits());
        }

        if (this.hudElementsSetting.getValue().contains(HudElementType.ARMOR_ALERT)) {
            this.armorAlertAnimation.update(this.armorAlertAnimationTimer.elapsedUnits());
        }

        if (this.hudElementsSetting.getValue().contains(HudElementType.POTIONS)) {
            this.potionAnimation.update(this.potionAnimationTimer.elapsedUnits());
        }

        if (this.hudElementsSetting.getValue().contains(HudElementType.HOTBAR)) {
            this.hotbarAnimation.update(this.hotbarAnimationTimer.elapsedUnits());
        }

        if (this.hudElementsSetting.getValue().contains(HudElementType.MINIMAP)) {
            this.minimapAnimation.update(this.minimapAnimationTimer.elapsedUnits());
        }
    }

    private enum HudElementType {
        WATERMARK,
        STAFF_LIST,
        KEY_BINDS,
        ARMOR_ALERT,
        POTIONS,
        HOTBAR,
        TIPS,
        MINIMAP
    }
}