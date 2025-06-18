// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.Melonity;
import ru.melonity.f.Client;
import ru.melonity.h.b.c.ModuleSetting;
import ru.melonity.h.c.Module;
import ru.melonity.w.Timer;
import ru.melonity.o.Category;

@Environment(EnvType.CLIENT)
public class BlinkModule extends Module {
    private final ModuleSetting settings = new ModuleSetting();

    public BlinkModule() {
        super("Blink", Category.MOVEMENT);
    }

    @Override
    public void onToggle(boolean isEnabled) {
        super.onToggle(isEnabled);
        if (isEnabled) {
            Client client = Melonity.modules.getClient();
            client.registerSetting(this.settings);
            this.settings.reset();
        } else {
            this.settings.reset();
            Client client = Melonity.modules.getClient();
            client.unregisterSetting(this.settings);
            Timer.setCooldown(0.1);
        }
    }
}