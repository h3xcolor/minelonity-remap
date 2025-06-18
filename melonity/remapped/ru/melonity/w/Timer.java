// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import lombok.Generated;

@Environment(value = EnvType.CLIENT)
public class Timer {
    private long startTime = System.currentTimeMillis();

    public void reset() {
        this.startTime = System.currentTimeMillis();
    }

    public boolean hasElapsed(long delay) {
        long currentTime = System.currentTimeMillis();
        return currentTime - this.startTime > delay;
    }

    public void setDelay(long delay) {
        this.startTime = System.currentTimeMillis() + delay;
    }

    public void setStartTime(long time) {
        this.startTime = time;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.startTime;
    }

    public boolean hasNotElapsed() {
        long currentTime = System.currentTimeMillis();
        return currentTime - this.startTime <= 0;
    }

    public boolean hasElapsed() {
        long currentTime = System.currentTimeMillis();
        return this.startTime < currentTime;
    }

    public boolean hasElapsed(long delay) {
        long currentTime = System.currentTimeMillis();
        return currentTime - this.startTime > delay;
    }

    public boolean hasElapsedAndReset(long delay, boolean reset) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.startTime > delay) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    @Generated
    public long getStartTime() {
        return this.startTime;
    }
}