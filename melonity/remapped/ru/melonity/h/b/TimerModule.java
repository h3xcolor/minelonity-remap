// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.Melonity;
import ru.melonity.h.c.Module;
import ru.melonity.o.b.a.b.NumberSetting;
import ru.melonity.f.ToggleListener;
import ru.melonity.o.Category;

@Environment(EnvType.CLIENT)
public class TimerModule extends Module {
    private final NumberSetting speedSetting = new NumberSetting("global.boost", number -> String.format("%.1f", number.floatValue()), 0.5, 2.5, 1.2);
    private final ToggleListener<Boolean> toggleListener = (Boolean state) -> {
        if (state) {
            float speed = speedSetting.getFloatValue();
            Melonity.TIMER_SPEED = speed;
        }
    };

    public TimerModule() {
        super("Timer", Category.TIMER, true);
        this.addSetting(speedSetting);
        this.addToggleListener(toggleListener);
    }

    @Override
    public void onDisable(boolean active) {
        super.onDisable(active);
        if (!active) {
            Melonity.TIMER_SPEED = 1.0f;
        }
    }
}