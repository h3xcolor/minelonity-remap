// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import ru.melonity.fabric.client.model.IItemStack;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public class ItemStackMixin implements IItemStack {
    private int price = -1;
    private boolean autoSell = false;
    private boolean bestPrice = false;

    @Override
    public int price() {
        return this.price;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean autosell() {
        return this.autoSell;
    }

    @Override
    public void setAutosell(boolean autoSell) {
        this.autoSell = autoSell;
    }

    @Override
    public boolean bestPrice() {
        return this.bestPrice;
    }

    @Override
    public void setBestPrice(boolean bestPrice) {
        this.bestPrice = bestPrice;
    }
}