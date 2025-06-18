// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.v;

import java.lang.reflect.ParameterizedType;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector2f;
import ru.melonity.v.VectorAction;
import ru.melonity.v.a.InteractionContext;

@Environment(value=EnvType.CLIENT)
public abstract class AbstractVectorHandler<T extends VectorAction> {
    protected final Vector2f position;
    private final T action;
    public static int publicId = 2034868605;

    public AbstractVectorHandler(Vector2f position) throws Exception {
        this.position = position;
        Class<?> clazz = (Class<?>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.action = (T) clazz.getDeclaredConstructor().newInstance();
    }

    public abstract void handle(Vector2f positionDelta, InteractionContext context);

    @Generated
    public Vector2f getPosition() {
        return this.position;
    }

    @Generated
    public T getAction() {
        return this.action;
    }
}