// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.t;

import java.util.function.IntConsumer;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL33;

@Environment(value = EnvType.CLIENT)
public class ShaderProgram {
    private static final IntConsumer NO_OP_CONSUMER = n -> {};
    private final ShaderSourceProvider sourceProvider;
    private IntConsumer programCreationCallback = NO_OP_CONSUMER;
    private Integer programId;
    private int previousProgramId;

    public ShaderProgram(ShaderSourceProvider sourceProvider) {
        this.sourceProvider = sourceProvider;
    }

    public void use() {
        this.previousProgramId = GL33.glGetInteger(35725);
        GL33.glUseProgram(getOrCreateProgramId());
    }

    public void restore() {
        GL33.glUseProgram(this.previousProgramId);
    }

    public Uniform getUniform(String name) {
        Uniform uniform = new Uniform(name);
        addProgramCreationCallback(uniform.getProgramSetter());
        return uniform;
    }

    public void delete() {
        GL33.glDeleteProgram(this.programId);
        this.programId = null;
    }

    private int getOrCreateProgramId() {
        if (this.programId != null) {
            return this.programId;
        }
        int id = createProgram();
        this.programCreationCallback.accept(id);
        this.programId = id;
        return this.programId;
    }

    private int createProgram() {
        int program = GL33.glCreateProgram();
        String vertexSource = this.sourceProvider.getVertexSource();
        int vertexShader = compileShader(35633, vertexSource);
        String fragmentSource = this.sourceProvider.getFragmentSource();
        int fragmentShader = compileShader(35632, fragmentSource);
        GL33.glAttachShader(program, vertexShader);
        GL33.glAttachShader(program, fragmentShader);
        GL33.glLinkProgram(program);
        int linkStatus = GL33.glGetProgrami(program, 35714);
        if (linkStatus != 1) {
            String log = GL33.glGetProgramInfoLog(program);
            throw new IllegalStateException("An error occurred during linking program" + log);
        }
        GL33.glDeleteShader(vertexShader);
        GL33.glDeleteShader(fragmentShader);
        return program;
    }

    private int compileShader(int type, String source) {
        int shader = GL33.glCreateShader(type);
        GL33.glShaderSource(shader, source);
        GL33.glCompileShader(shader);
        int compileStatus = GL33.glGetShaderi(shader, 35713);
        if (compileStatus != 1) {
            String log = GL33.glGetShaderInfoLog(shader);
            throw new IllegalStateException("An error occurred during compiling shader " + log);
        }
        return shader;
    }

    private void addProgramCreationCallback(IntConsumer consumer) {
        IntConsumer currentCallback = this.programCreationCallback;
        this.programCreationCallback = n -> {
            currentCallback.accept(n);
            consumer.accept(n);
        };
    }

    @Environment(value = EnvType.CLIENT)
    public interface ShaderSourceProvider {
        String getVertexSource();
        String getFragmentSource();
    }

    @Environment(value = EnvType.CLIENT)
    public class Uniform {
        private final String name;
        private int location = -1;

        public Uniform(String name) {
            this.name = name;
        }

        public void set() {
            if (this.location == -1) {
                this.location = GL33.glGetUniformLocation(programId, this.name);
            }
        }

        private IntConsumer getProgramSetter() {
            return programId -> this.location = GL33.glGetUniformLocation(programId, this.name);
        }
    }
}