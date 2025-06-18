// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1297;
import net.minecraft.class_1676;
import net.minecraft.class_1684;
import net.minecraft.class_1686;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_1935;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4081;
import net.minecraft.class_4587;
import net.minecraft.class_5944;
import net.minecraft.class_9334;
import net.minecraft.class_9801;
import org.lwjgl.opengl.GL11;
import ru.melonity.Melonity;
import ru.melonity.f.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll;
import ru.melonity.f.b.IIIlIIIllIIIllIlllIIllIlllIIlIIIllIIIlIIIllIIlllIIlllIIIlIlIllIlIIllIIlIllIIIlIlIIlllIIllIIIllIIllIIIlIIllIlIIllIIIllIlIIlllIIllIIIlIIlIIIllIlIIlllIIIllIllIlllIllIlIllIIIllIIlIllIIlIIlllIIllIlllIllIIlIllIIlIIllIlIllIIllIIl;
import ru.melonity.f.b.IlIllIlllIIllIlllIIllIIlllIIIllIIllIIlIIIlllIllIIlIlIIllIIIllIlIlIIllIllIlllIlIIllIIIlllIIllIIIlIIIlIIlIIlIIIlllIllIlllIIIlIIllIIIlIllIIIllIIlIIlIIIlIlIlIIIlllIlllIIlIllIIlllIIllIllIIlllIIIllIlIIllIIllIIlIIlIIllIllIIIllIl;
import ru.melonity.o.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;
import ru.melonity.s.c.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll;
import ru.melonity.w.IIIllIIlllIlIIIllIllIIIlllIIlllIIlllIlIlIIlIIIllIIllIlllIIlllIIlIlllIIlIIlIIllIIlIIIlllIllIIlllIIlIllIIIllIIlIIlIIlIIllIIlIIlIIIllIIllIIlIIlIIlllIIlllIIIllIlIIllIllIIlllIlIIIlllIIllIIlllIIllIlllIIllIIIllIIllIIllIIIlllIlIIlllIIllIIllIIlll;

@Environment(EnvType.CLIENT)
public class PredictionModule extends ru.melonity.h.c.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    private final IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll<IIIlIIIllIIIllIlllIIllIlllIIlIIIllIIIlIIIllIIlllIIlllIIIlIlIllIlIIllIIlIllIIIlIlIIlllIIllIIIllIIllIIIlIIllIlIIllIIIllIlIIlllIIllIIIlIIlIIIllIlIIlllIIIllIllIlllIllIlIllIIIllIIlIllIIlIIlllIIllIlllIllIIlIllIIlIIllIlIllIIllIIl> renderEventHandler = event -> {
        if (!isEnabled()) {
            return;
        }
        class_4587 matrixStack = event.getMatrixStack();
        class_4587 matrices = matrixStack;
        IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll renderer = Melonity.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.IllIllIlIIIllIIlIIllIIlllIIlllIIllIlllIIlIIllIIlllIlIllIIIllIllIIlllIlllIIlIIlIllIIllIlllIlllIIlIIlIIlIIlIIlllIllIIlIllIIIlllIIllIIIlIIIlllIlIIIlIlIlIIllIIIlIIllIlIIlIlIllIIlllIIllIIIlllIIllIIlIllIllIIllIllIIlllIIlIll();
        IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIll极lIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll rendererInstance = renderer;
        Iterable entities = PredictionModule.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.field_1687.method_18112();
        Iterator iterator = entities.iterator();
        Iterator entityIterator = iterator;
        while (true) {
            if (!entityIterator.hasNext()) {
                return;
            }
            class_1297 entity = (class_1297) entityIterator.next();
            if (!(entity instanceof class_1684)) {
                continue;
            }
            class_1684 projectile = (class_1684) entity;
            class_243 position = projectile.method_18798();
            class_243 currentPosition = position;
            class_243 velocity = projectile.method_19538();
            class_243 currentVelocity = velocity;
            double time = 0.0;
            class_243 lastPosition = currentVelocity;
            int step = 0;
            class_243 collisionPoint = null;
            while (step < 300) {
                class_243 startPos = currentVelocity;
                class_243 nextPosition = currentVelocity.method_1019(currentPosition);
                currentVelocity = nextPosition;
                class_243 nextAdjustedPosition = adjustPosition(projectile, currentPosition);
                currentPosition = nextAdjustedPosition;
                time += 0.05;
                class_3959 shape = new class_3959(startPos, currentVelocity, class_3959.class_3960.field_17558, class极3959.class_242.field_1348, entity);
                class_3959 collisionShape = shape;
                class_3965 collisionResult = PredictionModule.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.field_1687.method_17742(collisionShape);
                class_3965 result = collisionResult;
                class_239.class_240 collisionType = result.method_17783();
                if (collisionType == class_239.class_240.field_1332) {
                    collisionPoint = result.method_17784();
                    break;
                }
                if (currentVelocity.field_1351 < 0.0) {
                    break;
                }
                lastPosition = currentVelocity;
                step++;
            }
            if (collisionPoint == null) {
                collisionPoint = lastPosition;
            }
            double[] screenPos = IIIllIIlllIlIIIllIllIIIlllIIlllIIlllIlIlIIlIIIllIIllIlllIIlllIIlIlllIIlIIlIIllIIlIIIlllIllIIlllIIlIllIIIllIIlIIlIIlIIllIIlIIlIIIllIIllIIlIIlIIlllIIlllIIIllIlIIllIllIIlllIlIIIlllIIllIIlllIIllIlllIIllIIIllIIllIIllIIIlllIlIIlllIIllIIllIIlll.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll(collisionPoint.field_1352, collisionPoint.field_1351, collisionPoint.field_1350);
            if (screenPos == null) {
                continue;
            }
            String timeString = String.format("%.1f \u0441\u0435\u043a", time);
            String formattedTime = timeString.replace(',', '.');
            float textWidth = ru.melonity.i.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIl极llIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll(formattedTime);
            float totalWidth = 12.0f + textWidth + 2.0f;
            int x = (int) Math.round(screenPos[0] - (double) (totalWidth / 2.0f));
            int y = (int) Math.round(screenPos[1]);
            float renderX = x;
            float renderY = y;
            Color backgroundColor = Color.decode("#181818");
            rendererInstance.drawRoundedRect(renderX, renderY, totalWidth, 12.0f, 4.0f, backgroundColor, matrices);
            matrices.method_22903();
            matrices.method_46416(0.0f, 0.0f, 0.0f);
            matrices.method_22905(0.5f, 0.5f, 0.5f);
            matrices.method_46416((float) x, (float) y, 0.0f);
            class_332 itemRenderer = event.getContext();
            class_1799 clockItem = new class_1799(class_1802.field_8634);
            itemRenderer.method_51427(clockItem, x + 4, y + 4);
            matrices.method_22909();
            rendererInstance.drawText(ru.melonity.i.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll, formattedTime, (float) (x + 12), (float) (y + 5), matrices);
        }
    };
    private final IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll<IlIllIlllIIllIlllIIllIIlllIIIllIIllIIlIIIlllIllIIlIlIIllIIIllIlIlIIllIllIlllIlIIllIIIlllIIllIIIlIIIlIIlIIlIIIlllIllIlllIIIlIIllIIIlIllIIIllIIlIIlIIIlIlIlIIIlllIlllIIlIllIIlllIIllIllIIlllIIIllIlIIllIIllIIlIIlIIllIllIIIllIl> updateEventHandler = this::onUpdate;

    public PredictionModule() {
        super("Predictions", ru.melonity.o.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll.IlllIIlIIIlllIIIlIIllIlIllIIIlIIIllIlllIIIllIIlIIllIIllIlllIIllIIllIIllIIllIIlllIIlllIllIIllIlllIlIllIIlllIllIllIIlllIIllIIIlllIIIllIIIllIlllIIlllIIlIlIlIlIllIIIlllIIlIllIIllIIlllIIlIIIllIIIllIIlIIlllIlIllIllIIlIIIllIllIIlIIllIll);
    }

    private class_243 adjustPosition(class_1676 entity, class_243 position) {
        class_243 adjusted = position.method_1021(0.99);
        position = adjusted;
        boolean isOnGround = entity.method_5740();
        if (!isOnGround) {
            class_243 lowered = position.method_1031(0.0, -0.03, 0.0);
            position = lowered;
        }
        return position;
    }

    private void onUpdate(IlIllIlllIIllIlllIIllIIlllIIIllIIllIIlIIIlllIllIIlIlIIllIIIllIlIlIIllIllIlllIlIIllIIIlllIIllIIIlIIIlIIlIIlIIIlllIllIlllIIIlIIllIIIlIllIIIllIIlIIlIIIlIlIlIIIlllIlllIIlIllIIlllIIllIllIIlllIIIllIlIIllIIllIIlIIlIIllIllIIIllIl event) {
        if (!isEnabled()) {
            return;
        }
        class_4587 matrixStack = event.getMatrixStack();
        matrixStack.method_22903();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        GL11.glEnable(2848);
        class_243 cameraPos = PredictionModule.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.field_1773.method_19418();
        class_243 cameraOffset = cameraPos.method_19326();
        matrixStack.method_22904(-cameraOffset.field_1352, -cameraOffset.field_1351, -cameraOffset.field_1350);
        RenderSystem.lineWidth(1.0f);
        RenderSystem.setShader(net.minecraft.class_289::method_1348);
        class_289 shader = class_289.method_1348();
        class_9801 bufferBuilder = shader.method_60827(class_293.class_5596.field_27377, class_290.field_1576);
        class_9801 lineBuilder = bufferBuilder;
        boolean hasLines = false;
        Iterable entities = PredictionModule.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlll极lllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.field_1687.method_18112();
        Iterator iterator = entities.iterator();
        Iterator entityIterator = iterator;
        while (entityIterator.hasNext()) {
            class_1297 entity = (class_1297) entityIterator.next();
            if (!(entity instanceof class_1676)) {
                continue;
            }
            class_1676 projectile = (class_1676) entity;
            class_243 position = projectile.method_18798();
            class_243 currentPosition = position;
            class_243 velocity = projectile.method_19538();
            class_243 currentVelocity = velocity;
            int step = 0;
            while (step < 300) {
                class_243 startPos = currentVelocity;
                class_243 nextPosition = currentVelocity.method_1019(currentPosition);
                currentVelocity = nextPosition;
                class_243 nextAdjustedPosition = adjustPosition(projectile, currentPosition);
                currentPosition = nextAdjustedPosition;
                class_4587.MatrixEntry matrixEntry = matrixStack.method_23760();
                class_4587.Matrix matrix = matrixEntry.method_23761();
                class_9801.Vertex vertex = lineBuilder.method_22918(matrix, (float) startPos.field_1352, (float) startPos.field_1351, (float) startPos.field_1350);
                Color lineColor = Melonity.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.getColor(1, 60);
                vertex.method_39415(lineColor.getRGB());
                hasLines = true;
                class_3959 shape = new class_3959(startPos, currentVelocity, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, entity);
                class_3959 collisionShape = shape;
                class_3965 collisionResult = PredictionModule.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.field_1687.method_17742(collisionShape);
                class_3965 result = collisionResult;
                class_239.class_240 collisionType = result.method_17783();
                if (collisionType == class_239.class_240.field_1332) {
                    currentVelocity = result.method_17784();
                }
                class_310 client = class_310.method_1551();
                float distance = client.field_1724.method_5649((float) currentVelocity.field_1352, (float) currentVelocity.field_1351, (float) currentVelocity.field_1350);
                boolean isVisible = !(class_3532.method_15355(distance) > 1.3f);
                if (entity.method_24921() == PredictionModule.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.field_1724) {
                    isVisible = false;
                }
                if (projectile instanceof class_1686) {
                    class_1686 arrow = (class_1686) projectile;
                    class_1291 arrowData = arrow.method_7495();
                    class_1844 arrowType = arrowData.method_57825(class_9334.field_49651, class_1844.field_49274);
                    boolean hasPiercing = false;
                    for (class_1293 statusEffect : arrowType.method_57397()) {
                        if (statusEffect.method_5579().comp_349().method_18792() == class_4081.field_18272) {
                            hasPiercing = true;
                        }
                    }
                    if (hasPiercing) {
                        isVisible = false;
                    }
                }
                if (isVisible) {
                    Melonity.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.playSound();
                }
                matrixEntry = matrixStack.method_23760();
                matrix = matrixEntry.method_23761();
                vertex = lineBuilder.method_22918(matrix, (float) currentVelocity.field_1352, (float) currentVelocity.field_1351, (float) currentVelocity.field_1350);
                Color pointColor = Melonity.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll.getColor(1, 60);
                vertex.method_39415(pointColor.getRGB());
                if (collisionType == class_239.class_240.field_1332 || currentVelocity.field_1351 < 0.0) {
                    break;
                }
                step++;
            }
        }
        if (hasLines) {
            class_9801.BuiltBuffer builtBuffer = lineBuilder.method_60800();
            class_286.method_43433(builtBuffer);
        }
        GL11.glDisable(2848);
        RenderSystem.lineWidth(1.0f);
        RenderSystem.disableBlend();
        matrixStack.method极22909();
    }
}