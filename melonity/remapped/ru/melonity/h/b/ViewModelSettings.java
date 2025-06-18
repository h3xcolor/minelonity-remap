// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.h.c.ViewModelBase;
import ru.melonity.o.b.a.b.FloatSetting;
import ru.melonity.o.Category;

@Environment(EnvType.CLIENT)
public class ViewModelSettings extends ViewModelBase {
    private final FloatSetting leftXSetting = new FloatSetting("Left X", number -> String.format("%.1f", number.floatValue()), -1, 1, 0);
    private final FloatSetting leftYSetting = new FloatSetting("Left Y", number -> String.format("%.1f", number.floatValue()), -1, 1, 0);
    private final FloatSetting leftZSetting = new FloatSetting("Left Z", number -> String.format("%.1f", number.floatValue()), -1, 1, 0);
    private final FloatSetting rightXSetting = new FloatSetting("Right X", number -> String.format("%.1f", number.floatValue()), -1, 1, 0);
    private final FloatSetting rightYSetting = new FloatSetting("Right Y", number -> String.format("%.1f", number.floatValue()), -1, 1, 0);
    private final FloatSetting rightZSetting = new FloatSetting("Right Z", number -> String.format("%.1f", number.floatValue()), -1, 1, 0);
    public static int CONFIG_ID = 1436671989;

    public ViewModelSettings() {
        super("ViewModel", Category.VIEW_MODEL);
        addSetting(leftXSetting);
        addSetting(leftYSetting);
        addSetting(leftZSetting);
        addSetting(rightXSetting);
        addSetting(rightYSetting);
        addSetting(rightZSetting);
    }

    public FloatSetting getLeftXSetting() {
        return leftXSetting;
    }

    public FloatSetting getLeftYSetting() {
        return leftYSetting;
    }

    public FloatSetting getLeftZSetting() {
        return leftZSetting;
    }

    public FloatSetting getRightXSetting() {
        return rightXSetting;
    }

    public FloatSetting getRightYSetting() {
        return rightYSetting;
    }

    public FloatSetting getRightZSetting() {
        return rightZSetting;
    }
}