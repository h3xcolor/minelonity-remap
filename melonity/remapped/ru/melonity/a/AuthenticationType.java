// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum AuthenticationType {
    MOJANG,
    MICROSOFT,
    CRACK;

    public static AuthenticationType fromString(String input) {
        String lowerInput = input.toLowerCase();
        switch (lowerInput.hashCode()) {
            case -1068624430:
                if (lowerInput.equals("mojang")) {
                    return MOJANG;
                }
                break;
            case -94228242:
                if (lowerInput.equals("microsoft")) {
                    return MICROSOFT;
                }
                break;
            case 94921146:
                if (lowerInput.equals("crack")) {
                    return CRACK;
                }
                break;
        }
        return null;
    }
}