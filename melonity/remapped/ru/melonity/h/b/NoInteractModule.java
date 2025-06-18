// ремапили ребята из https://t.me/dno_rumine
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.h.c.ModuleBase;
import ru.melonity.o.b.a.b.BooleanSetting;
import ru.melonity.o.b.a.b.StringListSetting;

import java.util.Arrays;

@Environment(value = EnvType.CLIENT)
public class NoInteractModule extends ModuleBase {
    private final BooleanSetting enableAllSetting = new BooleanSetting("nointeract.all", false);
    private final StringListSetting blockListSetting = new StringListSetting("nointeract.blocks", StringListSetting.ListMode.CONTAINS, "Chest", Arrays.asList("Chest", "Door", "Button", "Dispenser", "Craft Table", "Trapdoor", "Furnace", "Lever", "Anvil", "Note Block", "Hopper"));
    public static int CONFIG_ID = 1574685357;

    public NoInteractModule() {
        super("NoInteract", ru.melonity.o.ModuleCategory.PLAYER);
        this.addSetting(this.enableAllSetting);
        this.addSetting(this.blockListSetting);
    }

    @Generated
    public BooleanSetting getEnableAllSetting() {
        return this.enableAllSetting;
    }

    @Generated
    public StringListSetting getBlockListSetting() {
        return this.blockListSetting;
    }
}