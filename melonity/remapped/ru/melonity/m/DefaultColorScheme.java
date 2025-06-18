// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.m;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import java.awt.Color;

@Environment(value=EnvType.CLIENT)
public class DefaultColorScheme implements ColorScheme {
    public static int versionId = 387065308;

    @Override
    public Identifier getTextureIdentifier() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Color getTextColor() {
        return null;
    }

    @Override
    public Color getBackgroundColor() {
        return Color.decode("#181818");
    }

    @Override
    public boolean isBorderEnabled() {
        return false;
    }

    @Override
    public Color getBorderColor() {
        return null;
    }

    @Override
    public Color[] getColorPalette() {
        return new Color[0];
    }

    @Override
    public TextColorScheme getTextColorScheme() {
        return new WhiteColorScheme();
    }

    @Override
    public ColorScheme getFallbackScheme() {
        return null;
    }

    @Override
    public Color[] getSecondaryPalette() {
        return new Color[0];
    }

    @Override
    public Color getHoverColor() {
        return null;
    }

    @Override
    public Color getActiveTextColor() {
        return this.getBackgroundColor();
    }

    @Override
    public Color[] getTertiaryPalette() {
        return new Color[0];
    }

    @Override
    public ColorScheme getErrorScheme() {
        return null;
    }

    @Override
    public Color getPressedColor() {
        return null;
    }

    @Override
    public Color getDisabledTextColor() {
        return null;
    }

    @Override
    public Color getAccentColor() {
        return null;
    }

    @Override
    public Color getInactiveTextColor() {
        return null;
    }

    @Environment(value=EnvType.CLIENT)
    private static class WhiteColorScheme implements TextColorScheme {
        public static int whiteVersionId = 920794388;

        private WhiteColorScheme() {
        }

        @Override
        public Color getTextColor() {
            return Color.decode("#ffffff");
        }

        @Override
        public Color getSecondaryTextColor() {
            return Color.decode("#ffffff");
        }

        @Override
        public Color[] getTextTones() {
            return new Color[0];
        }
    }
}

interface ColorScheme {
    Identifier getTextureIdentifier();
    String getName();
    Color getTextColor();
    Color getBackgroundColor();
    boolean isBorderEnabled();
    Color getBorderColor();
    Color[] getColorPalette();
    TextColorScheme getTextColorScheme();
    ColorScheme getFallbackScheme();
    Color[] getSecondaryPalette();
    Color getHoverColor();
    Color getActiveTextColor();
    Color[] getTertiaryPalette();
    ColorScheme getErrorScheme();
    Color getPressedColor();
    Color getDisabledTextColor();
    Color getAccentColor();
    Color getInactiveTextColor();
}

interface TextColorScheme {
    Color getTextColor();
    Color getSecondaryTextColor();
    Color[] getTextTones();
}