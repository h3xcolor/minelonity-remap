// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.m.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.json.JSONObject;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.s.d.TextField;
import ru.melonity.s.c.DrawHelper;
import ru.melonity.w.HotbarUtil;
import ru.melonity.m.Action;
import ru.melonity.m.ActionType;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class UseItemAction implements Action {
    private final FrameWeightCalculator animationTimer = FrameWeightCalculator.milliseconds(300L);
    private final StateAnimation stateAnimation = new StateAnimation();
    private boolean shouldRefreshItemList = true;
    private Item selectedItem = null;
    private String selectedItemName = "";
    private final TextField itemTextField = new TextField("Item", false, 156.0f, 16.0f);

    public UseItemAction() {
        this.itemTextField.setTextColor(Color.WHITE);
        this.itemTextField.setSuggestionProvider((String input) -> {
            if (this.shouldRefreshItemList) {
                this.selectedItemName = null;
                boolean exactMatchFound = false;
                Set<Map.Entry<Identifier, Item>> entries = Registries.ITEM.getEntrySet();
                Iterator<Map.Entry<Identifier, Item>> iterator = entries.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Identifier, Item> entry = iterator.next();
                    Item item = entry.getValue();
                    ItemStack stack = new ItemStack(item);
                    Text itemName = item.getName(stack);
                    String nameString = itemName.getString();
                    String lowerName = nameString.toLowerCase();
                    String lowerInput = input.toLowerCase();
                    boolean startsWith = lowerName.startsWith(lowerInput);
                    if (startsWith) {
                        Text exactItemName = item.getName(stack);
                        this.selectedItemName = exactItemName.getString();
                        this.selectedItem = item;
                        exactMatchFound = true;
                        continue;
                    }
                    boolean contains = nameString.contains(input);
                    if (!contains || exactMatchFound) continue;
                    Text fallbackItemName = item.getName(stack);
                    this.selectedItemName = fallbackItemName.getString();
                    this.selectedItem = item;
                }
                this.shouldRefreshItemList = false;
            }
            return this.selectedItemName;
        });
        this.itemTextField.setItemProvider((String string) -> {
            if (this.selectedItemName != null && this.selectedItem != null) {
                ItemStack stack = new ItemStack(this.selectedItem);
                return stack;
            }
            return null;
        });
    }

    @Override
    public String getActionName() {
        return "Use item";
    }

    @Override
    public ActionType getType() {
        return ActionType.USE_ITEM;
    }

    @Override
    public float getWidth() {
        return 0.0f;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", "use_item");
        json.put("item", this.itemTextField.getText());
        return json;
    }

    public static UseItemAction fromJson(JSONObject json) {
        UseItemAction action = new UseItemAction();
        action.itemTextField.setText(json.getString("item"));
        action.shouldRefreshItemList = true;
        action.selectedItemName = action.itemTextField.getText();
        return action;
    }

    @Override
    public void render(float x, float y, float mouseX, float mouseY, int width, int height, DrawHelper drawHelper, MatrixStack matrices) {
        DrawHelper.ColorSet colors = Melonity.getInstance().getConfigManager().getConfig().getColorSet();
        drawHelper.drawRoundedRect(x - 0.5f + 1.0f, y + 10.0f - 0.5f, mouseX + 1.0f - 2.0f, 30.0f, 8.0f, 0.0f, 0.0f, 8.0f, colors.getPrimary(), colors.getPrimary(), colors.getPrimary(), colors.getPrimary(), matrices);
        drawHelper.drawRoundedRect(x + 1.0f, y + 10.0f, mouseX - 2.0f, 29.0f, 8.0f, 0.0f, 0.0f, 8.0f, colors.getSecondary(), colors.getSecondary(), colors.getSecondary(), colors.getSecondary(), matrices);
        this.itemTextField.render(Melonity.getInstance().getConfigManager().getConfig().getColorSet(), x + 4.0f, y + 20.0f, width, height, drawHelper, matrices);
    }

    @Override
    public void renderOverlay(float x, float y, double mouseX, double mouseY, int id) {
        this.itemTextField.renderTooltip(x + 4.0f, y + 20.0f, this.itemTextField.getText(), this.itemTextField.getSuggestion(), mouseX, mouseY);
        this.itemTextField.renderOverlay(x + 4.0f, y + 20.0f, mouseX, mouseY, id);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        this.shouldRefreshItemList = true;
        return this.itemTextField.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onRemoved() {
        this.itemTextField.onRemoved();
    }

    @Override
    public float getHeight() {
        return 25.0f;
    }

    @Override
    public StateAnimation getStateAnimation() {
        return this.stateAnimation;
    }

    @Override
    public FrameWeightCalculator getFrameWeightCalculator() {
        return this.animationTimer;
    }

    @Override
    public void run() {
        Item targetItem = null;
        Set<Map.Entry<Identifier, Item>> entries = Registries.ITEM.getEntrySet();
        Iterator<Map.Entry<Identifier, Item>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Identifier, Item> entry = iterator.next();
            Item item = entry.getValue();
            ItemStack stack = new ItemStack(item);
            Text itemName = item.getName(stack);
            String nameString = itemName.getString();
            String currentItemName = this.itemTextField.getText();
            boolean exactMatch = nameString.equalsIgnoreCase(currentItemName);
            if (exactMatch) {
                targetItem = item;
                continue;
            }
            boolean startsWith = nameString.startsWith(currentItemName);
            if (!startsWith) continue;
            targetItem = item;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        int slot = HotbarUtil.findItemInHotbar(client.player, targetItem);
        if (slot == -1) {
            PlayerEntity player = client.player;
            String itemName = this.itemTextField.getText();
            slot = HotbarUtil.findItemInHotbarByName(player, itemName);
        }
        if (slot == -1) {
            CreativeInventoryScreen.CreativeSlot creativeSlot = client.player.currentScreenHandler.slots.get(0);
            ItemStack stack = new ItemStack(targetItem);
            Text itemName = targetItem.getName(stack);
            String name = itemName.getString();
            Text message = Text.of("Item not found in hotbar: " + name);
            client.inGameHud.getChatHud().addMessage(message);
        } else {
            ClientPlayerInteractionManager interactionManager = client.interactionManager;
            interactionManager.pickFromInventory(slot);
            interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            client.player.swingHand(Hand.MAIN_HAND);
            ClientPlayerEntity player = client.player;
            BlockHitResult hitResult = new BlockHitResult(Vec3d.ZERO, Direction.DOWN, BlockPos.ORIGIN, false);
            interactionManager.interactBlock(player, Hand.MAIN_HAND, hitResult);
        }
    }
}