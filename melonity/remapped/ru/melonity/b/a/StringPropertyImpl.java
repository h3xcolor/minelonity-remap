// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b.a;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.b.StringProperty;

@Environment(EnvType.CLIENT)
public class StringPropertyImpl implements StringProperty {
    private String value;
    public static int constantId = 1351134260;

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Generated
    public StringPropertyImpl(String value) {
        this.value = value;
    }
}