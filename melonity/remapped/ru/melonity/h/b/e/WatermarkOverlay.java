// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.e;

import java.awt.Color;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.o.a.Theme;
import ru.melonity.s.a.ThemeColors;
import ru.melonity.s.c.RenderHelper;
import ru.melonity.s.e.BaseOverlay;

@Environment(EnvType.CLIENT)
public class WatermarkOverlay extends BaseOverlay {
    private final List<WatermarkElement> watermarkElements;
    private float currentFps = 0.0f;
    private float smoothedFps = 0.0f;
    private boolean hasInitialized = false;
    private final FrameWeightCalculator frameWeightCalculator = FrameWeightCalculator.milliseconds(500L);

    public WatermarkOverlay(float scale) {
        super(1.0f, scale);
        this.watermarkElements = Arrays.asList(
            new WatermarkElement("fps.png", () -> String.format("%.0f FPS", smoothedFps)),
            new WatermarkElement("clock.png", () -> {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                return String.format("%02d:%02d", hour, minute);
            })
        );
    }

    @Override
    public void render(float x, float y, double mouseX, double mouseY, RenderHelper renderHelper, class_4587 matrixStack) {
        Theme theme = Melonity.getInstance().getThemeManager().getCurrentTheme();
        ThemeColors themeColors = theme.getColors();
        
        if (!hasInitialized) {
            smoothedFps = currentFps;
            hasInitialized = true;
        }
        
        float partialTicks = frameWeightCalculator.elapsedUnits();
        smoothedFps += (currentFps - smoothedFps) * 8.0f * partialTicks;
        
        float totalWidth = 22.0f;
        for (WatermarkElement element : watermarkElements) {
            float elementWidth = element.getWidth();
            totalWidth += elementWidth + 1.0f;
        }
        
        Color backgroundColor = themeColors.getBackgroundColor();
        RenderHelper.drawRoundedRect(x, y, totalWidth, this.getHeight(), 6.0f, backgroundColor, matrixStack);
        
        class_2960 overlayTexture = new class_2960("melonity/images/ui/watermark/overlay_blocks.png");
        Color overlayColor = themeColors.getOverlayColor();
        RenderHelper.drawTexture(overlayTexture, x + 0.3f, y + this.getHeight() - 3.0f, totalWidth - 0.6f, 7.5f, overlayColor, matrixStack);
        
        class_2960 logoTexture = new class_2960("melonity/images/ui/watermark/logo.png");
        Color logoColor = themeColors.getLogoColor();
        RenderHelper.drawTexture(logoTexture, x + 3.5f, y + 3.5f, 9.5f, 10.5f, logoColor, matrixStack);
        
        float elementX = x + 18.0f;
        for (WatermarkElement element : watermarkElements) {
            element.render(elementX, y + 3.0f, mouseX, mouseY, renderHelper, matrixStack);
            elementX += element.getWidth() + 1.0f;
        }
    }

    @Override
    public float getHeight() {
        float height = 22.0f;
        for (WatermarkElement element : watermarkElements) {
            height += element.getHeight() + 1.0f;
        }
        return height;
    }

    @Override
    public void onPositionUpdate(float x, float y) {}

    @Override
    public boolean onClick(int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public void onClose() {}

    @Generated
    public void setCurrentFps(float fps) {
        this.currentFps = fps;
    }

    @Environment(EnvType.CLIENT)
    private record WatermarkElement(String textureName, Supplier<String> textSupplier) {
        public void render(float x, float y, double mouseX, double mouseY, RenderHelper renderHelper, class_4587 matrixStack) {
            ThemeColors themeColors = Melonity.getInstance().getThemeManager().getCurrentTheme().getColors();
            float width = getWidth();
            float height = getHeight();
            
            renderHelper.drawRoundedRect(x, y, width, height, 4.0f, themeColors.getBackgroundColor().brighter(), matrixStack);
            renderHelper.drawText(textSupplier.get(), x + 5.0f + width / 2.0f - renderHelper.getTextWidth(textSupplier.get()) / 2.0f, y + 5.0f, matrixStack);
            
            class_2960 texture = new class_2960(String.format("melonity/images/ui/watermark/mini/%s", textureName));
            RenderHelper.drawTexture(texture, x + 2.5f, y + height / 2.0f - 3.75f, 7.5f, 7.5f, themeColors.getLogoColor(), matrixStack);
        }

        public float getWidth() {
            return 50.0f;
        }

        public float getHeight() {
            return 11.5f;
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{WatermarkElement.class, "textureName;textSupplier", "textureName", "textSupplier"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{WatermarkElement.class, "textureName;textSupplier", "textureName", "textSupplier"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{WatermarkElement.class, "textureName;textSupplier", "textureName", "textSupplier"}, this, object);
        }
    }

    @Environment(EnvType.CLIENT)
    private interface TextSupplier<T> {
        T get();
    }
}