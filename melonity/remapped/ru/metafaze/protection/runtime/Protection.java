// ремапили ребята из https://t.me/dno_rumine
package ru.metafaze.protection.runtime;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Protection {
    private static final String HOST_LOCATION = "https:
    private static final int BYTES_IN_KB = 0x100000;
    private static boolean initialized;
    private static final Map<Class<?>, String> PRIMITIVE_TYPE_TO_DESC_NAME;
    private static final Function<Class<?>, String> TYPE_TO_DESC_NAME;
    private static final Function<Method, String> METHOD_TO_DESCRIPTION;
    private static final Function<Field, String> FIELD_TO_DESCRIPTION;

    private static native void initialize(ByteBuffer buffer);
    private static native void free(ByteBuffer buffer);
    private static native void fillRequestBuffer(ByteBuffer buffer);
    private static native ByteBuffer clearAllocate0(int size);

    public static synchronized void interceptMain() {
        if (initialized) {
            return;
        }
        ByteBuffer requestBuffer = clearAllocate(BYTES_IN_KB);
        fillRequestBuffer(requestBuffer);
        makeProtectionHandshake(requestBuffer);
        initialize(requestBuffer);
        free(requestBuffer);
        initialized = true;
    }

    private static ByteBuffer clearAllocate(int size) {
        ByteBuffer buffer = clearAllocate0(size);
        return setupByteBuffer(buffer);
    }

    public static Field lazyFindField(Class<?> clazz, String name, String description, boolean isStatic) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            boolean fieldIsStatic = Modifier.isStatic(modifiers);
            String fieldName = field.getName();
            String fieldDescription = FIELD_TO_DESCRIPTION.apply(field);
            if (fieldIsStatic == isStatic && fieldName.equals(name) && fieldDescription.equals(description)) {
                return field;
            }
        }
        String formattedName = "%s%s".formatted(name, description);
        String errorMessage = "Field %s in class %s has not found".formatted(formattedName, clazz.getName());
        throw new IllegalArgumentException(errorMessage);
    }

    public static void makeProtectionHandshake(ByteBuffer buffer) {
        int capacity = buffer.capacity();
        byte[] requestBytes = new byte[capacity];
        buffer.get(requestBytes);
        String location = createHttpLocation("api/v1/protection/handshake");
        byte[] responseBytes = makeByteArrayHttpRequest(location, requestBytes);
        buffer.clear();
        buffer.put(responseBytes);
    }

    private static byte[] makeByteArrayHttpRequest(String location, byte[] outComingBytes) {
        try {
            java.net.URL url = new java.net.URL(location);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            connection.setRequestProperty("Content-Length", String.valueOf(outComingBytes.length));
            try (java.io.OutputStream os = connection.getOutputStream()) {
                os.write(outComingBytes);
            }
            try (java.io.InputStream is = connection.getInputStream()) {
                byte[] response = is.readAllBytes();
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException("HTTP request failed", e);
        }
    }

    public static Method lazyFindMethod(Class<?> clazz, String name, String description, boolean isStatic) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            int modifiers = method.getModifiers();
            boolean methodIsStatic = Modifier.isStatic(modifiers);
            String methodName = method.getName();
            String methodDescription = METHOD_TO_DESCRIPTION.apply(method);
            if (methodIsStatic == isStatic && methodName.equals(name) && methodDescription.equals(description)) {
                return method;
            }
        }
        String formattedName = "%s%s".formatted(name, description);
        String errorMessage = "Method %s in class %s has not found".formatted(formattedName, clazz.getName());
        throw new IllegalArgumentException(errorMessage);
    }

    private static String createHttpLocation(String endpoint) {
        return "%s%s".formatted(HOST_LOCATION, endpoint);
    }

    public static Class<?> lazyFindClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + name, e);
        }
    }

    private static ByteBuffer setupByteBuffer(ByteBuffer buffer) {
        return buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    static {
        PRIMITIVE_TYPE_TO_DESC_NAME = new HashMap<>();
        PRIMITIVE_TYPE_TO_DESC_NAME.put(Byte.TYPE, "B");
        PRIMITIVE_TYPE_TO_DESC_NAME.put(Boolean.TYPE, "Z");
        PRIMITIVE_TYPE_TO_DESC_NAME.put(Short.TYPE, "S");
        PRIMITIVE_TYPE_TO_DESC_NAME.put(Character.TYPE, "C");
        PRIMITIVE_TYPE_TO_DESC_NAME.put(Integer.TYPE, "I");
        PRIMITIVE_TYPE_TO_DESC_NAME.put(Float.TYPE, "F");
        PRIMITIVE_TYPE_TO_DESC_NAME.put(Long.TYPE, "J");
        PRIMITIVE_TYPE_TO_DESC_NAME.put(Double.TYPE, "D");
        PRIMITIVE_TYPE_TO_DESC_NAME.put(Void.TYPE, "V");

        TYPE_TO_DESC_NAME = clazz -> {
            if (PRIMITIVE_TYPE_TO_DESC_NAME.containsKey(clazz)) {
                return PRIMITIVE_TYPE_TO_DESC_NAME.get(clazz);
            }
            String className = clazz.getName();
            if (clazz.isArray()) {
                return className.replace(".", "/");
            }
            return "L" + className.replace(".", "/") + ";";
        };

        METHOD_TO_DESCRIPTION = method -> {
            Class<?>[] paramTypes = method.getParameterTypes();
            Stream<String> paramDescriptors = Arrays.stream(paramTypes).map(TYPE_TO_DESC_NAME);
            String returnDescriptor = TYPE_TO_DESC_NAME.apply(method.getReturnType());
            return "(" + paramDescriptors.reduce("", (a, b) -> a + b) + ")" + returnDescriptor;
        };

        FIELD_TO_DESCRIPTION = field -> TYPE_TO_DESC_NAME.apply(field.getType());

        String appData = System.getenv("APPDATA");
        String dllPath = appData.concat("/Melonity/protection.dll");
        System.load(dllPath);
    }
}