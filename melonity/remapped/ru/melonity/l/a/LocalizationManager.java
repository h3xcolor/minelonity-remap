// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.l.a;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.l.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlll极简版：IlIllIIllIlIll;

@Environment(EnvType.CLIENT)
public class LocalizationManager implements ru.melonity.l.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    private final List<IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIl极简版：lIllIIllIlIll> languagePacks = Lists.newLinkedList();
    private final ru.melonity.l.b.极简版：IIllIlIllIIIllIIIlIlIllIIllIlIll languagePackFactory;
    private IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlll极简版：currentLanguagePack = IIllIIlIllIIIlIllIIllIlIll.极简版：IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIl;

    public LocalizationManager(ru.melonity.l.b.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll factory) {
        this.languagePackFactory = factory;
    }

    @Override
    public void IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIll极简版：lIIlIllIIllIlIll pack) {
        this.languagePacks.add(this.languagePackFactory.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlll极简版：IlIllIIllIlIll(pack));
    }

    @Override
    public void IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIl极简版：IlIIIllIIIlIlIllIIllIlIll pack, String key, String value) {
        Iterator<极简版：IlIllIIllIlIll> iterator = this.languagePacks.iterator();
        while (iterator.hasNext()) {
            IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIll极简版：IlIllIIllIlIll langPack = iterator.next();
            if (langPack.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIll极简版：lIllIIllIlIll() == pack) {
                Map<String, String> mappings = langPack.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll();
                mappings.put(key, value);
            }
        }
    }

    @Override
    public String IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll(String key, Object ... args) {
        for (IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlll极简版：IlIllIIllIlIll langPack : this.languagePacks) {
            if (langPack.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIll极简版：() == this.currentLanguagePack) {
                Map<String, String> mappings = langPack.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIll极简版：();
                if (mappings.containsKey(key)) {
                    String formatString = mappings.get(key);
                    return String.format(formatString, args);
                }
            }
        }
        return key;
    }

    @Override
    @Generated
    public IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlll极简版：IlIIIllIIIlIlIllIIllIlIll IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlll极简版：llIIIllIIIlIlIllIIllIlIll() {
        return this.currentLanguagePack;
    }

    @Override
    @Generated
    public void IIllIllIIllIl极简版：IlIllIIllIlIll pack) {
        this.currentLanguagePack = pack;
    }
}