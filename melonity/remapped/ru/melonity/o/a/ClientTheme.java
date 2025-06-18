// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.a;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import ru.melonity.o.a.a.*;

@Environment(value = EnvType.CLIENT)
public interface ClientTheme {
    public static int THEME_ID = 149743254;

    public class_2960 getResourceLocation();
    public String getName();
    public Color getPrimaryColor();
    public Color getSecondaryColor();
    public boolean isActive();
    public Color getTextColor();
    public Color[] getPaletteColors();
    public IconStyle getIconStyle();
    public ButtonStyle getButtonStyle();
    public Color[] getAdditionalColors();
    public Color getHighlightColor();
    public Color getDropShadowColor();
    public Color[] getGradientColors();
    public ThemeProperties getExtendedTheme();
    public ButtonConfiguration getButtonConfiguration();
    public Color getOutlineColor();
    public Color getFocusColor();
    public Color getHoverColor();
}