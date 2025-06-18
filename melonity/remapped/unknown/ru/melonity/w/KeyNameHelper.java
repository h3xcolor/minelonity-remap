// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;

@Environment(value = EnvType.CLIENT)
public final class KeyNameHelper {
    public static String getKeyName(int keyCode) {
        if (keyCode == -1) {
            return "?";
        }
        String keyName = GLFW.glfwGetKeyName(keyCode, 0);
        if (keyName == null) {
            return "?";
        }
        return keyName.toUpperCase();
    }

    private KeyNameHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}