// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.util.List;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import org.joml.Quaternionf;
import ru.melonity.h.c.Module;
import ru.melonity.o.b.a.b.BooleanSetting;
import ru.melonity.o.b.a.b.EnumSetting;
import ru.melonity.o.b.a.b.IntegerSetting;
import ru.melonity.o.Category;

@Environment(value=EnvType.CLIENT)
public class SwingAnimationModule extends Module {
    private final EnumSetting modeSetting = new EnumSetting("global.mode", EnumSetting.ModeType, "Default", "Default", "Down", "Fap");
    private final IntegerSetting powerSetting = new IntegerSetting("swinganimation.power", number -> String.format("%d", number.intValue()), 1, 10, 5);
    private final BooleanSetting swordOnlySetting = new BooleanSetting("swinganimation.sword", true);

    public SwingAnimationModule() {
        super("SwingAnimation", Category.PLAYER);
        addSetting(modeSetting);
        addSetting(powerSetting);
        addSetting(swordOnlySetting);
    }

    public void applySwingAnimation(class_4587 matrixStack, float swingProgress, int handSide) {
        class_1268 hand = handSide == 1 ? class_1268.field_5808 : class_1268.field_5810;
        List<String> modeOptions = modeSetting.getOptions();
        String currentMode = modeOptions.getFirst();
        float sinSwing = class_3532.method_15374(swingProgress * swingProgress * (float)Math.PI);
        float cosSwing = class_3532.method_15355(swingProgress);
        float sinSwing2 = class_3532.method_15374(cosSwing * (float)Math.PI);
        float cosSwing2 = class_3532.method_15355(swingProgress);
        float sinSwing3 = class_3532.method_15374(cosSwing2 * ((float)Math.PI * 2));
        float swingFactor = 0.2f * sinSwing3;
        double sinProgress = Math.sin(swingProgress * 1.5707963267948966 * 2.0);
        float sinValue = (float)sinProgress;
        int powerValue = powerSetting.getValue().intValue();
        float powerFactor = powerValue * 10;
        int inversePower = powerSetting.getValue().intValue();
        float inverseFactor = (11 - inversePower) * 10;
        float sinSwing4 = class_3532.method_15374(swingProgress * (float)Math.PI);
        int modeIndex = -1;
        int modeHash = currentMode.hashCode();
        switch (modeHash) {
            case -1085510111:
                if (currentMode.equals("Default")) {
                    modeIndex = 0;
                }
                break;
            case 2136258:
                if (currentMode.equals("Down")) {
                    modeIndex = 1;
                }
                break;
            case 70389:
                if (currentMode.equals("Fap")) {
                    modeIndex = 2;
                }
        }
        switch (modeIndex) {
            case 0:
                Quaternionf rotationX = class_7833.field_40716.rotationDegrees(handSide * (45.0f + sinSwing * (-powerFactor / 4.0f)));
                matrixStack.method_22907(rotationX);
                float cosSwing3 = class_3532.method_15355(swingProgress);
                float sinSwing5 = class_3532.method_15374(cosSwing3 * (float)Math.PI);
                Quaternionf rotationY = class_7833.field_40718.rotationDegrees(handSide * sinSwing5 * -(powerFactor / 4.0f));
                matrixStack.method_22907(rotationY);
                Quaternionf rotationZ = class_7833.field_40714.rotationDegrees(sinSwing5 * -powerFactor);
                matrixStack.method_22907(rotationZ);
                Quaternionf resetRotation = class_7833.field_40716.rotationDegrees(handSide * -45.0f);
                matrixStack.method_22907(resetRotation);
                break;
            case 1:
                matrixStack.method_46416(0.4f, 0.0f, -0.5f);
                Quaternionf rot1 = class_7833.field_40716.rotationDegrees(90.0f);
                matrixStack.method_22907(rot1);
                Quaternionf rot2 = class_7833.field_40718.rotationDegrees(-30.0f);
                matrixStack.method_22907(rot2);
                Quaternionf rot3 = class_7833.field_40714.rotationDegrees(-90.0f - powerFactor * sinValue);
                matrixStack.method_22907(rot3);
                break;
            case 2:
                float xOffset = -0.4f * sinSwing2;
                float yOffset = 0.2f * swingFactor;
                float zOffset = -0.2f * sinSwing4;
                matrixStack.method_46416(0.96f, -0.02f, -0.72f);
                Quaternionf rot4 = new Quaternionf();
                double rad45 = Math.toRadians(45.0);
                Quaternionf rot5 = rot4.rotateAxis((float)rad45, 0.0f, 1.0f, 0.0f);
                matrixStack.method_22907(rot5);
                float rotX = class_3532.method_15374(0.0f) * -20.0f;
                float cosTemp = class_3532.method_15355(0.0f);
                float sinTemp = class_3532.method_15374(cosTemp * (float)Math.PI);
                float rotY = sinTemp * -20.0f;
                float cosTemp2 = class_3532.method_15355(0.0f);
                float sinTemp2 = class_3532.method_15374(cosTemp2 * (float)Math.PI);
                float rotZ = sinTemp2 * -80.0f;
                Quaternionf rot6 = new Quaternionf();
                double radRotX = Math.toRadians(rotX);
                Quaternionf rot7 = rot6.rotateAxis((float)radRotX, 0.0f, 1.0f, 0.0f);
                matrixStack.method_22907(rot7);
                Quaternionf rot8 = new Quaternionf();
                double radRotY = Math.toRadians(rotY);
                Quaternionf rot9 = rot8.rotateAxis((float)radRotY, 0.0f, 0.0f, 1.0f);
                matrixStack.method_22907(rot9);
                Quaternionf rot10 = new Quaternionf();
                double radRotZ = Math.toRadians(rotZ);
                Quaternionf rot11 = rot10.rotateAxis((float)radRotZ, 1.0f, 0.0f, 0.0f);
                matrixStack.method_22907(rot11);
                matrixStack.method_46416(-0.5f, 0.2f, 0.0f);
                Quaternionf rot12 = new Quaternionf();
                double rad30 = Math.toRadians(30.0);
                Quaternionf rot13 = rot12.rotateAxis((float)rad30, 0.0f, 1.0f, 0.0f);
                matrixStack.method_22907(rot13);
                Quaternionf rot14 = new Quaternionf();
                double radMinus80 = Math.toRadians(-80.0);
                Quaternionf rot15 = rot14.rotateAxis((float)radMinus80, 1.0f, 0.0f, 0.0f);
                matrixStack.method_22907(rot15);
                Quaternionf rot16 = new Quaternionf();
                double rad60 = Math.toRadians(60.0);
                Quaternionf rot17 = rot16.rotateAxis((float)rad60, 0.0f, 1.0f, 0.0f);
                matrixStack.method_22907(rot17);
                long currentTime = System.currentTimeMillis();
                long timeMod;
                if (currentTime % 255L > 127L) {
                    long timeValue = System.currentTimeMillis();
                    timeMod = Math.abs(timeValue % 255L - 255L);
                } else {
                    long timeValue = System.currentTimeMillis();
                    timeMod = timeValue % 255L;
                }
                long timeFactor = Math.min(255L, timeMod * 2L);
                int timeInt = (int)timeFactor;
                float adjustedFactor = yOffset > 0.5f ? 1.0f - yOffset : yOffset;
                matrixStack.method_46416(0.4f, 0.0f, 0.7f);
                matrixStack.method_46416(0.0f, 0.5f, 0.0f);
                Quaternionf rot18 = new Quaternionf();
                double rad90 = Math.toRadians(90.0);
                Quaternionf rot19 = rot18.rotateAxis((float)rad90, 1.0f, 0.0f, -1.0f);
                matrixStack.method_22907(rot19);
                matrixStack.method_46416(0.6f, 0.5f, 0.0f);
                Quaternionf rot20 = new Quaternionf();
                double radMinus90 = Math.toRadians(-90.0);
                Quaternionf rot21 = rot20.rotateAxis((float)radMinus90, 1.0f, 0.0f, -1.0f);
                matrixStack.method_22907(rot21);
                Quaternionf rot22 = new Quaternionf();
                double radMinus10 = Math.toRadians(-10.0);
                Quaternionf rot23 = rot22.rotateAxis((float)radMinus10, 1.0f, 0.0f, -1.0f);
                matrixStack.method_22907(rot23);
                Quaternionf rot24 = new Quaternionf();
                double radAdjusted = Math.toRadians(-adjustedFactor * 10.0f);
                Quaternionf rot25 = rot24.rotateAxis((float)radAdjusted, 10.0f, 10.0f, -9.0f);
                matrixStack.method_22907(rot25);
                Quaternionf rot26 = new Quaternionf();
                double rad10 = Math.toRadians(10.0);
                Quaternionf rot27 = rot26.rotateAxis((float)rad10, -1.0f, 0.0f, 0.0f);
                matrixStack.method_22907(rot27);
                matrixStack.method_22904(0.0, 0.0, -0.5);
                float scaleFactor = swordOnlySetting.getValue() ? (float)(-timeInt) / inverseFactor : 1.0f;
                Quaternionf rot28 = new Quaternionf();
                double radScale = Math.toRadians(scaleFactor);
                Quaternionf rot29 = rot28.rotateAxis((float)radScale, 1.0f, 0.0f, 1.0f);
                matrixStack.method_22907(rot29);
                matrixStack.method_22904(0.0, -0.1, 0.5);
                matrixStack.method_22905(1.3f, 1.3f, 1.3f);
                break;
        }
    }

    @Generated
    public EnumSetting getModeSetting() {
        return modeSetting;
    }

    @Generated
    public IntegerSetting getPowerSetting() {
        return powerSetting;
    }

    @Generated
    public BooleanSetting getSwordOnlySetting() {
        return swordOnlySetting;
    }
}