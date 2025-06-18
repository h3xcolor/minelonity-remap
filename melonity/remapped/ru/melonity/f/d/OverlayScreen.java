// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import ru.melonity.Melonity;
import ru.melonity.f.a.OverlayManager;
import ru.melonity.f.b.KeyEvent;
import ru.melonity.o.SomeAnnotation;

@Environment(EnvType.CLIENT)
@SomeAnnotation
public class OverlayScreen extends Screen {
    private final java.util.function.Consumer<KeyEvent> keyPressHandler = event -> {
        int keyCode = event.getKeyCode();
        if (keyCode == 344) {
            OverlayManager manager = Melonity.getOverlayManager();
            long currentTime = System.currentTimeMillis();
            manager.lastToggleTime = currentTime;
            manager.isOverlayVisible = false;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.currentScreen instanceof OverlayScreen) {
                manager.isOverlayVisible = true;
            } else {
                client.setScreen(new OverlayScreen());
            }
        }
    };

    public OverlayScreen() {
        super(Text.empty());
    }
}