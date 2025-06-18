// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.a.b;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;

@Environment(EnvType.CLIENT)
public class CalmGrayTheme implements ru.melonity.o.a.Theme {

    @Override
    public class_2960 getPreviewTexture() {
        return class_2960.method_60656("melonity/images/themes/preview/calm_gray.png");
    }

    @Override
    public String getName() {
        return "Calm Gray";
    }

    @Override
    public Color getBackgroundColor() {
        return Color.decode("#B0B0B0");
    }

    @Override
    public Color getForegroundColor() {
        return Color.decode("#1E1E1E");
    }

    @Override
    public boolean useGradient() {
        return true;
    }

    @Override
    public Color getPanelColor() {
        return Color.decode("#262626");
    }

    @Override
    public Color[] getGradientColors() {
        return new Color[]{Color.decode("#2A2A2A"), Color.decode("#2A2A2A")};
    }

    @Override
    public ru.melonity.o.a.a.ButtonColors getButtonColors() {
        return new ButtonColors();
    }

    @Override
    public ru.melonity.o.a.a.SliderColors getSliderColors() {
        return new SliderColors();
    }

    @Override
    public Color[] getSliderTrackColors() {
        return new Color[]{Color.decode("#B0B0B0"), Color.decode("#B0B0B0")};
    }

    @Override
    public Color getTextColor() {
        return getBackgroundColor();
    }

    @Override
    public Color getBorderColor() {
        return Color.decode("#3C3C3C");
    }

    @Override
    public Color[] getButtonHoverColors() {
        return new Color[]{Color.decode("#3C3C3C"), Color.decode("#4D4D4D")};
    }

    @Override
    public ru.melonity.o.a.a.GiraffeElementColors getGiraffeElementColors() {
        return new GiraffeElementColors();
    }

    @Override
    public ru.melonity.o.a.a.CheckboxColors getCheckboxColors() {
        return new CheckboxColors();
    }

    @Override
    public Color getDisabledTextColor() {
        return getForegroundColor();
    }

    @Override
    public Color getHoverBorderColor() {
        return Color.decode("#3C3C3C");
    }

    @Override
    public Color getHoverBorderSecondaryColor() {
        return getHoverBorderColor().darker();
    }

    @Environment(EnvType.CLIENT)
    private static class ButtonColors implements ru.melonity.o.a.a.ButtonColors {
        @Override
        public Color getNormalColor() {
            return Color.decode("#1E1E1E");
        }

        @Override
        public Color getHoverColor() {
            return Color.decode("#A0A0A0");
        }

        @Override
        public Color[] getTextColors() {
            return new Color[]{Color.decode("#B0B0B0"), Color.decode("#B0B0B0")};
        }
    }

    @Environment(EnvType.CLIENT)
    private static class SliderColors implements ru.melonity.o.a.a.SliderColors {
        @Override
        public Color[] getSliderThumbColors() {
            return new Color[]{Color.decode("#B0B0B0"), Color.decode("#B0B0B0")};
        }

        @Override
        public Color[] getSliderTrackHoverColors() {
            return new Color[]{Color.decode("#3C3C3C"), Color.decode("#3C3C3C")};
        }

        @Override
        public Color getTextColor() {
            return Color.decode("#E8E8E8");
        }

        @Override
        public Color getDisabledTextColor() {
            return Color.decode("#1E1E1E");
        }

        @Override
        public Color getBackgroundColor() {
            return Color.decode("#2A2A2A");
        }
    }

    @Environment(EnvType.CLIENT)
    private static class GiraffeElementColors implements ru.melonity.o.a.a.GiraffeElementColors {
        @Override
        public class_2960 getTexture() {
            return class_2960.method_60656("melonity/images/themes/gira/calm_gray.png");
        }

        @Override
        public Color getColor() {
            return Color.decode("#3C3C3C");
        }
    }

    @Environment(EnvType.CLIENT)
    private static class CheckboxColors implements ru.melonity.o.a.a.CheckboxColors {
        @Override
        public Color getBackgroundColor() {
            return Color.decode("#2A2A2A");
        }

        @Override
        public Color getCheckmarkColor() {
            return Color.decode("#B0B0B0").darker().darker();
        }

        @Override
        public Color getTextColor() {
            return Color.decode("#B0B0B0");
        }
    }
}