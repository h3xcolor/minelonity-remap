// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.v.b;

import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector2f;
import java.awt.Color;
import ru.melonity.v.IlllIIlIIIlllIIIlIIllIlIllIIIlIIIllIlllIIIllIIlIIllIIllIlllIIllIIllIIllIIllIIlllIIlllIllIIllIlllIlIllIIlllIllIllIIlllIIllIIIlllIIIllIIIllIlllIIlllIIlIlIlIlIllIIIlllIIlIllIIllIIlllIIlIIIllIIIllIIlIIlllIlIllIllIIlIIIllIllIIlIIllIll;
import ru.melonity.v.b.a.ButtonState;
import ru.melonity.v.a.RenderContext;
import ru.melonity.i.FontSize;

@Environment(EnvType.CLIENT)
public class IconTextButton extends IlllIIlIIIlllIIIlIIllIlIllIIIlIIIllIlllIIIllIIlIIllIIllIlllIIllIIllIIllIIllIIlllIIlllIllIIllIlllIlIllIIlllIllIllIIlllIIllIIIlllIIIllIIIllIlllIIlllIIlIlIlIlIllIIIlllIIlIllIIllIIlllIIlIIIllIIIllIIlIIlllIlIllIllIIlIIIllIllIIlIIllIll<ButtonState> {
    private final Identifier iconId;
    private final String text;

    public IconTextButton(Identifier iconId, String text) {
        super(new Vector2f(94.0f, 22.0f));
        this.iconId = iconId;
        this.text = text;
    }

    @Override
    public void render(Vector2f position, RenderContext context) {
        Color backgroundColor;
        Color textColor;

        ButtonState state = this.getState();
        int stateIndex = state.ordinal();
        if (stateIndex == 0) {
            backgroundColor = new Color(0, 0, 0, 0);
        } else if (stateIndex == 1) {
            backgroundColor = Color.decode("#181818");
        } else if (stateIndex == 2) {
            backgroundColor = Color.decode("#D3F942");
        } else {
            throw new IllegalStateException("Unexpected state: " + state);
        }

        ButtonState textState = this.getState();
        int textStateIndex = textState.ordinal();
        if (textStateIndex == 0 || textStateIndex == 1) {
            textColor = Color.decode("#888888");
        } else if (textStateIndex == 2) {
            textColor = Color.decode("#141414");
        } else {
            throw new IllegalStateException("Unexpected state: " + textState);
        }

        context.drawRoundedRectangle(position, this.getSize(), 8.0f, backgroundColor);
        context.drawTexture(this.iconId, new Vector2f(position.x + 8.0f, position.y + 6.0f), new Vector2f(9.5f, 9.5f), textColor);
        context.drawText(this.text, FontSize.SMALL, new Vector2f(position.x + 19.5f, position.y + 9.0f), textColor);
    }

    private ButtonState getState() {
        return null;
    }
}