// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.c;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.melonity.Melonity;
import ru.melonity.o.b.a.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;
import ru.melonity.o.b.a.b.IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll;
import ru.melonity.o.b.a.b.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIll极llIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll;
import ru.melonity.o.b.a.b.IlIIIlIIllIlllIlIIIlIIlllIIlIllIIlIIllIIIlllIIlllIIIlIIIlIIllIlllIIllIllIIIlllIlIllIIIlllIIlllIlIIllIIIllIlIIlIlllIllIIlIIllIllIlIlIIlIIIllIIllIIlIllIIllIIlIIlllIllIIIllIlllIIllIlllIlIIlIllIIlllIIIllIlllIIIllIIlIIllIlllIIll;
import ru.melonity.o.b.a.b.IlllIIlIIIlllIIIlIIllIlIllIIIlIIIllIlllIIIllIIlIIllIIllIlllIIllIIllIIllIIllIIlllIIlllIllIIllIlllIlIllIIlllIllIllIIlllIIllIIIlllIIIllIIIllIlllIIlllIIlIlIlIlIllIIIlllIIlIllIIllIIlllIIlIIIllIIIllIIlIIlllIlIllIllIIlIIIllIllIIlIIllIll;

@Environment(value=EnvType.CLIENT)
public class ConfigSerializer {

    public static JSONObject serializeConfig() {
        JSONObject root = new JSONObject();
        JSONObject featuresObject = new JSONObject();
        ru.melonity.h.c.ConfigSerializer featureManager = Melonity.getInstance().getFeatureManager();
        List<ru.melonity.h.Feature> features = featureManager.getFeatures();
        Iterator<ru.melonity.h.Feature> featureIterator = features.iterator();
        while (featureIterator.hasNext()) {
            ru.melonity.h.Feature feature = featureIterator.next();
            JSONObject featureSettings = new JSONObject();
            ru.melonity.o.b.a.FeatureSettings settings = feature.getSettings();
            List<IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll> settingList = settings.getSettings();
            Iterator<IIllIllIIllIlIIlIlIIlllIllIIll极llIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll> settingIterator = settingList.iterator();
            while (settingIterator.hasNext()) {
                IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll setting = settingIterator.next();
                if (setting instanceof ru.melonity.o.b.a.b.IntSetting) {
                    ru.melonity.o.b.a.b.IntSetting intSetting = (ru.melonity.o.b.a.b.IntSetting)setting;
                    String key = intSetting.getName();
                    int value = intSetting.getValue();
                    featureSettings.put(key, value);
                } else if (setting instanceof ru.melonity.o.b.a.b.BooleanSetting) {
                    ru.melonity.o.b.a.b.BooleanSetting boolSetting = (ru.melonity.o.b.a.b.BooleanSetting)setting;
                    String key = boolSetting.getName();
                    boolean value = boolSetting.getValue();
                    featureSettings.put(key, value);
                } else if (setting instanceof ru.melonity.o.b.a.b.ColorSetting) {
                    ru.melonity.o.b.a.b.ColorSetting colorSetting = (ru.melonity.o.b.a.b.ColorSetting)setting;
                    String key = colorSetting.getName();
                    Color color = colorSetting.getColor();
                    int rgb = color.getRGB();
                    featureSettings.put(key, rgb);
                } else if (setting instanceof ru.melonity.o.b.a.b.MultiStringSetting) {
                    ru.melonity.o.b.a.b.MultiStringSetting multiStringSetting = (ru.melonity.o.b.a.b.MultiStringSetting)setting;
                    String key = multiStringSetting.getName();
                    List<String> values = multiStringSetting.getValues();
                    String joined = String.join(",", values);
                    featureSettings.put(key, joined);
                } else if (setting instanceof ru.melonity.o.b.a.b.FloatSetting) {
                    ru.melonity.o.b.a.b.FloatSetting floatSetting = (ru.melonity.o.b.a.b.FloatSetting)setting;
                    String key = floatSetting.getName();
                    float value = floatSetting.getValue();
                    featureSettings.put(key, value);
                }
            }
            featuresObject.put(feature.getName(), featureSettings);
        }
        JSONArray dragsArray = new JSONArray();
        ru.melonity.e.DragManager dragManager = Melonity.getInstance().getDragManager();
        List<ru.melonity.e.DragComponent> drags = dragManager.getDrags();
        Iterator<ru.melonity.e.DragComponent> dragIterator = drags.iterator();
        while (dragIterator.hasNext()) {
            ru.melonity.e.DragComponent drag = dragIterator.next();
            JSONObject dragObject = new JSONObject();
            dragObject.put("x", drag.getX());
            dragObject.put("y", drag.getY());
            dragObject.put("id", drag.getId());
            dragsArray.put(dragObject);
        }
        root.put("features", featuresObject);
        root.put("drags", dragsArray);
        return root;
    }

    public static void loadConfigFromBase64(String base64Config) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Config);
        String jsonString = new String(decodedBytes, StandardCharsets.UTF_8);
        JSONObject root = new JSONObject(jsonString);
        if (root.has("features")) {
            JSONObject featuresObject = root.getJSONObject("features");
            ru.melonity.h.c.ConfigSerializer featureManager = Melonity.getInstance().getFeatureManager();
            List<ru.melonity.h.Feature> features = featureManager.getFeatures();
            Iterator<ru.melonity.h.Feature> featureIterator = features.iterator();
            while (featureIterator.hasNext()) {
                ru.melonity.h.Feature feature = featureIterator.next();
                String featureName = feature.getName();
                if (featuresObject.has(featureName)) {
                    JSONObject featureSettings = featuresObject.getJSONObject(featureName);
                    ru.melonity.o.b.a.FeatureSettings settings = feature.getSettings();
                    List<IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll> settingList = settings.getSettings();
                    Iterator<IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll> settingIterator = settingList.iterator();
                    while (settingIterator.hasNext()) {
                        IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll setting = settingIterator.next();
                        if (setting instanceof ru.melonity.o.b.a.b.IntSetting) {
                            ru.melonity.o.b.a.b.IntSetting intSetting = (ru.melonity.o.b.a.b.IntSetting)setting;
                            String key = intSetting.getName();
                            if (featureSettings.has(key)) {
                                int value = featureSettings.getInt(key);
                                intSetting.setValue(value);
                            }
                        } else if (setting instanceof ru.melonity.o.b.a.b.BooleanSetting) {
                            ru.melonity.o.b.a.b.BooleanSetting boolSetting = (ru.melonity.o.b.a.b.BooleanSetting)setting;
                            String key = boolSetting.getName();
                            if (featureSettings.has(key)) {
                                boolean value = featureSettings.getBoolean(key);
                                boolSetting.setValue(value);
                            }
                        } else if (setting instanceof ru.melonity.o.b.a.b.ColorSetting) {
                            ru.melonity.o.b.a.b.ColorSetting colorSetting = (ru.melonity.o.b.a.b.ColorSetting)setting;
                            String key = colorSetting.getName();
                            if (featureSettings.has(key)) {
                                try {
                                    int rgb = featureSettings.getInt(key);
                                    Color color = new Color(rgb);
                                    colorSetting.setColor(color);
                                } catch (Throwable ignored) {}
                            }
                        } else if (setting instanceof ru.melonity.o.b.a.b.MultiStringSetting) {
                            ru.melonity.o.b.a.b.MultiStringSetting multiStringSetting = (ru.melonity.o.b.a.b.MultiStringSetting)setting;
                            String key = multiStringSetting.getName();
                            if (featureSettings.has(key)) {
                                List<String> values = Lists.newArrayList();
                                String joined = featureSettings.getString(key);
                                String[] split = joined.split(",");
                                values.addAll(Arrays.asList(split));
                                multiStringSetting.setValues(values);
                            }
                        } else if (setting instanceof ru.melonity.o.b.a.b.FloatSetting) {
                            ru.melonity.o.b.a.b.FloatSetting floatSetting = (ru.melonity.o.b.a.b.FloatSetting)setting;
                            String key = floatSetting.getName();
                            if (featureSettings.has(key)) {
                                float value = (float)featureSettings.getDouble(key);
                                floatSetting.setValue(value);
                            }
                        }
                    }
                }
            }
        }
        if (root.has("drags")) {
            JSONArray dragsArray = root.getJSONArray("drags");
            Iterator<Object> dragIterator = dragsArray.iterator();
            while (dragIterator.hasNext()) {
                JSONObject dragObject = (JSONObject)dragIterator.next();
                ru.melonity.e.DragManager dragManager = Melonity.getInstance().getDragManager();
                int id = dragObject.getInt("id");
                ru.melonity.e.DragComponent drag = dragManager.getDragById(id);
                if (drag != null) {
                    float x = (float)dragObject.getDouble("x");
                    float y = (float)dragObject.getDouble("y");
                    drag.setX(x);
                    drag.setY(y);
                }
            }
        }
    }
}