// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.awt.Color;
import java.util.Iterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1541;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_243;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import ru.melonity.Melonity;
import ru.melonity.f.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll;
import ru.melonity.f.b.IIIlIIIllIIIllIlllIIllIlllIIlIIIllIIIlIIIllIIlllIIlllIIIlIlIllIlIIllIIlIllIIIlIlIIlllIIllIIIllIIllIIIlIIllIlIIllIIIllIlIIlllIIllIIIlIIlIIIllIlIIlllIIIllIllIlllIllIlIllIIIllIIlIllIIlIIlllIIllIlllIllIIlIllIIlIIllIlIllIIllIIl;
import ru.melonity.i.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll;
import ru.melonity.o.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;
import ru.melonity.w.IIIllIIlllIlIIIllIllIIIlllIIlllIIlllIlIlIIlIIIllIIllIlllIIlllIIlIlllIIlIIlIIllIIlIIIlllIllIIlllIIlIllIIIllIIlIIlIIlIIllIIlIIlIIIllIIllIIlIIlIIlllIIlllIIIllIlIIllIllIIlllIlIIIlllIIllIIlllIIllIlllIIllIIIllIIllIIllIIIlllIlIIlllIIllIIllIIlll;

@Environment(value=EnvType.CLIENT)
public class TNTTimerHud
extends IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    private final IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll<IIIlIIIllIIIllIlllIIllIlllIIlIIIllIIIlIIIllIIlllIIlllIIIlIlIllIlIIllIIlIllIIIlIlIIlllIIllIIIllIIllIIIlIIllIlIIllIIIllIlIIlllIIllIIIlIIlIIIllIlIIlllIIIllIllIlllIllIlIllIIIllIIlIllIIlIIlllIIllIlllIllIIlIllIIlIIllIlIllIIllIIl> renderCallback = event -> {
        if (!this.isEnabled()) {
            return;
        }
        Iterable entities = TNTTimerHud.mc.field_1687.method_18112();
        Iterator iterator = entities.iterator();
        while (iterator.hasNext()) {
            class_1297 entity = (class_1297)iterator.next();
            if (!(entity instanceof class_1541)) continue;
            class_1541 tnt = (class_1541)entity;
            float fuseTime = (float)tnt.method_6969() / 20.0f;
            String timerText = String.format("%.1f \u0441\u0435\u043a", fuseTime).replace(',', '.');
            class_243 pos = tnt.method_19538();
            double entityX = pos.field_1352;
            double entityY = pos.field_1351;
            double entityZ = pos.field_1350;
            float height = tnt.method_17682();
            double[] screenCoords = IIIllIIlllIlIIIllIllIIIlllIIlllIIlllIlIlIIlIIIllIIllIlllIIlllIIlIlllIIlIIlIIllIIlIIIlllIllIIlllIIlIllIIIllIIlIIlIIlIIllIIlIIlIIIllIIllIIlIIlIIlllIIlllIIIllIlIIllIllIIlllIlIIIlllIIllIIlllIIllIlllIIllIIIllIIllIIllIIIlllIlIIlllIIllIIllIIlll.worldToScreen(entityX, entityY + (double)height + 0.5, entityZ);
            if (screenCoords == null) continue;
            ru.melonity.s.c.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll renderer = Melonity.client.getRenderer();
            class_4587 matrices = event.getMatrices();
            float textWidth = renderer.getTextWidth(timerText);
            float boxWidth = 12.0f + textWidth + 2.0f;
            int boxX = (int)Math.round(screenCoords[0] - (double)(boxWidth / 2.0f));
            int boxY = (int)Math.round(screenCoords[1]);
            Color bgColor = Color.decode("#181818");
            renderer.drawBox(boxX, boxY, boxWidth, 12.0f, 4.0f, bgColor, matrices);
            matrices.method_22903();
            matrices.method_46416(0.0f, 0.0f, 0.0f);
            matrices.method_22905(0.5f, 0.5f, 0.5f);
            matrices.method_46416((float)boxX, (float)boxY, 0.0f);
            class_332 itemRenderer = event.getItemRenderer();
            class_1799 tntItem = new class_1799((class_1935)class_1802.field_8626);
            itemRenderer.method_51427(tntItem, boxX + 4, boxY + 4);
            matrices.method_22909();
            renderer.drawText(renderer.getTextRenderer(), timerText, (float)(boxX + 12), (float)(boxY + 5), matrices);
        }
    };

    public TNTTimerHud() {
        super("TNTTimer", IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll.HUD);
    }
}