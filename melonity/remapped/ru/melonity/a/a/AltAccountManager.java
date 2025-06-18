// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.a.a;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_320;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import ru.melonity.a.AccountType;
import ru.melonity.a.AccountSession;
import ru.melonity.a.AltAccount;

@Environment(value = EnvType.CLIENT)
public class AltAccountManager {
    private static final File ACCOUNTS_DIR = new File("C:/Melonity Minecraft", "alts");
    private final Map<AccountType, AccountSession> accountMap = Maps.newLinkedHashMap();
    private final List<AltAccount> accountList = Lists.newLinkedList();

    public List<AltAccount> getAccounts() {
        return accountList;
    }

    public void removeAccount(AltAccount account) {
        boolean removed = accountList.removeIf(acc -> acc.getName().equals(account.getName()));
        System.out.println(removed);
    }

    public void putAccount(AccountType type, AccountSession session) {
        accountMap.put(type, session);
    }

    public Optional<AccountSession> getAccount(AccountType type) {
        boolean containsKey = accountMap.containsKey(type);
        if (containsKey) {
            AccountSession session = accountMap.get(type);
            return Optional.of(session);
        } else {
            return Optional.empty();
        }
    }

    public AltAccount login(AccountType type, String username, String password) {
        return getAccount(type).map(session -> session.login(username, password)).orElse(null);
    }

    public void saveAccounts() {
        File accountsFile = new File(ACCOUNTS_DIR, "alts.json");
        JSONObject root = new JSONObject();
        JSONArray profilesArray = new JSONArray();
        Iterator<AltAccount> iterator = accountList.iterator();
        while (iterator.hasNext()) {
            AltAccount account = iterator.next();
            JSONObject accountObj = new JSONObject();
            accountObj.put("name", account.getName());
            AccountType profileType = account.getAccountType();
            accountObj.put("profiletype", profileType.name());
            accountObj.put("accesstoken", account.getAccessToken());
            UUID uuid = account.getUuid();
            accountObj.put("uuid", uuid.toString());
            class_320.class_321 sessionType = account.getSessionType();
            accountObj.put("sessiontype", sessionType.name());
            GameProfile gameProfile = account.getGameProfile();
            accountObj.put("gpname", gameProfile.getName());
            UUID profileId = gameProfile.getId();
            accountObj.put("gpid", profileId.toString());
            profilesArray.put(accountObj);
        }
        root.put("profiles", profilesArray);
        FileUtils.deleteQuietly(accountsFile);
        String jsonString = root.toString(4);
        FileUtils.writeStringToFile(accountsFile, jsonString);
    }

    public void loadAccounts() {
        File accountsFile = new File(ACCOUNTS_DIR, "alts.json");
        if (!accountsFile.exists()) {
            return;
        }
        try {
            String fileContent = FileUtils.readFileToString(accountsFile, StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(fileContent);
            if (!root.has("profiles")) {
                return;
            }
            JSONArray profilesArray = root.getJSONArray("profiles");
            if (profilesArray.isEmpty()) {
                return;
            }
            Iterator<Object> iterator = profilesArray.iterator();
            while (iterator.hasNext()) {
                Object element = iterator.next();
                JSONObject accountObj = (JSONObject) element;
                try {
                    String name = accountObj.getString("name");
                    String uuidStr = accountObj.getString("uuid");
                    UUID uuid = UUID.fromString(uuidStr);
                    String accessToken = accountObj.getString("accesstoken");
                    String sessionTypeStr = accountObj.getString("sessiontype");
                    class_320.class_321 sessionType = class_320.class_321.method_1679(sessionTypeStr);
                    String profileTypeStr = accountObj.getString("profiletype");
                    AccountType profileType = AccountType.valueOf(profileTypeStr);
                    AltAccount account = new AltAccount(name, uuid, accessToken, sessionType, profileType);
                    accountList.add(account);
                } catch (Throwable ignored) {
                }
            }
        } catch (Exception e) {
            try {
                FileUtils.delete(accountsFile);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }
}