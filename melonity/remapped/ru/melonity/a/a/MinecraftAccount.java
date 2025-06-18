// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.a.a;

import com.mojang.authlib.GameProfile;
import java.util.Optional;
import java.util.UUID;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import ru.melonity.a.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll;
import ru.melonity.fabric.client.model.IMinecraft;

@Environment(EnvType.CLIENT)
public class MinecraftAccount implements ru.melonity.a.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    private final String username;
    private final UUID uuid;
    private final String accessToken;
    private final Session.AccountType accountType;
    private final IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll accountData;
    public static int f24dc71c4c95240ecac9cc83405c6b549 = 1600461265;

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public boolean login() {
        Session session = new Session(this.username, this.uuid, this.accessToken, Optional.empty(), Optional.empty(), this.accountType);
        ((IMinecraft) MinecraftClient.getInstance()).setUser(session);
        return true;
    }

    @Override
    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public Session.AccountType getAccountType() {
        return this.accountType;
    }

    @Override
    public IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll getAccountData() {
        return this.accountData;
    }

    @Override
    public GameProfile getGameProfile() {
        return new GameProfile(this.uuid, this.username);
    }

    @Generated
    public MinecraftAccount(String username, UUID uuid, String accessToken, Session.AccountType accountType, IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll accountData) {
        this.username = username;
        this.uuid = uuid;
        this.accessToken = accessToken;
        this.accountType = accountType;
        this.accountData = accountData;
    }
}