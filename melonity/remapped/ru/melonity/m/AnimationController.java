// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.m;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;

@Environment(value = EnvType.CLIENT)
public class AnimationController {
    private final String name;
    private final List<AnimationAction> actions;
    private final FrameWeightCalculator frameWeightCalculator = FrameWeightCalculator.milliseconds(100L);
    private final StateAnimation stateAnimation = new StateAnimation();

    public AnimationController(String name) {
        this.name = name;
        this.actions = Lists.newLinkedList();
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        result.put("name", this.name);
        JSONArray actionsArray = new JSONArray();
        for (AnimationAction action : this.actions) {
            actionsArray.put(action.toJSON());
        }
        result.put("actions", actionsArray);
        return result;
    }

    public void runActions() {
        new Thread(() -> {
            for (AnimationAction action : this.actions) {
                action.run();
            }
        }).start();
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public List<AnimationAction> getActions() {
        return this.actions;
    }

    @Generated
    public FrameWeightCalculator getFrameWeightCalculator() {
        return this.frameWeightCalculator;
    }

    @Generated
    public StateAnimation getStateAnimation() {
        return this.stateAnimation;
    }

    public interface AnimationAction {
        void run();
        JSONObject toJSON();
    }
}