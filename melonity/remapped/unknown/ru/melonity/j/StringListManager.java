// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.j;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public final class StringListManager {
    private final List<String> stringList = Lists.newLinkedList();

    public void addString(String string) {
        this.stringList.add(string);
    }

    public void clearList() {
        this.stringList.clear();
    }

    public void removeString(String string) {
        this.stringList.remove(string);
    }

    public boolean containsString(String string) {
        return this.stringList.contains(string);
    }

    public List<String> getStringList() {
        return this.stringList;
    }
}