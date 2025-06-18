// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.c;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.Melonity;
import ru.melonity.f.EventBus;
import ru.melonity.h.IModuleManager;
import ru.melonity.h.Module;
import ru.melonity.i.ModulePriority;
import ru.melonity.o.FeatureType;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Environment(value = EnvType.CLIENT)
public class ModuleManager implements IModuleManager {
    private final List<Module> modules = Lists.newLinkedList();

    @Override
    public void add(Module module) {
        this.modules.add(module);
        EventBus eventBus = Melonity.getEventBus();
        eventBus.register(module);
    }

    @Override
    public <T extends Module> Optional<T> getModule(Class<T> clazz) {
        for (Module module : this.modules) {
            if (module.getClass().equals(clazz)) {
                return Optional.of(clazz.cast(module));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Module> getModule(String name) {
        for (Module module : this.modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return Optional.of(module);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Module> getModules() {
        return ImmutableList.copyOf(this.modules);
    }

    @Override
    public List<Module> getModules(FeatureType type) {
        return ImmutableList.copyOf(this.modules.stream()
                .filter(module -> module.getType() == type)
                .collect(Collectors.toList()));
    }

    @Override
    public void sortModules() {
        this.modules.sort(Comparator.comparingDouble(module -> {
            String name = module.getName();
            if (name.equalsIgnoreCase("KillAura") ||
                name.equalsIgnoreCase("ESP") ||
                name.equalsIgnoreCase("Hud") ||
                name.equalsIgnoreCase("3D Particles") ||
                name.equalsIgnoreCase("Circles") ||
                name.equalsIgnoreCase("ItemESP")) {
                return Double.NEGATIVE_INFINITY;
            }
            return -ModulePriority.getPriority(module.getName());
        }));
    }
}