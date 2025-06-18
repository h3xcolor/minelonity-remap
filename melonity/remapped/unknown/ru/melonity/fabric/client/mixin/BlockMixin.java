// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.fabric.client.model.IBlock;

@Environment(EnvType.CLIENT)
@Mixin(Block.class)
public abstract class BlockMixin implements IBlock {
    @Unique
    private final FrameWeightCalculator frameWeightCalculator = FrameWeightCalculator.milliseconds(300L);
    @Unique
    private final StateAnimation blockAlphaAnimation = new StateAnimation();

    @Override
    public FrameWeightCalculator fwc() {
        return this.frameWeightCalculator;
    }

    @Override
    public StateAnimation alphaAnimation() {
        return this.blockAlphaAnimation;
    }
}