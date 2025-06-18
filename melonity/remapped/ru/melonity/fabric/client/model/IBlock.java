// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;

@Environment(EnvType.CLIENT)
public interface IBlock {
    public static int BLOCK_RENDER_ID = 423246104;

    public FrameWeightCalculator getFrameWeightCalculator();

    public StateAnimation getAlphaAnimation();
}