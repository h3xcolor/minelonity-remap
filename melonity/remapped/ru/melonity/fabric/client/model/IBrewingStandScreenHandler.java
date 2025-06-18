// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.screen.PropertyDelegate;

@Environment(EnvType.CLIENT)
public interface IBrewingStandScreenHandler {
    int BREWING_STAND_CONTAINER_ID = 1952010906;

    PropertyDelegate propertyDelegate();
}