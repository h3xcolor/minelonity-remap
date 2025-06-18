// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public final class IOUtils {

    public static String readInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = createBufferedReader(inputStream);
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString();
    }

    public static BufferedReader createBufferedReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public static void writeStringToOutputStream(OutputStream outputStream, String content) throws IOException {
        BufferedWriter writer = createBufferedWriter(outputStream);
        writer.write(content);
    }

    public static BufferedWriter createBufferedWriter(OutputStream outputStream) {
        return new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    @Generated
    private IOUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}