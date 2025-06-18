// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.n.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;
import ru.melonity.Melonity;
import ru.melonity.fabric.client.model.IMainMenuStage;
import ru.melonity.i.FontRenderer;
import ru.melonity.n.a.ProxyListScreen;
import ru.melonity.r.ProxyEntry;
import ru.melonity.r.ProxyManager;
import ru.melonity.w.ButtonRenderer;
import ru.melonity.w.RectangleRenderer;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class ProxyListScreen implements ru.melonity.n.a.CustomScreen {
    private final Screen parentScreen;
    private float scrollOffset = 0.0f;

    public ProxyListScreen(Screen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void render(float partialTicks, float mouseX, float mouseY, double mouseActualX, double mouseActualY, ru.melonity.s.c.ProxyManager proxyManager, MatrixStack matrices) {
        ProxyManager proxyManagerInstance = Melonity.PROXY_MANAGER;
        ProxyManager.ProxyConfig proxyConfig = proxyManagerInstance.getConfig();
        Color backgroundColor = proxyConfig.getBackgroundColor();
        Color borderColor = proxyConfig.getBorderColor();
        Color titleColor = proxyConfig.getTitleColor();
        Color textColor = proxyConfig.getTextColor();
        FontRenderer.drawRoundedRect(partialTicks, mouseX, mouseY, 30.0f, 0.0f, 12.0f, 12.0f, 0.0f, backgroundColor, borderColor, titleColor, textColor, matrices);
        FontRenderer.drawString(FontRenderer.TITLE_FONT, "Proxy List", mouseX + 6.0f, mouseY + 14.0f, matrices);
        Color separatorColor = Color.decode("#222222");
        FontRenderer.drawHorizontalLine(mouseX, mouseY + 30.0f, partialTicks, 1.0f, separatorColor, matrices);
        ProxyManager.ProxyList proxyList = Melonity.PROXY_MANAGER.getProxyList();
        List<ProxyEntry> customProxies = proxyList.getCustomProxies();
        boolean hasCustomProxies = customProxies.isEmpty();
        float currentY = mouseY + 50.0f + (hasCustomProxies ? -8 : 0);
        List<ProxyEntry> allCustomProxies = proxyList.getCustomProxies();
        boolean hasAnyCustomProxies = allCustomProxies.isEmpty();
        if (!hasAnyCustomProxies) {
            Color sectionTitleColor = Color.decode("#888888");
            FontRenderer.drawString(FontRenderer.SUBTITLE_FONT, "Custom proxies", mouseX + 6.0f, mouseY + 41.0f, sectionTitleColor, matrices);
            double startX = mouseX;
            double startY = currentY;
            double width = partialTicks;
            List<ProxyEntry> customProxyList = proxyList.getCustomProxies();
            int customProxyCount = customProxyList.size();
            RectangleRenderer.drawRoundedRect(startX, startY, width, customProxyCount * 22 - 2);
            List<ProxyEntry> customProxyEntries = proxyList.getCustomProxies();
            Iterator<ProxyEntry> customProxyIterator = customProxyEntries.iterator();
            while (customProxyIterator.hasNext()) {
                ProxyEntry proxy = customProxyIterator.next();
                ProxyEntry selectedProxy = proxyList.getSelectedProxy();
                if (selectedProxy != null) {
                    String proxyName = proxy.getName();
                    ProxyEntry currentSelectedProxy = proxyList.getSelectedProxy();
                    String selectedProxyName = currentSelectedProxy.getName();
                    boolean isSelected = proxyName.equals(selectedProxyName);
                    if (isSelected) {
                        Color selectionColor = Melonity.PROXY_MANAGER.getColor(1, 60);
                        FontRenderer.drawRoundedRect(mouseX + 8.0f - 0.5f, currentY + scrollOffset - 0.5f, partialTicks - 16.0f + 1.0f, 18.0f, 6.0f, selectionColor, matrices);
                    }
                }
                Color entryBackground = Color.decode("#222222");
                FontRenderer.drawRoundedRect(mouseX + 8.0f, currentY + scrollOffset, partialTicks - 16.0f, 17.0f, 6.0f, entryBackground, matrices);
                String proxyName = proxy.getName();
                Color proxyNameColor = Color.decode("#888888");
                FontRenderer.drawString(FontRenderer.PROXY_NAME_FONT, proxyName, mouseX + 12.0f, currentY + 7.0f + scrollOffset, proxyNameColor, matrices);
                String pingText = String.format("%dms", proxy.getPing());
                String formattedPing = String.format("%dms", proxy.getPing());
                float pingTextWidth = FontRenderer.PROXY_PING_FONT.getWidth(formattedPing);
                FontRenderer.drawString(FontRenderer.PROXY_PING_FONT, pingText, mouseX + partialTicks - 10.0f - pingTextWidth - 4.0f, currentY + 7.0f + scrollOffset, Color.GRAY, matrices);
                currentY += 22.0f;
            }
            RectangleRenderer.finishRendering();
            currentY += 5.0f;
        }
        Color freeProxyTitleColor = Color.decode("#888888");
        FontRenderer.drawString(FontRenderer.SUBTITLE_FONT, "Free proxies", mouseX + 6.0f, currentY, freeProxyTitleColor, matrices);
        double freeProxyStartX = mouseX;
        double freeProxyStartY = currentY += 10.0f;
        double freeProxyWidth = partialTicks;
        List<ProxyEntry> freeProxies = proxyList.getFreeProxies();
        boolean hasFreeProxies = freeProxies.isEmpty();
        double freeProxyHeight;
        if (hasFreeProxies) {
            freeProxyHeight = 140.0;
        } else {
            List<ProxyEntry> allFreeProxies = proxyList.getFreeProxies();
            int freeProxyCount = allFreeProxies.size();
            freeProxyHeight = freeProxyCount * 22;
        }
        RectangleRenderer.drawRoundedRect(freeProxyStartX, freeProxyStartY, freeProxyWidth, freeProxyHeight);
        List<ProxyEntry> freeProxyEntries = proxyList.getFreeProxies();
        Iterator<ProxyEntry> freeProxyIterator = freeProxyEntries.iterator();
        while (freeProxyIterator.hasNext()) {
            ProxyEntry proxy = freeProxyIterator.next();
            ProxyEntry selectedProxy = proxyList.getSelectedProxy();
            if (selectedProxy != null) {
                String proxyName = proxy.getName();
                ProxyEntry currentSelectedProxy = proxyList.getSelectedProxy();
                String selectedProxyName = currentSelectedProxy.getName();
                boolean isSelected = proxyName.equals(selectedProxyName);
                if (isSelected) {
                    Color selectionColor = proxyConfig.getSelectionColor().darker();
                    FontRenderer.drawRoundedRect(mouseX + 8.0f - 0.5f, currentY + scrollOffset - 0.5f, partialTicks - 16.0f + 1.0f, 18.0f, 6.0f, selectionColor, matrices);
                }
            }
            Color entryBackground = proxyConfig.getEntryBackgroundColor().darker();
            FontRenderer.drawRoundedRect(mouseX + 8.0f, currentY + scrollOffset, partialTicks - 16.0f, 17.0f, 6.0f, entryBackground, matrices);
            String proxyName = proxy.getName();
            Color proxyNameColor = Color.decode("#888888");
            FontRenderer.drawString(FontRenderer.PROXY_NAME_FONT, proxyName, mouseX + 12.0f, currentY + 7.0f + scrollOffset, proxyNameColor, matrices);
            String pingText = String.format("%dms", proxy.getPing());
            String formattedPing = String.format("%dms", proxy.getPing());
            float pingTextWidth = FontRenderer.PROXY_PING_FONT.getWidth(formattedPing);
            FontRenderer.drawString(FontRenderer.PROXY_PING_FONT, pingText, mouseX + partialTicks - 10.0f - pingTextWidth - 4.0f, currentY + 7.0f + scrollOffset, Color.GRAY, matrices);
            currentY += 22.0f;
        }
        RectangleRenderer.finishRendering();
        Color footerColor = proxyConfig.getFooterColor();
        FontRenderer.drawRoundedRect(mouseX, mouseY + 166.0f, partialTicks, 34.0f, 12.0f, footerColor, matrices);
        boolean addButtonHovered = ButtonRenderer.isHovered(mouseX + 8.0f, mouseY + 166.0f + 9.0f, 92.0, 15.0, mouseActualX, mouseActualY);
        Color addButtonBackground = Color.decode("#2C2C2C");
        FontRenderer.drawRoundedRect(mouseX + 8.0f - 0.3f, mouseY + 166.0f + 9.0f - 0.3f, 92.6f, 17.0f, 8.0f, addButtonBackground, matrices);
        Color addButtonColor;
        if (addButtonHovered) {
            addButtonColor = Color.decode("#2C2C2C");
        } else {
            addButtonColor = Color.decode("#222222");
        }
        FontRenderer.drawRoundedRect(mouseX + 8.0f, mouseY + 166.0f + 9.0f, 92.0f, 15.0f, 8.极f, addButtonColor, matrices);
        float addButtonTextX = mouseX + 8.0f + 46.0f - FontRenderer.BUTTON_FONT.getWidth("Add proxy") / 2.0f;
        Color addButtonTextColor = Color.decode("#888888");
        FontRenderer.drawString(FontRenderer.BUTTON_FONT, "Add proxy", addButtonTextX, mouseY + 166.0f + 16.0f, addButtonTextColor, matrices);
        boolean chooseButtonHovered = ButtonRenderer.isHovered(mouseX + 8.0f + 96.0f, mouseY + 166.0f + 9.0f, 92.0, 17.0, mouseActualX, mouseActualY);
        Color chooseButtonColor;
        if (chooseButtonHovered) {
            chooseButtonColor = proxyConfig.getButtonHoverColor().darker();
        } else {
            chooseButtonColor = proxyConfig.getButtonColor();
        }
        FontRenderer.drawRoundedRect(mouseX + 8.0f + 96.0f, mouseY + 166.0f + 9.0f, 92.0f, 17.0f, 8.0f, chooseButtonColor, matrices);
        FontRenderer.drawString(FontRenderer.BUTTON_FONT, "Choose", mouseX + 96.0f + 8.0f + 46.0f - FontRenderer.BUTTON_FONT.getWidth("Choose") / 2.0f, mouseY + 166.0f + 17.0f, Color.BLACK, matrices);
        GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), Melonity.DEFAULT_CURSOR);
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY, float partialTicks, double mouseActualX, double mouseActualY, int button) {
        ProxyManager.ProxyList proxyList = Melonity.PROXY_MANAGER.getProxyList();
        List<ProxyEntry> customProxies = proxyList.getCustomProxies();
        boolean hasCustomProxies = customProxies.isEmpty();
        float currentY = mouseY + 50.0f + (hasCustomProxies ? -8 : 0);
        List<ProxyEntry> allCustomProxies = proxyList.getCustomProxies();
        boolean hasAnyCustomProxies = allCustomProxies.isEmpty();
        if (!hasAnyCustomProxies) {
            boolean customProxyAreaClicked = RectangleRenderer.isHovered(mouseX, currentY, partialTicks, 44.0, mouseActualX, mouseActualY);
            if (customProxyAreaClicked) {
                List<ProxyEntry> customProxyList = proxyList.getCustomProxies();
                Iterator<ProxyEntry> customProxyIterator = customProxyList.iterator();
                while (customProxyIterator.hasNext()) {
                    ProxyEntry proxy = customProxyIterator.next();
                    boolean proxyClicked = RectangleRenderer.isHovered(mouseX + 8.0f, currentY + scrollOffset, partialTicks - 16.0f, 17.0, mouseActualX, mouseActualY);
                    if (proxyClicked) {
                        proxyList.setSelectedProxy(proxy);
                    }
                    currentY += 22.0f;
                }
            }
            currentY = mouseY + 50.0f + 44.0f + 15.0f;
        }
        double freeProxyStartX = mouseX;
        double freeProxyStartY = currentY;
        double freeProxyWidth = partialTicks;
        List<ProxyEntry> freeProxies = proxyList.getFreeProxies();
        boolean hasFreeProxies = freeProxies.isEmpty();
        double freeProxyHeight;
        if (hasFreeProxies) {
            freeProxyHeight = 140.0;
        } else {
            List<ProxyEntry> allFreeProxies = proxyList.getFreeProxies();
            int freeProxyCount = allFreeProxies.size();
            freeProxyHeight = freeProxyCount * 22;
        }
        boolean freeProxyAreaClicked = RectangleRenderer.isHovered(freeProxyStartX, freeProxyStartY, freeProxyWidth, freeProxyHeight, mouseActualX, mouseActualY);
        if (freeProxyAreaClicked) {
            List<ProxyEntry> freeProxyEntries = proxyList.getFreeProxies();
            Iterator<ProxyEntry> freeProxyIterator = freeProxyEntries.iterator();
            while (freeProxyIterator.hasNext()) {
                ProxyEntry proxy = freeProxyIterator.next();
                boolean proxyClicked = RectangleRenderer.isHovered(mouseX + 8.0f, currentY + scrollOffset, partialTicks - 16.0f, 17.0, mouseActualX, mouseActualY);
                if (proxyClicked) {
                    proxyList.setSelectedProxy(proxy);
                }
                currentY += 22.0f;
            }
        }
        boolean addButtonClicked = ButtonRenderer.isClicked(mouseX + 8.0f, mouseY + 166.0f + 9.0f, 92.0, 15.0, mouseActualX, mouseActualY);
        if (addButtonClicked) {
            IMainMenuStage mainMenuStage = (IMainMenuStage) this.parentScreen;
            mainMenuStage.setStage(new AddProxyScreen(this.parentScreen));
        }
        boolean chooseButtonClicked = ButtonRenderer.isClicked(mouseX + 8.0f + 96.0f, mouseY + 166.0f + 9.0f, 92.0, 17.0, mouseActualX, mouseActualY);
        if (chooseButtonClicked) {
            IMainMenuStage mainMenuStage = (IMainMenuStage) this.parentScreen;
            mainMenuStage.setStage(new MainMenuScreen(this.parentScreen));
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            IMainMenuStage mainMenuStage = (IMainMenuStage) this.parentScreen;
            mainMenuStage.setStage(new MainMenuScreen(this.parentScreen));
        }
    }

    @Override
    public void mouseScrolled(float mouseX, float mouseY, float partialTicks, double scrollDeltaX, double scrollDeltaY, double scrollDelta) {
        ProxyManager.ProxyList proxyList = Melonity.PROXY_MANAGER.getProxyList();
        List<ProxyEntry> customProxies = proxyList.getCustomProxies();
        int customProxyCount = customProxies.size();
        ProxyManager.ProxyList proxyListInstance = Melonity.PROXY_MANAGER.getProxyList();
        List<ProxyEntry> freeProxies = proxyListInstance.getFreeProxies();
        int freeProxyCount = freeProxies.size();
        if (customProxyCount + freeProxyCount > 5) {
            this.scrollOffset += (float) (scrollDelta * 4.0);
        }
        if (this.scrollOffset > 0.0f) {
            this.scrollOffset = 0.0f;
        }
    }

    @Override
    public float getHeight() {
        return 200.0f;
    }
}