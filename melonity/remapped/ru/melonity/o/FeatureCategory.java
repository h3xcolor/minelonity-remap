// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public enum FeatureCategory {
    COMBAT("Combat", "combat.png"),
    MOVEMENT("Movement", "movement.png"),
    RENDER("Render", "render.png"),
    PLAYER("Player", "player.png"),
    BARITONE("Baritone", "baritone.png"),
    MISC("Misc", "misc.png"),
    MACROS("Macros", "macros.png"),
    CONFIGS("Configs", "configs.png");

    private final String displayName;
    private final String iconFileName;

    private FeatureCategory(String displayName, String iconFileName) {
        this.displayName = displayName;
        this.iconFileName = iconFileName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getIconFileName() {
        return this.iconFileName;
    }
}