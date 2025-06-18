// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import ru.melonity.Melonity;
import ru.melonity.f.event.RenderWorldEvent;
import ru.melonity.f.setting.BlockListSetting;
import ru.melonity.f.setting.EnumSetting;
import ru.melonity.o.Mod;
import ru.melonity.o.setting.Setting;

@Environment(EnvType.CLIENT)
public class BlockESPMod extends Mod {
    private final BlockListSetting blocksSetting = new BlockListSetting("blockesp.blocks", Setting.Category.RENDER, "blockesp.blocks.chest", Arrays.asList("blockesp.blocks.chest", "blockesp.blocks.ender_chest", "blockesp.blocks.hopper", "blockesp.blocks.obsidian", "blockesp.blocks.shulker", "blockesp.blocks.gold_ore", "blockesp.blocks.iron_ore", "blockesp.blocks.coal_ore", "blockesp.blocks.nether_gold_ore", "blockesp.blocks.lapis_ore", "blockesp.blocks.diamond_ore", "blockesp.blocks.redstone_ore", "blockesp.blocks.emerald_ore", "blockesp.blocks.ancient_debris"));
    private final EnumSetting modeSetting = new EnumSetting("global.mode", Setting.Category.GLOBAL, "blockesp.mode.lazy", Arrays.asList("blockesp.mode.fast", "blockesp.mode.lazy"));
    private final Map<BlockPos, Block> blockCache = Maps.newLinkedHashMap();
    private CopyOnWriteArrayList<BlockPos> blocksToScan = Lists.newCopyOnWriteArrayList();
    private final CopyOnWriteArrayList<BlockPos> blocksToRender = Lists.newCopyOnWriteArrayList();
    private BlockPos currentBlockPos;
    private Thread scanThread;
    private final RenderWorldEvent.Callback renderCallback = event -> {
        if (!isEnabled()) {
            return;
        }
        List<String> modeList = modeSetting.getValues();
        String mode = modeList.get(0);
        boolean isFastMode = mode.equals("blockesp.mode.fast");
        if (isFastMode) {
            for (int x = -30; x < 30; x++) {
                for (int y = -30; y < 30; y++) {
                    for (int z = -10; z < 10; z++) {
                        int playerX = MinecraftClient.getInstance().player.getBlockX();
                        int playerY = MinecraftClient.getInstance().player.getBlockY();
                        int playerZ = MinecraftClient.getInstance().player.getBlockZ();
                        BlockPos pos = new BlockPos(playerX + x, playerY + z, playerZ + y);
                        boolean isCached = blockCache.containsKey(pos);
                        Block block;
                        if (isCached) {
                            block = blockCache.get(pos);
                        } else {
                            World world = MinecraftClient.getInstance().world;
                            if (world == null) continue;
                            block = world.getBlockState(pos).getBlock();
                            blockCache.put(pos, block);
                        }
                        if (!isBlockToHighlight(block)) continue;
                        Color color = Melonity.COLOR_MANAGER.getColor(1, 60);
                        MatrixStack matrixStack = event.getMatrixStack();
                        MatrixStack.Entry entry = matrixStack.peek();
                        Matrix4f matrix = entry.getPositionMatrix();
                        renderBlockESP(pos, color, matrix, 1);
                    }
                }
            }
        } else {
            List<String> modeList2 = modeSetting.getValues();
            String mode2 = modeList2.get(0);
            boolean isLazyMode = mode2.equals("blockesp.mode.lazy");
            if (isLazyMode && blocksToRender != null) {
                if (currentBlockPos != null) {
                    Color color = new Color(255, 207, 72);
                    Matrix4f matrix = event.getMatrixStack().peek().getPositionMatrix();
                    renderBlockESP(currentBlockPos, color, matrix, 1);
                }
                Iterator<BlockPos> iterator = blocksToRender.iterator();
                while (iterator.hasNext()) {
                    BlockPos pos = iterator.next();
                    World world = MinecraftClient.getInstance().world;
                    if (world == null) continue;
                    Block block = world.getBlockState(pos).getBlock();
                    boolean shouldHighlight = isBlockToHighlight(block);
                    if (!shouldHighlight) continue;
                    int index = blocksToRender.indexOf(pos);
                    Color color = Melonity.COLOR_MANAGER.getColor(1 + index, 60);
                    Matrix4f matrix = event.getMatrixStack().peek().getPositionMatrix();
                    renderBlockESP(pos, color, matrix, 1);
                }
            }
        }
    };

    public BlockESPMod() {
        super("BlockESP", "blockesp");
        addSetting(modeSetting);
        addSetting(blocksSetting);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (MinecraftClient.getInstance().player == null || MinecraftClient.getInstance().world == null) {
            return;
        }
        if (isEnabled()) {
            CopyOnWriteArrayList<BlockPos> nearbyBlocks = findNearbyBlocks();
            CopyOnWriteArrayList<BlockPos> filteredBlocks = filterBlocks(nearbyBlocks, 3);
            this.blocksToScan = filteredBlocks;
            Thread thread = new Thread(() -> {
                if (MinecraftClient.getInstance().player != null) {
                    Iterator<BlockPos> iterator = blocksToScan.iterator();
                    while (iterator.hasNext()) {
                        BlockPos pos = iterator.next();
                        MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, Direction.UP));
                        blocksToRender.add(pos);
                        currentBlockPos = pos;
                        try {
                            int size = blocksToRender.size();
                            Thread.sleep(size % 100 == 0 ? 1000L : 35L);
                        } catch (InterruptedException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                }
            });
            this.scanThread = thread;
            this.scanThread.start();
        } else {
            if (scanThread != null) {
                scanThread.interrupt();
            }
            currentBlockPos = null;
            blocksToRender.clear();
            blocksToScan.clear();
        }
    }

    private boolean isBlockToHighlight(Block block) {
        List<String> blockList = blocksSetting.getValues();
        boolean chestEnabled = blockList.contains("blockesp.blocks.chest");
        if (chestEnabled && block == Blocks.CHEST) {
            return true;
        }
        boolean enderChestEnabled = blockList.contains("blockesp.blocks.ender_chest");
        if (enderChestEnabled && block == Blocks.ENDER_CHEST) {
            return true;
        }
        boolean hopperEnabled = blockList.contains("blockesp.blocks.hopper");
        if (hopperEnabled && block == Blocks.HOPPER) {
            return true;
        }
        boolean obsidianEnabled = blockList.contains("blockesp.blocks.obsidian");
        if (obsidianEnabled && block == Blocks.OBSIDIAN) {
            return true;
        }
        boolean shulkerEnabled = blockList.contains("blockesp.blocks.shulker");
        if (shulkerEnabled) {
            String blockName = block.getTranslationKey().toLowerCase();
            boolean isShulker = blockName.contains("shulker");
            if (isShulker) {
                return true;
            }
        }
        boolean goldOreEnabled = blockList.contains("blockesp.blocks.gold_ore");
        if (goldOreEnabled && block == Blocks.GOLD_ORE) {
            return true;
        }
        boolean ironOreEnabled = blockList.contains("blockesp.blocks.iron_ore");
        if (ironOreEnabled && block == Blocks.IRON_ORE) {
            return true;
        }
        boolean coalOreEnabled = blockList.contains("blockesp.blocks.coal_ore");
        if (coalOreEnabled && block == Blocks.COAL_ORE) {
            return true;
        }
        boolean netherGoldOreEnabled = blockList.contains("blockesp.blocks.nether_gold_ore");
        if (netherGoldOreEnabled && block == Blocks.NETHER_GOLD_ORE) {
            return true;
        }
        boolean lapisOreEnabled = blockList.contains("blockesp.blocks.lapis_ore");
        if (lapisOreEnabled && block == Blocks.LAPIS_ORE) {
            return true;
        }
        boolean diamondOreEnabled = blockList.contains("blockesp.blocks.diamond_ore");
        if (diamondOreEnabled && block == Blocks.DIAMOND_ORE) {
            return true;
        }
        boolean redstoneOreEnabled = blockList.contains("blockesp.blocks.redstone_ore");
        if (redstoneOreEnabled && block == Blocks.REDSTONE_ORE) {
            return true;
        }
        boolean emeraldOreEnabled = blockList.contains("blockesp.blocks.emerald_ore");
        if (emeraldOreEnabled && block == Blocks.EMERALD_ORE) {
            return true;
        }
        boolean ancientDebrisEnabled = blockList.contains("blockesp.blocks.ancient_debris");
        return ancientDebrisEnabled && block == Blocks.ANCIENT_DEBRIS;
    }

    public static void renderBlockESP(BlockPos pos, Color color, Matrix4f matrix, int size) {
        double x = pos.getX() - MinecraftClient.getInstance().gameRenderer.getCamera().getPos().x;
        double y = pos.getY() - MinecraftClient.getInstance().gameRenderer.getCamera().getPos().y;
        double z = pos.getZ() - MinecraftClient.getInstance().gameRenderer.getCamera().getPos().z;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.lineWidth(2.0f);
        renderBlockESPBox(new Box(x, y, z, x + size, y + size, z + size), color, matrix);
        RenderSystem.lineWidth(1.0f);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    public static void renderBlockESPBox(Box box, Color color, Matrix4f matrix) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.极狐f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.min极狐Z).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        buffer.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.minX, (极狐float) box.maxY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        buffer.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    private <T> CopyOnWriteArrayList<T> filterBlocks(CopyOnWriteArrayList<T> list, int step) {
        if (step == 1) {
            return list;
        }
        CopyOnWriteArrayList<T> filtered = Lists.newCopyOnWriteArrayList();
        Object[] array = list.toArray();
        for (int i = 0; i < array.length; i++) {
            if (i % step == 0) {
                filtered.add((T) array[i]);
            }
        }
        return filtered;
    }

    private CopyOnWriteArrayList<BlockPos> findNearbyBlocks() {
        CopyOnWriteArrayList<BlockPos> blocks = Lists.newCopyOnWriteArrayList();
        BlockPos playerPos = MinecraftClient.getInstance().player.getBlockPos();
        int range = 10;
        int verticalRange = 6;
        for (int y = verticalRange; y >= -verticalRange; --y) {
            for (int x = range; x >= -range; --x) {
                for (int z = range; z >= -range; --z) {
                    BlockPos pos = playerPos.add(x, y, z);
                    int height = pos.getY();
                    if (height <= 0) continue;
                    World world = MinecraftClient.getInstance().world;
                    if (world == null) continue;
                    Block block = world.getBlockState(pos).getBlock();
                    if (block instanceof net.minecraft.block.entity.BlockEntityProvider) continue;
                    blocks.add(pos);
                }
            }
        }
        return blocks;
    }
}