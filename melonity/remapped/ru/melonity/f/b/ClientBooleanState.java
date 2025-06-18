// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.f.BooleanState;

@Environment(value = EnvType.CLIENT)
public class ClientBooleanState implements BooleanState {
    private boolean value;
    public static int configId = 1694728650;

    @Generated
    public ClientBooleanState(boolean value) {
        this.value = value;
    }

    @Generated
    public boolean getValue() {
        return this.value;
    }

    @Generated
    public void setValue(boolean value) {
        this.value = value;
    }
}