// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s.a;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum FontType {
    ARIAL("arial.ttf"),
    INTER_REGULAR("inter_regular.ttf"),
    INTER_MEDIUM("inter_medium.ttf"),
    INTER_SEMIBOLD("inter_semibold.ttf"),
    INTER_LIGHT("inter_light.ttf"),
    INTER_BOLD("inter_bold.ttf");

    private final String fontFileName;

    private FontType(String fontFileName) {
        this.fontFileName = fontFileName;
    }

    @Generated
    public String getFontFileName() {
        return this.fontFileName;
    }
}