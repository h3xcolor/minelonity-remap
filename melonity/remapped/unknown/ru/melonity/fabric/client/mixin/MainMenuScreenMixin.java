// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.melonity.Melonity;
import ru.melonity.fabric.client.model.IMainMenuStage;
import ru.melonity.n.a.MainMenuStageImpl;
import ru.melonity.o.a.ThemeColor;
import ru.melonity.s.c.BackgroundRenderer;
import ru.melonity.w.ui.RoundedRectangle;

@Environment(EnvType.CLIENT)
@Mixin(TitleScreen.class)
public abstract class MainMenuScreenMixin extends Screen implements IMainMenuStage {
    @Unique
    private MainMenuStageImpl stage;
    private static final int STAGE_WIDTH = 202;

    protected MainMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Melonity.getRendererManager().getBackgroundRenderer() == null) {
            return;
        }
        if (this.stage == null) {
            this.stage = new MainMenuStageImpl((TitleScreen) (Object) this);
        }
        BackgroundRenderer backgroundRenderer = Melonity.getRendererManager().getBackgroundRenderer();
        ThemeColor color = Melonity.getRendererManager().getThemeManager().getCurrentActiveColor();
        RoundedRectangle roundedRectangle = new RoundedRectangle();
        float screenWidth = this.client.getWindow().getScaledWidth();
        float screenHeight = this.client.getWindow().getScaledHeight();
        backgroundRenderer.render(new Identifier("melonity", "images/ui/mainmenu/background_main_menu.png"), 0.0f, 0.0f, screenWidth, screenHeight, drawContext.getMatrices());
        float stageHeight = this.stage.getStageHeight();
        roundedRectangle.prepare(screenWidth / 2.0f - STAGE_WIDTH / 2.0f, screenHeight / 2.0f - stageHeight / 2.0f, STAGE_WIDTH, stageHeight);
        backgroundRenderer.drawRoundedRectangle(screenWidth / 2.0f - STAGE_WIDTH / 2.0f, screenHeight / 2.0f - stageHeight / 2.0f, STAGE_WIDTH, stageHeight, 12.0f, color.getColor(), drawContext.getMatrices());
        this.stage.render(screenWidth / 2.0f - STAGE_WIDTH / 2.0f, screenHeight / 2.0f - stageHeight / 2.0f, STAGE_WIDTH, mouseX, mouseY, backgroundRenderer, drawContext.getMatrices());
        roundedRectangle.cleanup();
        if (screenHeight > 320.0f) {
            backgroundRenderer.render(new Identifier("melonity", "images/ui/mainmenu/main_menu_logo.png"), screenWidth / 2.0f - 19.0f, 40.0f, 38.0f, 42.5f, color.getLogoVariant(), drawContext.getMatrices());
        }
        ci.cancel();
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (this.stage == null) {
            return;
        }
        float stageHeight = this.stage.getStageHeight();
        float centerX = this.client.getWindow().getScaledWidth() / 2.0f;
        float centerY = this.client.getWindow().getScaledHeight() / 2.0f;
        this.stage.handleMouseClick(centerX - STAGE_WIDTH / 2.0f, centerY - stageHeight / 2.0f, STAGE_WIDTH, mouseX, mouseY, button);
        cir.setReturnValue(true);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.stage == null) {
            return true;
        }
        this.stage.handleKeyPress(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.stage == null) {
            return true;
        }
        float centerX = this.client.getWindow().getScaledWidth() / 2.0f;
        float centerY = this.client.getWindow().getScaledHeight() / 2.0f;
        float stageHeight = this.stage.getStageHeight();
        this.stage.handleMouseDrag(centerX - STAGE_WIDTH / 2.0f, centerY - stageHeight / 2.0f, STAGE_WIDTH, deltaY, mouseX, mouseY);
        return true;
    }

    @Override
    public MainMenuStageImpl getStage() {
        return this.stage;
    }

    @Override
    public void setStage(MainMenuStageImpl stage) {
        this.stage = stage;
    }
}