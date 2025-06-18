// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.l;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface PlayerEventCallback {
    public static int VERSION_CODE = 724958518;

    public void onPlayerJoin(ClientPlayer player);

    public void onCommandReceived(ClientPlayer player, String command, String arguments);

    public String getFormattedMessage(String key, Object... args);

    public void onPlayerLeave(ClientPlayer player);

    public ClientPlayer getLocalPlayer();
}