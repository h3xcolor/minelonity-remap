// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1263;
import net.minecraft.class_1293;
import net.minecraft.class_1703;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1842;
import net.minecraft.class_1844;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_2561;
import net.minecraft.class_437;
import net.minecraft.class_6880;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import ru.melonity.f.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll;
import ru.melonity.f.b.IIlllIIllIlllIlllIIlllIIIlIllIIIlllIIlIIIlIlllIlllIIllIIIlIIllIIlIIlIllIIIllIIlllIIIllIIIllIIlllIIllIIllIIllIlllIlllIIIllIllIlIllIIlllIlllIIIlllIlllIlllIllIIllIIIllIIllIlllIIllIIllIIlllIIlIIlllIIIlIlIllIIlIIllIIIlIIlIIlIIIlllIIIllIIIllIIlIl;
import ru.melonity.fabric.client.model.IItemStack;
import ru.melonity.h.b.b.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;
import ru.melonity.o.b.a.b.IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll;
import ru.melonity.o.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;
import ru.metafaze.protection.annotation.Compile;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Environment(value = EnvType.CLIENT)
public class AutoBuyModule extends ru.melonity.h.c.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    private int currentPage = -1;
    private int lastClickedSlot = 0;
    private long lastClickTime = -1L;
    private boolean isUpdating = false;
    private class_1703 currentRecipe = null;
    private IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll guiElement = new IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll();
    private final ru.melonity.o.b.a.b.IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll delaySetting = new ru.melonity.o.b.a.b.IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll("global.delay", number -> String.format("%d", number.intValue()), 0, 3000, 200);
    private final CopyOnWriteArrayList<ItemPrice> itemPrices = Lists.newCopyOnWriteArrayList();
    public static boolean isModuleEnabled = false;
    public static boolean shouldRefresh = false;
    private int updateCooldown = 0;
    public static boolean isTransactionActive = false;
    public static Map<class_1799, Integer> priceMap = Maps.newLinkedHashMap();
    private static int suggestedPage = -1;
    private static int suggestedSlot = -1;
    private long lastUpdateTime = System.currentTimeMillis();
    private final IIlllIIllIlllIlllIIlllIIIlIllIIIlllIIlIIIlIlllIlllIIllIIIlIIllIIlIIlIllIIIllIIlllIIIllIIIllIIlllIIllIIllIIllIlllIlllIIIllIllIlIllIIlllIlllIIIlllIlllIlllIllIIllIIIllIIllIlllIIllIIllIIlllIIlIIlllIIIlIlIllIIlIIllIIIlIIlIIlIIIlllIIIllIIIllIIlIl settingAdder = this::addItemPrice;
    public static int obfRandom = 558254959;

    public AutoBuyModule() {
        super("AutoBuy", IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll.IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll);
        this.addItemPrice(this.delaySetting);
    }

    @Override
    public void onToggle(boolean isActive) {
        super.onToggle(isActive);
        if (isActive) {
            isModuleEnabled = true;
        }
    }

    @Compile(referenceCipherStrength = 3)
    private native void handleSetting(IIlllIIllIlllIlllIIlllIIIlIllIIIlllIIlIIIlIlllIlllIIllIIIlIIllIIlIIlIllIIIllIIlllIIIllIIIllIIlllIIllIIllIIllIlllIlllIIIllIllIlIllIIlllIlllIIIlllIlllIlllIllIIllIIIllIIllIlllIIllIIllIIlllIIlIIlllIIIlIlIllIIlIIllIIIlIIlIIlIIIlllIIIllIIIllIIlIl var1);

    private int findBestMatchingPrice(class_1799 targetItem) {
        int bestPrice = -1;
        Iterator<ItemPrice> iterator = this.itemPrices.iterator();
        Iterator<ItemPrice> priceIterator = iterator;
        while (priceIterator.hasNext()) {
            ItemPrice priceEntry = priceIterator.next();
            ItemPrice itemPrice = priceEntry;
            class_1799 sourceItem = itemPrice.item();
            Object object = sourceItem.method_57824(class_9334.field_49631);
            if (object != null) {
                class_2561 targetName = targetItem.method_7964();
                String targetText = targetName.getString().toLowerCase();
                class_2561 sourceName = sourceItem.method_7964();
                String sourceText = sourceName.getString().toLowerCase();
                if (targetText.contains(sourceText)) {
                }
            }
            int sourceCount = sourceItem.method_7947();
            if (sourceCount > 1) {
                int targetCount = targetItem.method_7947();
                int sourceStackSize = sourceItem.method_7947();
                if (targetCount != sourceStackSize) {
                    continue;
                }
            }
            if (bestPrice != -1) {
                class_1792 targetType = targetItem.method_7909();
                class_1799 priceItem = itemPrice.item();
                class_1792 sourceType = priceItem.method_7909();
                if (targetType != sourceType || itemPrice.priceValue() >= bestPrice) {
                    continue;
                }
            }
            bestPrice = itemPrice.priceValue();
        }
        return bestPrice;
    }

    private boolean isItemWanted(class_1799 item) {
        for (Map.Entry<class_1799, Integer> entry : priceMap.entrySet()) {
            class_1799 keyItem = entry.getKey();
            class_1792 keyItemType = keyItem.method_7909();
            class_1792 itemType = item.method_7909();
            boolean isSameType = keyItemType.equals(itemType);
            if (!isSameType) {
                return false;
            }
            Object object = keyItem.method_57824(class_9334.field_49631);
            if (object != null) {
                class_2561 itemName = item.method_7964();
                String itemText = itemName.getString().toLowerCase();
                class_2561 priceName = keyItem.method_7964();
                String priceText = priceName.getString().toLowerCase();
                if (!itemText.contains(priceText)) {
                    return false;
                }
            }
            int keyCount = keyItem.method_7947();
            if (keyCount > 1) {
                int itemCount = item.method_7947();
                int stackSize = keyItem.method_7947();
                if (itemCount != stackSize) {
                    return false;
                }
            }
            class_9304 keyAttributes = class_1890.method_57532(keyItem);
            Set set = keyAttributes.method_57534();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                class_6880 attribute = iterator.next();
                Object attributeValue = attribute.comp_349();
                class_2561 attrName = ((class_1887) attributeValue).comp_2686().getString();
                class_2561 itemAttrName = ((class_1887) object).comp_2686().getString();
                if (attrName.equals(itemAttrName)) {
                    return true;
                }
            }
            break;
        }
        return true;
    }

    private boolean shouldPurchaseItem(class_1799 item, int itemSlot, int slotLimit) {
        IItemStack stackWrapper = (IItemStack)item;
        boolean isAutosell = stackWrapper.autosell();
        if (isAutosell) {
            return false;
        }
        for (Map.Entry<class_1799, Integer> entry : priceMap.entrySet()) {
            class_1799 keyItem = entry.getKey();
            class_1792 keyItemType = keyItem.method_7909();
            class_1792 itemType = item.method_7909();
            boolean typeMatch = keyItemType.equals(itemType);
            if (!typeMatch) {
                continue;
            }
            Object enchantTag = keyItem.method_57824(class_9334.field_49631);
            if (enchantTag != null) {
                class_2561 itemName = item.method_7964();
                String itemText = itemName.getString().toLowerCase();
                class_2561 keyName = keyItem.method_7964();
                String keyText = keyName.getString().toLowerCase();
                if (!itemText.contains(keyText)) {
                    continue;
                }
            }
            int keyCount = keyItem.method_7947();
            if (keyCount > 1) {
                int itemCount = item.method_7947();
                int stackSize = keyItem.method_7947();
                if (itemCount != stackSize) {
                    continue;
                }
            }
            class_9304 keyAttributes = class_1890.method_57532(keyItem);
            Set attributes = keyAttributes.method_57534();
            Iterator iterator = attributes.iterator();
            while (iterator.hasNext()) {
                class_6880 attribute = iterator.next();
                Object attributeValue = attribute.comp_349();
                boolean attributeMatch = this.compareAttributes(item, (class_1887) attributeValue);
                if (!attributeMatch) {
                    return false;
                }
            }
            Optional optional = keyItem.method_57825(class_9334.field_49651, class_1844.field_49274);
            if (!optional.isPresent()) {
                continue;
            }
            class_6880 effectAttribute = optional.get();
            Object effectValue = effectAttribute.comp_349();
            List effects = ((class_1842) effectValue).method_8049();
            Iterator effectIterator = effects.iterator();
            while (effectIterator.hasNext()) {
                class_1293 effect = effectIterator.next();
                boolean effectMatch = this.compareEffects(item, effect);
                if (!effectMatch) {
                    return false;
                }
            }
            int price = entry.getValue();
            if (slotLimit == Integer.MAX_VALUE) {
                return true;
            }
            IItemStack priceItem = stackWrapper;
            boolean isBestPrice = priceItem.bestPrice();
            if (isBestPrice) {
                int foundPrice = this.findBestMatchingPrice(keyItem);
                if (foundPrice != -1) {
                    price = foundPrice;
                }
            }
            if (slotLimit < itemSlot) {
                return false;
            }
            return itemSlot <= price;
        }
        return false;
    }

    private boolean compareAttributes(class_1799 item, class_1887 attribute) {
        class_9304 attributes = class_1890.method_57532(item);
        Set set = attributes.method_57534();
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            class_6880 attr = it.next();
            Object attrValue = attr.comp_349();
            class_2561 attrName = ((class_1887) attrValue).comp_2686().getString();
            class_2561 compareName = attribute.comp_2686().getString();
            if (attrName.equals(compareName)) {
                return true;
            }
        }
        return false;
    }

    private boolean compareEffects(class_1799 item, class_1293 effect) {
        Object obj = item.method_57825(class_9334.field_49651, class_1844.field_49274);
        Optional optionalEffect = obj.comp_2378();
        if (optionalEffect.isPresent()) {
            class_6880 effectAttr = optionalEffect.get();
            Object effectValue = effectAttr.comp_349();
            List effects = ((class_1842) effectValue).method_8049();
            for (Iterator it = effects.iterator(); it.hasNext(); ) {
                class_1293 e = it.next();
                String effectName = e.method_5586();
                String compareName = effect.method_5586();
                if (effectName.equals(compareName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int findNextPageButton(class_1263 handler) {
        for (int i = 0; i < handler.method_5439(); i++) {
            class_1799 stack = handler.method_5438(i);
            if (stack.method_7960()) continue;
            class_2561 name = stack.method_7964();
            String text = name.getString().toLowerCase();
            if (text.contains("\u043f\u0440\u0435\u0434\u044b\u0434\u0443\u0449\u0430\u044f")) {
                return i;
            }
        }
        return -1;
    }

    private int findUpdateButton(class_1263 handler) {
        for (int i = 0; i < handler.method_5439(); i++) {
            class_1799 stack = handler.method_5438(i);
            if (stack.method_7960()) continue;
            class_2561 name = stack.method_7964();
            String text = name.getString().toLowerCase();
            if (text.contains("\u043e\u0431\u043d\u043e\u0432\u0438\u0442\u044c") || text.equals("[?] \u041e\u0431\u043d\u043e\u0432\u0438\u0442\u044c")) {
                return i;
            }
        }
        return -1;
    }

    private int findNextButton(class_1263 handler) {
        for (int i = 0; i < handler.method_5439(); i++) {
            class_1799 stack = handler.method_5438(i);
            if (stack.method_7960()) continue;
            class_2561 name = stack.method_7964();
            String text = name.getString().toLowerCase();
            if (text.contains("\u0441\u043b\u0435\u0434\u0443\u044e\u0449\u0430\u044f") || text.contains("\u0441\u043b\u0435\u0434\u0443\u0449\u0430\u044f")) {
                return i;
            }
        }
        return -1;
    }

    private static  String proxy_$2(int n) {
        return "/ah sell " + n;
    }

    @Environment(value = EnvType.CLIENT)
    private record ItemPrice(class_1799 item, int priceValue) {
        public static int obfRecord = 1506177476;

        @Override
        public String toString() {
            return "ItemPrice{item=" + this.item + ", priceValue=" + this.priceValue + "}";
        }

        @Override
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap("hashCode", new MethodHandle[]{ItemPrice.class, "item;priceValue", "item", "priceValue"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean) ObjectMethods.bootstrap("equals", new MethodHandle[]{ItemPrice.class, "item;priceValue", "item", "priceValue"}, this, object);
        }
    }
}