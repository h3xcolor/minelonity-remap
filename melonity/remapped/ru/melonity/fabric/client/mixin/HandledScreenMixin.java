// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.melonity.Melonity;
import ru.melonity.animation.Easings;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.f.b.InventorySortEvent;
import ru.melonity.h.b.InventoryManager;
import ru.melonity.h.c.InventoryUtils;
import ru.melonity.k.SortableItem;
import ru.melonity.w.InventorySortButton;
import ru.melonity.w.SortableItemFilter;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Unique
    private StateAnimation animation = new StateAnimation();
    @Unique
    private FrameWeightCalculator frameWeightCalculator = FrameWeightCalculator.milliseconds(300L);
    @Unique
    private boolean closing = false;
    @Shadow
    private ItemStack cursorStack;
    @Shadow
    @Final
    protected ScreenHandler handler;
    @Shadow
    @Final
    protected Set<Slot> cursorDragSlots;
    @Shadow
    private int dragSlot;
    @Shadow
    protected boolean isDragging;
    @Shadow
    private boolean isQuickCrafting;
    @Shadow
    protected int x;
    @Shadow
    protected int y;
    @Shadow
    private ItemStack quickCraftingItem;
    @Shadow
    private long quickCraftingTime;
    @Shadow
    private Slot quickCraftingSlot;
    @Shadow
    private int quickCraftingStartX;
    @Shadow
    private int quickCraftingStartY;

    @Shadow
    protected void renderBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

    @Shadow
    private void drawItem(DrawContext context, ItemStack stack, int x, int y, String amountText) {
    }

    @Shadow
    public void drawMouseoverTooltip(DrawContext context, int mouseX, int mouseY) {
    }

    @Shadow
    private boolean isPointOverSlot(Slot slot, double pointX, double pointY) {
        return false;
    }

    @Shadow
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlotHighlight(Lnet/minecraft/client/gui/DrawContext;III)V", ordinal = 0), cancellable = true)
    public void onRenderDrawSlotHighlight(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if ((Screen) this instanceof CreativeInventoryScreen) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V", ordinal = 0), cancellable = true)
    public void onRenderSuperRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if ((Screen) this instanceof CreativeInventoryScreen) {
            ci.cancel();
        }
    }

    @Inject(method = "close", at = @At("HEAD"), cancellable = true)
    public void onClose(CallbackInfo ci) {
        this.closing = true;
        ci.cancel();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V", shift = At.Shift.AFTER))
    public void onRenderReturn(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Melonity.getEventBus().post(new InventorySortEvent(context.getMatrices(), context));
    }

    @Inject(method = "drawSlot", at = @At("HEAD"))
    protected void onDrawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        if (!slot.hasStack()) {
            return;
        }
        Melonity.getEventBus().post(slot.getIndex(), slot.x, slot.y, 16.0f, 0.0f, context.getMatrices());
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void onRenderStart(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        HandledScreen screen = (HandledScreen) (Object) this;
        if (this.animation == null) {
            this.animation = new StateAnimation();
        }
        if (this.frameWeightCalculator == null) {
            this.frameWeightCalculator = FrameWeightCalculator.milliseconds(300L);
        }
        this.animation.state(!this.closing);
        this.animation.update(this.frameWeightCalculator.elapsedUnits());
        float animationProgress = (float) Easings.QUART_BOTH.ease(this.animation.value());
        if (!this.closing && this.animation.value() < 0.25) {
            this.animation.value(0.25);
        }
        MatrixStack matrices = context.getMatrices();
        MinecraftClient client = MinecraftClient.getInstance();
        if (screen instanceof CreativeInventoryScreen) {
            this.drawMouseoverTooltip(context, mouseX, mouseY);
        }
        if (this.closing && (animationProgress <= 0.1f || !(screen instanceof CreativeInventoryScreen))) {
            this.animation.reset();
            client.player.closeHandledScreen();
            client.setScreen(null);
        }
        for (int i = 0; i < this.handler.slots.size(); ++i) {
            Slot slot = this.handler.slots.get(i);
            if (!this.isPointOverSlot(slot, mouseX, mouseY) || !slot.isEnabled() || !((SortableItem) Melonity.getFeatureManager().getFeature(InventoryManager.class).get()).isSortingEnabled() || GLFW.glfwGetMouseButton(client.getWindow().getHandle(), 0) != 1 || GLFW.glfwGetKey(client.getWindow().getHandle(), 340) != 1 || slot.getStack().getItem() == Items.AIR) continue;
            this.onMouseClick(slot, slot.id, 0, SlotActionType.PICKUP);
        }
        if (screen instanceof CreativeInventoryScreen) {
            ItemStack stackToDraw;
            matrices.push();
            matrices.translate(client.getWindow().getScaledWidth() / 2.0f, client.getWindow().getScaledHeight() / 2.0f, 0.0f);
            matrices.scale(animationProgress, animationProgress, animationProgress);
            matrices.translate(-(client.getWindow().getScaledWidth() / 2.0f), -(client.getWindow().getScaledHeight() / 2.0f), 0.0f);
            InventorySortButton.renderButton(context, screen, client, mouseX, mouseY, this.x, this.y);
            ItemStack currentStack = stackToDraw = this.cursorStack.isEmpty() ? this.handler.getCursorStack() : this.cursorStack;
            if (!stackToDraw.isEmpty()) {
                int yOffset = this.cursorStack.isEmpty() ? 8 : 16;
                String amountText = null;
                if (!this.cursorStack.isEmpty() && this.isQuickCrafting) {
                    stackToDraw = stackToDraw.copyWithCount(MathHelper.floor((float) stackToDraw.getCount() / 2.0f));
                } else if (this.isDragging && this.cursorDragSlots.size() > 1 && (stackToDraw = stackToDraw.copyWithCount(this.dragSlot)).isEmpty()) {
                    amountText = String.valueOf(Formatting.RED) + "0";
                }
                this.drawItem(context, stackToDraw, mouseX - 3, mouseY - yOffset + 3, amountText);
            }
            if (!this.quickCraftingItem.isEmpty()) {
                float progress = (float) (Util.getMeasuringTimeMs() - this.quickCraftingTime) / 100.0f;
                if (progress >= 1.0f) {
                    progress = 1.0f;
                    this.quickCraftingItem = ItemStack.EMPTY;
                }
                int deltaX = this.quickCraftingSlot.x - this.quickCraftingStartX;
                int deltaY = this.quickCraftingSlot.y - this.quickCraftingStartY;
                int currentX = this.quickCraftingStartX + (int) (deltaX * progress);
                int currentY = this.quickCraftingStartY + (int) (deltaY * progress);
                this.drawItem(context, this.quickCraftingItem, currentX, currentY, null);
            }
            matrices.pop();
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        Screen screen = (Screen) this;
        float centerX = (float) screen.width / 2.0f - 90.0f;
        float centerY = (float) screen.height / 2.0f - 83.0f;
        MinecraftClient client = MinecraftClient.getInstance();
        if (InventorySortButton.isHovered(centerX + 90.0f - 25.0f, centerY - 24.0f, 50.0, 16.5, mouseX, mouseY)) {
            this.handler.slots.forEach(slot -> client.interactionManager.clickSlot(0, slot.id, 1, SlotActionType.THROW, client.player));
        }
        if (InventorySortButton.isSortingActive()) {
            for (int i = 0; i < 3; ++i) {
                if (!InventorySortButton.isHovered(centerX + 90.0f - 21.5f, centerY - 70.0f + (float) (i * 27) / 2.0f, 43.0, 14.5, mouseX, mouseY)) {
                    Melonity.getFeatureManager().setActiveFeature(Melonity.getFeatureManager().getFeatures().get(i));
                    InventorySortButton.setSortingActive(false);
                    Map<Integer, Item> filterMap = Melonity.getFeatureManager().getActiveFeature().getFilterMap();
                    if (filterMap == null || filterMap.isEmpty()) {
                        client.inGameHud.getChatHud().addMessage(Text.literal("No items saved in filter"));
                        cir.setReturnValue(true);
                        return;
                    }
                    if (filterMap != null) {
                        for (Map.Entry<Integer, Item> entry : filterMap.entrySet()) {
                            int targetSlot;
                            if (entry.getValue().getTranslationKey().toLowerCase().contains("shulker") || (targetSlot = InventoryUtils.findItemSlot(client.player, entry.getValue())) == -1 || targetSlot == entry.getKey()) continue;
                            client.interactionManager.clickSlot(0, targetSlot, 0, SlotActionType.SWAP, client.player);
                            client.interactionManager.clickSlot(0, entry.getKey(), 0, SlotActionType.SWAP, client.player);
                        }
                    } else {
                        System.err.println("No slots for ");
                    }
                    InventorySortButton.setSortingActive(false);
                }
            }
        }
        if (InventorySortButton.isHovered(centerX + 90.0f - 25.0f + 52.0f, centerY - 24.0f, 16.5, 16.5, mouseX, mouseY)) {
            LinkedHashMap<Integer, Item> armorMap = Maps.newLinkedHashMap();
            for (int i = 0; i < 46; ++i) {
                if (i >= 5 && i <= 8) continue;
                ItemStack stack = client.player.getInventory().getStack(i);
                if (stack.isEmpty() || stack.getItem() == Items.AIR) continue;
                armorMap.put(i, stack.getItem());
            }
            for (ItemStack armorStack : client.player.getInventory().armor) {
                String itemName = armorStack.getItem().toString();
                if (itemName.endsWith("helmet")) {
                    armorMap.put(5, armorStack.getItem());
                } else if (itemName.endsWith("chestplate")) {
                    armorMap.put(6, armorStack.getItem());
                } else if (itemName.endsWith("leggings")) {
                    armorMap.put(7, armorStack.getItem());
                } else if (itemName.endsWith("boots")) {
                    armorMap.put(8, armorStack.getItem());
                }
            }
            Collection<Item> items = armorMap.values();
            for (Item item : items) {
                if (Collections.frequency(items, item) <= 1) continue;
                armorMap.values().remove(item);
            }
            Melonity.getFeatureManager().getActiveFeature().setFilterMap(armorMap);
            cir.setReturnValue(true);
        }
        if (InventorySortButton.isHovered(centerX + 90.0f - 25.0f - 18.0f, centerY - 24.0f, 16.5, 16.5, mouseX, mouseY)) {
            InventorySortButton.toggleSortingActive();
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        for (int i = 0; i < 9; ++i) {
            if (!MinecraftClient.getInstance().options.offHandHotbarKeys[i].matchesKey(keyCode, scanCode) || InventorySortButton.getSortSlot() == -1) continue;
            MinecraftClient.getInstance().interactionManager.clickSlot(0, InventorySortButton.getSortSlot(), i, SlotActionType.SWAP, MinecraftClient.getInstance().player);
            cir.setReturnValue(true);
            return;
        }
        if (MinecraftClient.getInstance().options.dropKey.matchesKey(keyCode, scanCode) && InventorySortButton.getSortSlot() != -1) {
            MinecraftClient.getInstance().interactionManager.clickSlot(0, InventorySortButton.getSortSlot(), Screen.hasControlDown() ? 1 : 0, SlotActionType.THROW, MinecraftClient.getInstance().player);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "drawForeground", at = @At("HEAD"), cancellable = true)
    protected void onDrawForeground(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        if ((Screen) this instanceof CreativeInventoryScreen) {
            ci.cancel();
        }
    }

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    public void onRenderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ((Screen) this).renderBackgroundTexture(context);
        if (!((Screen) this instanceof CreativeInventoryScreen)) {
            this.renderBackground(context, delta, mouseX, mouseY);
        }
        ci.cancel();
    }
}