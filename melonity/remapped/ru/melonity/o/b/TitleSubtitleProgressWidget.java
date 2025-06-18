// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b;

import java.awt.Color;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4587;
import ru.melonity.i.Fonts;
import ru.melonity.o.a.GuiHelper;
import ru.melonity.s.c.DrawContext;
import ru.melonity.s.e.Widget;

@Environment(value = EnvType.CLIENT)
public class TitleSubtitleProgressWidget extends Widget {
    private String title;
    private String subtitle;
    public static int DEFAULT_COLOR = 1374090766;

    public TitleSubtitleProgressWidget(String title, String subtitle, float width, float height) {
        super(width, height);
        this.title = title;
        this.subtitle = subtitle;
    }

    public TitleSubtitleProgressWidget(String title, String subtitle) {
        this(title, subtitle, 179.0f, 215.0f);
    }

    @Override
    public void render(GuiHelper guiHelper, float mouseX, float mouseY, double deltaX, double deltaY, DrawContext drawContext, class_4587 matrixStack) {
        drawContext.drawBackground(mouseX, mouseY, this.width, this.height, 8.0f, guiHelper.getTitleFont().getColor(), matrixStack);
        drawContext.drawText(Fonts.TITLE_FONT, this.title, mouseX + 7.0f, mouseY + 10.0f, matrixStack);
        drawContext.drawText(Fonts.SUBTITLE_FONT, this.subtitle, mouseX + 7.0f, mouseY + 21.0f, guiHelper.getSubtitleFont().getColor(), matrixStack);
        drawContext.drawProgressBar(mouseX, mouseY + 33.0f, this.width, 1.0f, Color.decode("#222222"), matrixStack);
    }

    @Override
    public void onClick(float mouseX, float mouseY, double deltaX, double deltaY, int button) {
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public void onClose() {
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getSubtitle() {
        return this.subtitle;
    }
}