// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import ru.melonity.h.c.Module;
import ru.melonity.client.event.EventListener;
import ru.melonity.client.event.events.UpdateEvent;
import ru.melonity.client.setting.FloatSetting;

@Environment(EnvType.CLIENT)
public class AutoGapple extends Module {
    private final FloatSetting healthThreshold;
    private boolean isUsingGapple;
    private final EventListener<UpdateEvent> updateListener = event -> {
        if (!this.isEnabled()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        boolean isPlayerAlive = client.player.isAlive();
        if (!isPlayerAlive) {
            if (this.isUsingGapple) {
                client.options.useKey.setPressed(false);
                this.isUsingGapple = false;
            }
            return;
        }

        ItemStack activeItemStack = client.player.getActiveItem();
        if (activeItemStack.isOf(Items.SHIELD)) {
            if (this.isUsingGapple) {
                client.options.useKey.setPressed(false);
                this.isUsingGapple = false;
            }
            return;
        }

        Item activeItem = activeItemStack.getItem();
        if (activeItem == Items.GOLDEN_APPLE) {
            float health = client.player.getHealth();
            float thresholdValue = healthThreshold.getValue().floatValue();
            if (health <= thresholdValue) {
                this.isUsingGapple = true;
                client.options.useKey.setPressed(true);
                return;
            }
        }

        if (this.isUsingGapple) {
            client.options.useKey.setPressed(false);
            this.isUsingGapple = false;
        }
    };

    public AutoGapple() {
        super("AutoGapple", Category.COMBAT);
        this.healthThreshold = new FloatSetting("global.health", 16.0f, 1.0f, 20.0f, value -> String.format("%.1f", value));
        this.addSetting(healthThreshold);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled && MinecraftClient.getInstance().options != null) {
            MinecraftClient.getInstance().options.useKey.setPressed(false);
            this.isUsingGapple = false;
        }
    }
}