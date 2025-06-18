// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.melonity.f.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll;
import ru.melonity.f.b.IIlllIIllIlllIlllIIlllIIIlIllIIIlllIIlIIIlIlllIlllIIllIIIlIIllIIlIIlIllIIIllIIlllIIIllIIIllIIlllIIllIIllIIllIlllIlllIIIllIllIlIllIIlllIlllIIIlllIlllIlllIllIIllIIIllIIllIlllIIllIIllIIlllIIlIIlllIIIlIlIllIIlIIllIIIlIIlIIlIIIlllIIIllIIIllIIlIl;
import ru.melonity.o.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;

@Environment(value = EnvType.CLIENT)
public class BowSpammerModule extends ru.melonity.h.c.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    private final IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll<IIlllIIllIlllIlllIIlllIIIlIllIIIlllIIlIIIlIlllIlllIIllIIIlIIllIIlIIlIllIIIllIIlllIIIllIIIllIIlllIIllIIllIIllIlllIlllIIIllIllIlIllIIlllIlllIIIlllIlllIlllIllIIllIIIllIIllIlllIIllIIllIIlllIIlIIlllIIIlIlIllIIlIIllIIIlIIlIIlIIIlllIIIllIIIllIIlIl> onUseItemListener = event -> {
        if (!this.isActive()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        PlayerInventory inventory = player.getInventory();
        ItemStack stack = inventory.getMainHandStack();
        Item item = stack.getItem();
        if (item instanceof BowItem) {
            if (player.isUsingItem()) {
                int useTime = player.getItemUseTime();
                if (useTime >= 4) {
                    player.stopUsingItem();
                }
            }
        }
    };

    public BowSpammerModule() {
        super("BowSpammer", ru.melonity.o.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll);
    }
}