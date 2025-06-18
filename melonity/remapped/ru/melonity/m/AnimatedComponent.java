// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.m;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4587;
import org.json.JSONObject;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;

@Environment(EnvType.CLIENT)
public interface AnimatedComponent extends Runnable {
    public static int COMPONENT_ID = 496332688;

    public String getName();

    public JSONObject getConfig();

    public IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll getAnimationController();

    public float getAnimationProgress();

    public void render(float x, float y, float width, float height, int mouseX, int mouseY, ru.melonity.s.c.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll context, class_4587 matrixStack);

    public void onMouseEvent(float x, float y, double mouseX, double mouseY, int button);

    public boolean onKeyEvent(int keyCode, int scanCode, int modifiers);

    public void resetAnimation();

    public float getAnimationSpeed();

    public StateAnimation getStateAnimation();

    public FrameWeightCalculator getFrameWeightCalculator();
}