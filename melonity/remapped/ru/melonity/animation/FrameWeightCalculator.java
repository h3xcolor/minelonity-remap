// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.animation;

import java.util.concurrent.TimeUnit;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FrameWeightCalculator {
    private long lastUpdateTime;
    private final double nanosecondsPerUnit;

    public FrameWeightCalculator(double nanosecondsPerUnit) {
        this.nanosecondsPerUnit = nanosecondsPerUnit;
        this.lastUpdateTime = System.nanoTime();
    }

    public float elapsedUnits() {
        long currentTime = System.nanoTime();
        long elapsedNanos = currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;
        return (float) (elapsedNanos / nanosecondsPerUnit);
    }

    public static FrameWeightCalculator nanoUnit(long nanoseconds) {
        return new FrameWeightCalculator(nanoseconds);
    }

    public static FrameWeightCalculator milliseconds(long milliseconds) {
        return nanoUnit(TimeUnit.MILLISECONDS.toNanos(milliseconds));
    }

    public static FrameWeightCalculator seconds(long seconds) {
        return milliseconds(TimeUnit.SECONDS.toMillis(seconds));
    }
}