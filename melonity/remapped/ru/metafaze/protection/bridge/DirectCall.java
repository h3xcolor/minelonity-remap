// ремапили ребята из https://t.me/dno_rumine
package ru.metafaze.protection.bridge;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public class DirectCall {
    public static int BRIDGE_IDENTIFIER = 890574212;

    public static long getHardwareIdentifierAsHash() {
        return 0L;
    }

    public static void showMessageBox(String title, String message, int flags) {
        throw createUnreachableException();
    }

    public static void terminateProcess() {
        throw createUnreachableException();
    }

    private static UnsupportedOperationException createUnreachableException() {
        return new UnsupportedOperationException("Unreachable");
    }
}