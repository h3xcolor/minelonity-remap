// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import java.util.Iterator;
import java.util.function.Predicate;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1796;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_2868;
import net.minecraft.class_310;
import net.minecraft.class_338;
import net.minecraft.class_5250;
import net.minecraft.class_9779;
import ru.melonity.fabric.client.model.IMinecraft;

@Environment(value=EnvType.CLIENT)
public final class InventoryUtils {
    public static int findHotbarSlotByPredicate(class_1657 player, Predicate<class_1799> predicate) {
        for (int slot = 0; slot < 9; ++slot) {
            class_1799 stack = player.method_31548().method_5438(slot);
            if (predicate.test(stack)) {
                return slot;
            }
        }
        return -1;
    }

    public static int findHotbarSlotByItem(class_1657 player, class_1792 item) {
        for (int slot = 0; slot < 9; ++slot) {
            class_1799 stack = player.method_31548().method_5438(slot);
            class_1792 stackItem = stack.method_7909();
            if (stackItem.equals(item)) {
                return slot;
            }
        }
        return -1;
    }

    public static int findHotbarSlotByItemName(class_1657 player, String name) {
        for (int slot = 0; slot < 9; ++slot) {
            class_1799 stack = player.method_31548().method_5438(slot);
            class_2561 text = stack.method_7964();
            String stackName = text.getString();
            if (stackName.equalsIgnoreCase(name)) {
                return slot;
            }
        }
        return -1;
    }

    public static int findHotbarSlotByItemClass(class_1657 player, Class<? extends class_1792> itemClass) {
        for (int slot = 0; slot < 9; ++slot) {
            class_1799 stack = player.method_31548().method_5438(slot);
            class_1792 stackItem = stack.method_7909();
            Class<?> stackClass = stackItem.getClass();
            if (stackClass.equals(itemClass)) {
                return slot;
            }
        }
        return -1;
    }

    public static void swapSlots(int fromSlot, int toSlot, boolean completeSwap) {
        if (fromSlot == toSlot) {
            return;
        }
        clickSlot(fromSlot, 0);
        clickSlot(toSlot, 0);
        if (completeSwap) {
            clickSlot(fromSlot, 0);
        }
    }

    public static void swapSlots(int slot1, int slot2) {
        if (slot1 == slot2) {
            return;
        }
        clickSlot(slot1, 0);
        clickSlot(slot2, 0);
        clickSlot(slot1, 0);
    }

    public static void clickSlot(int slot, int button) {
        class_310.method_1551().field_1761.method_2906(class_310.method_1551().field_1724.field_7512.field_7763, slot, button, class_1713.field_7790, class_310.method_1551().field_1724);
    }

    public static int findSlotByItem(class_1657 player, class_1792 item) {
        Iterable<class_1799> iterable = player.method_5661();
        Iterator<class_1799> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            class_1799 stack = iterator.next();
            class_1792 stackItem = stack.method_7909();
            if (stackItem.equals(item)) {
                return -1;
            }
        }
        for (int slot = 0; slot < 9; ++slot) {
            class_1799 stack = player.method_31548().method_5438(slot);
            class_1792 stackItem = stack.method_7909();
            if (stackItem.equals(item)) {
                return 36 + slot;
            }
        }
        int containerSize = player.method_31548().method_5439();
        for (int slot = 0; slot < containerSize; ++slot) {
            class_1799 stack = player.method_31548().method_5438(slot);
            class_1792 stackItem = stack.method_7909();
            if (stackItem == item) {
                return slot;
            }
        }
        return -1;
    }

    public static int countItem(class_1657 player, class_1792 item) {
        int count = 0;
        int containerSize = player.method_31548().method_5439();
        for (int slot = 0; slot < containerSize; ++slot) {
            class_1799 stack = player.method_31548().method_5438(slot);
            class_1792 stackItem = stack.method_7909();
            if (stackItem == item) {
                count += stack.method_7947();
            }
        }
        return count;
    }

    public static int findInventorySlotByItemStack(class_1657 player, class_1799 targetStack) {
        int containerSize = player.method_31548().method_5439();
        for (int slot = 0; slot < containerSize; ++slot) {
            class_1799 stack = player.method_31548().method_5438(slot);
            class_1792 stackItem = stack.method_7909();
            class_1792 targetItem = targetStack.method_7909();
            if (stackItem == targetItem) {
                return slot;
            }
        }
        return -1;
    }

    public static int findSlotByPredicate(class_1657 player, Predicate<class_1799> predicate) {
        int containerSize = player.method_31548().method_5439();
        for (int slot = 0; slot < containerSize; ++slot) {
            class_1799 stack = player.method_31548().method_5438(slot);
            if (predicate.test(stack)) {
                return slot;
            }
        }
        return -1;
    }

    public static int findSlotByItemClass(class_1657 player, Class<? extends class_1792> itemClass) {
        int containerSize = player.method_31548().method_5439();
        for (int slot = 0; slot < containerSize; ++slot) {
            class_1799 stack = player.method_31548().method_5438(slot);
            class_1792 stackItem = stack.method_7909();
            Class<?> stackClass = stackItem.getClass();
            if (stackClass.equals(itemClass)) {
                return slot;
            }
        }
        return -1;
    }

    public static int findEmptySlotInInventory(class_1657 player) {
        int containerSize = player.method_31548().method_5439();
        for (int slot = 9; slot < containerSize; ++slot) {
            class_1799 stack = player.method_31548().method_5438(slot);
            if (stack.method_7960()) {
                return slot;
            }
        }
        return -1;
    }

    public static void useItemInSlot(int slot) {
        class_310 mc = class_310.method_1551();
        class_1661 inventory = mc.field_1724.method_31548();
        class_1796 hand = mc.field_1724.method_7357();
        class_1799 stack = inventory.method_5438(slot);
        class_1792 item = stack.method_7909();
        if (hand.method_7904(item)) {
            class_338 overlay = mc.field_1705.method_1743();
            class_1796 handStack = mc.field_1724.method_7357();
            class_1799 slotStack = inventory.method_5438(slot);
            class_1792 slotItem = slotStack.method_7909();
            class_9779 timer = mc.method_60646();
            float progress = timer.method_60637(true);
            float useTime = handStack.method_7905(slotItem, progress);
            int seconds = (int)(useTime * 20.0f);
            String message = "\u00a7c\u041f\u0440\u0435\u0434\u043c\u0435\u0442 \u0432 \u043a\u0443\u043b\u0434\u0430\u0443\u043d\u0435: %d\u0441".formatted(seconds);
            class_5250 text = class_2561.method_43470(message);
            overlay.method_1812(text);
            return;
        }
        int currentSlot = inventory.field_7545;
        inventory.field_7545 = slot;
        mc.field_1724.field_3944.method_52787(new class_2868(slot));
        ((IMinecraft)mc).tryUseItem();
        inventory.field_7545 = currentSlot;
        mc.field_1724.field_3944.method_52787(new class_2868(currentSlot));
    }

    public static boolean useItemInSlotWithHotbarSwap(int slot) {
        class_310 mc = class_310.method_1551();
        class_1661 inventory = mc.field_1724.method_31548();
        class_1796 hand = mc.field_1724.method_7357();
        class_1799 stack = inventory.method_5438(slot);
        class_1792 item = stack.method_7909();
        if (hand.method_7904(item)) {
            class_338 overlay = mc.field_1705.method_1743();
            class_1796 handStack = mc.field_1724.method_7357();
            class_1799 slotStack = inventory.method_5438(slot);
            class_1792 slotItem = slotStack.method_7909();
            class_9779 timer = mc.method_60646();
            float progress = timer.method_60637(true);
            float useTime = handStack.method_7905(slotItem, progress);
            int seconds = (int)(useTime * 20.0f);
            String message = "\u00a7c\u041f\u0440\u0435\u0434\u043c\u0435\u0442 \u0432 \u043a\u0443\u043b\u0434\u0430\u0443\u043d\u0435: %d\u0441".formatted(seconds);
            class_5250 text = class_2561.method_43470(message);
            overlay.method_1812(text);
            return false;
        }
        int emptySlot = findEmptySlotInInventory(mc.field_1724);
        if (emptySlot == -1) {
            class_338 overlay = mc.field_1705.method_1743();
            class_5250 text = class_2561.method_43470("\u00a7c\u041d\u0435\u0442\u0443 \u0441\u0432\u043e\u0431\u043e\u0434\u043d\u043e\u0433\u043e \u0441\u043b\u043e\u0442\u0430 \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435");
            overlay.method_1812(text);
            return false;
        }
        if (IllIllIlIIlllIIIlllIIIllIIIlIlIIIlllIIlIIllIIllIllIIIlIIIlllIIllIIIllIIllIIlllIlllIlIllIIlllIIllIIllIlIlllIIIllIIllIlllIIlllIIIlIIllIIIlllIlIIlIIlIllIlllIIllIllIIIllIIllIIIllIIllIIllIIllIIlIIlIIllIIIlIIIllIIlllIIIllIlIlllIllIIlIIIlllIll.isUsingItem()) {
            return false;
        }
        int currentHotbarSlot = inventory.field_7545;
        int hotbarSlot = 36 + currentHotbarSlot;
        new Thread(() -> {
            try {
                class_1799 hotbarStack = inventory.method_5438(hotbarSlot);
                boolean hotbarEmpty = hotbarStack.method_7960();
                if (!hotbarEmpty) {
                    mc.field_1761.method_2906(0, hotbarSlot, 0, class_1713.field_7790, mc.field_1724);
                    Thread.sleep(25L);
                    mc.field_1761.method_2906(0, emptySlot, 0, class_1713.field_7790, mc.field_1724);
                }
                Thread.sleep(25L);
                mc.field_1761.method_2906(0, slot, 0, class_1713.field_7790, mc.field极4);
                Thread.sleep(25L);
                class_1269 useResult = mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
                if (useResult.method_23665()) {
                    if (useResult.method_23666()) {
                        mc.field_1724.method_6104(class_1268.field_5808);
                    }
                }
                Thread.sleep(25L);
                mc.field_1761.method_2906(0, hotbarSlot, 0, class_1713.field_7790, mc.field_1724);
                Thread.sleep(25L);
                mc.field_1761.method_2906(0, slot, 0, class_1713.field_7790, mc.field_1724);
                Thread.sleep(35L);
                if (!hotbarEmpty) {
                    mc.field_1761.method_2906(0, emptySlot, 0, class_1713.field_7790, mc.field_1724);
                    Thread.sleep(25L);
                    mc.field_1761.method_2906(0, hotbarSlot, 0, class_1713.field_7790, mc.field_1724);
                }
            }
            catch (InterruptedException ignored) {
            }
        }).start();
        return true;
    }

    public static boolean useItem(class_1792 item) {
        class_310 mc = class_310.method_1551();
        int slot = findSlotByItem(mc.field_1724, item);
        if (slot != -1) {
            useItemInSlot(slot);
            return true;
        }
        int hotbarSlot = findHotbarSlotByItem(mc.field_1724, item);
        if (hotbarSlot != -1) {
            return useItemInSlotWithHotbarSwap(hotbarSlot);
        }
        return false;
    }

    public static boolean useItemByPredicate(Predicate<class_1799> predicate) {
        class_310 mc = class_310.method_1551();
        int slot = findSlotByPredicate(mc.field_1724, predicate);
        if (slot != -1) {
            useItemInSlot(slot);
            return true;
        }
        int hotbarSlot = findHotbarSlotByPredicate(mc.field_1724, predicate);
        if (hotbarSlot != -1) {
            return useItemInSlotWithHotbarSwap(hotbarSlot);
        }
        return false;
    }

    @Generated
    private InventoryUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}