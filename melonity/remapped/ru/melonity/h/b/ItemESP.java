// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import ru.melonity.Melonity;
import ru.melonity.f.RenderCallback;
import ru.melonity.f.RenderEvent;
import ru.melonity.f.Setting;
import ru.melonity.f.SettingType;
import ru.melonity.f.TextRenderer;
import ru.melonity.f.WorldUtils;
import ru.melonity.o.Category;
import ru.melonity.o.Module;
import ru.melonity.o.SettingList;

import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ItemESP extends Module {
    private final Setting itemSetting = new Setting("Items", SettingType.LIST, "Ores", "Ores", "Ancient Debris");
    private final RenderCallback<RenderEvent> renderCallback = event -> {
        if (!isEnabled()) {
            return;
        }
        Iterable<Entity> entities = getWorld().getEntities();
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (!(entity instanceof ItemEntity)) {
                continue;
            }
            ItemEntity itemEntity = (ItemEntity) entity;
            ItemStack itemStack = itemEntity.getStack();
            Item item = itemStack.getItem();
            if (!isTargetItem(item)) {
                continue;
            }
            double x = itemEntity.getX();
            double y = itemEntity.getY();
            double z = itemEntity.getZ();
            double[] screenPos = WorldUtils.toScreenPosition(x, y, z);
            if (screenPos == null) {
                continue;
            }
            float screenX = (float) screenPos[0];
            float screenY = (float) screenPos[1];
            TextRenderer textRenderer = Melonity.getTextRenderer();
            Text text = itemEntity.getName();
            String name = text.getString();
            Text displayText = itemEntity.getName();
            String displayName = displayText.getString();
            float nameWidth = textRenderer.getWidth(displayName);
            float adjustedX = screenX - nameWidth / 2.0f;
            MatrixStack matrixStack = event.getMatrixStack();
            textRenderer.draw(matrixStack, displayName, adjustedX, screenY + 10.0f);
        }
    };

    public ItemESP() {
        super("ItemESP", Category.WORLD);
        addSetting(itemSetting);
    }

    private boolean isTargetItem(Item item) {
        List<String> selectedOptions = itemSetting.getSelectedOptions();
        boolean oresEnabled = selectedOptions.contains("Ores");
        if (oresEnabled) {
            ItemStack stack = new ItemStack((ItemConvertible) item);
            Text text = item.getName(stack);
            String name = text.getString();
            String lowerName = name.toLowerCase();
            boolean isOre = lowerName.contains("ore");
            if (isOre) {
                return true;
            }
        }
        boolean ancientDebrisEnabled = selectedOptions.contains("Ancient Debris");
        return ancientDebrisEnabled && item == Items.ANCIENT_DEBRIS;
    }
}