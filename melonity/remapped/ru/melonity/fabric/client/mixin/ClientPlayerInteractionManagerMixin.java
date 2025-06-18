// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.SequencedPacketCreator;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.melonity.Melonity;
import ru.melonity.f.b.BlockInteractEvent;
import ru.melonity.f.b.BlockInteractModule;
import ru.melonity.f.b.AttackEntityEvent;
import ru.melonity.fabric.client.model.IClientPlayerInteractionManager;
import ru.melonity.fabric.client.model.ILocalPlayer;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {
    @Shadow
    private void sendSequencedPacket(ClientWorld world, SequencedPacketCreator packetCreator) {
    }

    @Inject(method = "clickSlot", at = @At("HEAD"))
    public void onClickSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
    }

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    public void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (player.getStackInHand(hand).getItem() == Items.ENDER_PEARL) {
            Melonity.EVENT_HANDLER.fireEvent(new BlockInteractEvent(hitResult.getBlockPos()));
        }
        BlockInteractModule blockInteractModule = Melonity.MODULE_MANAGER.getModule(BlockInteractModule.class).orElse(null);
        if (blockInteractModule != null && blockInteractModule.isActive()) {
            BlockState blockState = player.clientWorld.getBlockState(hitResult.getBlockPos());
            Block block = blockState.getBlock();
            Identifier blockId = Registries.BLOCK.getId(block);
            String blockName = this.createTranslationKey(blockId);
            boolean shouldCancel = 
                (blockInteractModule.isSettingEnabled("Chest") && block == Blocks.CHEST) ||
                (blockInteractModule.isSettingEnabled("Door") && blockName.contains("door")) ||
                (blockInteractModule.isSettingEnabled("Button") && blockName.contains("button")) ||
                (blockInteractModule.isSettingEnabled("Dispenser") && block == Blocks.DISPENSER) ||
                (blockInteractModule.isSettingEnabled("Craft Table") && block == Blocks.CRAFTING_TABLE) ||
                (blockInteractModule.isSettingEnabled("Trapdoor") && blockName.contains("trapdoor")) ||
                (blockInteractModule.isSettingEnabled("Furnace") && block == Blocks.FURNACE) ||
                (blockInteractModule.isSettingEnabled("Lever") && block == Blocks.LEVER) ||
                (blockInteractModule.isSettingEnabled("Anvil") && block == Blocks.ANVIL) ||
                (blockInteractModule.isSettingEnabled("Note Block") && block == Blocks.NOTE_BLOCK) ||
                (blockInteractModule.isSettingEnabled("Hopper") && block == Blocks.HOPPER);
            if (shouldCancel) {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }

    @Unique
    private String createTranslationKey(@org.jetbrains.annotations.Nullable Identifier blockId) {
        return blockId == null ? "block.unregistered_sadface" : "block." + blockId.getNamespace() + "." + blockId.getPath().replace('/', '.');
    }

    @Override
    public void trySendSequencedPacket(ClientWorld clientWorld, SequencedPacketCreator sequencedPacketCreator) {
        this.sendSequencedPacket(clientWorld, sequencedPacketCreator);
    }

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    public void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        MinecraftClient.getInstance().player.setLastAttackedTicks(0);
        MinecraftClient.getInstance().options.sprintKey.setPressed(false);
        AttackEntityEvent attackEvent = new AttackEntityEvent(target);
        Melonity.EVENT_HANDLER.fireEvent(attackEvent);
        if (attackEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "attackEntity", at = @At("RETURN"))
    public void onAttackEntityReturn(PlayerEntity player, Entity target, CallbackInfo ci) {
        MinecraftClient.getInstance().options.sprintKey.setPressed(((ILocalPlayer) MinecraftClient.getInstance().player).serverSprintState());
    }
}