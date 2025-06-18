// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.melonity.Melonity;
import ru.melonity.fabric.client.model.ILocalPlayer;
import ru.melonity.f.events.MovementEvent;
import ru.melonity.f.events.MovementUpdateEvent;
import ru.melonity.f.events.CloseScreenEvent;
import ru.melonity.f.events.ItemMoveEvent;
import ru.melonity.f.events.PlayerTickEvent;
import ru.melonity.f.modules.movement.NoSlowModule;
import ru.melonity.f.modules.movement.PacketFlyModule;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements ILocalPlayer {
    @Shadow
    private boolean pressingForward;
    @Shadow
    private boolean serverSprintState;
    @Shadow
    private double lastX;
    @Shadow
    private double lastBaseY;
    @Shadow
    private double lastZ;
    @Shadow
    private float lastYaw;
    @Shadow
    private float lastPitch;
    @Shadow
    private int ticksSinceLastPositionPacket;
    @Shadow
    private boolean lastOnGround;
    @Shadow
    private boolean autoJumpEnabled;
    @Shadow
    public Input input;
    @Unique
    private ItemMoveEvent itemMoveEvent;

    @Shadow
    private void resetPosition() {
    }

    @Shadow
    protected boolean isCamera() {
        return false;
    }

    @Shadow
    protected abstract boolean canStartSprinting();

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void onTickMovement(CallbackInfo ci) {
        this.itemMoveEvent = new ItemMoveEvent();
        Melonity.getEventManager().dispatchEvent(this.itemMoveEvent);
    }

    @Inject(method = "tickMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerEntity;ticksLeftToDoubleTapSprint:I", shift = At.Shift.BEFORE))
    public void onTickMovementCancelSlow(CallbackInfo ci) {
        if (this.itemMoveEvent != null && this.itemMoveEvent.isCancelled()) {
            this.input.movementSideways /= 0.2f;
            this.input.movementForward /= 0.2f;
        }
    }

    @Inject(method = "swingHand", at = @At("HEAD"), cancellable = true)
    public void onSwingHand(Hand hand, CallbackInfo ci) {
        if (Melonity.getModuleManager() != null && Melonity.getModuleManager().getModule(PacketFlyModule.class).isEnabled() && MinecraftClient.getInstance().crosshairTarget.getType() == HitResult.Type.MISS) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.BEFORE))
    public void onTick(CallbackInfo ci) {
        Melonity.getEventManager().dispatchEvent(new PlayerTickEvent());
    }

    @Inject(method = "closeHandledScreen", at = @At("HEAD"))
    public void onCloseHandledScreen(CallbackInfo ci) {
        Melonity.getEventManager().dispatchEvent(new CloseScreenEvent());
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void onMove(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        MovementEvent event = new MovementEvent(movementType, movement.x, movement.y, movement.z);
        Melonity.getEventManager().dispatchEvent(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
        movement = new Vec3d(event.getX(), event.getY(), event.getZ());
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void onSendMovementPackets(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (Melonity.getModuleManager().getModule(NoSlowModule.class).isEnabled()) {
            this.resetPosition();
        }
        boolean sprinting = player.isSprinting();
        if (sprinting != this.pressingForward) {
            ClientCommandC2SPacket.Mode mode = sprinting ? ClientCommandC2SPacket.Mode.START_SPRINTING : ClientCommandC2SPacket.Mode.STOP_SPRINTING;
            player.networkHandler.sendPacket(new ClientCommandC2SPacket(player, mode));
            this.pressingForward = sprinting;
        }
        if (this.isCamera()) {
            boolean onGround;
            MovementUpdateEvent movementUpdateEvent = new MovementUpdateEvent(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch(), player.isOnGround());
            Melonity.getEventManager().dispatchEvent(movementUpdateEvent);
            if (Melonity.getModuleManager().getModule(NoSlowModule.class).isEnabled()) {
                this.resetPosition();
            }
            if (movementUpdateEvent.isCancelled()) {
                return;
            }
            double deltaX = movementUpdateEvent.getX() - this.lastX;
            double deltaY = movementUpdateEvent.getY() - this.lastBaseY;
            double deltaZ = movementUpdateEvent.getZ() - this.lastZ;
            double deltaYaw = movementUpdateEvent.getYaw() - this.lastYaw;
            double deltaPitch = movementUpdateEvent.getPitch() - this.lastPitch;
            ++this.ticksSinceLastPositionPacket;
            boolean positionChanged = MathHelper.squaredMagnitude(deltaX, deltaY, deltaZ) > MathHelper.square(2.0E-4) || this.ticksSinceLastPositionPacket >= 20;
            boolean rotationChanged = deltaYaw != 0.0 || deltaPitch != 0.0;
            if (player.hasVehicle()) {
                Vec3d vehiclePos = player.getPos();
                player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(vehiclePos.x, -999.0, vehiclePos.z, movementUpdateEvent.getYaw(), movementUpdateEvent.getPitch(), movementUpdateEvent.isOnGround()));
                positionChanged = false;
            } else if (positionChanged && rotationChanged) {
                player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(movementUpdateEvent.getX(), movementUpdateEvent.getY(), movementUpdateEvent.getZ(), movementUpdateEvent.getYaw(), movementUpdateEvent.getPitch(), movementUpdateEvent.isOnGround()));
            } else if (positionChanged) {
                player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(movementUpdateEvent.getX(), movementUpdateEvent.getY(), movementUpdateEvent.getZ(), movementUpdateEvent.isOnGround()));
            } else if (rotationChanged) {
                player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(movementUpdateEvent.getYaw(), movementUpdateEvent.getPitch(), movementUpdateEvent.isOnGround()));
            } else if (this.lastOnGround != player.isOnGround()) {
                player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(movementUpdateEvent.isOnGround()));
            }
            if (positionChanged) {
                this.lastX = movementUpdateEvent.getX();
                this.lastBaseY = movementUpdateEvent.getY();
                this.lastZ = movementUpdateEvent.getZ();
                this.ticksSinceLastPositionPacket = 0;
            }
            if (rotationChanged) {
                this.lastYaw = movementUpdateEvent.getYaw();
                this.lastPitch = movementUpdateEvent.getPitch();
            }
            this.lastOnGround = movementUpdateEvent.isOnGround();
            this.autoJumpEnabled = MinecraftClient.getInstance().options.autoJump;
        }
        ci.cancel();
    }

    @Override
    public boolean serverSprintState() {
        return this.serverSprintState;
    }

    @Override
    public float getXRotLast() {
        return this.lastPitch;
    }

    @Override
    public boolean canStartSprint() {
        return this.canStartSprinting();
    }
}