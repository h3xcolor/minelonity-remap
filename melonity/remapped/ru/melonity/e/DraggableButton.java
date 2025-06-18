// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.e;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.Melonity;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class DraggableButton {
    private final int id;
    private float x;
    private float y;
    private float width;
    private float height;
    private final Supplier<Boolean> condition;
    private final DragHandler dragHandler = new DragHandler(this);
    public static int f5044e1ae1a6e42a5a2b6050e6b7217fd = 322447989;

    public DraggableButton(int id, float x, float y, float width, float height, Supplier<Boolean> condition) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.condition = condition;
        Melonity.getButtonManager().registerButton(this);
    }

    public DraggableButton(int id, float x, float y, float width, float height) {
        this(id, x, y, width, height, () -> true);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.dragHandler.isMouseOver(mouseX, mouseY);
    }

    public boolean isPointInBounds(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }

    public void drag(double deltaX, double deltaY) {
        this.dragHandler.drag(deltaX, deltaY);
    }

    public void update() {
        this.dragHandler.update();
    }

    public boolean isActive() {
        return this.condition.get();
    }

    public boolean isDragging() {
        return this.dragHandler.isDragging();
    }

    public int getId() {
        return this.id;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public Supplier<Boolean> getCondition() {
        return this.condition;
    }

    public DragHandler getDragHandler() {
        return this.dragHandler;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    private static class DragHandler {
        private final DraggableButton button;
        private boolean dragging;

        public DragHandler(DraggableButton button) {
            this.button = button;
        }

        public boolean isMouseOver(double mouseX, double mouseY) {
            return this.button.isPointInBounds(mouseX, mouseY);
        }

        public void drag(double deltaX, double deltaY) {
            if (this.button.isActive() && this.dragging) {
                this.button.setX((float) (this.button.getX() + deltaX));
                this.button.setY((float) (this.button.getY() + deltaY));
            }
        }

        public void update() {
            this.dragging = false;
        }

        public boolean isDragging() {
            return this.dragging;
        }
    }
}