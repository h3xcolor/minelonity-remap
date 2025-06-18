// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import ru.melonity.Melonity;
import ru.melonity.f.b.RenderContext;
import ru.melonity.f.b.RenderListener;
import ru.melonity.h.c.ModuleRenderer;
import ru.melonity.o.ModuleCategory;

@Environment(value=EnvType.CLIENT)
public class RegionViewerRenderer extends ModuleRenderer {
    private final RenderListener<RenderContext> renderListener = context -> {
        if (!this.isEnabled()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            return;
        }
        BlockHitResult blockHitResult = (BlockHitResult)client.crosshairTarget;
        BlockPos targetPos = blockHitResult.getBlockPos();
        ItemStack mainHandStack = client.player.getMainHandStack();
        Item mainHandItem = mainHandStack.getItem();
        int radius = 0;
        if (mainHandItem == Items.DIAMOND_SWORD) {
            radius = 2;
        } else if (mainHandItem == Items.GOLDEN_PICKAXE) {
            radius = 3;
        } else if (mainHandItem == Items.DIAMOND_AXE) {
            radius = 5;
        } else if (mainHandItem == Items.DIAMOND_PICKAXE) {
            radius = 7;
        } else if (mainHandItem == Items.DIAMOND_SHOVEL) {
            radius = 10;
        } else if (mainHandItem == Items.GOLDEN_AXE) {
            radius = 15;
        }
        World world = client.world;
        BlockState blockState = world.getBlockState(targetPos);
        if (blockState.isAir()) {
            return;
        }
        if (radius <= 0) {
            return;
        }
        Color color = Melonity.getColorSetting(1, 60);
        Matrix4f matrix = context.getModelMatrix();
        RegionRenderHelper.renderRegion(targetPos, color, matrix, radius);
    };
    
    public RegionViewerRenderer() {
        super("RegionViewer", ModuleCategory.RENDER);
    }

    private boolean isEnabled() {
        return true;
    }
}