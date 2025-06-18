// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.awt.Color;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import ru.melonity.Melonity;
import ru.melonity.animation.Easings;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.events.ItemUseEvent;
import ru.melonity.settings.SettingBase;
import ru.melonity.settings.SettingGroup;
import ru.melonity.settings.types.KeyHoldSetting;
import ru.melonity.settings.types.NumberSetting;
import ru.melonity.settings.types.PositionSetting;
import ru.melonity.ui.renderer.ModuleRenderer;

@Environment(value = EnvType.CLIENT)
public class GappleTimer extends ru.melonity.h.c.ModuleBase {
    private final StateAnimation animation = new StateAnimation();
    private final FrameWeightCalculator weightCalculator = FrameWeightCalculator.milliseconds(400L);
    private final NumberSetting timeSetting = new NumberSetting("global.time", value -> String.format("%d seconds", value.intValue()), 1, 6, 4.5);
    private final PositionSetting position = new PositionSetting(16, 200.0f, 80.0f, 34.5f, 73.0f, () -> true);
    private final KeyHoldSetting keyHold = new KeyHoldSetting(key -> {
        PlayerEntity player = Melonity.mc.player;
        ItemStack activeStack = player.getActiveItem();
        Item activeItem = activeStack.getItem();
        if (activeItem == Items.GOLDEN_APPLE || activeItem == Items.ENCHANTED_GOLDEN_APPLE) {
            player.stopUsingItem();
        }
    });
    private final SettingGroup<ModuleRenderer> rendererSetting = this::renderModule;

    public static int switchValue;

    public GappleTimer() {
        super("GappleTimer", SettingGroup.class);
        addSetting(timeSetting);
    }

    @Generated
    public StateAnimation getAnimation() {
        return animation;
    }

    @Generated
    public FrameWeightCalculator getWeightCalculator() {
        return weightCalculator;
    }

    @Generated
    public NumberSetting getTimeSetting() {
        return timeSetting;
    }

    @Generated
    public PositionSetting getPosition() {
        return position;
    }

    @Generated
    public KeyHoldSetting getKeyHold() {
        return keyHold;
    }

    @Generated
    public SettingGroup<ModuleRenderer> getRendererSetting() {
        return rendererSetting;
    }

    private void renderModule(ModuleRenderer renderer) {
        if (!position.isVisible()) {
            return;
        }
        PlayerEntity player = Melonity.mc.player;
        float goldenAppleProgress = getUseProgress(player, Items.GOLDEN_APPLE);
        float enchantedGoldenAppleProgress = getUseProgress(player, Items.ENCHANTED_GOLDEN_APPLE);
        boolean usingGapple = goldenAppleProgress > 0 || enchantedGoldenAppleProgress > 0;
        animation.setState(usingGapple);
        weightCalculator.update();
        animation.update(weightCalculator.elapsedUnits());
        float animProgress = animation.getAnimation();
        if (animProgress <= 0.0f) {
            return;
        }
        float eased = (float) Easings.BACK_OUT.ease(animProgress);
        renderer.resetMatrix();
        renderer.translate(position.getX() + position.getWidth() / 2.0f, position.getY() + position.getHeight() / 2.0f);
        renderer.scale(eased, eased, eased);
        renderer.translate(-(position.getX() + position.getWidth() / 2.0f), -(position.getY() + position.getHeight() / 2.0f));
        Melonity.renderEngine.fillRoundedRect(position.getX() - 1, position.getY() - 1, position.getWidth() + 2, position.getHeight() + 2, 6.0f, new Color(0x222222), renderer);
        Melonity.renderEngine.fillRoundedRect(position.getX(), position.getY(), position.getWidth(), position.getHeight(), 6.0f, new Color(0x141414), renderer);
        int itemCount = 0;
        for (Item appleItem : new Item[]{Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE}) {
            ItemStack stack = new ItemStack(appleItem);
            float progress = appleItem == Items.GOLDEN_APPLE ? goldenAppleProgress : enchantedGoldenAppleProgress;
            renderer.resetMatrix();
            renderer.translate(position.getX(), position.getY() + itemCount * 35);
            renderer.scale(1.3f, 1.3f, 1.3f);
            renderer.translate(-position.getX(), -position.getY() - itemCount * 35);
            Melonity.renderEngine.drawItem(stack, (int) (position.getX() + 17), (int) (position.getY() + 18 + itemCount * 35), renderer);
            renderer.resetMatrix();
            float circleX = position.getX() + 17;
            float circleY = position.getY() + 18 + itemCount * 35;
            Melonity.renderEngine.drawArc(circleX, circleY, 0.0f, 360.0f, 13.0f, 4.0f, false, new Color(0x222222), renderer);
            Melonity.renderEngine.drawArc(circleX, circleY, 0.0f, progress * 360.0f, 14.0f, 2.5f, false, Melonity.renderEngine.getSettingColor(1, 60), renderer);
            itemCount++;
        }
        renderer.resetMatrix();
    }

    private float getUseProgress(PlayerEntity player, Item item) {
        ItemStack activeStack = player.getActiveItem();
        if (activeStack.getItem() != item) {
            return 0.0f;
        }
        int useTicks = player.getItemUseTime();
        float progress = useTicks / (float) activeStack.getMaxUseTime();
        return MathHelper.clamp(progress, 0.0f, 1.0f);
    }
}