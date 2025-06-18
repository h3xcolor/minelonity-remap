// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.g.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.g.ActionHandler;

@Environment(value = EnvType.CLIENT)
public class ButtonActionWrapper implements ActionHandler {
    private final ButtonActionWrapper delegate;
    private final Runnable action;
    private static final int ACTION_ID = 1687232769;

    @Override
    public void performAction() {
        this.action.run();
    }

    public ButtonActionWrapper(ButtonActionWrapper delegate, Runnable action) {
        this.delegate = delegate;
        this.action = action;
    }

    @Override
    public void update() {
        this.delegate.update();
    }

    @Override
    public boolean isEnabled() {
        return this.delegate.isEnabled();
    }
}