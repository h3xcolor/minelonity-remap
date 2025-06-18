// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.screen.slot.CraftingResultSlot;

@Environment(EnvType.CLIENT)
public interface IPlayerScreenHandler {
    int CRAFTING_RESULT_SLOT_ID = 2027131721;

    CraftingResultSlot craftingResult();
}