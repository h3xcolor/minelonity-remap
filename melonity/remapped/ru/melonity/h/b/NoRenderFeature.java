// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import ru.melonity.f.*;
import ru.melonity.f.b.*;
import ru.melonity.o.Module;
import ru.melonity.o.b.a.b.MultiOptionSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import java.util.List;

@Environment(EnvType.CLIENT)
public class NoRenderFeature extends Module {
    private final MultiOptionSetting settings = new MultiOptionSetting("Removes", MultiOptionSetting.RenderCategory.TOGGLE, "Fire", "Bad Effects", "Totem", "View Bobbing", "Fire", "Thunder", "Gamma", "Boss Bar", "Chat Background");
    private boolean wasViewBobbingEnabled;
    private final SettingConsumer<Boolean> onSettingChange = value -> {
        if (!isEnabled()) return;

        List<String> selectedOptions = settings.getSelectedOptions();
        boolean removeBadEffects = selectedOptions.contains("Bad Effects");
        if (removeBadEffects) {
            Minecraft.getInstance().player.removeEffect(MobEffects.CONFUSION);
            Minecraft.getInstance().player.removeEffect(MobEffects.BLINDNESS);
        }

        boolean removeThunder = selectedOptions.contains("Thunder");
        if (removeThunder) {
            Minecraft.getInstance().level.setRainLevel(0.0f);
            Minecraft.getInstance().level.setThunderLevel(0.0f);
        }

        GameSettings gameSettings = Minecraft.getInstance().options;
        boolean disableViewBobbing = selectedOptions.contains("View Bobbing");
        gameSettings.bobView().set(!disableViewBobbing);
    };

    public NoRenderFeature() {
        super("NoRender", Module.Category.VISUAL);
        addSetting(settings);
    }

    @Override
    public void onToggle(boolean enabled) {
        super.onToggle(enabled);
        if (enabled) {
            wasViewBobbingEnabled = Minecraft.getInstance().options.bobView().get();
        } else {
            Minecraft.getInstance().options.bobView().set(wasViewBobbingEnabled);
        }
    }
}