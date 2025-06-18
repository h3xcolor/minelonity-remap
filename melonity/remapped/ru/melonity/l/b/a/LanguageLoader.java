// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.l.b.a;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;

@Environment(value = EnvType.CLIENT)
public class LanguageLoader implements ru.melonity.l.b.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll {

    @Override
    public ru.melonity.l.a.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll loadLanguage(ru.melonity.l.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll languageInfo) {
        LinkedHashMap<String, String> translations = Maps.newLinkedHashMap();
        try {
            Class<?> clazz = this.getClass();
            String languageCode = languageInfo.IlllIIlIIIlllIIIlIIllIlIllIIIlIIIllIlllIIIllIIlIIllIIllIlllIIllIIllIIllIIllIIlllIIlllIllIIllIlllIlIllIIlllIllIllIIlllIIllIIIlllIIIllIIIllIlllIIlllIIlIlIlIlIllIIIlllIIlIllIIllIIlllIIlIIIllIIIllIIlIIlllIlIllIllIIlIIIllIllIIlIIllIll();
            String resourcePath = "/assets/minecraft/melonity/languages/" + languageCode;
            InputStream inputStream = clazz.getResourceAsStream(resourcePath);
            String jsonContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray keysArray = jsonObject.getJSONArray("keys");
            Iterator<Object> iterator = keysArray.iterator();
            while (iterator.hasNext()) {
                Object element = iterator.next();
                if (element instanceof JSONObject) {
                    JSONObject entry = (JSONObject) element;
                    String key = entry.getString("key");
                    String value = entry.getString("value");
                    translations.put(key, value);
                }
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return new ru.melonity.l.a.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll(languageInfo, translations);
    }
}