// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.k.a;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1792;
import ru.melonity.k.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class KeyBindingProfileManager implements IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    private final List<KeyBindingProfile> profiles = Arrays.asList(
        new KeyBindingProfile("Default", Maps.newHashMap()),
        new KeyBindingProfile("Default 2", Maps.newHashMap()),
        new KeyBindingProfile("Default 3", Maps.newHashMap())
    );
    private KeyBindingProfile currentProfile = profiles.get(0);

    @Override
    public Map<Integer, class_1792> getKeyMapForProfile(String profileName) {
        for (KeyBindingProfile profile : profiles) {
            if (profile.getName().equals(profileName)) {
                return profile.getKeyMap();
            }
        }
        return null;
    }

    @Override
    public ImmutableList<KeyBindingProfile> getProfiles() {
        return ImmutableList.copyOf(profiles);
    }

    @Override
    public KeyBindingProfile getCurrentProfile() {
        return currentProfile;
    }

    @Override
    public void setCurrentProfile(KeyBindingProfile profile) {
        this.currentProfile = profile;
    }

    @Override
    public void updateProfileKeyMap(String profileName, Map<Integer, class_1792> keyMap) {
        for (KeyBindingProfile profile : profiles) {
            if (profile.getName().equals(profileName)) {
                profile.setKeyMap(keyMap);
                return;
            }
        }
    }

    public static class KeyBindingProfile {
        private final String name;
        private Map<Integer, class_1792> keyMap;

        public KeyBindingProfile(String name, Map<Integer, class_1792> keyMap) {
            this.name = name;
            this.keyMap = keyMap;
        }

        public String getName() {
            return name;
        }

        public Map<Integer, class_1792> getKeyMap() {
            return keyMap;
        }

        public void setKeyMap(Map<Integer, class_1792> keyMap) {
            this.keyMap = keyMap;
        }
    }
}