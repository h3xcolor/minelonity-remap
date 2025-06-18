// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.a;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_320;
import ru.melonity.a.PlayerState;
import java.util.UUID;

@Environment(value=EnvType.CLIENT)
public interface ClientPlayer {
    int VERSION_ID = 779757178;

    String getUsername();
    UUID getUuid();
    String getDisplayName();
    boolean isOperator();
    class_320.class_321 getGameMode();
    PlayerState getPlayerState();
    GameProfile getGameProfile();
}