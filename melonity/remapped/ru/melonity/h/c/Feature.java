// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.c;

import lombok.NonNull;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import ru.melonity.Melonity;
import ru.melonity.animation.StateAnimation;
import ru.melonity.h.IFeature;
import ru.melonity.h.d.HiddenFeature;
import ru.melonity.o.Category;
import ru.melonity.o.b.a.FeatureSettingManager;
import ru.melonity.o.b.a.b.Setting;
import ru.melonity.p.Notification;
import ru.melonity.p.NotificationManager;

@Environment(EnvType.CLIENT)
public class Feature implements IFeature {
    private final String featureName;
    private final Category category;
    private final FeatureSettingManager settingManager;
    private final boolean hidden;
    protected Setting exampleSetting = new Setting(-1);
    private boolean enabled;
    private final StateAnimation animation = new StateAnimation();

    public Feature(String featureName, Category category, boolean hidden) {
        this.featureName = featureName;
        this.category = category;
        this.settingManager = new FeatureSettingManager(this);
        this.hidden = hidden;
        addSetting(this.exampleSetting);
    }

    public Feature(String featureName, Category category) {
        this(featureName, category, false);
    }

    public void addSetting(Setting setting) {
        this.category.getSettingsRegistry().add(setting);
    }

    public void clearSettings() {
        this.exampleSetting = null;
        this.category.getSettingsRegistry().clear();
    }

    @Override
    @NonNull
    public String getName() {
        return this.featureName;
    }

    @Override
    @NonNull
    public Category getCategory() {
        return this.category;
    }

    @Override
    @NonNull
    public FeatureSettingManager getSettingManager() {
        return this.settingManager;
    }

    @Override
    @Nullable
    public Setting getExampleSetting() {
        return this.exampleSetting;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (this instanceof HiddenFeature) {
            this.enabled = false;
        }
        if (!(this instanceof HiddenFeature) && NotificationManager.notificationsEnabled()) {
            NotificationManager notificationManager = Melonity.getNotificationManager();
            Notification notification = new Notification(this.featureName, "Feature " + (enabled ? "enabled" : "disabled"), enabled ? "feature-on.png" : "feature-disabled.png");
            notificationManager.addNotification(notification);
        }
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    @Override
    public StateAnimation getAnimation() {
        return this.animation;
    }
}