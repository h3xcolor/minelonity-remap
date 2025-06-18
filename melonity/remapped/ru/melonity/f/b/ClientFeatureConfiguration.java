// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_315;
import ru.melonity.f.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIll极狐IllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;

@Environment(EnvType.CLIENT)
public class ClientFeatureConfiguration implements IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    private class_315 minecraftClient;
    private boolean flag1;
    private boolean flag2;
    private boolean flag3;
    private boolean flag4;
    public static int CONFIG_ID = 1562841703;

    @Generated
    public ClientFeatureConfiguration(class_315 minecraftClient, boolean flag1, boolean flag2, boolean flag3, boolean flag4) {
        this.minecraftClient = minecraftClient;
        this.flag1 = flag1;
        this.flag2 = flag2;
        this.flag3 = flag3;
        this.flag4 = flag4;
    }

    @Generated
    public class_315 getMinecraftClient() {
        return this.minecraftClient;
    }

    @Generated
    public boolean isFlag1() {
        return this.flag1;
    }

    @Generated
    public boolean isFlag2() {
        return this.flag2;
    }

    @Generated
    public boolean isFlag3() {
        return this.flag3;
    }

    @Generated
    public boolean isFlag4() {
        return this.flag4;
    }

    @Generated
    public void setMinecraftClient(class_315 minecraftClient) {
        this.minecraftClient = minecraftClient;
    }

    @Generated
    public void setFlag1(boolean flag1) {
        this.flag1 = flag1;
    }

    @Generated
    public void setFlag2(boolean flag2) {
        this.flag2 = flag2;
    }

    @Generated
    public void setFlag3(boolean flag3) {
        this.flag3 = flag3;
    }

    @Generated
    public void setFlag4(boolean flag4) {
        this.flag4 = flag4;
    }
}