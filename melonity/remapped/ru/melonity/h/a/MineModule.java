// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.a;

import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import ru.melonity.Melonity;
import ru.melonity.h.a.b.BlockSelector;
import ru.melonity.h.c.Module;
import ru.melonity.o.Category;
import ru.melonity.o.ModuleManager;

@Environment(value = EnvType.CLIENT)
public class MineModule extends Module {
    private final BlockSelector blockSelector = new BlockSelector("Choose blocks");

    public MineModule() {
        super("Mine", Category.WORLD);
        this.initialize();
        this.addSetting(this.blockSelector);
    }

    @Override
    public void onToggle(boolean enabled) {
        super.onToggle(enabled);
        MinecraftClient client = MinecraftClient.getInstance();
        client.player.networkHandler.sendChatMessage("#stop");
        if (enabled) {
            StringBuilder blockListBuilder = new StringBuilder();
            ModuleManager moduleManager = Melonity.getModuleManager();
            BlockSelector blockSelectorModule = moduleManager.getModule(BlockSelector.class);
            List<Block> selectedBlocks = blockSelectorModule.getSelectedBlocks();
            Iterator<Block> iterator = selectedBlocks.iterator();
            while (iterator.hasNext()) {
                Block block = iterator.next();
                String translationKey = block.getTranslationKey();
                int prefixLength = "block.minecraft.".length();
                String blockName = translationKey.substring(prefixLength);
                String blockId = "minecraft:" + blockName;
                blockListBuilder.append(" ").append(blockId);
            }
            if (blockListBuilder.isEmpty()) {
                return;
            }
            ClientPlayNetworkHandler networkHandler = client.player.networkHandler;
            String command = "#mine" + blockListBuilder.toString();
            networkHandler.sendChatMessage(command);
        }
    }

    public BlockSelector getBlockSelector() {
        return this.blockSelector;
    }
}