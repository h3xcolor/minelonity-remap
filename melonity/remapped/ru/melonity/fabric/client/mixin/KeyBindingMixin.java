// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ru.melonity.fabric.client.model.IKeyBinding;

@Environment(EnvType.CLIENT)
@Mixin(KeyBinding.class)
public class KeyBindingMixin implements IKeyBinding {
    @Shadow
    private InputUtil.Key boundKey;

    @Override
    public InputUtil.Key boundKey() {
        return this.boundKey;
    }
}