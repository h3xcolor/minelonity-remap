// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.n;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import ru.melonity.s.e.AbstractButtonWidget;

@Environment(EnvType.CLIENT)
public class AltManagerButton extends AbstractButtonWidget {
    public static final int ID = 337433478;

    public AltManagerButton() {
        super(202.0f, 35.0f);
    }

    @Override
    public void renderButton(float x, float y, double mouseX, double mouseY, GuiContext context, MatrixStack matrices) {
        context.drawTexture(new Identifier("melonity/images/ui/altmanager/altmanager.png"), x + 11.0f, y + 15.0f, 10.0f, 10.0f, matrices);
        context.drawText(TitleFont.INSTANCE, "Alt Manager", x + 26.0f, y + 14.0f, matrices);
        context.drawText(SubtitleFont.INSTANCE, "Add new or switch account.", x + 26.0f, y + 24.5f, Color.GRAY, matrices);
        this.renderDivider(x, y + 35.0f, context, 1.0f, Color.decode("#222222"), matrices);
    }

    @Override
    public void onClick(float x, float y, double mouseX, double mouseY, int button) {
    }

    @Override
    public void onRelease() {
    }
}