// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.fabric.client.model.IMobEffectInstance;

@Environment(EnvType.CLIENT)
@Mixin(StatusEffectInstance.class)
public class StatusEffectInstanceMixin implements IMobEffectInstance {
    @Unique
    private long effectStartTime = -1L;
    @Unique
    private StateAnimation scaleAnimation = null;
    @Unique
    private final FrameWeightCalculator frameWeightCalculator = FrameWeightCalculator.milliseconds(400L);

    @Override
    public StateAnimation getScale() {
        if (this.scaleAnimation == null) {
            this.scaleAnimation = new StateAnimation();
        }
        return this.scaleAnimation;
    }

    @Override
    public FrameWeightCalculator getFWC() {
        return this.frameWeightCalculator;
    }

    @Override
    public long getTimes() {
        return this.effectStartTime;
    }

    @Override
    public void setTimes(long time) {
        this.effectStartTime = time;
    }
}