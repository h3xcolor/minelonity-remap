// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import ru.melonity.Melonity;
import ru.melonity.h.c.BaseModuleGui;
import ru.melonity.h.b.e.ToggleButtonWidget;
import ru.melonity.f.OptionSliderWidget;
import ru.melonity.f.b.SliderEvent;
import ru.melonity.s.c.Scheduler;

@Environment(EnvType.CLIENT)
public class SchedulerSettingsGui extends BaseModuleGui {
    private final ToggleButtonWidget toggleButton = new ToggleButtonWidget();
    private final OptionSliderWidget timeSlider = new OptionSliderWidget(99, 10.0f, 350.0f, 1.0f, 1.0f, () -> true);
    private final OptionSliderWidget.SliderCallback<SliderEvent> sliderCallback = sliderEvent -> {
        boolean toggled = this.toggleButton.isActive();
        if (!toggled) {
            return;
        }
        Scheduler scheduler = Melonity.getScheduler();
        float currentValue = this.timeSlider.getValue();
        this.timeSlider.updateValue(currentValue);
        float maxValue = this.timeSlider.getMaxValue();
        this.timeSlider.setMaxValue(maxValue);
        boolean sliderActive = this.timeSlider.isActive();
        if (!sliderActive) {
            return;
        }
        float sliderWidth = this.timeSlider.getWidth();
        float sliderX = this.timeSlider.getX();
        MatrixStack matrixStack = sliderEvent.getMatrixStack();
        this.toggleButton.render(sliderX, sliderWidth, 0.0, 0.0, scheduler, matrixStack);
    };

    public SchedulerSettingsGui() {
        super("Schedules", OptionSliderWidget.SLIDER_TYPE);
    }
}