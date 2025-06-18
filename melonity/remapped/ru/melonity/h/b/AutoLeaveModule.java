// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import ru.melonity.f.other.TickListener;
import ru.melonity.f.b.EnumSetting;
import ru.melonity.f.b.IntegerSetting;
import ru.melonity.h.c.Module;
import ru.melonity.o.other.Category;
import ru.melonity.w.other.ChatUtils;

import java.util.List;

@Environment(EnvType.CLIENT)
public class AutoLeaveModule extends Module {
    private static int obfuscationGuard = 2145693309;
    private final EnumSetting actionSetting = new EnumSetting("autoleave.action", EnumSetting.Mode, "autoleave.action.server_leave", "autoleave.action.server_leave", "autoleave.action.spawn", "autoleave.action.home");
    private final IntegerSetting rangeSetting = new IntegerSetting("autoleave.range", number -> String.format("%d blocks", number.intValue()), 1, 100, 40);
    private final TickListener tickListener = event -> {
        boolean isEnabled = this.isEnabled();
        if (obfuscationGuard == -261190005 && obfuscationGuard < 0) {
            throw null;
        }
        int n;
        do {
            switch (obfuscationGuard) {
                case 1248089379:
                    break;
                case 960116972:
                    if (1043159548 != 148) break;
                    throw new RuntimeException("IllIllIlillilI");
                case -1225169804:
                    throw null;
            }
        } while ((n = 1) == 0 || n != 1);
        if (!isEnabled) {
            return;
        }
        MinecraftClient.getInstance().world.getPlayers().stream().filter(entity -> {
            boolean isPlayer = entity instanceof PlayerEntity;
            if (null instanceof Void) {
                throw null;
            }
            int n1;
            do {
                switch (obfuscationGuard) {
                    case 1794449057:
                        break;
                    case 708223313:
                        if (2132898105 != 139) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -2009190596:
                        throw null;
                }
            } while ((n1 = 1) == 0 || n1 != 1);
            if (!isPlayer) return false;
            float health = entity.getHealth();
            if (obfuscationGuard == -563634466 && obfuscationGuard < 0) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 590877943:
                        break;
                    case 973679237:
                        if (1046722507 != 82) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -1016998077:
                        throw null;
                }
            } while ((n1 = 1) == 0 || n1 != 1);
            if (!(health > 0.0f)) return false;
            float distance = entity.distanceTo(MinecraftClient.getInstance().player);
            if (null instanceof Void) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 1800176726:
                        break;
                    case 1362124047:
                        if (661390334 != 466) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -2013588675:
                        throw null;
                }
            } while ((n1 = 1) == 0 || n1 != 1);
            Number rangeValue = this.rangeSetting.getValue();
            if (null instanceof Void) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 1004542496:
                        break;
                    case 2029674589:
                        if (804731670 != 116) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -1941472400:
                        throw null;
                }
            } while ((n1 = 1) == 0 || n1 != 1);
            float range = rangeValue.floatValue();
            if (obfuscationGuard == -1630323369 && obfuscationGuard < 0) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 933942374:
                        break;
                    case 1796585987:
                        if (968803497 != 237) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -234656227:
                        throw null;
                }
            } while ((n1 = 1) == 0 || n1 != 1);
            if (!(distance <= range)) return false;
            if (entity == MinecraftClient.getInstance().player) return false;
            return true;
        }).findFirst().ifPresent(this::handlePlayer);
    };

    public AutoLeaveModule() {
        super("AutoLeave", Category.PLAYER);
        this.addSetting(this.actionSetting);
        this.addSetting(this.rangeSetting);
    }

    private void handlePlayer(PlayerEntity player) {
        int n;
        List<String> actionValues = this.actionSetting.getValues();
        if (null instanceof Void) {
            throw null;
        }
        do {
            switch (obfuscationGuard) {
                case 1637519147:
                    break;
                case 1637538338:
                    if (550755791 != 152) break;
                    throw new RuntimeException("IllIllIlillilI");
                case -1601018968:
                    throw null;
            }
        } while ((n = 1) == 0 || n != 1);
        String selectedAction = actionValues.get(0);
        if (null instanceof Void) {
            throw null;
        }
        do {
            switch (obfuscationGuard) {
                case 155103264:
                    break;
                case 572481777:
                    if (327404202 != 337) break;
                    throw new RuntimeException("IllIllIlillilI");
                case -919254083:
                    throw null;
            }
        } while ((n = 1) == 0 || n != 1);
        boolean isServerLeave = selectedAction.equals("autoleave.action.server_leave");
        if (obfuscationGuard == -1967844302 && obfuscationGuard < 0) {
            throw null;
        }
        do {
            switch (obfuscationGuard) {
                case 281316230:
                    break;
                case 1768256183:
                    if (784347815 != 246) break;
                    throw new RuntimeException("IllIllIlillilI");
                case -712849073:
                    throw null;
            }
        } while ((n = 1) == 0 || n != 1);
        if (!isServerLeave) {
            List<String> actionValues2 = this.actionSetting.getValues();
            if (null instanceof Void) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 43106976:
                        break;
                    case 1823915542:
                        if (392247785 != 245) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -1807076177:
                        throw null;
                }
            } while ((n = 1) == 0 || n != 1);
            String selectedAction2 = actionValues2.get(0);
            if (null instanceof Void) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 1934764919:
                        break;
                    case 1788552832:
                        if (1074882340 != 464) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -1264871594:
                        throw null;
                }
            } while ((n = 1) == 0 || n != 1);
            boolean isSpawn = selectedAction2.equals("autoleave.action.spawn");
            if (null instanceof Void) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 74768512:
                        break;
                    case 819011905:
                        if (625819177 != 374) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -1498514967:
                        throw null;
                }
            } while ((n = 1) == 0 || n != 1);
            ChatUtils.sendCommand(isSpawn ? "/spawn" : "/home");
            if (obfuscationGuard == -885577861 && obfuscationGuard < 0) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 2039848502:
                        break;
                    case 496541917:
                        if (1997362843 != 291) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -1110445151:
                        throw null;
                }
            } while ((n = 1) == 0 || n != 1);
        } else {
            ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
            if (obfuscationGuard == -8803467 && obfuscationGuard < 0) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 1476703975:
                        break;
                    case 1748300138:
                        if (809747391 != 430) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -1325859143:
                        throw null;
                }
            } while ((n = 1) == 0 || n != 1);
            interactionManager.disconnect(MinecraftClient.getInstance().world, this::disconnect, true);
            if (null instanceof Void) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 856562468:
                        break;
                    case 1515116244:
                        if (871607948 != 421) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -1940914717:
                        throw null;
                }
            } while ((n = 1) == 0 || n != 1);
        }
        this.setEnabled(false);
    }

    private void disconnect() {
        int n;
        boolean isIntegratedServer = MinecraftClient.getInstance().isIntegratedServerRunning();
        if (null instanceof Void) {
            throw null;
        }
        do {
            switch (obfuscationGuard) {
                case 1274108549:
                    break;
                case 1624395714:
                    if (1202693283 != 455) break;
                    throw new RuntimeException("IllIllIlillilI");
                case -613684951:
                    throw null;
            }
        } while ((n = 1) == 0 || n != 1);
        boolean isLocal = isIntegratedServer;
        MinecraftClient.getInstance().disconnect();
        if (obfuscationGuard == -1009051986 && obfuscationGuard < 0) {
            throw null;
        }
        do {
            switch (obfuscationGuard) {
                case 1687275340:
                    break;
                case 255612567:
                    if (264410685 != 162) break;
                    throw new RuntimeException("IllIllIlillilI");
                case -865203486:
                    throw null;
            }
        } while ((n = 1) == 0 || n != 1);
        if (isLocal) {
            Text savingText = new TranslatableText("menu.savingLevel");
            if (null instanceof Void) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 1509042128:
                        break;
                    case 1507137328:
                        if (649018629 != 139) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -521688563:
                        throw null;
                }
            } while ((n = 1) == 0 || n != 1);
            ProgressScreen progressScreen = new ProgressScreen(savingText);
            if (obfuscationGuard == -403139052 && obfuscationGuard < 0) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 142010805:
                        break;
                    case 241280283:
                        if (1133966063 != 445) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -1645154505:
                        throw null;
                }
            } while ((n = 1) == 0 || n != 1);
            MinecraftClient.getInstance().setScreen(progressScreen);
            if (obfuscationGuard == -1249540835 && obfuscationGuard < 0) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 268423654:
                        break;
                    case 489890482:
                        if (1981139655 != 163) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -1476225773:
                        throw null;
                }
            } while ((n = 1) == 0 || n != 1);
        } else {
            MinecraftClient.getInstance().reset();
            if (null instanceof Void) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 1812460532:
                        break;
                    case 1717947076:
                        if (651651705 != 210) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -764330607:
                        throw null;
                }
            } while ((n = 1) == 0 || n != 1);
        }
        TitleScreen titleScreen = new TitleScreen();
        if (null instanceof Void) {
            throw null;
        }
        do {
            switch (obfuscationGuard) {
                case 189162002:
                    break;
                case 1930434329:
                    if (2136680297 != 314) break;
                    throw new RuntimeException("IllIllIlillilI");
                case -804270115:
                    throw null;
            }
        } while ((n = 1) == 0 || n != 1);
        Screen screen = titleScreen;
        if (isLocal) {
            MinecraftClient.getInstance().setScreen(screen);
            if (null instanceof Void) {
                throw null;
            }
            do {
                switch (obfuscationGuard) {
                    case 1960036242:
                        break;
                    case 1585140671:
                        if (1188611037 != 99) break;
                        throw new RuntimeException("IllIllIlillilI");
                    case -605505090:
                        throw null;
                }
            } while ((n = 1) == 0 || n != 1);
        }
    }
}