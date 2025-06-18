// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.l;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum Language {
    RUSSIAN("russian.png", "ru_RU", "russian.json"),
    ENGLISH("english.png", "en_US", "english.json");

    private final String imageName;
    private final String locale;
    private final String jsonFileName;

    private Language(String imageName, String locale, String jsonFileName) {
        this.imageName = imageName;
        this.locale = locale;
        this.jsonFileName = jsonFileName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getLocale() {
        return locale;
    }

    public String getJsonFileName() {
        return jsonFileName;
    }
}