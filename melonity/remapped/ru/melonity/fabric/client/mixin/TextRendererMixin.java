// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.melonity.Melonity;
import ru.melonity.fabric.client.model.ITextRenderer;
import ru.melonity.fabric.client.reorder.CustomCharacterRenderer;
import ru.melonity.fabric.client.modules.CustomTextModule;

@Environment(EnvType.CLIENT)
@Mixin(TextRenderer.class)
public class TextRendererMixin implements ITextRenderer {
    @Shadow
    @Final
    private boolean validateAdvance;

    @Inject(method = "drawLayer(Lnet/minecraft/text/OrderedText;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)F", at = @At("HEAD"), cancellable = true)
    private void drawLayer(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layerType, int backgroundColor, int light, CallbackInfoReturnable<Float> cir) {
        TextRenderer renderer = (TextRenderer) (Object) this;
        int playerNameIndex = -1;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && Melonity.client != null && Melonity.client.getModuleManager() != null && Melonity.client.getModuleManager().getModule(CustomTextModule.class).isPresent() && Melonity.client.getModuleManager().getModule(CustomTextModule.class).get().isEnabled()) {
            StringBuilder builder = new StringBuilder();
            text.accept((index, style, codePoint) -> {
                builder.append((char) codePoint);
                return true;
            });
            playerNameIndex = builder.indexOf(client.player.getName().getString());
        }
        CustomCharacterRenderer characterRenderer = new CustomCharacterRenderer(renderer, vertexConsumers, x, y, color, shadow, matrix, layerType, light, playerNameIndex);
        characterRenderer.charactersDisplayed = 0;
        characterRenderer.replacementIndex = 0;
        characterRenderer.index = 0;
        text.accept(characterRenderer);
        cir.setReturnValue(characterRenderer.drawLayer(backgroundColor, x));
    }

    @Shadow
    private FontStorage getFontStorage(Identifier id) {
        return null;
    }

    @Shadow
    private void drawGlyph(Glyph glyph, boolean bold, boolean italic, float weight, float x, float y, Matrix4f matrix, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int light) {
    }

    @Override
    public FontStorage fontStorage(Identifier id) {
        return this.getFontStorage(id);
    }

    @Override
    public boolean validateAdvance() {
        return this.validateAdvance;
    }

    @Override
    public void myDrawGlyph(Glyph glyph, boolean bold, boolean italic, float weight, float x, float y, Matrix4f matrix, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int light) {
        this.drawGlyph(glyph, bold, italic, weight, x, y, matrix, vertexConsumer, red, green, blue, alpha, light);
    }
}