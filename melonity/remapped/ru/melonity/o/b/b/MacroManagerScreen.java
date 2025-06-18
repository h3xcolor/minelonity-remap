// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.m.Macro;
import ru.melonity.m.MacroManager;
import ru.melonity.m.action.FunctionToggleAction;
import ru.melonity.m.action.UseItemAction;
import ru.melonity.m.action.WaitAction;
import ru.melonity.o.a.MouseEvent;
import ru.melonity.s.c.RenderUtils;
import ru.melonity.s.e.BaseScreen;
import ru.melonity.s.e.a.BaseButton;
import ru.melonity.w.FontUtils;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Environment(value = EnvType.CLIENT)
public class MacroManagerScreen extends BaseScreen {
    private String selectedMacroName = "";
    private final MacroManager.Label macroListLabel = new MacroManager.Label("Macros list", "Macros you have created or imported");
    private final MacroManager.Label newMacroLabel = new MacroManager.Label("New macro", "Build or import from clipboard macro", 174.0f, 82.0f);
    private final MacroManager.Label selectedMacroLabel = new MacroManager.Label("Selected macro", "Managing the selected macro from the list", 174.0f, 127.0f);
    private final BaseButton openBuilderButton = new BaseButton("Open builder", 158.0f, 16.0f);
    private final BaseButton importFromClipboardButton;
    private final BaseButton addToWheelButton;
    private final BaseButton editInBuilderButton;
    private final BaseButton exportToClipboardButton;
    private final BaseButton deleteButton;

    public MacroManagerScreen(float width, float height) {
        super(width, height);
        this.openBuilderButton.setSelected(true);
        this.importFromClipboardButton = new BaseButton("Import from clipboard", 158.0f, 16.0f);
        this.importFromClipboardButton.setSelected(true);
        this.addToWheelButton = new BaseButton("Add to wheel", 158.0f, 16.0f);
        this.addToWheelButton.setSelected(true);
        this.editInBuilderButton = new BaseButton("Edit in builder", 158.0f, 16.0f);
        this.editInBuilderButton.setSelected(true);
        this.exportToClipboardButton = new BaseButton("Export to clipboard", 158.0f, 16.0f);
        this.exportToClipboardButton.setSelected(true);
        this.deleteButton = new BaseButton("Delete", 158.0f, 16.0f);
        this.deleteButton.setSelected(true);
    }

    @Override
    public void render(MouseEvent mouseEvent, float x, float y, double mouseX, double mouseY, RenderUtils renderer, MatrixStack matrixStack) {
        super.render(mouseEvent, x, y, mouseX, mouseY, renderer, matrixStack);
        MacroManager macroManager = Melonity.getMacroManager();
        MacroManager macroManagerInstance = macroManager;
        float cursorY = y + 40.0f;
        List<Macro> macros = macroManagerInstance.getMacros();
        Iterator<Macro> macroIterator = macros.iterator();

        while (macroIterator.hasNext()) {
            Macro macro = macroIterator.next();
            String macroName = macro.getName();
            boolean isSelected = macroName.equals(this.selectedMacroName);
            boolean isHovered = RenderUtils.isPointWithinBounds(x + 8.0f, cursorY, 164.0, 16.0, mouseX, mouseY);
            macro.getSelectAnimation().state(isSelected);
            StateAnimation animation = macro.getHoverAnimation();
            FrameWeightCalculator frameWeightCalculator = macro.getFrameWeightCalculator();
            float elapsedUnits = frameWeightCalculator.elapsedUnits();
            animation.update(elapsedUnits);
            float animationProgress = 0.0f;
            List<Macro.Action> actions = macro.getActions();
            Iterator<Macro.Action> actionIterator = actions.iterator();
            while (actionIterator.hasNext()) {
                Macro.Action action = actionIterator.next();
                float actionDuration = action.getDuration();
                animationProgress += actionDuration;
            }
            int actionCount = actions.size();
            String durationString = String.format("%.1f", animationProgress / 1000.0f).replace(',', '.');
            String summary = actionCount + " actions  " + durationString + " s";
            if (isHovered) {
                Color highlightColor = mouseEvent.getHighlightColor();
                renderer.renderRectangle(x + 8.0f, cursorY, 164.0f, 16.0f, 8.0f, highlightColor, matrixStack);
            }
            Color labelColor = mouseEvent.getLabelColor();
            StateAnimation hoverAnimation = macro.getHoverAnimation();
            float animationValue = hoverAnimation.animation();
            Color animationColor = RenderUtils.adjustColorAlpha(labelColor, (int) (animationValue * 255.0f));
            renderer.renderRectangle(x + 8.0f, cursorY, 164.0f, 16.0f, 8.0f, animationColor, matrixStack);
            Color textColor;
            if (isSelected) {
                textColor = Color.decode("#141414");
            } else {
                textColor = Color.decode("#888888");
            }
            renderer.renderText(FontUtils.getMainFont(), macroName, x + 12.0f, cursorY + 6.5f, textColor, matrixStack);
            float textWidth = FontUtils.getMainFont().getWidth(summary);
            float textX = x + 164.0f - textWidth;
            if (isSelected) {
                textColor = Color.decode("#141414");
            } else {
                textColor = Color.decode("#888888");
            }
            renderer.renderText(FontUtils.getMainFont(), summary, textX, cursorY + 6.5f, textColor, matrixStack);
            cursorY += 18.0f;
        }
        float rightColumnX = x + 179.0f + 6.0f;
        this.newMacroLabel.render(mouseEvent, rightColumnX, y, mouseX, mouseY, renderer, matrixStack);
        this.openBuilderButton.render(rightColumnX + 8.0f, y + 40.0f, mouseX, mouseY, renderer, matrixStack);
        this.importFromClipboardButton.render(rightColumnX + 8.0f, y + 60.0f, mouseX, mouseY, renderer, matrixStack);
        this.selectedMacroLabel.render(mouseEvent, rightColumnX, y + 88.0f, mouseX, mouseY, renderer, matrixStack);
        this.addToWheelButton.render(rightColumnX + 8.0f, y + 128.0f, mouseX, mouseY, renderer, matrixStack);
        this.editInBuilderButton.render(rightColumnX + 8.0f, y + 152.0f, mouseX, mouseY, renderer, matrixStack);
        this.exportToClipboardButton.render(rightColumnX + 8.0f, y + 172.0f, mouseX, mouseY, renderer, matrixStack);
        this.deleteButton.render(rightColumnX + 8.0f, y + 195.0f, mouseX, mouseY, renderer, matrixStack);
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY, double pointX, double pointY, int button) {
        MacroManager macroManager = Melonity.getMacroManager();
        MacroManager manager = macroManager;
        float cursorY = mouseY + 40.0f;
        List<Macro> macros = manager.getMacros();
        Iterator<Macro> macroIterator = macros.iterator();

        while (macroIterator.hasNext()) {
            Macro macro = macroIterator.next();
            String macroName = macro.getName();
            Optional<Macro> currentMacro = Optional.of(macro);
            boolean isPointInMacro = RenderUtils.isPointWithinBounds(mouseX + 8.0f, cursorY, 164.0, 16.0, pointX, pointY);
            if (isPointInMacro) {
                this.selectedMacroName = macroName;
            }
            cursorY += 18.0f;
        }
        float rightColumnX = mouseX + 179.0f + 6.0f;
        double buttonX1 = rightColumnX + 8.0f;
        double buttonY1 = mouseY + 40.0f;
        float buttonWidth1 = this.openBuilderButton.getWidth();
        float buttonHeight1 = this.openBuilderButton.getHeight();
        boolean openBuilderClicked = RenderUtils.isPointWithinBounds(buttonX1, buttonY1, buttonWidth1, buttonHeight1, pointX, pointY);
        if (openBuilderClicked) {
            MinecraftClient client = MinecraftClient.getInstance();
            ru.melonity.m.c.MacroBuilderScreen macroBuilderScreen = new ru.melonity.m.c.MacroBuilderScreen(null);
            client.setScreen(macroBuilderScreen);
        }
        double buttonX2 = rightColumnX + 8.0f;
        double buttonY2 = mouseY + 60.0f;
        float buttonWidth2 = this.importFromClipboardButton.getWidth();
        float buttonHeight2 = this.importFromClipboardButton.getHeight();
        boolean importClicked = RenderUtils.isPointWithinBounds(buttonX2, buttonY2, buttonWidth2, buttonHeight2, pointX, pointY);
        if (importClicked) {
            try {
                MinecraftClient client = MinecraftClient.getInstance();
                String clipboard = client.keyboard.getClipboard();
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] decodedBytes = decoder.decode(clipboard.getBytes(StandardCharsets.UTF_8));
                String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
                JSONObject jsonObject = new JSONObject(decodedString);
                String macroName = jsonObject.getString("name");
                JSONArray actionsArray = jsonObject.getJSONArray("actions");
                Macro newMacro = new Macro(macroName);
                Iterator<Object> actionIterator = actionsArray.iterator();
                while (actionIterator.hasNext()) {
                    JSONObject actionObj = (JSONObject) actionIterator.next();
                    String actionId = actionObj.getString("id");
                    int actionType = -1;
                    int idHash = actionId.hashCode();
                    switch (idHash) {
                        case -2119843429:
                            if ("function_toggle".equals(actionId)) {
                                actionType = 0;
                            }
                            break;
                        case -283391285:
                            if ("use_item".equals(actionId)) {
                                actionType = 1;
                            }
                            break;
                        case 1116303536:
                            if ("wait_ms".equals(actionId)) {
                                actionType = 2;
                            }
                    }
                    switch (actionType) {
                        case 0:
                            List<Macro.Action> actionsList = newMacro.getActions();
                            FunctionToggleAction toggleAction = FunctionToggleAction.createFromJSON(actionObj);
                            actionsList.add(toggleAction);
                            break;
                        case 1:
                            List<Macro.Action> useItemActions = newMacro.getActions();
                            UseItemAction useItemAction = UseItemAction.createFromJSON(actionObj);
                            useItemActions.add(useItemAction);
                            break;
                        case 2:
                            List<Macro.Action> waitActions = newMacro.getActions();
                            WaitAction waitAction = WaitAction.createFromJSON(actionObj);
                            waitActions.add(waitAction);
                            break;
                        default:
                            MinecraftClient mc = MinecraftClient.getInstance();
                            ToastManager toastManager = mc.getToastManager();
                            Text errorText = new LiteralText("Invalid action: " + actionId);
                            toastManager.add(new ru.melonity.p.InfoToast("Error", errorText, null));
                    }
                }
                manager.addMacro(newMacro);
            } catch (Throwable t) {
                ru.melonity.p.InfoToast errorToast = new ru.melonity.p.InfoToast("Error", "Invalid clipboard data", null);
                Melonity.getGuiManager().displayToast(errorToast);
            }
        }
        double buttonX3 = rightColumnX + 8.0f;
        double buttonY3 = mouseY + 128.0f;
        float buttonWidth3 = this.addToWheelButton.getWidth();
        float buttonHeight3 = this.addToWheelButton.getHeight();
        boolean addToWheelClicked = RenderUtils.isPointWithinBounds(buttonX3, buttonY3, buttonWidth3, buttonHeight3, pointX, pointY);
        if (addToWheelClicked && this.selectedMacroName != null) {
            Optional<Macro> selectedMacro = manager.getMacroByName(this.selectedMacroName);
            selectedMacro.ifPresent(macro -> {
                Melonity.getGuiManager().getMacroWheel().setVisible(false);
                Melonity.getGuiManager().getMacroWheel().addMacro(macro);
            });
        }
        double buttonX4 = rightColumnX + 8.0f;
        double buttonY4 = mouseY + 152.0f;
        float buttonWidth4 = this.editInBuilderButton.getWidth();
        float buttonHeight4 = this.editInBuilderButton.getHeight();
        boolean editInBuilderClicked = RenderUtils.isPointWithinBounds(buttonX4, buttonY4, buttonWidth4, buttonHeight4, pointX, pointY);
        if (editInBuilderClicked && this.selectedMacroName != null) {
            Optional<Macro> selectedMacro = manager.getMacroByName(this.selectedMacroName);
            selectedMacro.ifPresent(macro -> MinecraftClient.getInstance().setScreen(new ru.melonity.m.c.MacroBuilderScreen(macro)));
        }
        double buttonX5 = rightColumnX + 8.0f;
        double buttonY5 = mouseY + 172.0f;
        float buttonWidth5 = this.exportToClipboardButton.getWidth();
        float buttonHeight5 = this.exportToClipboardButton.getHeight();
        boolean exportClicked = RenderUtils.isPointWithinBounds(buttonX5, buttonY5, buttonWidth5, buttonHeight5, pointX, pointY);
        if (exportClicked && this.selectedMacroName != null) {
            Optional<Macro> selectedMacro = manager.getMacroByName(this.selectedMacroName);
            if (selectedMacro.isPresent()) {
                Base64.Encoder encoder = Base64.getEncoder();
                Macro macro = selectedMacro.get();
                JSONObject json = macro.toJSON();
                String jsonString = json.toString();
                byte[] encodedBytes = encoder.encode(jsonString.getBytes(StandardCharsets.UTF_8));
                String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
                MinecraftClient.getInstance().keyboard.setClipboard(encodedString);
            }
        }
        double buttonX6 = rightColumnX + 8.0f;
        double buttonY6 = mouseY + 195.0f;
        float buttonWidth6 = this.deleteButton.getWidth();
        float buttonHeight6 = this.deleteButton.getHeight();
        boolean deleteClicked = RenderUtils.isPointWithinBounds(buttonX6, buttonY6, buttonWidth6, buttonHeight6, pointX, pointY);
        if (deleteClicked && this.selectedMacroName != null) {
            manager.removeMacroByName(this.selectedMacroName);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void close() {
    }
}