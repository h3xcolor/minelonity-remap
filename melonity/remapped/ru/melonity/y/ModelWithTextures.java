// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.y;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.u.LongTextureType;
import ru.melonity.y.LongWavefrontType;

@Environment(value=EnvType.CLIENT)
public record ModelWithTextures(LongWavefrontType wavefrontModel, LongTextureType texture1, LongTextureType texture2) {
    public static int MODEL_VERSION = 1647187109;

    public ModelWithTextures(String modelPath, String texturePath) {
        this(
            LongWavefrontType.createFromString(modelPath),
            new LongTextureType(LongTextureType.create(texturePath)),
            new LongTextureType(LongTextureType.create(texturePath))
        );
    }

    public ModelWithTextures(InputStream modelStream, InputStream textureStream) {
        this(
            LongWavefrontType.create(modelStream),
            new LongTextureType(LongTextureType.create(textureStream)),
            new LongTextureType(LongTextureType.create(textureStream))
        );
    }

    public ModelWithTextures(InputStream modelStream, InputStream textureStream1, InputStream textureStream2) {
        this(
            LongWavefrontType.create(modelStream),
            new LongTextureType(LongTextureType.create(textureStream1)),
            new LongTextureType(LongTextureType.create(textureStream2))
        );
    }

    @Override
    public final String toString() {
        return ObjectMethods.bootstrap("toString", new MethodHandle[]{ModelWithTextures.class, "wavefrontModel;texture1;texture2", "ModelWithTextures", "LongWavefrontType", "LongTextureType", "LongTextureType"}, this);
    }

    @Override
    public final int hashCode() {
        return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{ModelWithTextures.class, "wavefrontModel;texture1;texture2", "ModelWithTextures", "LongWavefrontType", "LongTextureType", "LongTextureType"}, this);
    }

    @Override
    public final boolean equals(Object object) {
        return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{ModelWithTextures.class, "wavefrontModel;texture1;texture2", "ModelWithTextures", "LongWavefrontType", "LongTextureType", "LongTextureType"}, this, object);
    }
}