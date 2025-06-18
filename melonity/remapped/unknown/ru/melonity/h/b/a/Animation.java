// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.h.b.a.AnimationDirection;
import ru.melonity.w.Timer;

@Environment(value = EnvType.CLIENT)
public abstract class Animation {
    public Timer timer = new Timer();
    protected int duration;
    protected double targetValue;
    protected AnimationDirection direction;

    public Animation(int duration, double targetValue) {
        this.duration = duration;
        this.targetValue = targetValue;
        this.direction = AnimationDirection.FORWARD;
    }

    public Animation(int duration, double targetValue, AnimationDirection direction) {
        this.duration = duration;
        this.targetValue = targetValue;
        this.direction = direction;
    }

    public boolean isFinished(AnimationDirection direction) {
        if (!isFinished()) {
            return false;
        }
        if (!this.direction.equals(direction)) {
            return false;
        }
        return true;
    }

    public double getProgressFactor() {
        return 1.0 - (double) this.timer.getTime() / (double) this.duration * this.targetValue;
    }

    public double getTargetValue() {
        return this.targetValue;
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }

    public void reset() {
        this.timer.reset();
    }

    public boolean isFinished() {
        return this.timer.hasReached(this.duration);
    }

    public AnimationDirection getDirection() {
        return this.direction;
    }

    public void setDirection(AnimationDirection direction) {
        if (this.direction != direction) {
            this.direction = direction;
            long currentTime = System.currentTimeMillis();
            long elapsed = Math.min(this.duration, this.timer.getTime());
            this.timer.setTime(currentTime - (this.duration - elapsed));
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    protected boolean isReversed() {
        return false;
    }

    public double getValue() {
        if (this.direction == AnimationDirection.FORWARD) {
            if (isFinished()) {
                return this.targetValue;
            }
            double progress = this.timer.getTime() / (double) this.duration;
            return easingFunction(progress) * this.targetValue;
        } else {
            if (isFinished()) {
                return 0.0;
            }
            if (isReversed()) {
                long elapsed = Math.min(this.duration, this.timer.getTime());
                long remaining = Math.max(0, this.duration - this.timer.getTime());
                long adjustedElapsed = Math.min(elapsed, remaining);
                double progress = adjustedElapsed / (double) this.duration;
                return easingFunction(progress) * this.targetValue;
            }
            return (1.0 - easingFunction(this.timer.getTime() / (double) this.duration)) * this.targetValue;
        }
    }

    protected abstract double easingFunction(double progress);
}