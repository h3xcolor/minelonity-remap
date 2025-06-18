// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.p;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.o.a.ThemeSettings;
import ru.melonity.p.Notification;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class NotificationRenderer {
    private final List<Notification> notifications = Lists.newLinkedList();
    private static final FrameWeightCalculator animationCalculator = FrameWeightCalculator.milliseconds(400L);
    public static boolean enabled = true;

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

    public void renderNotifications(Notification notification, MatrixStack matrices) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        int screenWidth = textRenderer.getWidth();
        float yPosition = screenWidth - 5;
        float notificationWidth = 104.0f;
        float notificationHeight = 32.0f;
        float spacing = 2.0f;
        ThemeSettings themeSettings = Melonity.themeSettings.getThemeSettings();
        ThemeSettings.ThemeColors themeColors = themeSettings.getThemeColors();
        float elapsedUnits = animationCalculator.elapsedUnits();
        try {
            int notificationCount = this.notifications.size();
            if (notificationCount > 10) {
                this.notifications.removeFirst();
            }
            Iterator<Notification> iterator = this.notifications.iterator();
            while (iterator.hasNext()) {
                Notification currentNotification = iterator.next();
                long currentTime = System.currentTimeMillis();
                long creationTime = currentNotification.getCreationTime();
                float timeSinceCreation = (float) (currentTime - creationTime) / 1000.0f;
                boolean shouldRemove = timeSinceCreation > 5.0f;
                StateAnimation animation = currentNotification.getAnimation();
                animation.state(!shouldRemove);
                animation.update(elapsedUnits);
                float animationProgress = animation.animation();
                if (animationProgress < 0.08f) continue;
                float animatedHeight = (notificationHeight + spacing) * animationProgress;
                yPosition -= animatedHeight;
                TextRenderer currentTextRenderer = client.textRenderer;
                int screenHeight = currentTextRenderer.getHeight();
                float xPosition = (float) screenHeight - notificationWidth + 1.0f;
                matrices.push();
                matrices.translate(xPosition + notificationWidth / 2.0f, yPosition + notificationHeight / 2.0f, 0.0f);
                matrices.translate(-xPosition - notificationWidth / 2.0f, -yPosition - notificationHeight / 2.0f, 0.0f);
                matrices.translate(notificationWidth, 0.0f, 0.0f);
                float slideProgress = animation.animation();
                matrices.translate(-(slideProgress * notificationWidth), 0.0f, 0.0f);
                float progressBarWidth = notificationWidth - timeSinceCreation / 5.3f * notificationWidth;
                Color backgroundColor = themeColors.getBackgroundColor();
                Notification.renderNotificationBackground(xPosition, yPosition, notificationWidth, notificationHeight, 5.0f, backgroundColor, matrices);
                Color progressColor = themeColors.getProgressColor();
                Color brighterProgressColor = progressColor.brighter();
                Notification.renderNotificationProgress(xPosition, yPosition + notificationHeight - 2.5f, notificationWidth, 2.5f, 3.0f, brighterProgressColor, matrices);
                float progressBarX = xPosition + notificationWidth / 2.0f - progressBarWidth / 2.0f;
                Color barColor = themeColors.getBarColor();
                Notification.renderNotificationProgress(progressBarX, yPosition + notificationHeight - 2.5f, progressBarWidth, 2.5f, 3.0f, barColor, matrices);
                String iconName = currentNotification.getIconName();
                String iconPath = "melonity/images/ui/notifications/".concat(iconName);
                Identifier iconIdentifier = new Identifier(iconPath);
                Notification.renderNotificationIcon(iconIdentifier, xPosition + 6.0f, yPosition + 8.5f, 12.0f, 12.0f, matrices);
                String title = currentNotification.getTitle();
                Notification.renderNotificationText(title, xPosition + 24.0f, yPosition + 9.0f, matrices);
                String description = currentNotification.getDescription();
                Notification.renderNotificationDescription(description, xPosition + 24.0f, yPosition + 17.0f, Color.GRAY, matrices);
                matrices.pop();
            }
        } catch (Throwable ignored) {
        }
    }
}