// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.metafaze.protection.runtime.Protection;

@Environment(EnvType.CLIENT)
public class MelonityFabricClient implements ClientModInitializer {
    public static int clientProtectionInitializer = 948367808;

    @Override
    public void onInitializeClient() {
        Protection.interceptMain();
    }
}