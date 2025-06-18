// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.g;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2248;
import net.minecraft.class_2480;
import java.util.ArrayList;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public record ShulkerBoxData(class_1799 shulkerBox, int color, int slot, List<class_1799> items) {

    public static ShulkerBoxData createShulkerBoxData(class_1799 stack, int slot) {
        class_1792 item = stack.method_7909();
        if (item instanceof class_1747) {
            class_2248 block = ((class_1747) item).method_7711();
            if (block instanceof class_2480) {
                List<class_1799> items = new ArrayList<>();
                return new ShulkerBoxData(stack, -1, slot, items);
            }
        }
        return null;
    }
}