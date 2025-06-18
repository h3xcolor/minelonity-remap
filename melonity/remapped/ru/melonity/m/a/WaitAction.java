// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.m.a;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4587;
import org.json.JSONObject;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.m.ActionType;
import ru.melonity.m.ValueHolder;
import ru.melonity.m.a.BaseAction;
import ru.melonity.s.d.TextField;
import ru.melonity.s.c.RenderContext;
import ru.melonity.w.TextRenderer;

@Environment(EnvType.CLIENT)
public class WaitAction extends BaseAction {
    private final FrameWeightCalculator frameWeightCalculator = FrameWeightCalculator.milliseconds(300L);
    private final StateAnimation stateAnimation = new StateAnimation();
    private TextField textField = new TextField("100", false, 20.0f, 12.0f);
    private final ValueHolder valueHolder = new ValueHolder();

    @Override
    public String getActionName() {
        return this.isTextFieldValid() ? "Wait" : "Wait ... ms";
    }

    @Override
    public ActionType getActionType() {
        return ActionType.WAIT_MS;
    }

    @Override
    public float getWaitTime() {
        try {
            String text = this.textField.getText();
            int value = Integer.parseInt(text);
            return value;
        }
        catch (Throwable throwable) {
            return 0.0f;
        }
    }

    @Override
    public void render(float x, float y, float width, float height, int mouseX, int mouseY, RenderContext renderContext, class_4587 matrixStack) {
        String text = this.textField.getText();
        boolean isEmpty = text.isEmpty();
        if (!isEmpty) {
            String numericText = text.replaceAll("[^0-9]", "");
            this.textField.setText(numericText);
        }
        this.textField.setWidth(Math.max(26.0f, TextRenderer.getTextWidth(this.textField.getText()) + 14.0f));
        this.textField.render(this.valueHolder, x + 36.0f, y + 2.5f, mouseX, mouseY, renderContext, matrixStack);
        TextRenderer.drawText(renderContext, "ms", x + 36.0f + this.textField.getWidth() + 4.0f, y + 6.5f, Color.decode("#888888"), matrixStack);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "wait_ms");
        jsonObject.put("millis", (int)this.getWaitTime());
        return jsonObject;
    }

    public static WaitAction fromJson(JSONObject jsonObject) {
        WaitAction waitAction = new WaitAction();
        waitAction.textField.setText(String.valueOf(jsonObject.getInt("millis")));
        waitAction.textField.setValid(true);
        return waitAction;
    }

    @Override
    public void onMouseMove(float x, float y, double mouseX, double mouseY, int button) {
        this.textField.onMouseMove(this.valueHolder, x + 36.0f, y + 2.5f, mouseX, mouseY);
        this.textField.onMouseMove(x + 36.0f, y + 2.5f, mouseX, mouseY, button);
    }

    @Override
    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        return this.textField.onMouseClick(mouseX, mouseY, button);
    }

    @Override
    public void onMouseRelease() {
        this.textField.onMouseRelease();
    }

    @Override
    public float getWidth() {
        return 0.0f;
    }

    @Override
    public StateAnimation getStateAnimation() {
        return this.stateAnimation;
    }

    @Override
    public FrameWeightCalculator getFrameWeightCalculator() {
        return this.frameWeightCalculator;
    }

    @Override
    public void run() {
        float waitTime = this.getWaitTime();
        try {
            Thread.sleep((int)waitTime);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean isTextFieldValid() {
        try {
            String text = this.textField.getText();
            Integer.parseInt(text);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}