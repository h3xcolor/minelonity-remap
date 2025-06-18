// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.a.b;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;

@Environment(EnvType.CLIENT)
public class GradientNoirTheme implements ru.melonity.o.a.Theme {
    public static int themeId = 2135184431;

    @Override
    public class_2960 getPreviewTexture() {
        return class_2960.method_60656("melonity/images/themes/preview/gradient_noir.png");
    }

    @Override
    public String getThemeName() {
        return "Gradient Noir";
    }

    @Override
    public Color getAccentColor() {
        return Color.decode("#D3F942");
    }

    @Override
    public Color getBackgroundColor() {
        return Color.decode("#0D0C0A");
    }

    @Override
    public Color[] getButtonGradient() {
        return new Color[]{Color.decode("#21201E"), Color.decode("#171614")};
    }

    @Override
    public boolean isFrameExtended() {
        return false;
    }

    @Override
    public Color getHoverColor() {
        return null;
    }

    @Override
    public Color getButtonTextColor() {
        return Color.decode("#222222");
    }

    @Override
    public Color[] getFrameGradient() {
        return new Color[]{Color.decode("#222222"), Color.decode("#2C2C2C")};
    }

    @Override
    public ru.melonity.o.a.a.ButtonConfig getButtonConfig() {
        return new ButtonConfigImpl();
    }

    @Override
    public ru.melonity.o.a.a.FrameConfig getFrameConfig() {
        return new FrameConfigImpl();
    }

    @Override
    public Color[] getSliderGradient() {
        return new Color[]{Color.decode("#21201E"), Color.decode("#171614")};
    }

    @Override
    public Color getTextColor() {
        return Color.WHITE;
    }

    @Override
    public ru.melonity.o.a.a.TitleConfig getTitleConfig() {
        return new TitleConfigImpl();
    }

    @Override
    public ru.melonity.o.a.a.ButtonAccentConfig getButtonAccentConfig() {
        return new ButtonAccentConfigImpl();
    }

    @Override
    public Color getHighlightedTextColor() {
        return getBackgroundColor().brighter().brighter();
    }

    @Override
    public Color getAlternateTextColor() {
        return Color.decode("#222222");
    }

    @Override
    public Color getDisabledButtonTextColor() {
        return Color.decode("#2C2C2C");
    }

    @Environment(EnvType.CLIENT)
    private static class ButtonConfigImpl implements ru.melonity.o.a.a.ButtonConfig {
        public static int buttonConfigId = 322127164;

        private ButtonConfigImpl() {
        }

        @Override
        public Color getTextColor() {
            return Color.WHITE;
        }

        @Override
        public Color getDisabledTextColor() {
            return Color.decode("#888888");
        }

        @Override
        public Color[] getButtonGradientColors() {
            return new Color[]{Color.decode("#21201E"), Color.decode("#171614")};
        }
    }

    @Environment(EnvType.CLIENT)
    private static class FrameConfigImpl implements ru.melonity.o.a.a.FrameConfig {
        public static int frameConfigId = 2136972286;

        private FrameConfigImpl() {
        }

        @Override
        public Color[] getFrameGradientColors() {
            return new Color[]{Color.decode("#2B2A28"), Color.decode("#171614")};
        }

        @Override
        public Color[] getInnerPanelGradient() {
            return new Color[]{Color.decode("#222222"), Color.decode("#222222")};
        }

        @Override
        public Color getBorderColor() {
            return Color.decode("#0D0C0A");
        }

        @Override
        public Color getTextColor() {
            return Color.WHITE;
        }

        @Override
        public Color getInnerPanelColor() {
            return Color.decode("#171614");
        }
    }

    @Environment(EnvType.CLIENT)
    private static class TitleConfigImpl implements ru.melonity.o.a.a.TitleConfig {
        public static int titleConfigId = 439059632;

        private TitleConfigImpl() {
        }

        @Override
        public class_2960 getTitleTexture() {
            return class_2960.method_60656("melonity/images/themes/gira/gradient_noir.png");
        }

        @Override
        public Color getBackgroundColor() {
            return Color.decode("#222222");
        }
    }

    @Environment(EnvType.CLIENT)
    private static class ButtonAccentConfigImpl implements ru.melonity.o.a.a.ButtonAccentConfig {
        public static int accentConfigId = 1202020618;

        private ButtonAccentConfigImpl() {
        }

        @Override
        public Color getButtonBackgroundColor() {
            return Color.decode("#171614");
        }

        @Override
        public Color getHoveredButtonColor() {
            return Color.decode("#505C25");
        }

        @Override
        public Color getAccentColor() {
            return Color.decode("#D3F942");
        }
    }
}