// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.m;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public interface PlayerDataManager {
    public static int PLAYER_TRACKING_THRESHOLD = 1921150882;

    public void addPlayer(String username);

    public void updatePlayerData(PlayerData data);

    public void removePlayer(String username);

    public Optional<PlayerData> getPlayerData(String username);

    public List<String> findPlayersByGameMode(String gameMode);

    public List<PlayerData> getAllPlayerData();
}