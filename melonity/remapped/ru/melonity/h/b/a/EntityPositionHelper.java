// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_310;

@Environment(value = EnvType.CLIENT)
public class EntityPositionHelper {
    private static final class_310 MINECRAFT = class_310.method_1551();
    public static double playerX;
    public static double playerY;
    public static double playerZ;
    public static double pos1X;
    public static double pos1Y;
    public static double pos1Z;
    public static double pos2X;
    public static double pos2Y;
    public static double pos2Z;

    public static void updateEntityPosition(class_1297 entity) {
        playerX = MINECRAFT.field_1724.method_23317();
        playerY = MINECRAFT.field_1724.method_23318();
        playerZ = MINECRAFT.field_1724.method_23321();
        class_243 entityPos = entity.method_19538();
        class_243 point1 = new class_243(pos1X, pos1Y, pos1Z);
        class_243 point2 = new class_243(pos2X, pos2Y, pos2Z);
        double distanceSqToPoint1 = entityPos.method_1022(point1);
        double distanceSqToPoint2 = entityPos.method_1022(point2);
        class_243 targetPoint = distanceSqToPoint1 > distanceSqToPoint2 ? point2 : point1;
        if (pos1X != 0.0 && pos1Y != 0.0 && pos1Z != 0.0 && pos2X != 0.0 && pos2Y != 0.0 && pos2Z != 0.0) {
            entity.method_5814(targetPoint.field_1352, targetPoint.field_1351, targetPoint.field_1350);
        }
    }

    public static void resetPlayerPosition(class_1297 entity) {
        if (playerY != -999.0) {
            MINECRAFT.field_1724.method_5814(playerX, playerY, playerZ);
            playerY = -999.0;
        }
    }

    public static void resetPositionData() {
        playerX = 0.0;
        playerY = -999.0;
        playerZ = 0.0;
        pos1X = 0.0;
        pos1Y = 0.0;
        pos1Z = 0.0;
        pos2X = 0.0;
        pos2Y = 0.0;
        pos2Z = 0.0;
    }
}