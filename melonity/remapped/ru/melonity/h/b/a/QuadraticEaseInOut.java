// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class QuadraticEaseInOut extends EasingFunction {
    public QuadraticEaseInOut(int duration, double startValue) {
        super(duration, startValue);
    }

    public QuadraticEaseInOut(int duration, double startValue, EasingMode mode) {
        super(duration, startValue, mode);
    }

    @Override
    protected double ease(double progress) {
        double normalizedTime = progress / duration;
        if (normalizedTime < 0.5) {
            return 2 * normalizedTime * normalizedTime;
        } else {
            double t = -2 * normalizedTime + 2;
            return 1 - (t * t) / 2;
        }
    }
}