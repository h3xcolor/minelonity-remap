// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_634;

@Environment(EnvType.CLIENT)
public class AutoTravel extends ru.melonity.h.c.Module {
    private final CoordinateInput coordinateInput = new CoordinateInput("Go to coordinates", "X", "Y", "Z");

    public AutoTravel() {
        super("AutoTravel", ru.melonity.o.Category.MOVEMENT);
        this.initialize();
        this.addSetting(this.coordinateInput);
    }

    @Override
    public void onToggle(boolean enabled) {
        super.onToggle(enabled);
        class_310 client = class_310.method_1551();
        client.field_1724.field_3944.method_45729("#stop");
        if (enabled) {
            class_310 mc = class_310.method_1551();
            class_634 networkHandler = mc.field_1724.field_3944;
            int x = this.coordinateInput.getX();
            int y = this.coordinateInput.getY();
            int z = this.coordinateInput.getZ();
            networkHandler.method_45729("#goto " + x + " " + y + " " + z);
            ModuleHelper.completeAction(null);
        }
    }
}