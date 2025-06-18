// ремапили ребята из https://t.me/dno_rumine
```java
package ru.melonity.m.a;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.json.JSONObject;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;
import ru.melonity.m.IGuiComponent;
import ru.melonity.m.a.FeatureToggleComponent;
import ru.melonity.s.d.TextFieldWidget;
import ru.melonity.s.c.RenderHelper;

@Environment(EnvType.CLIENT)
public abstract class FeatureToggleComponent implements IGuiComponent {
    private final FrameWeightCalculator animationTimer = FrameWeightCalculator.milliseconds(300L);
    private final StateAnimation stateAnimation = new StateAnimation();
    private final boolean enabled;
    protected final TextFieldWidget featureNameField = new TextFieldWidget("Feature name", false, 156.0f, 16.0f);
    
    public FeatureToggleComponent(boolean enabled) {
        this.enabled = enabled;
        this.featureNameField.setColor(Color.WHITE);
        this.featureNameField.setSuggestionProvider(input -> {
            String searchTerm = input.toLowerCase();
            ru.melonity.h.c.FeatureManager featureManager = Melonity.getFeatureManager();
            List<ru.melonity.h.Feature> features