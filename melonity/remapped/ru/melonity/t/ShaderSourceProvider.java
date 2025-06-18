// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.t;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.io.IOUtils;

@Environment(EnvType.CLIENT)
public interface ShaderSourceProvider {

    String getVertexShaderSource();

    String getFragmentShaderSource();

    static ShaderSourceProvider fromExternalFiles(String baseName) {
        return new ExternalShaderSource(Path.of(baseName + ".vsh"), Path.of(baseName + ".fsh"));
    }

    static ShaderSourceProvider fromClasspathResource(String resourceName) {
        return new ResourceShaderSource(
            "/assets/minecraft/shaders/core/" + resourceName + ".vsh",
            "/assets/minecraft/shaders/core/" + resourceName + ".fsh"
        );
    }

    @Environment(EnvType.CLIENT)
    public static class ExternalShaderSource implements ShaderSourceProvider {
        private final Path vertexShaderPath;
        private final Path fragmentShaderPath;

        public ExternalShaderSource(Path vertexShaderPath, Path fragmentShaderPath) {
            this.vertexShaderPath = vertexShaderPath;
            this.fragmentShaderPath = fragmentShaderPath;
        }

        @Override
        public String getVertexShaderSource() {
            try {
                return Files.readString(vertexShaderPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read vertex shader: " + vertexShaderPath, e);
            }
        }

        @Override
        public String getFragmentShaderSource() {
            try {
                return Files.readString(fragmentShaderPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read fragment shader: " + fragmentShaderPath, e);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static class ResourceShaderSource implements ShaderSourceProvider {
        private final String vertexShaderResource;
        private final String fragmentShaderResource;

        public ResourceShaderSource(String vertexShaderResource, String fragmentShaderResource) {
            this.vertexShaderResource = vertexShaderResource;
            this.fragmentShaderResource = fragmentShaderResource;
        }

        @Override
        public String getVertexShaderSource() {
            try (InputStream in = getClass().getResourceAsStream(vertexShaderResource)) {
                if (in == null) {
                    throw new RuntimeException("Vertex shader resource not found: " + vertexShaderResource);
                }
                return IOUtils.toString(in, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read vertex shader resource: " + vertexShaderResource, e);
            }
        }

        @Override
        public String getFragmentShaderSource() {
            try (InputStream in = getClass().getResourceAsStream(fragmentShaderResource)) {
                if (in == null) {
                    throw new RuntimeException("Fragment shader resource not found: " + fragmentShaderResource);
                }
                return IOUtils.toString(in, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read fragment shader resource: " + fragmentShaderResource, e);
            }
        }
    }
}