// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b.a.b;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.o.b.a.IRenderable;
import ru.melonity.s.a.IContextProvider;
import ru.melonity.w.IAnimationHelper;

import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class OptionGroup implements IRenderable {
    private final FrameWeightCalculator scrollTimer;
    private final String title;
    private final List<String> options;
    private final SelectionMode selectionMode;
    private List<String> selectedOptions;
    private boolean expanded = false;
    private final StateAnimation expandAnimation = new StateAnimation();
    private final StateAnimation scrollAnimation = new StateAnimation();
    private final FrameWeightCalculator animationTimer = FrameWeightCalculator.milliseconds(200L);

    public OptionGroup(String title, SelectionMode selectionMode, String initialSelection, List<String> options) {
        this.title = title;
        this.selectionMode = selectionMode;
        this.options = options;
        this.selectedOptions = Lists.newLinkedList();
        this.selectedOptions.add(initialSelection);
        this.scrollTimer = FrameWeightCalculator.milliseconds(1000 / class_3532.method_15340(options.size(), 3, 5));
    }

    public OptionGroup(String title, SelectionMode selectionMode, List<String> initialSelections, List<String> options) {
        this.title = title;
        this.selectionMode = selectionMode;
        this.options = options;
        this.selectedOptions = Lists.newLinkedList();
        this.selectedOptions.addAll(initialSelections);
        this.scrollTimer = FrameWeightCalculator.milliseconds(1000 / class_3532.method_15340(options.size(), 3, 5));
    }

    public OptionGroup(String title, SelectionMode selectionMode, List<String> initialSelections, String... options) {
        this.title = title;
        this.selectionMode = selectionMode;
        this.options = Arrays.asList(options);
        this.selectedOptions = Lists.newLinkedList();
        this.selectedOptions.addAll(initialSelections);
        this.scrollTimer = FrameWeightCalculator.milliseconds(1000 / class_3532.method_15340(this.options.size(), 3, 5));
    }

    public OptionGroup(String title, SelectionMode selectionMode, String initialSelection, String... options) {
        this.title = title;
        this.selectionMode = selectionMode;
        this.options = Arrays.asList(options);
        this.selectedOptions = Lists.newLinkedList();
        this.selectedOptions.add(initialSelection);
        this.scrollTimer = FrameWeightCalculator.milliseconds(1000 / class_3532.method_15340(this.options.size(), 3, 5));
    }

    public boolean isSelected(String option) {
        return this.selectedOptions.contains(option);
    }

    @Override
    public void render(ru.melonity.o.a.IRenderContext context, float x, float y, float width, double mouseX, double mouseY, IContextProvider renderer, class_4587 matrix, class_332 guiHelper) {
        IContextProvider contextProvider = this.getContextProvider();
        String titleText = contextProvider.getText(title);
        renderer.render(ru.melonity.i.ITextConstants.TITLE, titleText, x, y, matrix);
        
        int barColor = context.getBarColor();
        renderer.renderRect(x, y + 10.0f, width - 16.0f + 1.0f, 4.0f, barColor, matrix);
        
        StringBuilder builder = new StringBuilder();
        if (selectedOptions.isEmpty()) {
            builder.append("N/A");
        } else {
            for (int i = 0; i < selectedOptions.size(); i++) {
                String option = selectedOptions.get(i);
                String optionText = contextProvider.getText(option);
                builder.append(optionText);
                if (i != selectedOptions.size() - 1) {
                    builder.append(", ");
                }
            }
        }
        this.expandAnimation.setState(expanded);
        float expandTime = animationTimer.elapsedUnits();
        this.expandAnimation.update(expandTime);
        float expandProgress = this.expandAnimation.getProgress() * 180.0f;
        
        matrix.method_22903();
        matrix.method_46416(x + width - 19.0f - 7.5f + 3.75f, y + 13.5f + 3.75f, 0.0f);
        matrix.method_22907(class_7833.field_40718.rotationDegrees(expandProgress));
        matrix.method_46416(-(x + width - 19.0f - 7.5f + 3.75f), -(y + 13.5f + 3.75f), 0.0f);
        
        class_2960 arrowIcon = class_2960.method_60656("melonity/images/ui/icons/up.png");
        int baseAlpha = 255 - (int)(Math.abs(expandProgress) / 180.0f * 136.0f);
        baseAlpha = class_3532.method_15340(baseAlpha, 0, 255);
        Color arrowColor = new Color(baseAlpha, baseAlpha, baseAlpha);
        renderer.renderTexture(arrowIcon, x + width - 19.0f - 7.5f, y + 13.5f, 7.5f, 7.5f, arrowColor, matrix);
        matrix.method_22909();
        
        String textValue = builder.toString();
        if (textValue.length() > 38) {
            textValue = textValue.substring(0, 38);
        }
        
        Color textColor = Color.decode("#888888");
        renderer.render(ru.melonity.i.ITextConstants.OPTION_TEXT, textValue, x + 4.0f, y + 15.5f, textColor, matrix);
        
        this.scrollAnimation.setState(expanded);
        float scrollTime = scrollTimer.elapsedUnits();
        this.scrollAnimation.update(scrollTime);
        float scrollProgress = scrollAnimation.getProgress();
        
        if (scrollProgress > 0.02f) {
            float currentY = 16.0f;
            float listHeight = (options.size() * 14) * scrollProgress + 1.0f;
            int listColor = context.getListColor();
            renderer.renderRect(x - 0.5f, y + 10.0f + currentY - 0.5f, width - 16.0f + 1.0f, listHeight, 4.0f, listColor[0], matrix);
            
            Iterator<String> optionsIterator = options.iterator();
            while (optionsIterator.hasNext()) {
                String option = optionsIterator.next();
                int optionIndex = options.indexOf(option);
                int optionsSize = options.size();
                float optionHeight = (optionIndex == optionsSize - 1 ? 14 : 15) * scrollProgress;
                float offset = 0.0f;
                if (optionIndex == 0 || optionIndex == optionsSize - 1) {
                    offset = 4.0f;
                }
                
                int optionColor;
                if (optionIndex % 2 == 0) {
                    optionColor = context.getListOptionColors()[0];
                } else {
                    optionColor = context.getListOptionColors()[1];
                }
                renderer.renderRect(x, y + 10.0f + currentY, width - 16.0f, optionHeight, offset, optionColor, matrix);
                
                String displayText = contextProvider.getText(option);
                while (renderer.getTextWidth(ru.melonity.i.ITextConstants.OPTION_TEXT, displayText) > width - 20.0f) {
                    displayText = displayText.substring(0, displayText.length() - 1);
                }
                
                if (scrollProgress > 0.4f) {
                    Color displayColor = isSelected(option) ? Color.WHITE : Color.decode("#888888");
                    renderer.render(ru.melonity.i.ITextConstants.OPTION_TEXT, displayText, x + 4.0f, y + 15.5f + currentY, displayColor, matrix);
                }
                currentY += 14.0f * scrollProgress;
            }
        }
    }

    private int getTextWidth(String text, IContextProvider contextProvider) {
        return (int) contextProvider.getTextWidth(text);
    }

    @Override
    public boolean onMouseClicked(float x, float y, float width, double mouseX, double mouseY, int button) {
        if (IAnimationHelper.isPointInBox(x, y + 10.0f, width - 16.0f, 14.0, mouseX, mouseY)) {
            expanded = !expanded;
        }
        
        if (expanded) {
            float currentY = 16.0f;
            List<String> originalOrder = selectedOptions.stream().collect(Collectors.toList());
            Iterator<String> optionsIterator = options.iterator();
            
            while (optionsIterator.hasNext()) {
                String option = optionsIterator.next();
                float animationProgress = this.scrollAnimation.getProgress();
                if (IAnimationHelper.isPointInBox(x, y + 11.0f + currentY, width, 13.0, mouseX, mouseY) && button == 0) {
                    try {
                        boolean isCurrentlySelected = selectedOptions.contains(option);
                        if (isCurrentlySelected) {
                            if (selectionMode == SelectionMode.SINGLE) {
                                selectedOptions.remove(option);
                            }
                        } else {
                            if (selectionMode == SelectionMode.SINGLE) {
                                selectedOptions.clear();
                            }
                            selectedOptions.add(option);
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
                currentY += 14.0f * animationProgress;
            }
        }
        return false;
    }

    @Override
    public void onRemoved() {
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public float getHeight() {
        return 34.0f + (options.size() * 14 + 4) * this.expandAnimation.getProgress();
    }

    @Override
    public void resetState() {
        this.expandAnimation.reset();
    }

    public FrameWeightCalculator getScrollTimer() {
        return this.scrollTimer;
    }

    public String getTitle() {
        return this.title;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public SelectionMode getSelectionMode() {
        return this.selectionMode;
    }

    public List<String> getSelectedOptions() {
        return this.selectedOptions;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public StateAnimation getExpandAnimation() {
        return this.expandAnimation;
    }

    public StateAnimation getScrollAnimation() {
        return this.scrollAnimation;
    }

    public FrameWeightCalculator getAnimationTimer() {
        return this.animationTimer;
    }

    public void setSelectedOptions(List<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Environment(EnvType.CLIENT)
    public enum SelectionMode {
        SINGLE,
        MULTIPLE
    }
}