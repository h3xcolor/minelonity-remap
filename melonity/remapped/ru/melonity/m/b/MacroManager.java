// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.m.b;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.Melonity;
import ru.melonity.m.Macro;
import ru.melonity.m.MacroManagerInterface;
import ru.melonity.m.MacroType;
import ru.melonity.p.Notification;
import ru.melonity.p.NotificationManager;

import java.util.List;
import java.util.Optional;

@Environment(value = EnvType.CLIENT)
public class MacroManager implements MacroManagerInterface {
    private final List<Macro> macros;
    private final ImmutableList<MacroType> macroTypes = ImmutableList.of(
        new MacroType(),
        new MacroType(),
        new MacroType(),
        new MacroType(),
        new MacroType()
    );

    public MacroManager() {
        this.macros = Lists.newLinkedList();
    }

    @Override
    public void addMacro(String name) {
        Optional<Macro> existingMacro = this.getMacro(name);
        if (existingMacro.isPresent()) {
            NotificationManager notificationManager = Melonity.getNotificationManager();
            Notification notification = new Notification("Error", "Macros already exists", "feature-disabled.png");
            notificationManager.addNotification(notification);
            return;
        }
        this.macros.add(new Macro(name));
    }

    @Override
    public void addMacro(Macro macro) {
        String name = macro.getName();
        Optional<Macro> existingMacro = this.getMacro(name);
        if (existingMacro.isPresent()) {
            NotificationManager notificationManager = Melonity.getNotificationManager();
            Notification notification = new Notification("Error", "Macros already exists", "feature-disabled.png");
            notificationManager.addNotification(notification);
            return;
        }
        this.macros.add(macro);
    }

    @Override
    public void removeMacro(String name) {
        this.macros.removeIf(macro -> macro.getName().equals(name));
    }

    @Override
    public Optional<Macro> getMacro(String name) {
        return this.macros.stream().filter(macro -> macro.getName().equals(name)).findFirst();
    }

    @Override
    public List<MacroType> getMacroTypesByType(MacroType type) {
        return this.macroTypes.stream().filter(macroType -> macroType == type).toList();
    }

    @Override
    public List<Macro> getAllMacros() {
        return ImmutableList.copyOf(this.macros);
    }
}