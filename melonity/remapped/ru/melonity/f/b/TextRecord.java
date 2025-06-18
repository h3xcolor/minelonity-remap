// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.f.TextComponent;

@Environment(EnvType.CLIENT)
public record TextRecord(String text) implements TextComponent {}