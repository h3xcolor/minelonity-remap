// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.e.a;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.animation.StateAnimation;

@Environment(value = EnvType.CLIENT)
public class AnimationData {
    private final String id;
    private final String name;
    private final String stateName;
    private long duration;
    private final StateAnimation animation = new StateAnimation();
    public static int PUBLIC_CONSTANT = 967901293;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getStateName() {
        return this.stateName;
    }

    @Generated
    public long getDuration() {
        return this.duration;
    }

    @Generated
    public StateAnimation getAnimation() {
        return this.animation;
    }

    @Generated
    public AnimationData setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    @Generated
    public AnimationData(String id, String name, String stateName, long duration) {
        this.id = id;
        this.name = name;
        this.stateName = stateName;
        this.duration = duration;
    }
}