// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.e;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import ru.melonity.e.WindowElement;
import ru.melonity.w.DragHelper;

@Environment(value = EnvType.CLIENT)
final class WindowDragHandler {
    private final WindowElement parentWindow;
    private boolean dragging;
    private float dragStartX;
    private float dragStartY;

    public WindowDragHandler(WindowElement parentWindow) {
        this.parentWindow = parentWindow;
        this.dragging = false;
    }

    boolean onMouseDragged(double mouseX, double mouseY) {
        MinecraftClient client = MinecraftClient.getInstance();
        Window minecraftWindow = client.getWindow();

        if (this.dragging) {
            float deltaX = (float) mouseX - this.dragStartX;
            float deltaY = (float) mouseY - this.dragStartY;

            int screenWidth = minecraftWindow.getWidth();
            int screenHeight = minecraftWindow.getHeight();
            float windowWidth = this.parentWindow.getWidth();
            float windowHeight = this.parentWindow.getHeight();
            float screenCenterX = (float) screenWidth / 2.0f;
            float screenCenterY = (float) screenHeight / 2.0f;
            float snapThreshold = 30.0f;

            float windowRightEdge = deltaX + windowWidth;
            float distToCenterX = Math.abs(windowRightEdge - screenCenterX);
            if (distToCenterX <= snapThreshold) {
                deltaX = screenCenterX - windowWidth;
            }

            float windowBottomEdge = deltaY + windowHeight;
            float distToCenterY = Math.abs(windowBottomEdge - screenCenterY);
            if (distToCenterY <= snapThreshold) {
                deltaY = screenCenterY - windowHeight;
            }

            this.parentWindow.setX(deltaX);
            this.parentWindow.setY(deltaY);
        }

        float clampedX = this.parentWindow.getX();
        if (clampedX < 0.0f) {
            this.parentWindow.setX(0.0f);
            clampedX = 0.0f;
        }

        float clampedY = this.parentWindow.getY();
        if (clampedY < 0.0f) {
            this.parentWindow.setY(0.0f);
            clampedY = 0.0f;
        }

        float containerWidth = this.parentWindow.getWidth();
        if (clampedX + containerWidth > minecraftWindow.getWidth()) {
            this.parentWindow.setX(minecraftWindow.getWidth() - containerWidth);
        }

        float containerHeight = this.parentWindow.getHeight();
        if (clampedY + containerHeight > minecraftWindow.getHeight()) {
            this.parentWindow.setY(minecraftWindow.getHeight() - containerHeight);
        }

        return DragHelper.isMouseOver(this.parentWindow.getX(), this.parentWindow.getY(), this.parentWindow.getWidth(), this.parentWindow.getHeight(), mouseX, mouseY);
    }

    void startDragging(double mouseX, double mouseY) {
        if (DragHelper.isMouseOver(this.parentWindow.getX(), this.parentWindow.getY(), this.parentWindow.getWidth(), this.parentWindow.getHeight(), mouseX, mouseY)) {
            this.dragging = true;
            this.dragStartX = (float) mouseX - this.parentWindow.getX();
            this.dragStartY = (float) mouseY - this.parentWindow.getY();
        }
    }

    void stopDragging() {
        this.dragging = false;
    }

    boolean isDragging() {
        return this.dragging;
    }
}