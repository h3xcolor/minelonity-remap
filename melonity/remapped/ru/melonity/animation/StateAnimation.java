// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.animation;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public class StateAnimation {
    private double progress;
    private float easedValue;
    private float easedDropValue;
    private boolean targetState;
    private float speed = 1.0f;

    public StateAnimation() {
    }

    public StateAnimation(float initialEasedValue) {
        this.easedValue = initialEasedValue;
    }

    public StateAnimation(boolean initialState) {
        this.targetState = initialState;
    }

    public void setState(boolean state) {
        this.targetState = state;
    }

    public void update(float delta) {
        this.progress = clamp(this.progress + (double) (delta * (this.targetState ? 1.0f : -1.0f) * this.speed), 0.0, 1.0);
        this.easedValue = (float) makeAnimation(this.progress);
        this.easedDropValue = (float) makeDropAnimation(this.progress);
    }

    public void reset() {
        this.progress = 0.0;
        this.targetState = false;
        this.easedDropValue = 0.0f;
        this.easedValue = 0.0f;
    }

    public boolean isNotOne() {
        return !isOne();
    }

    public boolean isOne() {
        return this.progress == 1.0;
    }

    public boolean isNotZero() {
        return !isZero();
    }

    public boolean isZero() {
        return this.progress == 0.0;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static double makeAnimation(double progress) {
        return Easings.QUAD_BOTH.ease(progress);
    }

    private static double makeDropAnimation(double progress) {
        return Easings.BACK_OUT.ease(progress);
    }

    @Generated
    public double progress() {
        return this.progress;
    }

    @Generated
    public StateAnimation progress(double progress) {
        this.progress = progress;
        return this;
    }

    @Generated
    public float animation() {
        return this.easedValue;
    }

    @Generated
    public float dropAnimation() {
        return this.easedDropValue;
    }

    @Generated
    public StateAnimation speed(float speed) {
        this.speed = speed;
        return this;
    }
}