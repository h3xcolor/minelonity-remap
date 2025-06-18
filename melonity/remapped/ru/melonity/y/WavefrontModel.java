// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.y;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class WavefrontModel {
    private static final int POSITION_SIZE = 3;
    private static final int TEX_COORD_SIZE = 2;
    private static final int VERTEX_STRIDE = 20;
    private static final int FLOAT_SIZE = 4;

    private Map<String, ModelPart> modelParts;
    private int vaoId;
    private int elementCount;

    public WavefrontModel(String objContent) {
        loadModel(objContent);
    }

    public void loadModel(String objContent) {
        List<ModelObject> objects = parseObjContent(Arrays.asList(objContent.split("\n")));
        this.modelParts = createModelParts(objects);
        this.elementCount = this.modelParts.values().stream().mapToInt(ModelPart::getIndexCount).max().orElseThrow();
    }

    public void render() {
        GL33.glBindVertexArray(this.vaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, this.elementCount, GL33.GL_UNSIGNED_INT, 0L);
    }

    public void renderPart(String partName) {
        ModelPart part = this.modelParts.get(partName);
        GL33.glBindVertexArray(this.vaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, part.getIndexCount(), GL33.GL_UNSIGNED_INT, part.getOffsetInBytes());
    }

    private Map<String, ModelPart> createModelParts(List<ModelObject> objects) {
        Map<String, ModelPart> partsMap = new HashMap<>();
        LinkedHashMap<Vertex, Integer> vertexIndexMap = new LinkedHashMap<>();
        Consumer<Vertex> vertexIndexer = vertex -> {
            if (vertexIndexMap.containsKey(vertex)) {
                return;
            }
            vertexIndexMap.put(vertex, vertexIndexMap.size());
        };

        for (ModelObject obj : objects) {
            for (Face face : obj.getFaces()) {
                vertexIndexer.accept(face.getVertex1());
                vertexIndexer.accept(face.getVertex2());
                vertexIndexer.accept(face.getVertex3());
                if (face.isQuad()) {
                    vertexIndexer.accept(face.getVertex4());
                }
            }
        }

        List<Vertex> uniqueVertices = new ArrayList<>(vertexIndexMap.keySet());
        int currentVao = GL33.glGetInteger(GL33.GL_VERTEX_ARRAY_BINDING);
        int currentArrayBuffer = GL33.glGetInteger(GL33.GL_ARRAY_BUFFER_BINDING);
        int currentElementBuffer = GL33.glGetInteger(GL33.GL_ELEMENT_ARRAY_BUFFER_BINDING);

        this.vaoId = GL33.glGenVertexArrays();
        GL33.glBindVertexArray(this.vaoId);

        int vbo = GL33.glGenBuffers();
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);

        int ebo = GL33.glGenBuffers();
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, ebo);

        int vertexDataSize = uniqueVertices.stream().mapToInt(Vertex::getStride).sum();
        ByteBuffer vertexBuffer = MemoryUtil.memAlloc(vertexDataSize);
        for (Vertex vertex : uniqueVertices) {
            vertex.write(vertexBuffer);
        }
        vertexBuffer.position(0);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertexBuffer, GL33.GL_STATIC_DRAW);

        int indexDataSize = objects.stream().mapToInt(ModelObject::getFaceCount).sum() * 3 * FLOAT_SIZE;
        ByteBuffer indexBuffer = MemoryUtil.memAlloc(indexDataSize);
        int indexOffset = 0;
        for (ModelObject obj : objects) {
            int startIndex = indexOffset;
            for (Face face : obj.getFaces()) {
                int idx1 = vertexIndexMap.get(face.getVertex1());
                int idx2 = vertexIndexMap.get(face.getVertex2());
                int idx3 = vertexIndexMap.get(face.getVertex3());
                indexBuffer.putInt(idx1).putInt(idx2).putInt(idx3);
                indexOffset += 3;
                if (face.isQuad()) {
                    int idx4 = vertexIndexMap.get(face.getVertex4());
                    indexBuffer.putInt(idx1).putInt(idx3).putInt(idx4);
                    indexOffset += 3;
                }
            }
            partsMap.put(obj.getName(), new ModelPart(indexOffset - startIndex, indexOffset, startIndex * FLOAT_SIZE));
        }
        indexBuffer.position(0);
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL33.GL_STATIC_DRAW);

        GL33.glEnableVertexAttribArray(0);
        GL33.glEnableVertexAttribArray(1);
        GL33.glVertexAttribPointer(0, POSITION_SIZE, GL33.GL_FLOAT, false, VERTEX_STRIDE, 0L);
        GL33.glVertexAttribPointer(1, TEX_COORD_SIZE, GL33.GL_FLOAT, false, VERTEX_STRIDE, 12L);

        GL33.glBindVertexArray(currentVao);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, currentArrayBuffer);
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, currentElementBuffer);

        return partsMap;
    }

    private List<ModelObject> parseObjContent(List<String> lines) {
        List<ModelObject> objects = new ArrayList<>();
        ObjParserState parserState = new ObjParserState();
        VertexPositionCollection positions = new VertexPositionCollection();
        TextureCoordinateCollection texCoords = new TextureCoordinateCollection();
        Consumer<String> objectHandler = name -> parserState.startObject(name);
        Consumer<String> faceHandler = faceData -> parserState.addFace(faceData, positions, texCoords);
        Consumer<String> positionHandler = data -> positions.add(VertexPosition.parse(data));
        Consumer<String> texCoordHandler = data -> texCoords.add(TextureCoordinate.parse(data));
        parseObjLines(lines, objectHandler, faceHandler, positionHandler, texCoordHandler);
        parserState.finishObject();
        return objects;
    }

    private void parseObjLines(List<String> lines, Consumer<String> objectHandler, Consumer<String> faceHandler, Consumer<String> positionHandler, Consumer<String> texCoordHandler) {
        for (String line : lines) {
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            if (line.startsWith("f ")) {
                faceHandler.accept(line.substring(2));
            } else if (line.startsWith("v ")) {
                positionHandler.accept(line.substring(2));
            } else if (line.startsWith("vt ")) {
                texCoordHandler.accept(line.substring(3));
            } else if (line.startsWith("o ")) {
                objectHandler.accept(line.substring(2));
            }
        }
    }

    public static WavefrontModel fromFile(String path) throws Exception {
        Path filePath = Path.of(path);
        String content = Files.readString(filePath);
        return new WavefrontModel(content);
    }

    public static WavefrontModel fromInputStream(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            System.out.println("Wavefront#fromInputStream null");
            return null;
        }
        byte[] encrypted = IOUtils.toByteArray(inputStream);
        for (int i = 0; i < encrypted.length; i++) {
            encrypted[i] = (byte) (encrypted[i] ^ 0xAB);
        }
        ByteArrayInputStream byteStream = new ByteArrayInputStream(encrypted);
        String content = IOUtils.toString(byteStream, StandardCharsets.UTF_8);
        return new WavefrontModel(content);
    }

    @Environment(EnvType.CLIENT)
    private static class ObjParserState {
        private ModelObject currentObject;
        private final List<ModelObject> objects = new ArrayList<>();

        public void startObject(String name) {
            finishObject();
            this.currentObject = new ModelObject(name);
        }

        public void addFace(String faceData, VertexPositionCollection positions, TextureCoordinateCollection texCoords) {
            if (this.currentObject == null) {
                throw new IllegalStateException("Part absent");
            }
            String[] vertexIndices = faceData.split(" ");
            Vertex v1 = parseVertex(vertexIndices[0], positions, texCoords);
            Vertex v2 = parseVertex(vertexIndices[1], positions, texCoords);
            Vertex v3 = parseVertex(vertexIndices[2], positions, texCoords);
            if (vertexIndices.length == 3) {
                this.currentObject.addFace(new Face(v1, v2, v3));
            } else {
                Vertex v4 = parseVertex(vertexIndices[3], positions, texCoords);
                this.currentObject.addFace(new Face(v1, v2, v3));
                this.currentObject.addFace(new Face(v1, v3, v4));
            }
        }

        private Vertex parseVertex(String vertexRef, VertexPositionCollection positions, TextureCoordinateCollection texCoords) {
            String[] indices = vertexRef.split("/");
            int posIndex = Integer.parseInt(indices[0]);
            int texIndex = Integer.parseInt(indices[1]);
            return new Vertex(positions.get(posIndex), texCoords.get(texIndex));
        }

        public void finishObject() {
            if (this.currentObject != null) {
                this.objects.add(this.currentObject);
                this.currentObject = null;
            }
        }

        public List<ModelObject> getObjects() {
            return objects;
        }
    }

    @Environment(EnvType.CLIENT)
    private record ModelPart(int indexCount, int endIndex, int offsetInBytes) {
        public int getIndexCount() {
            return indexCount;
        }

        public int getOffsetInBytes() {
            return offsetInBytes;
        }
    }

    @Environment(EnvType.CLIENT)
    private static class ModelObject {
        private final String name;
        private final List<Face> faces = new ArrayList<>();

        public ModelObject(String name) {
            this.name = name;
        }

        public void addFace(Face face) {
            this.faces.add(face);
        }

        public int getFaceCount() {
            return faces.size();
        }

        public String getName() {
            return name;
        }

        public List<Face> getFaces() {
            return faces;
        }
    }

    @Environment(EnvType.CLIENT)
    private record Face(Vertex vertex1, Vertex vertex2, Vertex vertex3, Vertex vertex4) {
        public Face(Vertex v1, Vertex v2, Vertex v3) {
            this(v1, v2, v3, null);
        }

        public boolean isQuad() {
            return vertex4 != null;
        }

        public Vertex getVertex1() {
            return vertex1;
        }

        public Vertex getVertex2() {
            return vertex2;
        }

        public Vertex getVertex3() {
            return vertex3;
        }

        public Vertex getVertex4() {
            return vertex4;
        }
    }

    @Environment(EnvType.CLIENT)
    private record Vertex(VertexPosition position, TextureCoordinate texCoord) {
        public int getStride() {
            return 20;
        }

        public void write(ByteBuffer buffer) {
            position.write(buffer);
            texCoord.write(buffer);
        }
    }

    @Environment(EnvType.CLIENT)
    private record VertexPosition(float x, float y, float z) {
        public static final VertexPosition ORIGIN = new VertexPosition(0.0f, 0.0f, 0.0f);

        public static VertexPosition parse(String data) {
            String[] parts = data.split(" ");
            return new VertexPosition(
                Float.parseFloat(parts[0]),
                Float.parseFloat(parts[1]),
                Float.parseFloat(parts[2])
            );
        }

        public void write(ByteBuffer buffer) {
            buffer.putFloat(x).putFloat(y).putFloat(z);
        }
    }

    @Environment(EnvType.CLIENT)
    private record TextureCoordinate(float u, float v) {
        public static final TextureCoordinate ORIGIN = new TextureCoordinate(0.0f, 0.0f);

        public static TextureCoordinate parse(String data) {
            String[] parts = data.split(" ");
            return new TextureCoordinate(
                Float.parseFloat(parts[0]),
                Float.parseFloat(parts[1])
            );
        }

        public void write(ByteBuffer buffer) {
            buffer.putFloat(u).putFloat(v);
        }
    }

    @Environment(EnvType.CLIENT)
    private static class VertexPositionCollection {
        private final List<VertexPosition> positions = new ArrayList<>();

        public void add(VertexPosition position) {
            positions.add(position);
        }

        public VertexPosition get(int index) {
            return positions.get(index - 1);
        }
    }

    @Environment(EnvType.CLIENT)
    private static class TextureCoordinateCollection {
        private final List<TextureCoordinate> texCoords = new ArrayList<>();

        public void add(TextureCoordinate texCoord) {
            texCoords.add(texCoord);
        }

        public TextureCoordinate get(int index) {
            return texCoords.get(index - 1);
        }
    }
}