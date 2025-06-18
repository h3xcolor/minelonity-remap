// ремапили ребята из https://t.me/dno_rumine
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.o.a.UITheme;
import ru.melonity.s.e.Container;
import ru.melonity.s.e.UIComponent;
import ru.melonity.w.UIUtils;

import java.awt.Color;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class RadialMenu extends Container {
    private String hoveredItem = null;
    private final UIComponent deleteButton = new Container.ChildComponent(new class_2960("melonity/images/ui/baritone/delete.png"), "Remove", 42.0f, 16.0f, 8.0f, 8.0f);
    private String hoveredItemName = null;
    private double deleteButtonX;
    private double deleteButtonY;
    private final FrameWeightCalculator deleteButtonTimer = FrameWeightCalculator.milliseconds(300L);
    private final StateAnimation deleteButtonAnimation = new StateAnimation();
    private static final float[][] ITEM_POSITIONS = new float[][]{{-112.0f, 46.0f}, {-112.0f, -7.0f}, {-56.0f, -63.0f}, {46.0f, -63.0f}, {106.0f, 46.0f}, {106.0f, -12.0f}};

    public RadialMenu(float width, float height) {
        super(width, height);
        this.deleteButton.setVisible(false);
        this.deleteButton.setY(-1.0f);
        this.deleteButtonTimer.setTarget(3.0f);
        this.deleteButton.setCentered(true);
    }

    @Override
    public void render(UITheme theme, float x, float y, double mouseX, double mouseY, UIComponent renderHelper, class_4587 matrices) {
        float circleRadius = 70.0f;
        float centerX = x + this.width / 2.0f - 50.0f;
        float centerY = y + this.height / 2.0f - 50.0f;
        List<UIComponent> items = Melonity.containerManager.getElements();
        List<UIComponent> itemList = items;
        Color[] themeColors = theme.getColors();
        renderHelper.renderCircle(centerX - 0.5f, centerY - 0.5f, circleRadius + 1.0f, circleRadius + 1.0f, circleRadius, themeColors[1], matrices);
        Color backgroundColor = theme.getBackgroundColor();
        renderHelper.renderCircle(centerX, centerY, circleRadius, circleRadius, circleRadius, backgroundColor, matrices);
        Color[] borderColors = theme.getBorderColors();
        renderHelper.renderLine(centerX - 8.0f, centerY + 48.0f, centerX - 34.0f, centerY + 48.0f + 10.0f, 2.0f, borderColors[1]);
        Color[] accentColors = theme.getAccentColors();
        renderHelper.renderLine(centerX - 8.0f, centerY + 20.0f, centerX - 34.0f, centerY + 20.0f - 10.0f, 2.0f, accentColors[1]);
        Color[] highlightColors = theme.getHighlightColors();
        renderHelper.renderLine(centerX + 20.0f, centerY - 5.0f, centerX + 12.0f, centerY - 10.0f - 15.0f, 2.0f, highlightColors[1]);
        Color[] shadowColors = theme.getShadowColors();
        renderHelper.renderLine(centerX + 46.0f, centerY - 5.0f, centerX + 53.0f, centerY - 10.0f - 15.0f, 2.0f, shadowColors[1]);
        Color[] outlineColors = theme.getOutlineColors();
        renderHelper.renderLine(centerX + 110.0f - 8.0f, centerY + 48.0f + 10.0f, centerX + 110.0f - 34.0f, centerY + 48.0f, 2.0f, outlineColors[1]);
        Color[] glowColors = theme.getGlowColors();
        renderHelper.renderLine(centerX + 110.0f - 8.0f, centerY + 20.0f - 10.0f, centerX + 110.0f - 34.0f, centerY + 20.0f, 2.0f, glowColors[1]);
        for (int i = 0; i < ITEM_POSITIONS.length; i++) {
            float itemX = centerX + ITEM_POSITIONS[i][0];
            float itemY = centerY + ITEM_POSITIONS[i][1];
            UIComponent itemComponent = itemList.get(i);
            itemComponent.render(theme, itemX, itemY, mouseX, mouseY, renderHelper, matrices);
            if (this.hoveredItem != null) {
                double d3 = itemX;
                double d4 = itemY;
                UIComponent component = itemList.get(i);
                float width = component.getWidth();
                double widthDbl = width;
                UIComponent component2 = itemList.get(i);
                float height = component2.getHeight();
                boolean isHovered = UIUtils.isHovered(d3, d4, widthDbl, height, mouseX, mouseY);
                if (isHovered) {
                    UIComponent hoveredComponent = itemList.get(i);
                    float hoveredWidth = hoveredComponent.getWidth();
                    UIComponent hoveredComponent2 = itemList.get(i);
                    float hoveredHeight = hoveredComponent2.getHeight();
                    Color highlight = new Color(255, 255, 255, 20);
                    renderHelper.renderCircle(itemX, itemY, hoveredWidth, hoveredHeight, 6.0f, highlight, matrices);
                }
            }
        }
        this.deleteButtonAnimation.state(this.hoveredItem != null);
        float elapsed = this.deleteButtonTimer.elapsedUnits();
        this.deleteButtonAnimation.update(elapsed);
        float animationProgress = this.deleteButtonAnimation.animation();
        if (this.hoveredItem != null) {
            matrices.method_22903();
            matrices.method_22904(this.deleteButtonX, this.deleteButtonY, 0.0);
            matrices.method_22905(animationProgress, animationProgress, animationProgress);
            matrices.method_22904(-this.deleteButtonX, -this.deleteButtonY, 0.0);
            float btnX = (float) this.deleteButtonX - 0.5f;
            float btnY = (float) this.deleteButtonY - 0.5f;
            Color borderColor = Color.decode("#222222");
            renderHelper.renderCircle(btnX, btnY, 51.0f, 24.0f, 10.0f, borderColor, matrices);
            float deleteBtnX = (float) this.deleteButtonX;
            float deleteBtnY = (float) this.deleteButtonY;
            Color backgroundColor2 = Color.decode("#181818");
            renderHelper.renderCircle(deleteBtnX, deleteBtnY, 50.0f, 23.0f, 10.0f, backgroundColor2, matrices);
            this.deleteButton.render(theme, (float) this.deleteButtonX + 4.0f, (float) this.deleteButtonY + 4.0f, mouseX, mouseY, renderHelper, matrices);
            matrices.method_22909();
        }
    }

    @Override
    public void onMouseAction(float x, float y, double mouseX, double mouseY, int actionButton) {
        float centerX = x + this.width / 2.0f - 50.0f;
        float centerY = y + this.height / 2.0f - 50.0f;
        List<UIComponent> items = Melonity.containerManager.getElements();
        List<UIComponent> itemList = items;
        boolean clicked = false;
        if (this.hoveredItem != null) {
            double btnX = (float) this.deleteButtonX + 4.0f;
            double btnY = (float) this.deleteButtonY + 4.0f;
            float btnWidth = this.deleteButton.getWidth();
            double btnW = btnWidth;
            float btnHeight = this.deleteButton.getHeight();
            boolean isHovered = UIUtils.isHovered(btnX, btnY, btnW, btnHeight, mouseX, mouseY);
            if (isHovered) {
                this.hoveredItem = null;
            }
        }
        for (int i = 0; i < ITEM_POSITIONS.length; i++) {
            float itemX = centerX + ITEM_POSITIONS[i][0];
            float itemY = centerY + ITEM_POSITIONS[i][1];
            double itemXDbl = itemX;
            double itemYDbl = itemY;
            UIComponent component = itemList.get(i);
            float itemWidth = component.getWidth();
            double widthDbl = itemWidth;
            UIComponent component2 = itemList.get(i);
            float itemHeight = component2.getHeight();
            boolean isHovered = UIUtils.isHovered(itemXDbl, itemYDbl, widthDbl, itemHeight, mouseX, mouseY);
            if (!isHovered) continue;
            if (actionButton == 0 && this.hoveredItem != null) {
                UIComponent clickedComponent = itemList.get(i);
                clickedComponent.onAction(this.hoveredItem);
                this.hoveredItem = null;
            }
            if (actionButton != 1) continue;
            UIComponent rightClickedComponent = itemList.get(i);
            Object itemId = rightClickedComponent.getId();
            if (itemId == null) continue;
            UIComponent component3 = itemList.get(i);
            Object itemId2 = component3.getId();
            String name = itemId2.toString();
            this.hoveredItem = name;
            this.deleteButtonX = mouseX;
            this.deleteButtonY = mouseY;
            clicked = true;
        }
        if (!clicked) {
            this.hoveredItem = null;
        }
    }

    @Override
    public boolean onKeyAction(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void tick() {
    }

    @Generated
    public void setHoveredItem(Object hoveredItem) {
        this.hoveredItem = hoveredItem != null ? hoveredItem.toString() : null;
    }

    @Generated
    public void setHoveredItemName(String hoveredItemName) {
        this.hoveredItemName = hoveredItemName;
    }

    @Generated
    public void setDeleteButtonX(double deleteButtonX) {
        this.deleteButtonX = deleteButtonX;
    }

    @Generated
    public void setDeleteButtonY(double deleteButtonY) {
        this.deleteButtonY = deleteButtonY;
    }
}