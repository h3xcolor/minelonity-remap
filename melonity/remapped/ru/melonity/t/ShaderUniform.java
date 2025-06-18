// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.t;

import java.nio.FloatBuffer;
import java.util.function.IntConsumer;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
public class ShaderUniform {
    private static final FloatBuffer MATRIX_BUFFER = MemoryUtil.memAllocFloat(16);
    private final String uniformName;
    private int uniformLocation;

    public IntConsumer getLocationSetter() {
        return this::setUniformLocation;
    }

    public void setMatrix(Matrix4f matrix) {
        matrix.get(MATRIX_BUFFER);
        GL33.glUniformMatrix4fv(uniformLocation, false, MATRIX_BUFFER);
    }

    public void setFloat(float value) {
        GL33.glUniform1f(uniformLocation, value);
    }

    public void setVec3(float x, float y, float z) {
        GL33.glUniform3f(uniformLocation, x, y, z);
    }

    public void setInt(int value) {
        GL33.glUniform1i(uniformLocation, value);
    }

    public void setVec4(float x, float y, float z, float w) {
        GL33.glUniform4f(uniformLocation, x, y, z, w);
    }

    private void setUniformLocation(int programId) {
        uniformLocation = GL33.glGetUniformLocation(programId, uniformName);
    }

    @Generated
    public ShaderUniform(String uniformName) {
        this.uniformName = uniformName;
    }
}