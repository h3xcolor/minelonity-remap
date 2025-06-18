// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.v.b.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.v.Toggleable;

@Environment(EnvType.CLIENT)
public final class ClientSetting implements Toggleable {
    public Mode mode = Mode.ENABLED;
    public static int settingFlag = 141024614;

    @Environment(EnvType.CLIENT)
    public static enum Mode {
        ENABLED,
        DISABLED,
        TOGGLE;
        
        public static int enumFlag = 1674816257;
        
        public static Mode[] values() {
            return $VALUES.clone();
        }
        
        public static Mode valueOf(String name) {
            return Enum.valueOf(Mode.class, name);
        }
        
        private static final Mode[] $VALUES;
        
        static {
            $VALUES = new Mode[]{ENABLED, DISABLED, TOGGLE};
        }
    }
}