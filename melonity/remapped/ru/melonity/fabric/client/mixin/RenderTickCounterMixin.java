// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.melonity.Melonity;

@Environment(EnvType.CLIENT)
@Mixin(class_9779.class_9781.class)
public class RenderTickCounterMixin {
    @Shadow
    private float frameTime;
    @Shadow
    private float partialTicks;
    @Shadow
    private long lastTimeNanos;
    @Shadow
    @Final
    private FloatUnaryOperator tickTimeProvider;
    @Shadow
    @Final
    private float tickDurationMs;

    @Inject(method = "beginRenderTick", at = @At("HEAD"), cancellable = true)
    private void beforeBeginRenderTick(long currentTimeNanos, CallbackInfoReturnable<Integer> callback) {
        float speedModifier = Melonity.IllIIlIIlIIllIlIIIllIIIllIlIlllIlllIlIIllIIllIlIllIlllIIIllIIlIIIlIIllIIlIllIIlIIlIIllIIlIlIllIIlllIlIlIIllIIIlIIlllIllIIIlIIlIIIlllIlllIIllIIlIIllIIlIIllIIIllIIlllIIlllIIIlllIIIlIIllIIlIIlllIlIIIllIIIllIIllIIllIllIIlllIIll;
        float deltaTimeNanos = (float) (currentTimeNanos - this.lastTimeNanos);
        float adjustedTickTime = this.tickTimeProvider.apply(this.tickDurationMs);
        this.frameTime = deltaTimeNanos / adjustedTickTime * speedModifier;
        this.lastTimeNanos = currentTimeNanos;
        this.partialTicks += this.frameTime;
        int ticks = (int) this.partialTicks;
        this.partialTicks -= ticks;
        callback.setReturnValue(ticks);
    }
}