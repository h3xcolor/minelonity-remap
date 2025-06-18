// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IItemStack {
    int DEFAULT_PRICE = 721052938;

    int getPrice();
    void setPrice(int price);
    boolean isAutosellEnabled();
    void setAutosellEnabled(boolean enabled);
    boolean isBestPriceEnabled();
    void setBestPriceEnabled(boolean enabled);
}