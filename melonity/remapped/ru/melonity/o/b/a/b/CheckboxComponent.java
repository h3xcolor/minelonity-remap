// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b.a.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.i.TextRenderer;
import ru.melonity.o.b.a.UIConfigComponent;
import ru.melonity.s.c.DrawHelper;
import ru.melonity.w.InputHelper;

@Environment(value = EnvType.CLIENT)
public class CheckboxComponent implements UIConfigComponent {
    private final String label;
    private final FrameWeightCalculator frameWeightCalculator = FrameWeightCalculator.milliseconds(250L);
    private final StateAnimation stateAnimation = new StateAnimation();
    private boolean checked;

    public CheckboxComponent(String label, boolean checked) {
        this.label = label;
        this.checked = checked;
    }

    @Override
    public void render(UIConfigComponent component, float x, float y, float delta, double mouseX, double mouseY, DrawHelper drawHelper, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        Identifier checkboxTexture = new Identifier("melonity/images/ui/checkbox.png");
        drawHelper.drawTexture(checkboxTexture, x, y - 4.0f, 8.0f, 8.5f, matrices);
        this.stateAnimation.setState(this.checked);
        float elapsedUnits = this.frameWeightCalculator.elapsedUnits();
        this.stateAnimation.update(elapsedUnits);
        Identifier toggledTexture = new Identifier("melonity/images/ui/checkbox_toggled.png");
        Color baseColor = component.getTextColor();
        float animationProgress = this.stateAnimation.animation();
        int alpha = MathHelper.clamp((int) (animationProgress * 255.0f), 0, 255);
        Color color = DrawHelper.withAlpha(baseColor, alpha);
        drawHelper.drawTexture(toggledTexture, x, y - 4.0f, 8.0f, 8.5f, color, matrices);
        TextHelper textHelper = this.getTextHelper();
        String text = textHelper.translate(this.label);
        drawHelper.drawText(TextRenderer.DEFAULT, text, x + 12.0f, y - 1.0f, matrices);
        boolean hovered = InputHelper.isHovered(x, y - 4.0f, 8.0, 8.5, mouseX, mouseY);
        if (hovered) {
            Melonity.hoverHandled = true;
        }
    }

    @Override
    public void onClick(float x, float y, float delta, double mouseX, double mouseY, int button) {
        boolean hovered = InputHelper.isHovered(x, y - 4.0f, 8.0, 8.5, mouseX, mouseY);
        if (hovered) {
            this.checked = !this.checked;
        }
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void onRemoved() {
    }

    @Override
    public float getHeight() {
        return 16.0f;
    }

    public void resetAnimation() {
        this.stateAnimation.reset();
    }

    public boolean isChecked() {
        return this.checked;
    }

    public String getLabel() {
        return this.label;
    }

    public FrameWeightCalculator getFrameWeightCalculator() {
        return this.frameWeightCalculator;
    }

    public StateAnimation getStateAnimation() {
        return this.stateAnimation;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    private TextHelper getTextHelper() {
        return TextHelper.INSTANCE;
    }
}