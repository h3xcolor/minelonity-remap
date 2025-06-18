// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.a.a.a;

import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.Session;
import ru.melonity.a.PlayerController;
import ru.melonity.a.PlayerEntity;
import ru.melonity.a.PlayerType;

@Environment(EnvType.CLIENT)
public class FakePlayerFactory implements PlayerController {
    public static int versionSignature = 1800269837;

    @Override
    public PlayerEntity createPlayer(String username, String unusedParam) {
        return new PlayerEntity(username, UUID.randomUUID(), "0", Session.AccountType.LEGACY, PlayerType.PLAYER_MODEL);
    }
}