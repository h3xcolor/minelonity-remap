// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s.b;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.nio.FloatBuffer;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;

@Environment(value = EnvType.CLIENT)
public class ShaderProgramWrapper {
    private final ShaderProgram shaderProgram;
    public static int unusedField = 1826244141;

    public ShaderProgramWrapper(RenderPhase.Shader shader, String name, VertexFormat vertexFormat) {
        this.shaderProgram = new ShaderProgram(shader, name, vertexFormat);
    }

    public void bind() {
        RenderSystem.setShader(() -> this.shaderProgram);
    }

    public void unbind() {
        this.shaderProgram.close();
    }

    public void setUniform(String name, int value) {
        this.shaderProgram.setUniform(name, value);
    }

    public void setUniform1f(String name, float value) {
        this.shaderProgram.getUniform(name).set(value);
    }

    public void setUniform2f(String name, float v1, float v2) {
        this.shaderProgram.getUniform(name).set(v1, v2);
    }

    public void setUniform3f(String name, float v1, float v2, float v3) {
        this.shaderProgram.getUniform(name).set(v1, v2, v3);
    }

    public void setUniform4f(String name, float v1, float v2, float v3, float v4) {
        this.shaderProgram.getUniform(name).set(v1, v2, v3, v4);
    }

    public void setUniformColor(String name, Color color) {
        setUniform4f(name, (float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
    }

    public void setUniformFloatArray256(String name, FloatBuffer buffer) {
        float[] array = new float[256];
        if (buffer != null) {
            for (int i = 0; i < 256; ++i) {
                array[i] = buffer.get(i);
            }
        }
        this.shaderProgram.getUniform(name).set(array);
    }

    public void setUniform1i(String name, int value) {
        this.shaderProgram.getUniform(name).set(value);
    }

    public void setUniform2i(String name, int v1, int v2) {
        this.shaderProgram.getUniform(name).set(v1, v2);
    }

    public void setUniform3i(String name, int v1, int v2, int v3) {
        this.shaderProgram.getUniform(name).set(v1, v2, v3);
    }

    public void setUniform4i(String name, int v1, int v2, int v3, int v4) {
        this.shaderProgram.getUniform(name).set(v1, v2, v3, v4);
    }

    public void setUniformFloatArrayFromBuffer(String name, FloatBuffer buffer) {
        this.shaderProgram.getUniform(name).set(buffer.array());
    }

    public void setUniformFloatArray(String name, float[] array) {
        this.shaderProgram.getUniform(name).set(array);
    }

    @Generated
    public ShaderProgram getShaderProgram() {
        return this.shaderProgram;
    }
}