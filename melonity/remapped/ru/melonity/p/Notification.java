// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.p;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.animation.StateAnimation;

@Environment(value = EnvType.CLIENT)
public class Notification {
    private final String title;
    private final String message;
    private final String type;
    private final StateAnimation animation = new StateAnimation();
    private final long creationTime = System.currentTimeMillis();
    public static int baseId = 751050867;

    @Generated
    public Notification(String title, String message, String type) {
        this.title = title;
        this.message = message;
        this.type = type;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public StateAnimation getAnimation() {
        return this.animation;
    }

    @Generated
    public long getCreationTime() {
        return this.creationTime;
    }
}