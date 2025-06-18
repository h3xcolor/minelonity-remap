// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.c;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import ru.melonity.f.Event;
import ru.melonity.f.EventListener;
import ru.melonity.f.a.EventHandler;
import ru.melonity.f.EventBus;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Environment(value = EnvType.CLIENT)
public class EventBusImpl implements EventBus {
    private final Map<Class<? extends Event>, List<ListenerWithPriority>> eventListenersMap = new HashMap<>();

    @Override
    public void unregister(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Stream<Field> fieldStream = Arrays.stream(fields);
        Stream<Field> filteredStream = fieldStream.filter(EventBusImpl::isEventListenerField);
        Stream<Field> accessibleStream = filteredStream.map(EventBusImpl::makeAccessible);
        Stream<ListenerField> listenerFieldStream = accessibleStream.map(field -> new ListenerField(object, field, 0));
        List<ListenerField> listenerFields = listenerFieldStream.toList();
        List<ListenerField> listenerFieldList = listenerFields;
        Iterator<ListenerField> iterator = listenerFieldList.iterator();
        while (iterator.hasNext()) {
            ListenerField listenerField = iterator.next();
            Class<?> eventType = listenerField.getEventType();
            EventListener<? extends Event> eventListener = listenerField.getEventListener();
            List<ListenerWithPriority> listeners = this.eventListenersMap.get(eventType);
            if (listeners == null) {
                continue;
            }
            listeners.removeIf(listener -> listener.getListener() == eventListener);
            if (listeners.isEmpty()) {
                this.eventListenersMap.remove(eventType);
            }
        }
    }

    @Override
    public void post(Event event) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }
        Class<?> eventClass = event.getClass();
        List<ListenerWithPriority> listeners = this.eventListenersMap.get(eventClass);
        if (listeners == null) {
            return;
        }
        Stream<ListenerWithPriority> stream = listeners.stream();
        Comparator<ListenerWithPriority> comparator = Comparator.comparingInt(ListenerWithPriority::getPriority);
        Stream<ListenerWithPriority> sortedStream = stream.sorted(comparator);
        List<ListenerWithPriority> sortedListeners = sortedStream.toList();
        listeners = sortedListeners;
        Iterator<ListenerWithPriority> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            ListenerWithPriority listenerWithPriority = iterator.next();
            EventListener<? extends Event> eventListener = listenerWithPriority.getListener();
            eventListener.callEvent(event);
        }
    }

    @Override
    public void register(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Stream<Field> fieldStream = Arrays.stream(fields);
        Stream<Field> filteredStream = fieldStream.filter(EventBusImpl::isEventListenerField);
        Stream<Field> accessibleStream = filteredStream.map(EventBusImpl::makeAccessible);
        Stream<ListenerField> listenerFieldStream = accessibleStream.map(field -> {
            int priority = 0;
            if (field.isAnnotationPresent(EventHandler.class)) {
                EventHandler annotation = field.getAnnotation(EventHandler.class);
                priority = annotation.priority();
            }
            return new ListenerField(object, field, priority);
        });
        List<ListenerField> listenerFields = listenerFieldStream.toList();
        List<ListenerField> listenerFieldList = listenerFields;
        Iterator<ListenerField> iterator = listenerFieldList.iterator();
        while (iterator.hasNext()) {
            ListenerField listenerField = iterator.next();
            Class<?> eventType = listenerField.getEventType();
            EventListener<? extends Event> eventListener = listenerField.getEventListener();
            int priority = listenerField.getPriority();
            List<ListenerWithPriority> listeners = this.eventListenersMap.computeIfAbsent(eventType, k -> new ArrayList<>());
            ListenerWithPriority listenerWithPriority = new ListenerWithPriority(eventListener, priority);
            listeners.add(listenerWithPriority);
        }
    }

    private static Field makeAccessible(Field field) {
        field.setAccessible(true);
        return field;
    }

    private static boolean isEventListenerField(Field field) {
        return field.getType().isAssignableFrom(EventListener.class);
    }

    @Environment(value = EnvType.CLIENT)
    private static final class ListenerField extends Record {
        private final Object accessor;
        private final Field field;
        private final int priority;

        private ListenerField(Object accessor, Field field, int priority) {
            this.accessor = accessor;
            this.field = field;
            this.priority = priority;
        }

        Class<? extends Event> getEventType() {
            Type type = this.field.getGenericType();
            validateGenericType(type, ListenerField::isParameterizedType, IllegalArgumentException::new, "Unrecognized generic type. Context [%s]", getContextDescription());
            return (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        }

        EventListener<? extends Event> getEventListener() {
            Object fieldValue = this.field.get(this.accessor);
            validateFieldValue(fieldValue, Objects::nonNull, IllegalArgumentException::new, "Field value must not be null. Context [%s]", getContextDescription());
            return (EventListener) fieldValue;
        }

        private String getContextDescription() {
            return "class name = %s, field name = %s".formatted(this.accessor.getClass().getName(), this.field.getName());
        }

        private static boolean isParameterizedType(Type type) {
            return type instanceof ParameterizedType;
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{ListenerField.class, "accessor;field;priority", "accessor", "field", "priority"}, this);
        }

        @Override
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap("hashCode", new MethodHandle[]{ListenerField.class, "accessor;field;priority", "accessor", "field", "priority"}, this);
        }

        @Override
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap("equals", new MethodHandle[]{ListenerField.class, "accessor;field;priority", "access极长", "field", "priority"}, this, o);
        }

        public Object getAccessor() {
            return this.accessor;
        }

        public Field getField() {
            return this.field;
        }

        public int getPriority() {
            return this.priority;
        }
    }

    @Environment(value = EnvType.CLIENT)
    private record ListenerWithPriority(EventListener<? extends Event> listener, int priority) {
        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{ListenerWithPriority.class, "listener;priority", "listener", "priority"}, this);
        }

        @Override
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap("hashCode", new MethodHandle[]{ListenerWithPriority.class, "listener;priority", "listener", "priority"}, this);
        }

        @Override
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap("equals", new MethodHandle[]{ListenerWithPriority.class, "listener;priority", "listener", "priority"}, this, o);
        }

        public EventListener<? extends Event> getListener() {
            return this.listener;
        }

        public int getPriority() {
            return this.priority;
        }
    }

    private static <T> void validateGenericType(T value, Predicate<T> validator, Function<String, ? extends RuntimeException> exceptionFactory, String message, Object... args) {
        if (!validator.test(value)) {
            throw exceptionFactory.apply(String.format(message, args));
        }
    }

    private static <T> void validateFieldValue(T value, Predicate<T> validator, Function<String, ? extends RuntimeException> exceptionFactory, String message, Object... args) {
        if (!validator.test(value)) {
            throw exceptionFactory.apply(String.format(message, args));
        }
    }
}