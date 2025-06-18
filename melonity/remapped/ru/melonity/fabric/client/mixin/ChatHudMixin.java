// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.h.b.ChatModule;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Shadow
    private boolean scrolled;
    @Shadow
    @Final
    private List<ChatHudLine> visibleMessages;
    @Shadow
    private int scrollPosition;

    @Shadow
    private boolean isChatFocused() {
        return false;
    }

    @Shadow
    public int getVisibleLineCount() {
        return 0;
    }

    @Shadow
    public double getChatScale() {
        return 0.0;
    }

    @Shadow
    public int getWidth() {
        return 0;
    }

    @Shadow
    private int getMessageIndexAt(double mouseX, double mouseY) {
        return 0;
    }

    @Shadow
    private double toChatScreenX(double screenX) {
        return 0.0;
    }

    @Shadow
    private int getLineHeight() {
        return 0;
    }

    @Shadow
    private double toChatScreenY(double screenY) {
        return 0.0;
    }

    @Shadow
    private int getIndicatorWidth(ChatHudLine line) {
        return 0;
    }

    @Shadow
    private void drawIndicator(DrawContext context, int x, int y, MessageIndicator.Icon icon) {
    }

    @Shadow
    private static double getMessageOpacityMultiplier(int age) {
        return 0.0;
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int currentTick, int mouseX, int mouseY, boolean blitPass, CallbackInfo callbackInfo) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!this.isChatFocused()) {
            int visibleLines = this.getVisibleLineCount();
            int totalMessages = this.visibleMessages.size();
            if (totalMessages > 0) {
                int chatWidth;
                int indicatorColor;
                int backgroundColor;
                int textColor;
                int messageAge;
                client.getProfiler().push("chat");
                float scale = (float) this.getChatScale();
                int scaledWidth = MathHelper.floor((float) this.getWidth() / scale);
                int chatHeight = context.getScaledWindowHeight();
                context.getMatrices().push();
                context.getMatrices().scale(scale, scale, 1.0f);
                context.getMatrices().translate(4.0f, 0.0f, 0.0f);
                int scaledHeight = MathHelper.floor((float) (chatHeight - 40) / scale);
                int hoveredIndex = this.getMessageIndexAt(this.toChatScreenX(mouseX), this.toChatScreenY(mouseY));
                double backgroundOpacity = client.options.getTextBackgroundOpacity().getValue() * 0.9f + 0.1f;
                double textOpacity = client.options.getTextOpacity().getValue();
                double shadowOpacity = client.options.getTextShadowOpacity().getValue();
                int lineSpacing = this.getLineHeight();
                int shadowOffset = (int) Math.round(-8.0 * (shadowOpacity + 1.0) + 4.0 * shadowOpacity);
                int renderedLines = 0;
                for (int i = 0; i + this.scrollPosition < this.visibleMessages.size() && i < visibleLines; ++i) {
                    MessageIndicator indicator;
                    int messageIndex = i + this.scrollPosition;
                    ChatHudLine line = this.visibleMessages.get(messageIndex);
                    if (line == null || (messageAge = currentTick - line.getCreationTick()) >= 200 && !blitPass) continue;
                    double fadeFactor = blitPass ? 1.0 : getMessageOpacityMultiplier(messageAge);
                    textColor = (int) (255.0 * fadeFactor * backgroundOpacity);
                    backgroundColor = (int) (255.0 * fadeFactor * textOpacity);
                    ++renderedLines;
                    if (textColor <= 3) continue;
                    int indicatorOffset = 0;
                    int lineY = scaledHeight - i * lineSpacing;
                    int textY = lineY + shadowOffset;
                    ChatModule chatModule = Melonity.INSTANCE.getModuleManager().getModule(ChatModule.class).get();
                    if (!chatModule.isEnabled() || !chatModule.getSettings().getValueAsString().contains("Chat Background")) {
                        context.fill(-4, lineY - lineSpacing, scaledWidth + 4 + 4, lineY, backgroundColor << 24);
                    }
                    if ((indicator = line.getIndicator()) != null) {
                        int indicatorColorValue = indicator.getColor() | textColor << 24;
                        context.fill(-4, lineY - lineSpacing, -2, lineY, indicatorColorValue);
                        if (messageIndex == hoveredIndex && indicator.getIcon() != null) {
                            int indicatorWidth = this.getIndicatorWidth(line);
                            int iconY = textY + 9;
                            this.drawIndicator(context, indicatorWidth, iconY, indicator.getIcon());
                        }
                    }
                    context.getMatrices().push();
                    context.getMatrices().translate(0.0f, 0.0f, 50.0f);
                    context.drawText(client.textRenderer, line.getText(), 0, textY, 0xFFFFFF + (textColor << 24), false);
                    context.getMatrices().pop();
                }
                long queueSize = client.getChatQueueSize();
                if (queueSize > 0L) {
                    int queueBackgroundOpacity = (int) (128.0 * backgroundOpacity);
                    int queueTextOpacity = (int) (255.0 * textOpacity);
                    context.getMatrices().push();
                    context.getMatrices().translate(0.0f, (float) scaledHeight, 0.0f);
                    context.fill(-2, 0, scaledWidth + 4, 9, queueTextOpacity << 24);
                    context.getMatrices().translate(0.0f, 0.0f, 50.0f);
                    context.drawText(client.textRenderer, Text.translatable("chat.queue", queueSize), 0, 1, 0xFFFFFF + (queueBackgroundOpacity << 24), false);
                    context.getMatrices().pop();
                }
                if (blitPass) {
                    int totalHeight = totalMessages * lineSpacing;
                    int visibleHeight = renderedLines * lineSpacing;
                    int scrollOffset = this.scrollPosition * visibleHeight / totalMessages - scaledHeight;
                    int scrollHeight = visibleHeight * visibleHeight / totalHeight;
                    if (totalHeight != visibleHeight) {
                        int scrollBarColor = scrollOffset > 0 ? 170 : 96;
                        int scrollBarFillColor = this.scrolled ? 0xCC3333 : 0x3333AA;
                        int scrollBarX = scaledWidth + 4;
                        context.fill(scrollBarX, -scrollOffset, scrollBarX + 2, -scrollOffset - scrollHeight, 100, scrollBarFillColor + (scrollBarColor << 24));
                        context.fill(scrollBarX + 2, -scrollOffset, scrollBarX + 1, -scrollOffset - scrollHeight, 100, 0xCCCCCC + (scrollBarColor << 24));
                    }
                }
                context.getMatrices().pop();
                client.getProfiler().pop();
            }
        }
        callbackInfo.cancel();
    }
}