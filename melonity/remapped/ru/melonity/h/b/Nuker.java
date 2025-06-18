// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import ru.melonity.Melonity;
import ru.melonity.f.Setting;
import ru.melonity.f.b.BooleanSetting;
import ru.melonity.f.b.IntSetting;
import ru.melonity.f.b.ModeSetting;
import ru.melonity.o.b.a.b.EnumPriority;
import ru.melonity.o.Module;
import ru.melonity.o.ModuleCategory;

@Environment(value=EnvType.CLIENT)
public class Nuker extends Module {
    private final IntSetting radiusXZ = new IntSetting("Radius XZ", number -> String.format("%d", number.intValue()), 1, 5, 3);
    private final IntSetting radiusY = new IntSetting("Radius Y", number -> String.format("%d", number.intValue()), 0, 6, 1);
    private final ModeSetting priorityMode = new ModeSetting("Priority", EnumPriority.class, "On ores", "On ores", "Only on ores", "No priority");
    private final BooleanSetting ignoreY = new BooleanSetting("Ignore Y", true);
    private Vec3d targetVec;
    private BlockPos targetBlockPos;
    private final Setting.BlockBreakCallback blockBreakCallback = event -> {
        boolean ignoreYValue = ignoreY.getValue();
        if (!ignoreYValue) {
            return;
        }
        if (this.targetBlockPos == null || this.targetVec == null) {
            return;
        }
        event.setBlockDelay(Melonity.TICK_DELAY.getValue());
    };
    private final Setting.UpdateCallback updateCallback = event -> {
        boolean ignoreYValue = ignoreY.getValue();
        if (!ignoreYValue) {
            return;
        }
        this.findTargetBlock();
    };

    public Nuker() {
        super("Nuker", ModuleCategory.WORLD, false);
        addSettings(radiusXZ, radiusY, priorityMode, ignoreY);
    }

    public void findTargetBlock() {
        BlockPos candidate = findCandidateBlock();
        this.targetBlockPos = candidate;
        if (this.targetBlockPos != null) {
            BlockState blockState = this.world.getBlockState(this.targetBlockPos);
            BlockHitResult hitResult = blockState.getCollisionRaycastShadow(this.world, this.targetBlockPos, Direction.UP);
            if (hitResult != null) {
                this.targetVec = hitResult.getPos();
                Vec3d rayVec = this.targetVec.subtract(this.player.getPos());
                player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    private BlockPos findCandidateBlock() {
        ArrayList<BlockPos> allValidBlocks = new ArrayList<>();
        ArrayList<BlockPos> oreBlocks = new ArrayList<>();
        int radiusXZValue = radiusXZ.getValue();
        int startX = -radiusXZValue;
        int startY;
        if (ignoreY.getValue()) {
            startY = 0;
        } else {
            startY = -radiusY.getValue();
        }
        int startZ = -radiusXZValue;
        int endX = radiusXZValue;
        int endY = radiusY.getValue();
        int endZ = radiusXZValue;
        BlockPos playerPos = player.getBlockPos();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                for (int z = startZ; z <= endZ; z++) {
                    BlockPos candidate = playerPos.add(x, y, z);
                    if (!isValidBlock(candidate)) {
                        continue;
                    }
                    BlockState state = world.getBlockState(candidate);
                    Block block = state.getBlock();
                    if (isOre(block)) {
                        oreBlocks.add(candidate);
                    }
                    String priorityValue = priorityMode.getValue();
                    if (!"Only on ores".equals(priorityValue)) {
                        allValidBlocks.add(candidate);
                    }
                }
            }
        }
        Comparator<BlockPos> distanceComparator = Comparator.comparingDouble(pos -> pos.getSquaredDistance(player.getPos()));
        String priorityValue = priorityMode.getValue();
        if ("Priority on ores".equals(priorityValue)) {
            if (!oreBlocks.isEmpty()) {
                oreBlocks.sort(distanceComparator);
                return oreBlocks.get(0);
            }
        }
        if ("Only on ores".equals(priorityValue)) {
            if (!oreBlocks.isEmpty()) {
                oreBlocks.sort(distanceComparator);
                return oreBlocks.get(0);
            }
            return null;
        }
        if (!allValidBlocks.isEmpty()) {
            allValidBlocks.sort(distanceComparator);
            return allValidBlocks.get(0);
        }
        return null;
    }

    private boolean isValidBlock(BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (state.isAir()) {
            return false;
        }
        if (block == Blocks.BEDROCK) {
            return false;
        }
        if (block == Blocks.OBSIDIAN) {
            return false;
        }
        if (block == Blocks.CHEST) {
            return false;
        }
        if (block == Blocks.ENDER_CHEST) {
            return false;
        }
        if (block == Blocks.TRAPPED_CHEST) {
            return false;
        }
        return true;
    }

    private boolean isOre(Block block) {
        return block == Blocks.COAL_ORE || 
               block == Blocks.IRON_ORE || 
               block == Blocks.GOLD_ORE || 
               block == Blocks.DIAMOND_ORE || 
               block == Blocks.EMERALD_ORE || 
               block == Blocks.REDSTONE_ORE || 
               block == Blocks.LAPIS_ORE || 
               block == Blocks.NETHER_QUARTZ_ORE || 
               block == Blocks.NETHER_GOLD_ORE;
    }
}