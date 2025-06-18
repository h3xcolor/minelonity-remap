// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.b;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.melonity.Melonity;
import ru.melonity.h.b.AutoBuyManager;
import ru.melonity.s.c.RenderHelper;
import ru.melonity.s.c.Theme;
import ru.melonity.s.c.ThemeManager;
import ru.melonity.s.i.FontManager;
import ru.melonity.s.i.FontRenderer;
import ru.melonity.w.InputHelper;

import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class AutoBuyScreen extends Screen {
    private int pageOffsetAllItems = 0;
    private int pageOffsetSelectedItems = 0;
    private int selectedItemIndex = 0;
    private final IconButton addButton = new IconButton(new Identifier("melonity/images/ui/autobuy/autobuy_add.png"), "Add item", 172.5f, 16.0f, 8.5f, 8.0f);
    private final IconButton startStopButton = new IconButton(new Identifier("melonity/images/ui/autobuy/autobuy_start.png"), "Start", 172.5f, 16.0f, 8.5f, 8.0f);
    private final List<ItemStack> selectedItems = Lists.newLinkedList();
    private ItemSettingsScreen itemSettingsScreen = null;
    private boolean showContextMenu = false;
    private int contextMenuItemIndex = -1;
    private float contextMenuX;
    private float contextMenuY;
    private static final List<ItemStack> allItems = Registries.ITEM.getIds().stream()
            .filter(id -> !(Registries.ITEM.get(id) instanceof net.minecraft.item.BlockItem))
            .distinct()
            .map(ItemStack::new)
            .collect(Collectors.toList());

    static {
        allItems.add(new ItemStack(Items.ENDER_CHEST));
        allItems.add(new ItemStack(Items.CHEST));
    }

    public AutoBuyScreen() {
        super(Text.of("Autobuy"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        MatrixStack matrixStack = matrices;
        pageOffsetAllItems = Math.max(0, pageOffsetAllItems);
        float panelWidth = 377.0f;
        float panelHeight = 282.0f;
        MinecraftClient client = this.client;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        float panelX = (float) screenWidth / 2 - panelWidth / 2;
        float panelY = (float) screenHeight / 2 - panelHeight / 2;
        RenderHelper renderHelper = Melonity.renderHelper;
        Theme theme = Melonity.themeManager.getCurrentTheme();
        Color backgroundColor = theme.getBackgroundColor();
        renderHelper.drawPanel(panelX, panelY, panelWidth, panelHeight, backgroundColor, matrixStack);
        if (itemSettingsScreen != null) {
            itemSettingsScreen.render(panelX, panelY, mouseX, mouseY, renderHelper, matrixStack, this);
            return;
        }
        Identifier overlayBlocks = new Identifier("melonity/images/ui/overlay_blocks.png");
        Color headerColor = theme.getHeaderColor();
        renderHelper.drawTexturedPanel(overlayBlocks, panelX, panelY - 17.0f + 2.0f, panelWidth, 17.0f, headerColor, matrixStack);
        Color separatorColor = Color.decode("#222222");
        renderHelper.drawHorizontalLine(panelX, panelY + 36.0f, panelWidth, 1.0f, separatorColor, matrixStack);
        FontRenderer titleFont = FontManager.titleFont;
        renderHelper.drawText(titleFont, "AutoBuy", panelX + 8.0f, panelY + 14.0f, matrixStack);
        Color subtitleColor = Color.decode("#888888");
        FontRenderer subtitleFont = FontManager.subtitleFont;
        renderHelper.drawText(subtitleFont, "Automatically buy items from auction", panelX + 8.0f, panelY + 24.0f, subtitleColor, matrixStack);
        if (AutoBuyManager.isRunning) {
            startStopButton.setText("Stop");
        } else {
            if (!selectedItems.isEmpty()) {
                AutoBuyManager.selectedItems.clear();
            }
            startStopButton.setText("Start");
        }
        float leftSectionWidth = panelWidth / 2 - 8.0f;
        Color sectionColor = theme.getSectionColor();
        renderHelper.drawRoundedRect(panelX + 6.0f, panelY + 36.0f + 6.0f, leftSectionWidth, panelHeight - 72.0f, 8.0f, sectionColor, matrixStack);
        renderHelper.drawText(titleFont, "All items", panelX + 12.0f, panelY + 36.0f + 15.0f, matrixStack);
        renderHelper.drawHorizontalLine(panelX + 6.0f, panelY + 36.0f + 6.0f + 21.0f, leftSectionWidth, 1.0f, separatorColor, matrixStack);
        float rightSectionX = panelX + 6.0f + panelWidth / 2 - 4.0f;
        float rightSectionWidth = panelWidth / 2 - 8.0f;
        renderHelper.drawRoundedRect(rightSectionX, panelY + 36.0f + 6.0f, rightSectionWidth, panelHeight - 72.0f, 8.0f, sectionColor, matrixStack);
        renderHelper.drawText(titleFont, "Selected items", panelX - 4.0f + 6.0f + panelWidth / 2 + 6.0f, panelY + 36.0f + 15.0f, matrixStack);
        renderHelper.drawHorizontalLine(rightSectionX, panelY + 36.0f + 6.0f + 21.0f, rightSectionWidth, 1.0f, separatorColor, matrixStack);
        renderHelper.setScissor(panelX, panelY + 40.0f, panelWidth, panelHeight - 72.0f);
        float itemX = panelX + 14.0f;
        float itemY = panelY + 73.0f;
        int itemIndex = 1 + pageOffsetAllItems * 4;
        while (itemIndex < allItems.size()) {
            if (itemIndex == selectedItemIndex) {
                Color highlightColor = theme.getHighlightColor();
                renderHelper.drawRoundedRect(itemX - 0.5f, itemY - 0.5f, 41.0f, 41.0f, 8.0f, highlightColor, matrixStack);
            }
            Color itemBgColor = (itemIndex == selectedItemIndex) ? theme.getHighlightColor().darker().darker().darker() : theme.getSectionColor().brighter();
            renderHelper.drawRoundedRect(itemX, itemY, 40.0f, 40.0f, 8.0f, itemBgColor, matrixStack);
            ItemStack itemStack = allItems.get(itemIndex);
            if (itemStack.getName() != null) {
                String itemName = itemStack.getName().getString();
                float textWidth = subtitleFont.getWidth(itemName);
                renderHelper.drawText(subtitleFont, itemName, itemX + 20.5f - textWidth / 2 - 1.0f, itemY + 6.0f, matrixStack);
            }
            matrixStack.push();
            matrixStack.translate(itemX, itemY, 0.0f);
            matrixStack.scale(1.4f, 1.4f, 1.4f);
            matrixStack.translate(-itemX, -itemY, 0.0f);
            this.itemRenderer.renderInGuiWithOverrides(itemStack, (int) (itemX + 6.0f), (int) (itemY + 6.0f));
            matrixStack.pop();
            itemX += 40.0f;
            if (itemIndex != 0 && itemIndex != 2 && itemIndex % 4 == 0) {
                itemX = panelX + 14.0f;
                itemY += 40.5f;
            }
            itemIndex++;
        }
        renderHelper.disableScissor();
        Identifier bucketText = new Identifier("melonity/images/ui/autobuy/bucket_text.png");
        renderHelper.drawTexturedPanel(bucketText, panelX + panelWidth - 58.0f, panelY + 46.0f, 41.5f, 14.5f, matrixStack);
        if (InputHelper.isMouseOver(panelX + panelWidth - 58.0f, panelY + 46.0f, 41.5, 14.5, mouseX, mouseY)) {
            Color hoverColor = new Color(255, 255, 255, 100);
            renderHelper.drawTexturedPanel(bucketText, panelX + panelWidth - 58.0f, panelY + 46.0f, 41.5f, 14.5f, hoverColor, matrixStack);
        }
        renderHelper.setScissor(panelX, panelY + 40.0f, panelWidth, panelHeight - 72.0f);
        itemX = panelX + 185.0f + 14.0f;
        itemY = panelY + 73.0f;
        itemIndex = 1 + pageOffsetSelectedItems * 4;
        while (itemIndex < selectedItems.size()) {
            try {
                Color itemBgColor = theme.getSectionColor().brighter();
                renderHelper.drawRoundedRect(itemX, itemY, 40.0f, 40.0f, 8.0f, itemBgColor, matrixStack);
                ItemStack itemStack = selectedItems.get(itemIndex);
                if (itemStack.getName() != null) {
                    String itemName = itemStack.getName().getString();
                    float textWidth = subtitleFont.getWidth(itemName);
                    renderHelper.drawText(subtitleFont, itemName, itemX + 20.5f - textWidth / 2 - 1.0f, itemY + 6.0f, matrixStack);
                }
                matrixStack.push();
                matrixStack.translate(itemX, itemY, 0.0f);
                matrixStack.scale(1.4f, 1.4f, 1.4f);
                matrixStack.translate(-itemX, -itemY, 0.0f);
                this.itemRenderer.renderInGuiWithOverrides(itemStack, (int) (itemX + 6.0f), (int) (itemY + 6.0f));
                matrixStack.pop();
            } catch (Throwable ignored) {
                matrixStack.pop();
            }
            itemX += 40.0f;
            if (itemIndex != 0 && itemIndex != 2 && itemIndex % 4 == 0) {
                itemX = panelX + 185.0f + 14.0f;
                itemY += 40.5f;
            }
            itemIndex++;
        }
        renderHelper.disableScissor();
        float scrollbarY = panelY + 72.0f + (float) pageOffsetAllItems / 4.0f * 95.0f;
        Color scrollbarColor = theme.getScrollbarColor().brighter().brighter();
        renderHelper.drawRoundedRect(panelX + 181.0f, scrollbarY, 3.0f, 83.5f, 4.0f, scrollbarColor, matrixStack);
        float selectedScrollbarY = panelY + 72.0f + (float) pageOffsetSelectedItems / (float) selectedItems.size() * 100.0f;
        Color selectedScrollbarColor = theme.getScrollbarColor().brighter().brighter();
        renderHelper.drawRoundedRect(panelX + 181.0f + 185.0f, selectedScrollbarY, 3.0f, 83.5f, 4.0f, selectedScrollbarColor, matrixStack);
        Color buttonColor = theme.getButtonColor();
        renderHelper.drawRoundedRect(panelX + 6.0f, panelY + panelHeight - 25.0f, panelWidth - 12.0f, 22.0f, 8.0f, buttonColor, matrixStack);
        addButton.render(panelX + 12.0f, panelY + panelHeight - 36.0f + 14.0f, mouseX, mouseY, renderHelper, matrixStack);
        startStopButton.render(panelX + addButton.getWidth() + 6.0f + 14.0f, panelY + panelHeight - 36.0f + 14.0f, mouseX, mouseY, renderHelper, matrixStack);
        if (showContextMenu) {
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
            Color contextBgColor = theme.getContextMenuColor().brighter();
            renderHelper.drawRoundedRect(contextMenuX, contextMenuY, 62.0f, 42.0f, 8.0f, contextBgColor, matrixStack);
            List<String> contextOptions = Arrays.asList("Settings", "Delete");
            float optionY = contextMenuY + 4.0f;
            for (String option : contextOptions) {
                Color optionBgColor = theme.getContextMenuColor().brighter().brighter();
                renderHelper.drawRoundedRect(contextMenuX + 3.5f, optionY - 0.5f, 55.0f, 17.0f, 5.0f, optionBgColor, matrixStack);
                renderHelper.drawRoundedRect(contextMenuX + 4.0f, optionY, 54.0f, 16.0f, 5.0f, optionBgColor, matrixStack);
                if (InputHelper.isMouseOver(contextMenuX + 4.0f, optionY, 54.0, 16.0, mouseX, mouseY)) {
                    Color hoverColor = new Color(255, 255, 255, 10);
                    renderHelper.drawRoundedRect(contextMenuX + 4.0f, optionY, 54.0f, 16.0f, 5.0f, hoverColor, matrixStack);
                }
                Color textColor = Color.decode("#888888");
                renderHelper.drawText(subtitleFont, option, contextMenuX + 8.0f, optionY + 7.0f, textColor, matrixStack);
                optionY += 19.0f;
            }
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float panelWidth = 377.0f;
        float panelHeight = 282.0f;
        MinecraftClient client = this.client;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        float panelX = (float) screenWidth / 2 - panelWidth / 2;
        float panelY = (float) screenHeight / 2 - panelHeight / 2;
        float addButtonX = panelX + 12.0f;
        float addButtonY = panelY + panelHeight - 36.0f + 14.0f;
        float addButtonWidth = addButton.getWidth();
        float addButtonHeight = addButton.getHeight();
        if (InputHelper.isMouseOver(addButtonX, addButtonY, addButtonWidth, addButtonHeight, mouseX, mouseY)) {
            AutoBuyManager.selectedItems.clear();
            for (ItemStack itemStack : selectedItems) {
                try {
                    int price = ((IItemStack) (Object) itemStack).getPrice();
                    if (price > 0) {
                        AutoBuyManager.selectedItems.put(itemStack, price);
                    }
                } catch (Throwable ignored) {
                }
            }
            AutoBuyManager.isRunning = true;
            this.close();
            if (!AutoBuyManager.hasScannedAuctions) {
                client.player.sendMessage(Text.of("/ah"));
            }
            return true;
        }
        if (showContextMenu) {
            List<String> contextOptions = Arrays.asList("Settings", "Delete");
            float optionY = contextMenuY + 4.0f;
            for (String option : contextOptions) {
                if (InputHelper.isMouseOver(contextMenuX + 4.0f, optionY, 54.0, 16.0, mouseX, mouseY)) {
                    int optionIndex = contextOptions.indexOf(option);
                    if (optionIndex == 1) {
                        selectedItems.remove(contextMenuItemIndex);
                        contextMenuItemIndex = -1;
                    } else if (optionIndex == 0) {
                        ItemStack itemStack = selectedItems.get(contextMenuItemIndex);
                        itemSettingsScreen = new ItemSettingsScreen(itemStack);
                    }
                    showContextMenu = false;
                }
                optionY += 19.0f;
            }
            return true;
        }
        if (itemSettingsScreen != null) {
            ItemStack itemStack = selectedItems.get(contextMenuItemIndex);
            ItemStack result = itemSettingsScreen.handleClick(itemStack, panelX, panelY, mouseX, mouseY, button);
            if (result != null) {
                if (result.getItem() == Items.AIR) {
                    selectedItems.remove(contextMenuItemIndex);
                } else {
                    selectedItems.set(contextMenuItemIndex, result);
                }
                itemSettingsScreen = null;
            }
            return true;
        }
        if (InputHelper.isMouseOver(panelX + panelWidth - 58.0f, panelY + 46.0f, 41.5, 14.5, mouseX, mouseY)) {
            selectedItems.clear();
        }
        float itemX = panelX + 14.0f;
        float itemY = panelY + 73.0f;
        int itemIndex = 1 + pageOffsetAllItems * 4;
        while (itemIndex < allItems.size()) {
            if (itemY <= panelY + panelHeight - 70.0f) {
                if (InputHelper.isMouseOver(itemX, itemY, 40.0, 40.0, mouseX, mouseY)) {
                    selectedItemIndex = itemIndex;
                }
                itemX += 40.0f;
                if (itemIndex != 0 && itemIndex != 2 && itemIndex % 4 == 0) {
                    itemX = panelX + 14.0f;
                    itemY += 40.5f;
                }
            }
            itemIndex++;
        }
        itemX = panelX + 185.0f + 14.0f;
        itemY = panelY + 73.0f;
        itemIndex = 1 + pageOffsetSelectedItems * 4;
        while (itemIndex < selectedItems.size()) {
            try {
                if (InputHelper.isMouseOver(itemX, itemY, 40.0, 40.0, mouseX, mouseY)) {
                    showContextMenu = !showContextMenu;
                    contextMenuItemIndex = itemIndex;
                    contextMenuX = (float) mouseX;
                    contextMenuY = (float) mouseY;
                    break;
                }
            } catch (Throwable ignored) {
            }
            itemX += 40.0f;
            if (itemIndex != 0 && itemIndex != 2 && itemIndex % 4 == 0) {
                itemX = panelX + 185.0f + 14.0f;
                itemY += 40.5f;
            }
            itemIndex++;
        }
        float addButtonAreaX = panelX + 12.0f;
        float addButtonAreaY = panelY + panelHeight - 36.0f + 14.0f;
        float addButtonAreaWidth = addButton.getWidth();
        float addButtonAreaHeight = addButton.getHeight();
        if (InputHelper.isMouseOver(addButtonAreaX, addButtonAreaY, addButtonAreaWidth, addButtonAreaHeight, mouseX, mouseY)) {
            ItemStack itemStack = allItems.get(selectedItemIndex);
            selectedItems.add(itemStack);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (itemSettingsScreen != null) {
            itemSettingsScreen.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        float panelWidth = 377.0f;
        float panelHeight = 282.0f;
        MinecraftClient client = this.client;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        float panelX = (float) screenWidth / 2 - panelWidth / 2;
        float panelY = (float) screenHeight / 2 - panelHeight / 2;
        if (InputHelper.isMouseOver(panelX, panelY + 40.0f, 180.0, panelHeight - 72.0f, mouseX, mouseY)) {
            if (amount > 0) {
                pageOffsetAllItems--;
            } else if (amount < 0) {
                pageOffsetAllItems++;
            }
            if (pageOffsetAllItems < 0) {
                pageOffsetAllItems = 0;
            }
            if (pageOffsetAllItems > 78) {
                pageOffsetAllItems = 78;
            }
        } else if (InputHelper.isMouseOver(panelX + 185.0f, panelY + 40.0f, 180.0, panelHeight - 72.0f, mouseX, mouseY)) {
            if (amount > 0) {
                pageOffsetSelectedItems--;
            } else if (amount < 0) {
                pageOffsetSelectedItems++;
            }
            if (pageOffsetSelectedItems < 0) {
                pageOffsetSelectedItems = 0;
            }
            if (pageOffsetSelectedItems > (int) ((float) selectedItems.size() / 4.0f) - 4) {
                pageOffsetSelectedItems = (int) ((float) selectedItems.size() / 4.0f) - 4;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    private static class IconButton {
        private final Identifier texture;
        private String text;
        private final float width;
        private final float height;
        private final float iconWidth;
        private final float iconHeight;
        private boolean hovered;

        public IconButton(Identifier texture, String text, float width, float height, float iconWidth, float iconHeight) {
            this.texture = texture;
            this.text = text;
            this.width = width;
            this.height = height;
            this.iconWidth = iconWidth;
            this.iconHeight = iconHeight;
        }

        public void setText(String text) {
            this.text = text;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        public void render(float x, float y, double mouseX, double mouseY, RenderHelper renderHelper, MatrixStack matrices) {
            hovered = InputHelper.isMouseOver(x, y, width, height, mouseX, mouseY);
            Theme theme = Melonity.themeManager.getCurrentTheme();
            Color buttonColor = theme.getButtonColor();
            Color buttonHoverColor = buttonColor.brighter();
            renderHelper.drawRoundedRect(x, y, width, height, 6.0f, buttonHoverColor, matrices);
            Color buttonBgColor = buttonColor.brighter();
            renderHelper.drawRoundedRect(x + 0.5f, y + 0.5f, width - 1.0f, height - 1.0f, 6.0f, buttonBgColor, matrices);
            if (hovered) {
                Color hoverOverlay = new Color(255, 255, 255, 10);
                renderHelper.drawRoundedRect(x + 0.5f, y + 0.5f, width - 1.0f, height - 1.0f, 6.0f, hoverOverlay, matrices);
            }
            FontRenderer font = FontManager.subtitleFont;
            Color textColor = Color.decode("#888888");
            renderHelper.drawText(font, text, x + 3.0f + width / 2 - font.getWidth(text) / 2 - 1.0f, y + 4.0f, textColor, matrices);
        }
    }

    @Environment(EnvType.CLIENT)
    private static class ItemSettingsScreen {
        private final ItemStack itemStack;

        public ItemSettingsScreen(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public void render(float panelX, float panelY, RenderHelper renderHelper, MatrixStack matrices) {
        }

        public ItemStack handleClick(ItemStack original, float panelX, float panelY, double mouseX, double mouseY, int button) {
            return null;
        }

        public void keyPressed(int keyCode, int scanCode, int modifiers) {
        }
    }

    public interface IItemStack {
        int getPrice();
    }
}