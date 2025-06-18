// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.k;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import java.util.Map;

@Environment(value = EnvType.CLIENT)
public class ItemGroupData {
    public String groupName;
    public Map<Integer, Item> itemsByIndex;
    public static int GROUP_ID = 695126025;

    public String getGroupName() {
        return this.groupName;
    }

    public Map<Integer, Item> getItemsByIndex() {
        return this.itemsByIndex;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setItemsByIndex(Map<Integer, Item> itemsByIndex) {
        this.itemsByIndex = itemsByIndex;
    }

    public ItemGroupData(String groupName, Map<Integer, Item> itemsByIndex) {
        this.groupName = groupName;
        this.itemsByIndex = itemsByIndex;
    }
}