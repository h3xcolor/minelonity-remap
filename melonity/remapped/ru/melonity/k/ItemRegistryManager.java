// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.k;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import java.util.Map;

@Environment(EnvType.CLIENT)
public interface ItemRegistryManager {
    int REGISTRY_VERSION = 2034687586;

    Map<Integer, Item> getRegistry(String name);

    ImmutableList<ItemRegistryEntry> getRegistryEntries();

    ItemRegistryEntry createEntry();

    void addEntry(ItemRegistryEntry entry);

    void registerRegistry(String name, Map<Integer, Item> registry);
}