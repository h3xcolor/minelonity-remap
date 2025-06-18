// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.m.c;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.m.Macro;
import ru.melonity.m.MacroAction;
import ru.melonity.m.MacroCategory;
import ru.melonity.m.MacroManager;
import ru.melonity.m.MacroWheel;
import ru.melonity.s.d.TextFieldWidget;
import ru.melonity.s.e.a.IconButtonWidget;
import ru.melonity.w.UIHelper;
import ru.melonity.w.WidgetHelper;

@Environment(EnvType.CLIENT)
public class MacroBuilderScreen extends Screen {
    private final FrameWeightCalculator animationTimer = FrameWeightCalculator.milliseconds(370L);
    private final StateAnimation screenAnimation = new StateAnimation();
    private float centerX;
    private float centerY;
    private final float width = 376.0f;
    private final float height = 270.0f;
    private final List<MacroAction> macroActions = Lists.newCopyOnWriteArrayList();
    private float scrollOffset = 0.0f;
    private float targetScrollOffset = 0.0f;
    private boolean initialScrollSet = false;
    private final FrameWeightCalculator scrollAnimation = FrameWeightCalculator.milliseconds(500L);
    private MacroAction draggedAction = null;
    private int dragIndex = -1;
    private float dragStartX;
    private float dragStartY;
    private float dragCurrentX;
    private float dragCurrentY;
    private final TextFieldWidget macroNameField = new TextFieldWidget("Macro name", false, 115.0f, 16.0f);
    private final IconButtonWidget saveButton = new IconButtonWidget(new Identifier("melonity/images/ui/icons/navbar/save.png"), "Save", 114.0f, 16.0f, 8.0f, 8.0f);
    private final IconButtonWidget saveAndAddButton = new IconButtonWidget(new Identifier("melonity/images/ui/add.png"), "Save and add to wheel", 115.0f, 16.0f, 10.0f, 10.0f);
    private final MacroWheel macroWheel;
    public static int obfuscationControl = 1369113864;

    public MacroBuilderScreen(MacroWheel macroWheel) {
        super(Text.literal("Macro builder"));
        this.macroNameField.setBackgroundColor(Color.decode("#888888"));
        this.macroNameField.setEditable(true);
        this.macroNameField.setVisible(true);
        this.saveButton.setEditable(true);
        this.saveButton.setVisible(true);
        this.saveAndAddButton.setEditable(true);
        this.saveAndAddButton.setVisible(true);
        this.macroWheel = macroWheel;
        if (macroWheel != null) {
            this.macroNameField.setEditable(true);
            this.macroNameField.setText(macroWheel.getName());
            this.macroActions.addAll(macroWheel.getActions());
            for (MacroAction action : this.macroActions) {
                if (!(action instanceof Macro)) continue;
                Macro macro = (Macro)action;
                macro.setEditable(true);
            }
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int n;
        DrawContext matrices = context;
        WidgetHelper widgetHelper = Melonity.getInstance().getWidgetHelper();
        UIHelper uiHelper = Melonity.getInstance().getUIHelper();
        MacroManager macroManager = Melonity.getInstance().getMacroManager();
        this.centerX = (float)this.width / 2.0f - 188.0f;
        this.centerY = (float)this.height / 2.0f - 135.0f;
        this.screenAnimation.updateState(true);
        float elapsed = this.animationTimer.elapsedUnits();
        this.screenAnimation.update(elapsed);
        float animationProgress = this.screenAnimation.getAnimationProgress();
        matrices.getMatrices().push();
        matrices.getMatrices().translate(this.centerX + 188.0f, this.centerY + 135.0f, 0.0f);
        matrices.getMatrices().scale(animationProgress, animationProgress, animationProgress);
        matrices.getMatrices().translate(-(this.centerX + 188.0f), -(this.centerY + 135.0f), 0.0f);
        Identifier overlayTexture = new Identifier("melonity/images/ui/overlay_blocks.png");
        Color backgroundColor = widgetHelper.getBackgroundColor();
        UIHelper.drawRoundedRectangle(overlayTexture, this.centerX, this.centerY - 17.0f + 2.0f, 376.0f, 17.0f, backgroundColor, matrices);
        Color headerColor = widgetHelper.getHeaderColor();
        UIHelper.drawRoundedRectangle(this.centerX, this.centerY, 376.0f, 25.0f, headerColor, matrices);
        Color borderColor = widgetHelper.getBorderColor();
        UIHelper.drawRoundedRectangle(this.centerX, this.centerY, 376.0f, 270.0f, 12.0f, borderColor, matrices);
        Identifier backButtonTexture = new Identifier("melonity/images/ui/autobuy/back_button.png");
        UIHelper.drawTexture(backButtonTexture, this.centerX + 8.0f, this.centerY + 8.0f, 16.0f, 16.0f, matrices);
        boolean backButtonHovered = UIHelper.isHovered(this.centerX + 8.0f, this.centerY + 8.0f, 16.0, 16.0, mouseX, mouseY);
        if (backButtonHovered) {
            Color hoverColor = new Color(255, 255, 255, 20);
            UIHelper.drawRoundedRectangle(this.centerX + 8.0f, this.centerY + 8.0f, 16.0f, 16.0f, 8.0f, hoverColor, matrices);
        }
        UIHelper.drawText(uiHelper.getTitleFont(), "Macros builder", this.centerX + 28.0f, this.centerY + 10.0f, matrices);
        Color subtitleColor = widgetHelper.getSubtitleColor();
        UIHelper.drawText(uiHelper.getSubtitleFont(), "Building your own macro", this.centerX + 28.0f, this.centerY + 22.0f, subtitleColor, matrices);
        Color separatorColor = Color.decode("#222222");
        UIHelper.drawHorizontalLine(this.centerX, this.centerY + 35.0f, 376.0f, 1.0f, separatorColor, matrices);
        Color panelColor = widgetHelper.getPanelColor();
        UIHelper.drawRoundedRectangle(this.centerX + 6.0f, this.centerY + 42.0f, 178.0f, 185.0f, 8.0f, panelColor, matrices);
        UIHelper.drawText(uiHelper.getTitleFont(), "Macro structure", this.centerX + 12.0f, this.centerY + 52.0f, matrices);
        Color panelSeparatorColor = Color.decode("#222222");
        UIHelper.drawHorizontalLine(this.centerX + 6.0f, this.centerY + 65.0f, 178.0f, 1.0f, panelSeparatorColor, matrices);
        if (!this.initialScrollSet) {
            this.targetScrollOffset = this.scrollOffset;
            this.initialScrollSet = true;
        }
        float scrollDelta = this.scrollAnimation.elapsedUnits();
        this.scrollOffset += (this.targetScrollOffset - this.scrollOffset) * 12.0f * scrollDelta;
        float actionsY = this.centerY + 75.0f + this.scrollOffset;
        boolean actionHovered = false;
        boolean dragTargetSet = false;
        int actionIndex = 0;
        while (true) {
            MacroAction action;
            if (actionIndex < this.macroActions.size()) {
                action = this.macroActions.get(actionIndex);
                if (this.draggedAction != null) {
                    boolean dragAreaHovered = UIHelper.isHovered(this.centerX, actionsY - 15.0f, 164.0, 46.0, this.dragCurrentX, this.dragCurrentY, mouseX, mouseY);
                    if (dragAreaHovered) {
                        this.dragIndex = actionIndex;
                        actionHovered = true;
                        dragTargetSet = true;
                        StateAnimation actionAnimation = action.getAnimation();
                        actionAnimation.updateState(true);
                        Color dragHighlightBorder = Color.decode("#222222");
                        UIHelper.drawRoundedRectangle(this.centerX + 12.0f - 0.5f, actionsY - 0.5f, 165.0f, 17.0f, 8.0f, dragHighlightBorder, matrices);
                        Color dragHighlightFill = Color.decode("#181818");
                        UIHelper.drawRoundedRectangle(this.centerX + 12.0f, actionsY, 164.0f, 16.0f, 8.0f, dragHighlightFill, matrices);
                    }
                }
                StateAnimation actionAnimation = action.getAnimation();
                FrameWeightCalculator actionTimer = action.getTimer();
                float actionElapsed = actionTimer.elapsedUnits();
                actionAnimation.update(actionElapsed);
                float actionAnimationProgress = actionAnimation.getAnimationProgress();
                actionsY += 20.0f * actionAnimationProgress;
                UIHelper.setScissor(this.centerX, this.centerY + 75.0f - 1.0f, 188.0, 150.0);
                this.renderAction(action, this.centerX + 12.0f, actionsY, mouseX, mouseY, widgetHelper, matrices);
                Identifier deleteIcon = new Identifier("melonity/images/ui/baritone/delete.png");
                boolean deleteHovered = UIHelper.isHovered(this.centerX + 168.0f - 5.0f, actionsY + 3.5f, 8.0, 8.0, mouseX, mouseY);
                UIHelper.drawTexture(deleteIcon, this.centerX + 168.0f - 5.0f, actionsY + 3.5f, 8.0f, 8.0f, deleteHovered ? Color.LIGHT_GRAY : Color.WHITE, matrices);
                UIHelper.resetScissor();
                float actionHeight = action.getHeight();
                actionsY += 20.0f + actionHeight;
                actionIndex++;
            } else {
                if (this.draggedAction != null && !dragTargetSet) {
                    boolean dragAreaHovered = UIHelper.isHovered(this.centerX, actionsY - 15.0f, 164.0, 46.0, this.dragCurrentX, this.dragCurrentY, mouseX, mouseY);
                    if (dragAreaHovered) {
                        this.dragIndex = this.macroActions.size();
                        actionHovered = true;
                    }
                }
                if (!actionHovered) {
                    this.dragIndex = -1;
                }
                float actionsPanelX = this.centerX + 2.0f + 188.0f;
                Color actionsPanelColor = widgetHelper.getPanelColor();
                UIHelper.drawRoundedRectangle(actionsPanelX, this.centerY + 42.0f, 178.0f, 185.0f, 8.0f, actionsPanelColor, matrices);
                UIHelper.drawText(uiHelper.getTitleFont(), "Action list", actionsPanelX + 8.0f, this.centerY + 52.0f, matrices);
                Color actionsSeparatorColor = Color.decode("#222222");
                UIHelper.drawHorizontalLine(actionsPanelX, this.centerY + 65.0f, 178.0f, 1.0f, actionsSeparatorColor, matrices);
                float categoryY = this.centerY + 75.0f;
                for (MacroCategory category : MacroCategory.values()) {
                    String categoryName = category.name().toLowerCase();
                    Identifier categoryIcon = new Identifier("melonity/images/ui/macros/category/" + categoryName + ".png");
                    UIHelper.drawTexture(categoryIcon, actionsPanelX + 13.0f, categoryY, 9.0f, 9.0f, matrices);
                    String displayName = category.name().charAt(0) + categoryName.substring(1);
                    UIHelper.drawText(uiHelper.getTitleFont(), displayName, actionsPanelX + 25.0f, categoryY + 3.0f, matrices);
                    List<MacroAction> categoryActions = macroManager.getActionsForCategory(category);
                    for (MacroAction categoryAction : categoryActions) {
                        this.renderAction(categoryAction, actionsPanelX + 9.0f, categoryY, mouseX, mouseY, widgetHelper, matrices);
                        categoryY += 20.0f;
                    }
                    categoryY += 5.0f;
                }
                break;
            }
        }
        float footerY = this.centerY + 270.0f - 36.0f;
        UIHelper.drawRoundedRectangle(this.centerX + 6.0f, footerY, 364.0f, 27.0f, 8.0f, widgetHelper.getFooterColor(), matrices);
        this.macroNameField.render(this.centerX + 13.0f, footerY + 5.0f, mouseX, mouseY, widgetHelper, matrices);
        this.saveButton.render(this.centerX + 131.0f, footerY + 5.0f, mouseX, mouseY, widgetHelper, matrices);
        this.saveAndAddButton.render(this.centerX + 248.0f, footerY + 5.0f, mouseX, mouseY, widgetHelper, matrices);
        matrices.getMatrices().pop();
    }

    private void renderAction(MacroAction action, float x, float y, int mouseX, int mouseY, WidgetHelper widgetHelper, DrawContext matrices) {
        if (!(action instanceof Macro)) {
            action.render(x, y, 164.0f, 16.0f, mouseX, mouseY, widgetHelper, matrices);
        }
        this.renderActionContent(action, x, y, mouseX, mouseY, widgetHelper, matrices);
        if (action instanceof Macro) {
            action.render(x, y, 164.0f, 16.0f, mouseX, mouseY, widgetHelper, matrices);
        }
    }

    private void renderActionContent(MacroAction action, float x, float y, int mouseX, int mouseY, WidgetHelper widgetHelper, DrawContext matrices) {
        widgetHelper.drawRoundedRectangle(x, y, 164.0f, 16.0f, 8.0f, widgetHelper.getActionBackgroundColor(), matrices);
        widgetHelper.drawTexture(new Identifier("melonity/images/ui/macros/grip.png"), x + 4.0f, y + 3.5f, 8.0f, 8.5f, matrices);
        widgetHelper.drawText(widgetHelper.getActionFont(), action.getName(), x + 15.0f, y + 6.5f, widgetHelper.getActionTextColor(), matrices);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        MacroManager macroManager = Melonity.getInstance().getMacroManager();
        boolean backButtonClicked = UIHelper.isHovered(this.centerX + 8.0f, this.centerY + 8.0f, 16.0, 16.0, mouseX, mouseY);
        if (backButtonClicked) {
            Screen previousScreen = Melonity.getInstance().getPreviousScreen();
            this.client.setScreen(previousScreen);
        }
        float footerY = this.centerY + 270.0f - 36.0f;
        double saveButtonX = this.centerX + 248.0f;
        double saveButtonY = footerY + 5.0f;
        float saveButtonWidth = this.saveAndAddButton.getWidth();
        float saveButtonHeight = this.saveAndAddButton.getHeight();
        boolean saveAndAddHovered = UIHelper.isHovered(saveButtonX, saveButtonY, saveButtonWidth, saveButtonHeight, mouseX, mouseY);
        boolean saveHovered = UIHelper.isHovered(this.centerX + 131.0f, footerY + 5.0f, this.saveButton.getWidth(), this.saveButton.getHeight(), mouseX, mouseY);
        if (saveHovered || saveAndAddHovered) {
            String macroName = this.macroNameField.getText();
            if (macroName.isBlank()) {
                return true;
            }
            if (this.macroWheel != null) {
                List<MacroAction> actions = this.macroWheel.getActions();
                actions.clear();
                actions.addAll(this.macroActions);
            } else {
                Optional<MacroWheel> existingWheel = macroManager.getMacroWheel(macroName);
                if (existingWheel.isPresent()) {
                    Screen errorScreen = new ru.melonity.p.ErrorScreen("Error", "Macros already exists", "feature-disabled.png");
                    this.client.setScreen(errorScreen);
                    return true;
                }
                MacroWheel newWheel = new MacroWheel(macroName);
                newWheel.getActions().addAll(this.macroActions);
                macroManager.addMacroWheel(newWheel);
            }
            Screen macroScreen = Melonity.getInstance().getMacroScreen();
            this.client.setScreen(macroScreen);
        }
        float actionsY = this.centerY + 75.0f + this.scrollOffset;
        for (MacroAction action : this.macroActions) {
            boolean actionHovered = UIHelper.isHovered(this.centerX, this.centerY + 75.0f - 1.0f, 188.0, 150.0, mouseX, mouseY);
            if (actionHovered) {
                action.mouseClicked(this.centerX + 12.0f, actionsY, mouseX, mouseY, button);
            }
            boolean deleteHovered = UIHelper.isHovered(this.centerX + 168.0f - 5.0f, actionsY + 3.5f, 8.0, 8.0, mouseX, mouseY);
            if (deleteHovered) {
                this.macroActions.remove(action);
            }
            actionsY += 20.0f + action.getHeight();
        }
        float actionsPanelX = this.centerX + 2.0f + 188.0f;
        float categoryY = this.centerY + 75.0f;
        for (MacroCategory category : MacroCategory.values()) {
            categoryY += 15.0f;
            List<MacroAction> categoryActions = macroManager.getActionsForCategory(category);
            for (MacroAction categoryAction : categoryActions) {
                boolean categoryActionHovered = UIHelper.isHovered(actionsPanelX + 9.0f, categoryY, 164.0, 16.0, mouseX, mouseY);
                if (categoryActionHovered) {
                    this.draggedAction = categoryAction;
                    this.dragStartX = actionsPanelX + 9.0f;
                    this.dragStartY = categoryY;
                    this.dragCurrentX = (float)mouseX - this.dragStartX;
                    this.dragCurrentY = (float)mouseY - this.dragStartY;
                }
                categoryY += 20.0f;
            }
            categoryY += 5.0f;
        }
        this.macroNameField.mouseClicked(this.centerX + 13.0f, footerY + 5.0f, mouseX, mouseY, button);
        this.macroNameField.mouseClicked(this.centerX + 13.0f, footerY + 5.0f, mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        this.targetScrollOffset += (float)deltaY * 20.0f;
        if (this.targetScrollOffset > 0.0f) {
            this.targetScrollOffset = 0.0f;
        }
        float maxScroll = 0.0f;
        for (MacroAction action : this.macroActions) {
            maxScroll += 20.0f + action.getHeight();
        }
        maxScroll -= 40.0f;
        if (this.targetScrollOffset < -maxScroll) {
            this.targetScrollOffset = -maxScroll;
        }
        return true;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.draggedAction != null && this.dragIndex != -1) {
            Class<?> actionClass = this.draggedAction.getClass();
            try {
                Constructor<?> constructor = actionClass.getConstructor();
                Object newAction = constructor.newInstance();
                if (newAction instanceof Macro) {
                    ((Macro)newAction).setEditable(true);
                }
                this.macroActions.add(this.dragIndex, (MacroAction)newAction);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.draggedAction = null;
        this.dragIndex = -1;
        for (MacroAction action : this.macroActions) {
            action.resetState();
        }
        this.macroNameField.resetState();
        boolean result = super.mouseReleased(mouseX, mouseY, button);
        return result;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (MacroAction action : this.macroActions) {
            action.keyPressed(keyCode, scanCode, modifiers);
        }
        this.macroNameField.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}