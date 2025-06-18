// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.hack.module.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import ru.melonity.framework.event.Listener;
import ru.melonity.framework.event.events.MouseEvent;
import ru.melonity.hack.module.Module;
import ru.melonity.hack.module.Category;
import ru.melonity.hack.util.InventoryUtils;

@Environment(EnvType.CLIENT)
public class MiddleClickPearl extends Module {
    private final Listener<MouseEvent> listener = event -> {
        if (!isEnabled()) {
            return;
        }
        int slot = InventoryUtils.getSlotWithItem(mc.player, Items.ENDER_PEARL);
        if (slot != -1) {
            InventoryUtils.swapToHotbar(slot);
        }
    };

    public MiddleClickPearl() {
        super("MiddleClickPearl", Category.COMBAT);
    }
}