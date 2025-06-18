// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b.a;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_338;
import net.minecraft.class_4587;
import net.minecraft.class_5250;
import ru.melonity.Melonity;
import ru.melonity.animation.Easings;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.h.Module;
import ru.melonity.h.ModuleCategory;
import ru.melonity.i.FontRenderer;
import ru.melonity.o.a.MouseState;
import ru.melonity.s.c.Localization;
import ru.melonity.w.RenderUtils;

@Environment(value = EnvType.CLIENT)
public class ModulePanel {
    protected float width;
    protected float height;
    private final Module module;
    private final List<Component> children = Lists.newLinkedList();
    private final FrameWeightCalculator animationCalculator = FrameWeightCalculator.milliseconds(350L);
    private final StateAnimation animation = new StateAnimation();
    private boolean expanded;
    private float panelHeight = -1.0f;
    public static int obfuscationControl = 1015703257;

    public ModulePanel(Module module) {
        this.width = module instanceof ModuleCategory ? 358.0f : 179.0f;
        this.height = 36.0f;
        this.module = module;
        this.expanded = false;
    }

    public void render(MouseState mouseState, float x, float y, double mouseX, double mouseY, RenderUtils renderer, boolean hovered, class_4587 matrices, class_332 vertexConsumerProvider) {
        if (this.module instanceof ModuleCategory) {
            this.panelHeight = 32.0f;
        } else if (this.panelHeight <= 20.0f) {
            this.panelHeight = 30.0f;
        }
        if (hovered) {
            FontRenderer fontRenderer = FontRenderer.INSTANCE;
            String moduleName = this.module.getName();
            Color textColor = Color.decode("#888888");
            renderer.drawText(fontRenderer, moduleName, x - 1.0f, y - 11.0f, textColor, matrices);
            class_2960 arrowIcon = class_2960.method_60656("melonity/images/ui/clickgui/text_arrow.png");
            String moduleDescription = this.module.getDescription();
            float textWidth = fontRenderer.getWidth(moduleDescription);
            renderer.drawTexture(arrowIcon, x - 1.0f + textWidth, y - 11.5f, 5.5f, 4.5f, matrices);
            String moduleState = this.module.getState();
            float stateTextWidth = fontRenderer.getWidth(moduleState);
            float stateX = x - 1.0f + stateTextWidth + 7.0f;
            Color stateColor = Color.decode("#888888");
            renderer.drawText(fontRenderer, moduleState, stateX, y - 11.0f, stateColor, matrices);
        }
        Color backgroundColor = mouseState.getBackgroundColor();
        renderer.drawRect(x, y, this.width + 1.0f, this.panelHeight + 4.0f, 8.0f, backgroundColor, matrices);
        float textY = 10.0f;
        Localization localization = Melonity.INSTANCE.getLocalization();
        String moduleNameKey = this.module.getName().toLowerCase() + ".name";
        String localizedName = localization.get(moduleNameKey, new Object[0]);
        if (localizedName == null) {
            localizedName = this.module.getName();
        } else {
            String moduleNameCheck = this.module.getName().concat(".name");
            if (localizedName.equalsIgnoreCase(moduleNameCheck)) {
                localizedName = this.module.getName();
            }
        }
        renderer.drawText(FontRenderer.INSTANCE, localizedName, x + 8.0f, y + textY, matrices);
        float toggleX = x + this.width - 15.0f - 10.0f;
        boolean moduleEnabled = this.module.isEnabled();
        this.animation.state(moduleEnabled);
        float elapsed = this.animationCalculator.elapsedUnits();
        this.animation.update(elapsed);
        boolean toggleHovered = RenderUtils.isHovered(toggleX, y + textY - 3.0f, 17.0, 10.0, mouseX, mouseY);
        Color[] toggleColors = Melonity.INSTANCE.getTheme().getToggleColors();
        if (moduleEnabled) {
            toggleColors = this.module.getStateColors();
        }
        if (!(this.module instanceof ModuleCategory)) {
            float animationValue = this.animation.value();
            float easedValue = Easings.CUBIC_BOTH.ease(animationValue);
            float togglePosX = toggleX + 2.0f + (float) easedValue * 5.0f;
            float togglePosY = y + textY - 2.0f;
            renderer.drawRoundedRect(toggleX, y + textY - 3.0f, 17.0f, 10.0f, 9.5f, toggleColors[0], toggleColors[1], toggleColors[1], toggleColors[0], matrices);
            if (moduleEnabled) {
                Color enabledColor = mouseState.getEnabledColor();
                renderer.drawCircle(togglePosX, togglePosY, 8.0f, 8.0f, 8.0f, enabledColor, matrices);
            } else if (toggleHovered) {
                Color hoveredColor = mouseState.getHoveredColor();
                Color toggleColor = RenderUtils.darken(hoveredColor, 200);
                renderer.drawCircle(togglePosX, togglePosY, 8.0f, 8.0f, 8.0f, toggleColor, matrices);
            } else {
                Color disabledColor = mouseState.getDisabledColor();
                renderer.drawCircle(togglePosX, togglePosY, 8.0f, 8.0f, 8.0f, disabledColor, matrices);
            }
        }
        float descriptionSpacing = FontRenderer.INSTANCE.getHeight("1234");
        textY += descriptionSpacing + 2.0f;
        Localization descLocalization = Melonity.INSTANCE.getLocalization();
        String moduleDescKey = this.module.getName().toLowerCase() + ".description";
        String localizedDescription = descLocalization.get(moduleDescKey, new Object[0]);
        if (localizedDescription != null) {
            String[] lines = localizedDescription.split("\n");
            List<String> lineList = Arrays.asList(lines);
            Iterator<String> lineIterator = lineList.iterator();
            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                int lineIndex = lineList.indexOf(line);
                float lineY = y + textY + (float) (lineIndex * 7);
                Color lineColor = Color.decode("#888888");
                renderer.drawText(FontRenderer.SMALL, line, x + 8.0f, lineY, lineColor, matrices);
            }
        }
        boolean hasDescription = localizedDescription != null && localizedDescription.contains("\n");
        textY = this.height - 5.0f + (hasDescription ? 18 : 15);
        if (this.module instanceof ModuleCategory) {
            textY -= 32.0f;
        }
        Iterator<Component> childIterator = this.children.iterator();
        while (childIterator.hasNext()) {
            Component child = childIterator.next();
            if (this.expanded) {
                if (child instanceof ConfigComponent) {
                    int configId = ((ConfigComponent) child).getConfigId();
                    if (configId == -1) continue;
                }
            }
            child.render(mouseState, x + 8.0f, y + textY, this.width, mouseX, mouseY, renderer, matrices, vertexConsumerProvider);
            float childHeight = child.getHeight();
            textY += childHeight;
        }
        if (!this.children.isEmpty() && !(this.module instanceof ModuleCategory)) {
            boolean hasNewlines = localizedDescription != null && localizedDescription.contains("\n");
            float separatorY = y + (hasNewlines ? 40 : 35);
            Color separatorColor = Color.decode("#222222");
            renderer.drawLine(x, separatorY, this.width, 1.0f, separatorColor, matrices);
        }
    }

    public void mouseClicked(float x, float y, double mouseX, double mouseY, int button) {
        float toggleX = x + this.width - 15.0f - 10.0f;
        boolean toggleHovered = RenderUtils.isHovered(toggleX, y + 10.0f - 3.0f, 17.0, 10.0, mouseX, mouseY);
        if (toggleHovered && !(this.module instanceof ModuleCategory)) {
            boolean safeMode = Melonity.INSTANCE.getSafeMode();
            if (safeMode) {
                if (this.module.isSafe()) {
                    class_310 client = class_310.method_1551();
                    class_338 chatHud = client.field_1705.method_1743();
                    class_5250 message = class_2561.method_43470("Включен melonity safe, функция помечена как небезопасная");
                    chatHud.method_1812(message);
                    return;
                }
            }
            boolean enabled = this.module.isEnabled();
            this.module.setEnabled(!enabled);
        }
        Localization localization = Melonity.INSTANCE.getLocalization();
        String moduleNameKey = this.module.getName().toLowerCase() + ".description";
        String localizedDescription = localization.get(moduleNameKey, new Object[0]);
        boolean hasNewlines = localizedDescription != null && localizedDescription.contains("\n");
        float childY = this.height - 5.0f + (hasNewlines ? 18 : 15);
        if (this.module instanceof ModuleCategory) {
            childY -= 32.0f;
        }
        Iterator<Component> iterator = this.children.iterator();
        while (iterator.hasNext()) {
            Component child = iterator.next();
            if (!(child instanceof ConfigComponent)) continue;
            boolean handled = child.mouseClicked(x + 8.0f, y + childY, this.width, mouseX, mouseY, button);
            if (handled) {
                return;
            }
            float childHeight = child.getHeight();
            childY += childHeight;
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        this.children.forEach(Component::keyPressed);
    }

    public float getTotalHeight(float spacing) {
        if (this.panelHeight <= 16.0f) {
            return 0.0f;
        }
        float totalHeight = this.height + spacing;
        Iterator<Component> iterator = this.children.iterator();
        while (iterator.hasNext()) {
            Component child = iterator.next();
            if (this.expanded) {
                if (child instanceof ConfigComponent) {
                    int configId = ((ConfigComponent) child).getConfigId();
                    if (configId == -1) continue;
                }
            }
            float childHeight = child.getHeight();
            totalHeight += childHeight + spacing;
        }
        return totalHeight;
    }

    public float calculateHeight() {
        Localization localization = Melonity.INSTANCE.getLocalization();
        String moduleNameKey = this.module.getName().toLowerCase() + ".description";
        String localizedDescription = localization.get(moduleNameKey, new Object[0]);
        int descriptionHeight;
        if (localizedDescription == null) {
            descriptionHeight = 6;
        } else {
            boolean hasNewlines = localizedDescription.contains("\n");
            descriptionHeight = hasNewlines ? 15 : 6;
        }
        float height = descriptionHeight;
        boolean hasChildren = !this.children.isEmpty();
        float childrenHeight = hasChildren ? 16.0f : 0.0f;
        height += childrenHeight;
        Iterator<Component> iterator = this.children.iterator();
        while (iterator.hasNext()) {
            Component child = iterator.next();
            if (this.expanded) {
                if (child instanceof ConfigComponent) {
                    int configId = ((ConfigComponent) child).getConfigId();
                    if (configId == -1) continue;
                }
            }
            float childHeight = child.getHeight();
            height += childHeight;
        }
        this.panelHeight = height + 14.0f;
        if (this.panelHeight <= 20.0f) {
            this.panelHeight = 30.0f;
        }
        float totalHeight = this.height;
        Iterator<Component> childIterator = this.children.iterator();
        while (childIterator.hasNext()) {
            Component child = childIterator.next();
            if (this.expanded) {
                if (child instanceof ConfigComponent) {
                    int configId = ((ConfigComponent) child).getConfigId();
                    if (configId == -1) continue;
                }
            }
            float childHeight = child.getHeight();
            totalHeight += childHeight;
        }
        return this.panelHeight - 13.0f;
    }

    @Generated
    public float getWidth() {
        return this.width;
    }

    @Generated
    public Module getModule() {
        return this.module;
    }

    @Generated
    public List<Component> getChildren() {
        return this.children;
    }

    @Generated
    public FrameWeightCalculator getAnimationCalculator() {
        return this.animationCalculator;
    }

    @Generated
    public StateAnimation getAnimation() {
        return this.animation;
    }

    @Generated
    public boolean isExpanded() {
        return this.expanded;
    }

    @Generated
    public float getHeight() {
        return this.height;
    }

    @Generated
    public void setWidth(float width) {
        this.width = width;
    }

    @Generated
    public void setHeight(float height) {
        this.height = height;
    }

    @Generated
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Generated
    public void setPanelHeight(float panelHeight) {
        this.panelHeight = panelHeight;
    }
}