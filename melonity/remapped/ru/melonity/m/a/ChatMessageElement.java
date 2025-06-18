// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.m.a;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import org.json.JSONObject;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.m.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll;
import ru.melonity.m.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;
import ru.melonity.w.IIllIllIlIlllIIlllIIlllIlIIIllIIlIIllIIllIIlIIIlllIIIlIIlllIIlllIIIlIlIIlIIllIlIIIllIIlIIllIIlIIlIIlllIIIllIlllIIlIIlllIllIllIIlllIlllIIlllIlIIIllIIllIlIlIIIllIlIIIlIIIllIIllIIlIlllIllIllIIIlIIIlIIllIIllIlIIIlIIIlIIllIIIllIIlll;
import ru.melonity.s.d.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll;

@Environment(EnvType.CLIENT)
public class ChatMessageElement implements IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll {
    private final FrameWeightCalculator animationTimeCalculator = FrameWeightCalculator.milliseconds(300L);
    private final StateAnimation stateAnimation = new StateAnimation();
    protected final IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll textInput = new IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll("Message text", false, 156.0f, 16.0f);

    public ChatMessageElement() {
        this.textInput.setTextColor(Color.WHITE);
        this.textInput.setItemSupplier(inputText -> {
            boolean isCommand = inputText.contains("/");
            if (isCommand) {
                return new class_1799(class_1802.field_8866);
            } else {
                return null;
            }
        });
    }

    @Override
    public IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllElIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlll2lllIIIlIIllIIIlll getAlignment() {
        return IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll.DEFAULT_ALIGNMENT;
    }

    @Override
    public float getVerticalMargin() {
        return 0.0f;
    }

    @Override
    public String getIdentifier() {
        return "Chat message";
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "chat");
        jsonObject.put("text", this.textInput.getText());
        return jsonObject;
    }

    public static ChatMessageElement fromJson(JSONObject jsonObject) {
        ChatMessageElement chatMessage = new ChatMessageElement();
        chatMessage.textInput.setText(jsonObject.getString("text"));
        chatMessage.textInput.setFocused(true);
        return chatMessage;
    }

    @Override
    public void render(float x, float y, float z, float width, int mouseX, int mouseY, ru.melonity.s.c.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll context, class_4587 matrices) {
        ru.melonity.o.a.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll theme = Melonity.getInstance().getThemeManager().getCurrentTheme();
        context.drawRectangle(x - 0.5f, y + 10.0f - 0.5f, z + 1.0f - 2.0f, 30.0f, 8.0f, 0.0f, 0.0f, 8.0f, theme.getBackgroundColor(), theme.getBackgroundColor(), theme.getBackgroundColor(), theme.getBackgroundColor(), matrices);
        context.drawRectangle(x + 1.0f, y + 10.0f, z - 2.0f, 29.0f, 8.0f, 0.0f, 0.0f, 8.0f, theme.getBorderColor(), theme.getBorderColor(), theme.getBorderColor(), theme.getBorderColor(), matrices);
        this.textInput.render(Melonity.getInstance().getFontRenderer(), x + 4.0f, y + 20.0f, mouseX, mouseY, context, matrices);
    }

    @Override
    public void handleMouseMove(float x, float y, double mouseX, double mouseY, int button) {
        this.textInput.setHovered(this.textInput.isMouseOver(x + 4.0f, y + 20.0f, this.textInput.getWidth(), this.textInput.getHeight(), mouseX, mouseY));
        this.textInput.handleMouseMove(x + 4.0f, y + 20.0f, mouseX, mouseY, button);
    }

    @Override
    public boolean handleMouseClick(int mouseX, int mouseY, int button) {
        return this.textInput.handleMouseClick(mouseX, mouseY, button);
    }

    @Override
    public void handleMouseRelease() {
        this.textInput.handleMouseRelease();
    }

    @Override
    public float getHeight() {
        return 25.0f;
    }

    @Override
    public StateAnimation getStateAnimation() {
        return this.stateAnimation;
    }

    @Override
    public FrameWeightCalculator getAnimationTimeCalculator() {
        return this.animationTimeCalculator;
    }

    @Override
    public void run() {
        class_310.getPlayer().sendChatMessage(class_2561.literal(this.textInput.getText()));
    }
}