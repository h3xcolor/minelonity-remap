// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.a;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.h.b.HealthBarColorProvider;
import ru.melonity.o.a.HudElement;
import ru.melonity.o.a.HudElementRenderer;

@Environment(value = EnvType.CLIENT)
public class EntityHudRenderer {
    private float width;
    private float height;
    private final FrameWeightCalculator stateAnimationTimer = FrameWeightCalculator.milliseconds(250L);
    private final StateAnimation stateAnimation = new StateAnimation();
    private final FrameWeightCalculator healthAnimationTimer = FrameWeightCalculator.milliseconds(250L);
    private float currentHealth;
    private boolean healthReset = false;
    private final FrameWeightCalculator absorptionAnimationTimer = FrameWeightCalculator.milliseconds(250L);
    private float currentAbsorption;
    private boolean absorptionReset = false;
    private Entity entity;
    private boolean isActive;
    private HudType hudType;
    public static int unusedField = 1416349235;

    public EntityHudRenderer(float width, float height, HudType hudType) {
        this.width = width;
        this.height = height;
        this.hudType = hudType;
    }

    public void render(float x, float y, double entityX, double entityY, HudElementRenderer renderer, DrawContext context) {
        if (this.entity == null) {
            return;
        }
        context.getMatrices().push();
        float animationProgress = this.stateAnimationTimer.elapsedUnits();
        this.stateAnimation.update(animationProgress);
        float dropAnimation = this.stateAnimation.dropAnimation();
        if (dropAnimation <= 0.0f) {
            return;
        }
        float entityHealth = this.entity.getHealth();
        if (!this.healthReset) {
            this.currentHealth = entityHealth;
            this.healthReset = true;
        }
        float healthUpdate = this.healthAnimationTimer.elapsedUnits();
        this.currentHealth += (entityHealth - this.currentHealth) * 15.0f * healthUpdate;
        float maxHealth = this.entity.getMaxHealth();
        this.currentHealth = MathHelper.clamp(this.currentHealth, 0.0f, maxHealth);
        if (!this.absorptionReset) {
            this.currentAbsorption = entityHealth;
            this.absorptionReset = true;
        }
        float absorptionUpdate = this.absorptionAnimationTimer.elapsedUnits();
        this.currentAbsorption += (entityHealth - this.currentAbsorption) * absorptionUpdate;
        float maxAbsorption = this.entity.getMaxHealth();
        this.currentAbsorption = MathHelper.clamp(this.currentAbsorption, 0.0f, maxAbsorption);
        context.getMatrices().translate(x + this.width * renderer.getWidthScale() / 2.0f, y + this.height * renderer.getHeightScale() / 2.0f, 0.0f);
        context.getMatrices().scale(dropAnimation, dropAnimation, dropAnimation);
        context.getMatrices().translate(-(x + this.width * renderer.getWidthScale() / 2.0f), -(y + this.height * renderer.getHeightScale() / 2.0f), 0.0f);
        float nameplateWidth = this.hudType == HudType.DEFAULT ? 98.0f : 110.0f;
        float nameplateHeight = this.hudType == HudType.DEFAULT ? 45.0f : 48.0f;
        Text entityName = this.entity.getName();
        String nameString = entityName.getString();
        HudElement hudElement = Melonity.getHudElementManager().getCurrentHudElement();
        if (hudElement != null) {
            HudElement currentHudElement = Melonity.getHudElementManager().getCurrentHudElement();
            Optional<HealthBarColorProvider> optional = currentHudElement.getProvider(HealthBarColorProvider.class);
            HealthBarColorProvider healthBarColorProvider = optional.get();
            if (healthBarColorProvider.isCustomNameEnabled()) {
                nameString = "Melonity";
            }
        }
        HudElementRenderer hudRenderer = Melonity.getHudElementManager().getCurrentRenderer();
        HealthBarColorProvider colorProvider = hudRenderer.getHealthBarColorProvider();
        float stateAnim = this.stateAnimation.animation();
        renderer.setAlpha(0.5f + stateAnim);
        Color baseColor = colorProvider.getBaseColor();
        renderer.renderEntityHud(this.entity, x, y, this.width, this.height, 8.0f, baseColor, context.getMatrices());
        switch (this.hudType.ordinal()) {
            case 0:
                renderer.renderEntityIcon(this.entity, x + 3.5f, y + 4.0f, 33.0f, 34.0f, 8.0f, context.getMatrices());
                String displayName;
                if (nameString.length() > 12) {
                    displayName = nameString.substring(0, 12) + "...";
                } else {
                    displayName = nameString;
                }
                renderer.renderText(HudElementRenderer.TextType.ENTITY_NAME, displayName, x + 45.0f, y + 10.0f, context.getMatrices());
                String healthText = String.format("HP: %.1f", this.currentHealth);
                Color textColor = Color.decode("#888888");
                renderer.renderText(HudElementRenderer.TextType.ENTITY_NAME, healthText, x + 45.0f, y + 21.0f, textColor, context.getMatrices());
                float barWidth = 58.0f;
                float barHeight = 6.5f;
                Color barBackground = Color.decode("#222222");
                renderer.renderRoundedRect(x + 45.0f, y + this.height - barHeight - 4.0f, barWidth, barHeight, 4.0f, barBackground, context.getMatrices());
                float healthRatio = MathHelper.clamp(this.currentHealth / maxHealth, 0.0f, 1.0f);
                float healthBarWidth = barWidth * healthRatio;
                Color healthColor = colorProvider.getHealthColor();
                renderer.renderRoundedRect(x + 45.0f, y + this.height - barHeight - 4.0f, healthBarWidth, barHeight, 4.0f, healthColor, context.getMatrices());
                float absorptionRatio = MathHelper.clamp(this.currentAbsorption / maxAbsorption, 0.0f, 1.0f);
                float absorptionBarWidth = barWidth * absorptionRatio;
                Color absorptionColor = colorProvider.getAbsorptionColor();
                Color fadedAbsorption = HudElementRenderer.darkenColor(absorptionColor, 60);
                renderer.renderRoundedRect(x + 45.0f, y + this.height - barHeight - 4.0f, absorptionBarWidth, barHeight, 4.0f, fadedAbsorption, context.getMatrices());
                break;
            case 1:
                renderer.renderEntityIcon(this.entity, x + 4.0f, y + 5.5f, 33.0f, 34.0f, 8.0f, context.getMatrices());
                Color corner1 = Color.decode("#222222");
                Color corner2 = Color.decode("#222222");
                Color corner3 = Color.decode("#222222");
                Color corner4 = Color.decode("#222222");
                renderer.renderRoundedCorners(x, y + this.height - 3.0f, this.width, 3.0f, 4.0f, 0.0f, 0.0f, 4.0f, corner1, corner2, corner3, corner4, context.getMatrices());
                float healthBar = this.width * (this.currentHealth / maxHealth);
                Color healthBarColor1 = colorProvider.getHealthColor();
                Color healthBarColor2 = colorProvider.getHealthColor();
                Color healthBarColor3 = colorProvider.getHealthColor();
                Color healthBarColor4 = colorProvider.getHealthColor();
                renderer.renderRoundedCorners(x, y + this.height - 3.0f, healthBar, 3.0f, 4.0f, 0.0f, 0.0f, 4.0f, healthBarColor1, healthBarColor2, healthBarColor3, healthBarColor4, context.getMatrices());
                String displayNameCompact;
                if (nameString.length() > 10) {
                    displayNameCompact = nameString.substring(0, 10) + "...";
                } else {
                    displayNameCompact = nameString;
                }
                renderer.renderText(HudElementRenderer.TextType.ENTITY_NAME, displayNameCompact, x + 42.0f, y + 9.0f, context.getMatrices());
                String healthTextCompact = String.format("HP: %.1f", entityHealth);
                healthTextCompact = healthTextCompact.replace(',', '.');
                Color textColorCompact = Color.decode("#888888");
                renderer.renderText(HudElementRenderer.TextType.ENTITY_NAME, healthTextCompact, x + 42.0f, y + 32.5f, textColorCompact, context.getMatrices());
                float itemX = x + 42.0f;
                Iterable<ItemStack> armorItems = this.entity.getArmorItems();
                LinkedList<ItemStack> armorList = Lists.newLinkedList(armorItems);
                Collections.reverse(armorList);
                Iterator<ItemStack> armorIterator = armorList.iterator();
                while (armorIterator.hasNext()) {
                    ItemStack stack = armorIterator.next();
                    if (stack.isEmpty()) continue;
                    context.getMatrices().push();
                    context.getMatrices().translate(itemX, y + 17.0f, 0.0f);
                    context.getMatrices().scale(0.7f, 0.7f, 0.7f);
                    context.getMatrices().translate(-itemX, -(y + 17.0f), 0.0f);
                    context.drawItem(stack, (int) itemX, (int) y + 17);
                    if (stack.isDamaged()) {
                        int damage = stack.getDamage();
                        int maxDurability = stack.getMaxDamage();
                        float durabilityRatio = Math.max(0.0f, (maxDurability - damage) / (float) maxDurability);
                        int colorValue = MathHelper.hsvToRgb(durabilityRatio / 3.0f, 1.0f, 1.0f);
                        Color durabilityColor = new Color(colorValue);
                        renderer.renderRect(itemX + 4.0f, y + 32.0f, 8.0f, 2.5f, durabilityColor, context.getMatrices());
                    }
                    context.getMatrices().pop();
                    itemX += 12.0f;
                }
                break;
        }
        renderer.resetAlpha();
        context.getMatrices().pop();
    }

    private float getMaxHealth() {
        return this.entity.getMaxHealth();
    }

    @Generated
    public void setWidth(float width) {
        this.width = width;
    }

    @Generated
    public void setHeight(float height) {
        this.height = height;
    }

    @Generated
    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth;
    }

    @Generated
    public void setHealthReset(boolean healthReset) {
        this.healthReset = healthReset;
    }

    @Generated
    public void setCurrentAbsorption(float currentAbsorption) {
        this.currentAbsorption = currentAbsorption;
    }

    @Generated
    public void setAbsorptionReset(boolean absorptionReset) {
        this.absorptionReset = absorptionReset;
    }

    @Generated
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Generated
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Generated
    public void setHudType(HudType hudType) {
        this.hudType = hudType;
    }

    @Environment(value = EnvType.CLIENT)
    public static enum HudType {
        DEFAULT,
        COMPACT;

        private static final HudType[] VALUES = values();
        public static int unusedEnumField = 720042203;
    }
}