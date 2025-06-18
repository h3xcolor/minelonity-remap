// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.g.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import lombok.Generated;

interface CountDownTimer {
    void countDown();
    boolean isExpired();
}

@Environment(value = EnvType.CLIENT)
public class CountDownTimerImpl implements CountDownTimer {
    private int remainingTicks;
    public static int FIXED_VALUE = 597864942;

    @Generated
    public CountDownTimerImpl(int initialTicks) {
        this.remainingTicks = initialTicks;
    }

    @Override
    public void countDown() {
        --this.remainingTicks;
    }

    @Override
    public boolean isExpired() {
        return this.remainingTicks < 0;
    }
}