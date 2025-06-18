// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import ru.melonity.f.setting.BooleanSetting;
import ru.melonity.f.setting.EnumSetting;
import ru.melonity.f.setting.NumberSetting;
import ru.melonity.f.setting.Setting;
import ru.melonity.f.setting.SettingChangeListener;
import ru.melonity.o.Module;
import ru.melonity.o.ModuleCategory;
import ru.melonity.w.InventoryUtils;

@Environment(value = EnvType.CLIENT)
public class AutoTotem extends Module {
    private final NumberSetting healthThreshold = new NumberSetting("global.health", number -> String.format("%.1f", Float.valueOf(number.floatValue())), 1, 20, Float.valueOf(3.5f));
    private final EnumSetting<Condition> conditionsSetting = new EnumSetting<>("autototem.conditions", Condition.class, Condition.FALL, "autototem.conditions.fall", "autototem.conditions.fall", "autototem.conditions.crystal");
    private final BooleanSetting noBallSwitchSetting = new BooleanSetting("autototem.no_ball_switch", true);
    private final BooleanSetting absorptionSetting = new BooleanSetting("autototem.absorption", false);
    private int prevTotemSlot = -1;
    private final ItemStack totemItem = new ItemStack(Items.TOTEM_OF_UNDYING);
    private final SettingChangeListener<Boolean> onTickListener = setting -> {
        if (!isActive()) {
            return;
        }
        int totemSlot = InventoryUtils.findItemSlot(mc.player, Items.TOTEM_OF_UNDYING);
        ItemStack offhandStack = mc.player.getOffHandStack();
        Item offhandItem = offhandStack.getItem();
        boolean isTotemInOffhand = offhandItem.equals(Items.TOTEM_OF_UNDYING);
        ItemStack mainHandStack = mc.player.getMainHandStack();
        Item mainHandItem = mainHandStack.getItem();
        boolean isMainHandNotPotion = !(mainHandItem instanceof PotionItem);
        boolean shouldActivate = shouldActivateTotem();
        if (shouldActivate) {
            if (totemSlot >= 0 && !isTotemInOffhand) {
                mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND, offhandStack);
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND, mainHandStack);
                if (isMainHandNotPotion) {
                    mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND, offhandStack);
                    if (this.prevTotemSlot == -1) {
                        this.prevTotemSlot = totemSlot;
                    }
                }
            }
        } else if (this.prevTotemSlot >= 0) {
            mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND, offhandStack);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND, mainHandStack);
            if (isMainHandNotPotion) {
                mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND, offhandStack);
            }
            this.prevTotemSlot = -1;
        }
    };

    public AutoTotem() {
        super("AutoTotem", ModuleCategory.COMBAT);
        this.addSetting(this.healthThreshold);
        this.addSetting(this.conditionsSetting);
        this.addSetting(this.noBallSwitchSetting);
        this.addSetting(this.absorptionSetting);
    }

    private boolean isNearCrystal() {
        List<Condition> conditions = this.conditionsSetting.getSelected();
        if (!conditions.contains(Condition.CRYSTAL)) {
            return false;
        }
        return false;
    }

    private boolean shouldActivateTotem() {
        float health = mc.player.getHealth();
        if (this.absorptionSetting.getValue()) {
            health += mc.player.getAbsorptionAmount();
        }
        float threshold = this.healthThreshold.getValue().floatValue();
        if (threshold >= health) {
            return true;
        }
        boolean hasTotem = hasTotemInInventory();
        if (!hasTotem) {
            List<Condition> conditions = this.conditionsSetting.getSelected();
            if (conditions.contains(Condition.CRYSTAL)) {
                return !isNearCrystal();
            }
            if (conditions.contains(Condition.FALL)) {
                return mc.player.fallDistance >= 5.0f;
            }
        }
        return false;
    }

    public boolean hasTotemInInventory() {
        ItemStack offhandStack = mc.player.getOffHandStack();
        Text displayName = offhandStack.getName();
        String name = displayName.getString().toLowerCase();
        if (name.contains("шар")) {
            return true;
        }
        displayName = offhandStack.getName();
        name = displayName.getString().toLowerCase();
        if (name.contains("head")) {
            return true;
        }
        return false;
    }

    private int countTotemsInInventory() {
        int count = 0;
        PlayerInventory inventory = mc.player.getInventory();
        int size = inventory.size();
        for (int i = 0; i < size; i++) {
            ItemStack stack = inventory.getStack(i);
            Item item = stack.getItem();
            if (item.equals(Items.TOTEM_OF_UNDYING)) {
                count++;
            }
        }
        return count;
    }

    private boolean isEndCrystalAt(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        return state.getBlock().equals(Blocks.END_CRYSTAL);
    }

    private boolean isObsidianAt(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        return state.getBlock().equals(Blocks.OBSIDIAN);
    }

    private double distanceTo(Entity entity, BlockPos pos) {
        return distanceTo(entity.getX(), entity.getY(), entity.getZ(), pos.getX(), pos.getY(), pos.getZ());
    }

    private double distanceTo(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private enum Condition {
        FALL, CRYSTAL
    }
}