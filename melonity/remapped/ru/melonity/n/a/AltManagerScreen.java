// ремапили ребята из https://t.me/dno_rumine
```java
package ru.melonity.n.a;

import java.awt.Color;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import ru.melonity.Melonity;
import ru.melonity.fabric.client.model.IMainMenuStage;
import ru.melonity.n.IMainMenuStage;
import ru.melonity.n.AccountListWidget;
import ru.melonity.n.AddAccountButton;
import ru.melonity.utils.RenderUtils;

@Environment(value=EnvType.CLIENT)
public class AltManagerScreen implements IMainMenuStage {
    private final Screen parentScreen;
    private final AccountListWidget accountListWidget = new AccountListWidget();
    private final AddAccountButton addAccountButton;
    private boolean isLoggingIn;

    public AltManagerScreen(TitleScreen titleScreen) {
        this.parentScreen = titleScreen;
        this.isLoggingIn = false;
        this.addAccountButton = new AddAccountButton(() -> {
            ru.melonity.a.AccountManager accountManager = Melonity.ACCOUNT_MANAGER;
            List<ru.melonity.a.AccountEntry> accounts = accountManager.getAccounts();
            boolean noAccounts = accounts.isEmpty();
            if (noAccounts) {
                MinecraftClient client = MinecraftClient.getInstance();
                TitleScreen defaultScreen = new TitleScreen();
                client.setScreen(defaultScreen);
            } else {
                IMainMenuStage menuStage = (IMainMenuStage)titleScreen;
                AccountListWidget accountList = new AccountListWidget(titleScreen);
                menuStage.setStage(accountList);
            }
        }, () -> {});
    }

    @Override
    public void render(float mouseX, float mouseY, float delta, double x, double y, ru.melonity.s.c.Renderer renderer, MatrixStack matrices) {
        ru.melonity.a.AccountManager accountManager = Melonity.ACCOUNT_MANAGER;
        this.accountListWidget.render(mouseX, mouseY, x, y, accountManager, matrices);
        if (!this.isLoggingIn) {
            boolean microsoftHovered = RenderUtils.isHovered(mouseX + 13.0f, mouseY + 44.0f, 91.0, 42.0, x, y);
            RenderUtils.drawRoundedGradientRect(mouseX + 13.0f, mouseY + 44.0f, 87.5f, 42.0f, 8.0f, Color.RED, Color.CYAN, Color.YELLOW, Color.GREEN, matrices);
            Color microsoftBg = Color.decode(microsoftHovered ? "#2C2C2C" : "#181818");
            RenderUtils.drawRoundedRect(mouseX + 13.0f, mouseY + 44.0f, 87.5f, 42.0f, 8.0f, microsoftBg, matrices);
            Identifier microsoftIcon = new Identifier("melonity/images/ui/altmanager/microsoft.png");
            Color iconColor = Color.decode("#888888");
            RenderUtils.drawTexture(microsoftIcon, mouseX + 13.0f + 43.75f - 6.0f, mouseY + 44.0f + 8.0f, 12.0f, 12.0f, iconColor, matrices);
            float microsoftTextWidth = RenderUtils.getTextWidth("Microsoft");
            float microsoftTextX = mouseX + 13.0f + 45.5f - microsoftTextWidth / 2.0f;
            Color textColor = Color.decode("#888888");
            RenderUtils.drawText("Microsoft", microsoftTextX, mouseY + 44.0f + 29.0f, textColor, matrices);
            boolean mojangHovered = RenderUtils.isHovered(mouseX + 13.0f + 91.0f, mouseY + 44.0f, 90.0, 42.0, x, y);
            Color mojangColor1 = Color.decode("#888888");
            Color mojangColor2 = Color.decode("#181818");
            Color mojangColor3 = Color.decode("#181818");
            Color mojangColor4 = Color.decode("#888888");
            RenderUtils.drawRoundedGradientRect(mouseX + 13.0f + 90.0f, mouseY + 44.0f, 87.5f, 42