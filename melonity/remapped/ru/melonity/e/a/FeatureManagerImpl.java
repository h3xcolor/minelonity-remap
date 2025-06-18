// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.e.a;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.e.Feature;
import ru.melonity.e.FeatureManager;

import java.util.List;

@Environment(value = EnvType.CLIENT)
public class FeatureManagerImpl implements FeatureManager {
    private final List<Feature> features = Lists.newLinkedList();

    @Override
    public List<Feature> getFeatures() {
        return ImmutableList.copyOf(features);
    }

    @Override
    public void addFeature(Feature feature) {
        features.add(feature);
    }

    @Override
    public <T extends Feature> T getFeature(Class<T> clazz) {
        return features.stream()
                .filter(feature -> feature.getClass().equals(clazz))
                .findFirst()
                .map(clazz::cast)
                .orElse(null);
    }

    @Override
    public Feature getFeatureById(int id) {
        for (Feature feature : features) {
            if (feature.getId() == id) {
                return feature;
            }
        }
        return null;
    }
}