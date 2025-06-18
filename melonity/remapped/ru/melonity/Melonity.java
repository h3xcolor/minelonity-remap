// ремапили ребята из https://t.me/dno_rumine
package ru.melonity;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.EnumSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.metafaze.protection.annotation.Compile;
import ru.metafaze.protection.annotation.Handshake;

@Environment(EnvType.CLIENT)
public enum Melonity {
    INSTANCE;

    public static String clientVersion;
    private static boolean isInitialized;
    private Object moduleManager;
    private Object eventBus;
    private Object renderSystem;
    private Object textureManager;
    private Object shaderManager;
    private Object modelLoader;
    private Object configHandler;
    private Object networkHandler;
    private Object inputHandler;
    private Object audioHandler;
    private Object worldManager;
    private Object entityManager;
    private Object playerManager;
    private Object inventoryManager;
    private Object chatManager;
    private Object commandManager;
    private Object guiManager;
    private Object hudManager;
    private Object menuManager;
    private Object keyBindingManager;
    private Object sessionManager;
    private Object accountManager;
    private Object friendManager;
    private Object macroManager;
    private Object scriptManager;
    private Object themeManager;
    private Object fontManager;
    private Object colorManager;
    private Object animationManager;
    private Object effectManager;
    private Object particleManager;
    private Object lightManager;
    private Object weatherManager;
    private Object timeManager;
    private Object biomeManager;
    private Object dimensionManager;
    private Object blockManager;
    private Object itemManager;
    private Object entityRenderer;
    private Object blockRenderer;
    private Object itemRenderer;
    private Object skyRenderer;
    private Object cloudRenderer;
    private Object weatherRenderer;
    private Object particleRenderer;
    private Object lightRenderer;
    private Object postProcessor;
    private Object frameBuffer;
    private Object shaderProgram;
    private Object vertexBuffer;
    private Object textureObject;
    private Object modelData;
    private Object vertexData;
    private Object indexData;
    private Object material;
    private Object mesh;
    private Object animation;
    private Object skeleton;
    private Object bone;
    private Object vertexShader;
    private Object fragmentShader;
    private Object geometryShader;
    private Object computeShader;
    private Object shaderSource;
    private Object uniformLocation;
    private Object attributeLocation;
    private Object bufferObject;
    private Object vertexArrayObject;
    private Object textureUnit;
    private Object samplerLocation;
    private Object matrixLocation;
    private Object vectorLocation;
    private Object floatLocation;
    private Object intLocation;
    private Object booleanLocation;
    private Object colorModulator;
    private Object lightMap;
    private Object overlayTexture;
    private Object mainTexture;
    private Object normalMap;
    private Object specularMap;
    private Object depthMap;
    private Object shadowMap;
    private Object reflectionMap;
    private Object refractionMap;
    private Object environmentMap;
    private Object cubeMap;
    private Object renderTarget;
    private Object depthBuffer;
    private Object stencilBuffer;
    private Object colorBuffer;
    private Object frameBufferObject;
    private Object renderBufferObject;
    private Object queryObject;
    private Object syncObject;
    private Object fence;
    private Object timerQuery;
    private Object occlusionQuery;
    private Object transformFeedback;
    private Object shaderStorage;
    private Object atomicCounter;
    private Object imageTexture;
    private Object bufferTexture;
    private Object multisampleTexture;
    private Object compressedTexture;
    private Object textureArray;
    private Object texture3D;
    private Object textureCube;
    private Object textureRectangle;
    private Object textureBuffer;
    private Object textureMultisample;
    private Object texture2D;
    private Object texture1D;
    private Object textureHandle;
    private Object samplerHandle;
    private Object programHandle;
    private Object shaderHandle;
    private Object bufferHandle;
    private Object vertexArrayHandle;
    private Object queryHandle;
    private Object framebufferHandle;
    private Object renderbufferHandle;
    private Object transformFeedbackHandle;
    private Object shaderStorageHandle;
    private Object atomicCounterHandle;
    private Object imageHandle;
    private Object programPipelineHandle;
    private Object contextHandle;
    private Object displayHandle;
    private Object windowHandle;
    private Object surfaceHandle;
    private Object configHandle;
    private Object pbufferHandle;
    private Object pixmapHandle;
    private Object cursorHandle;
    private Object monitorHandle;
    private Object videoModeHandle;
    private Object gammaRampHandle;
    private Object inputHandle;
    private Object joystickHandle;
    private Object gamepadHandle;
    private Object touchDeviceHandle;
    private Object sensorHandle;
    private Object clipboardHandle;
    private Object timerHandle;
    private Object threadHandle;
    private Object mutexHandle;
    private Object conditionHandle;
    private Object tlsHandle;
    private Object libraryHandle;
    private Object moduleHandle;
    private Object functionHandle;
    private Object symbolHandle;
    private Object addressHandle;
    private Object fileHandle;
    private Object directoryHandle;
    private Object streamHandle;
    private Object socketHandle;
    private Object pipeHandle;
    private Object processHandle;
    private Object memoryHandle;
    private Object virtualMemoryHandle;
    private Object physicalMemoryHandle;
    private Object sharedMemoryHandle;
    private Object mappedMemoryHandle;
    private Object allocationHandle;
    private Object heapHandle;
    private Object arenaHandle;
    private Object poolHandle;
    private Object stackHandle;
    private Object registryHandle;
    private Object keyHandle;
    private Object valueHandle;
    private Object sectionHandle;
    private Object entryHandle;
    private Object iteratorHandle;
    private Object listHandle;
    private Object mapHandle;
    private Object setHandle;
    private Object queueHandle;
    private Object dequeHandle;
    private Object treeHandle;
    private Object graphHandle;
    private Object nodeHandle;
    private Object edgeHandle;
    private Object pathHandle;
    private Object algorithmHandle;
    private Object sortHandle;
    private Object searchHandle;
    private Object hashHandle;
    private Object cryptHandle;
    private Object randomHandle;
    private Object mathHandle;
    private Object vectorHandle;
    private Object matrixHandle;
    private Object quaternionHandle;
    private Object transformHandle;
    private Object cameraHandle;
    private Object lightHandle;
    private Object materialHandle;
    private Object meshHandle;
    private Object modelHandle;
    private Object animationHandle;
    private Object skeletonHandle;
    private Object boneHandle;
    private Object vertexHandle;
    private Object indexHandle;
    private Object textureHandle2;
    private Object shaderHandle2;
    private Object programHandle2;
    private Object bufferHandle2;
    private Object framebufferHandle2;
    private Object renderbufferHandle2;
    private Object queryHandle2;
    private Object samplerHandle2;
    private Object transformFeedbackHandle2;
    private Object shaderStorageHandle2;
    private Object atomicCounterHandle2;
    private Object imageHandle2;
    private Object programPipelineHandle2;
    private Object contextHandle2;
    private Object displayHandle2;
    private Object windowHandle2;
    private Object surfaceHandle2;
    private Object configHandle2;
    private Object pbufferHandle2;
    private Object pixmapHandle2;
    private Object cursorHandle2;
    private Object monitorHandle2;
    private Object videoModeHandle2;
    private Object gammaRampHandle2;
    private Object inputHandle2;
    private Object joystickHandle2;
    private Object gamepadHandle2;
    private Object touchDeviceHandle2;
    private Object sensorHandle2;
    private Object clipboardHandle2;
    private Object timerHandle2;
    private Object threadHandle2;
    private Object mutexHandle2;
    private Object conditionHandle2;
    private Object tlsHandle2;
    private Object libraryHandle2;
    private Object moduleHandle2;
    private Object functionHandle2;
    private Object symbolHandle2;
    private Object addressHandle2;
    private Object fileHandle2;
    private Object directoryHandle2;
    private Object streamHandle2;
    private Object socketHandle2;
    private Object pipeHandle2;
    private Object processHandle2;
    private Object memoryHandle2;
    private Object virtualMemoryHandle2;
    private Object physicalMemoryHandle2;
    private Object sharedMemoryHandle2;
    private Object mappedMemoryHandle2;
    private Object allocationHandle2;
    private Object heapHandle2;
    private Object arenaHandle2;
    private Object poolHandle2;
    private Object stackHandle2;
    private Object registryHandle2;
    private Object keyHandle2;
    private Object valueHandle2;
    private Object sectionHandle2;
    private Object entryHandle2;
    private Object iteratorHandle2;
    private Object listHandle2;
    private Object mapHandle2;
    private Object setHandle2;
    private Object queueHandle2;
    private Object dequeHandle2;
    private Object treeHandle2;
    private Object graphHandle2;
    private Object nodeHandle2;
    private Object edgeHandle2;
    private Object pathHandle2;
    private Object algorithmHandle2;
    private Object sortHandle2;
    private Object searchHandle2;
    private Object hashHandle2;
    private Object cryptHandle2;
    private Object randomHandle2;
    private Object mathHandle2;
    private Object vectorHandle2;
    private Object matrixHandle2;
    private Object quaternionHandle2;
    private Object transformHandle2;
    private Object cameraHandle2;
    private Object lightHandle2;
    private Object materialHandle2;
    private Object meshHandle2;
    private Object modelHandle2;
    private Object animationHandle2;
    private Object skeletonHandle2;
    private Object boneHandle2;
    private Object vertexHandle2;
    private Object indexHandle2;
    private boolean isEnabled;
    private Object currentModule;
    private Object currentSetting;
    private Object currentValue;
    private Object currentKey;
    private Object currentButton;
    private Object currentAction;
    private Object currentModifiers;
    private Object currentScreen;
    private Object currentGui;
    private Object currentHud;
    private Object currentMenu;
    private Object currentWorld;
    private Object currentPlayer;
    private Object currentEntity;
    private Object currentItem;
    private Object currentBlock;
    private Object currentBiome;
    private Object currentDimension;
    private Object currentWeather;
    private Object currentTime;
    private Object currentLight;
    private Object currentParticle;
    private Object currentEffect;
    private Object currentSound;
    private Object currentMusic;
    private Object currentAmbient;
    private Object currentVoice;
    private Object currentNarration;
    private Object currentSubtitle;
    private Object currentCaption;
    private Object currentNotification;
    private Object currentMessage;
    private Object currentCommand;
    private Object currentMacro;
    private Object currentScript;
    private Object currentTheme;
    private Object currentFont;
    private Object currentColor;
    private Object currentAnimation;
    private Object currentTexture;
    private Object currentShader;
    private Object currentProgram;
    private Object currentBuffer;
    private Object currentFramebuffer;
    private Object currentRenderbuffer;
    private Object currentQuery;
    private Object currentSampler;
    private Object currentTransformFeedback;
    private Object currentShaderStorage;
    private Object currentAtomicCounter;
    private Object currentImage;
    private Object currentProgramPipeline;
    private Object currentContext;
    private Object currentDisplay;
    private Object currentWindow;
    private Object currentSurface;
    private Object currentConfig;
    private Object currentPBuffer;
    private Object currentPixmap;
    private Object currentCursor;
    private Object currentMonitor;
    private Object currentVideoMode;
    private Object currentGammaRamp;
    private Object currentInput;
    private Object currentJoystick;
    private Object currentGamepad;
    private Object currentTouchDevice;
    private Object currentSensor;
    private Object currentClipboard;
    private Object currentTimer;
    private Object currentThread;
    private Object currentMutex;
    private Object currentCondition;
    private Object currentTls;
    private Object currentLibrary;
    private Object currentModule2;
    private Object currentFunction;
    private Object currentSymbol;
    private Object currentAddress;
    private Object currentFile;
    private Object currentDirectory;
    private Object currentStream;
    private Object currentSocket;
    private Object currentPipe;
    private Object currentProcess;
    private Object currentMemory;
    private Object currentVirtualMemory;
    private Object currentPhysicalMemory;
    private Object currentSharedMemory;
    private Object currentMappedMemory;
    private Object currentAllocation;
    private Object currentHeap;
    private Object currentArena;
    private Object currentPool;
    private Object currentStack;
    private Object currentRegistry;
    private Object currentKey2;
    private Object currentValue2;
    private Object currentSection;
    private Object currentEntry;
    private Object currentIterator;
    private Object currentList;
    private Object currentMap;
    private Object currentSet;
    private Object currentQueue;
    private Object currentDeque;
    private Object currentTree;
    private Object currentGraph;
    private Object currentNode;
    private Object currentEdge;
    private Object currentPath;
    private Object currentAlgorithm;
    private Object currentSort;
    private Object currentSearch;
    private Object currentHash;
    private Object currentCrypt;
    private Object currentRandom;
    private Object currentMath;
    private Object currentVector;
    private Object currentMatrix;
    private Object currentQuaternion;
    private Object currentTransform;
    private Object currentCamera;
    private Object currentLight2;
    private Object currentMaterial2;
    private Object currentMesh2;
    private Object currentModel2;
    private Object currentAnimation2;
    private Object currentSkeleton2;
    private Object currentBone2;
    private Object currentVertex2;
    private Object currentIndex2;
    public static boolean isRendering;
    public static boolean isGuiOpen;
    public static boolean isWorldLoaded;
    public static boolean isPlayerAlive;
    public static long lastRenderTime;
    public static long lastUpdateTime;
    public static long lastTickTime;
    public static long lastFrameTime;
    public static float renderPartialTicks;
    private static final long INIT_TIME;
    private final List<Object> moduleList;
    public net.minecraft.client.util.math.MatrixStack matrixStack;

    public static Melonity[] values() {
        return (Melonity[]) $VALUES.clone();
    }

    public static Melonity valueOf(String name) {
        return Enum.valueOf(Melonity.class, name);
    }

    private Melonity() {
        this.moduleList = Lists.newArrayList();
    }

    public Color getColor(int red, int green) {
        return new Color(red, green, 0);
    }

    @Compile
    @Handshake
    public native void onRender(MatrixStack matrices);

    @Compile
    public native void initialize();

    public void setupModules() {
    }

    public void onKeyPress(int keyCode) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
            }
        };
        timer.schedule(task, 300L);
    }

    @Compile
    public native void update();

    public void renderItemEnchantmentGlint(ItemStack stack, float x, float y, float scale, float alpha, MatrixStack matrices) {
        if (stack.getItem() instanceof ToolItem) {
            List<EnchantmentLevelEntry> enchantments = EnchantmentHelper.getPossibleEntries(30, stack, true);
            for (EnchantmentLevelEntry entry : enchantments) {
                Enchantment enchantment = entry.enchantment;
                Identifier id = Registry.ENCHANTMENT.getId(enchantment);
                if (id == null) continue;
                if (id.getPath().contains("fire_aspect")) {
                    Color color = new Color(255, 0, 0, 100);
                }
            }
        }
    }

    @Compile
    public native void render(MatrixStack matrices);

    @Generated
    public Object getModuleManager() {
        return moduleManager;
    }

    @Generated
    public Object getEventBus() {
        return eventBus;
    }

    @Generated
    public Object getRenderSystem() {
        return renderSystem;
    }

    @Generated
    public Object getTextureManager() {
        return textureManager;
    }

    @Generated
    public Object getShaderManager() {
        return shaderManager;
    }

    @Generated
    public Object getModelLoader() {
        return modelLoader;
    }

    @Generated
    public Object getConfigHandler() {
        return configHandler;
    }

    @Generated
    public Object getNetworkHandler() {
        return networkHandler;
    }

    @Generated
    public Object getInputHandler() {
        return inputHandler;
    }

    @Generated
    public Object getAudioHandler() {
        return audioHandler;
    }

    @Generated
    public Object getWorldManager() {
        return worldManager;
    }

    @Generated
    public Object getEntityManager() {
        return entityManager;
    }

    @Generated
    public Object getPlayerManager() {
        return playerManager;
    }

    @Generated
    public Object getInventoryManager() {
        return inventoryManager;
    }

    @Generated
    public Object getChatManager() {
        return chatManager;
    }

    @Generated
    public Object getCommandManager() {
        return commandManager;
    }

    @Generated
    public Object getGuiManager() {
        return guiManager;
    }

    @Generated
    public Object getHudManager() {
        return hudManager;
    }

    @Generated
    public Object getMenuManager() {
        return menuManager;
    }

    @Generated
    public Object getKeyBindingManager() {
        return keyBindingManager;
    }

    @Generated
    public Object getSessionManager() {
        return sessionManager;
    }

    @Generated
    public Object getAccountManager() {
        return accountManager;
    }

    @Generated
    public Object getFriendManager() {
        return friendManager;
    }

    @Generated
    public Object getMacroManager() {
        return macroManager;
    }

    @Generated
    public Object getScriptManager() {
        return scriptManager;
    }

    @Generated
    public Object getThemeManager() {
        return themeManager;
    }

    @Generated
    public Object getFontManager() {
        return fontManager;
    }

    @Generated
    public Object getColorManager() {
        return colorManager;
    }

    @Generated
    public Object getAnimationManager() {
        return animationManager;
    }

    @Generated
    public Object getEffectManager() {
        return effectManager;
    }

    @Generated
    public Object getParticleManager() {
        return particleManager;
    }

    @Generated
    public Object getLightManager() {
        return lightManager;
    }

    @Generated
    public Object getWeatherManager() {
        return weatherManager;
    }

    @Generated
    public Object getTimeManager() {
        return timeManager;
    }

    @Generated
    public Object getBiomeManager() {
        return biomeManager;
    }

    @Generated
    public Object getDimensionManager() {
        return dimensionManager;
    }

    @Generated
    public Object getBlockManager() {
        return blockManager;
    }

    @Generated
    public Object getItemManager() {
        return itemManager;
    }

    @Generated
    public Object getEntityRenderer() {
        return entityRenderer;
    }

    @Generated
    public Object getBlockRenderer() {
        return blockRenderer;
    }

    @Generated
    public Object getItemRenderer() {
        return itemRenderer;
    }

    @Generated
    public Object getSkyRenderer() {
        return skyRenderer;
    }

    @Generated
    public Object getCloudRenderer() {
        return cloudRenderer;
    }

    @Generated
    public Object getWeatherRenderer() {
        return weatherRenderer;
    }

    @Generated
    public Object getParticleRenderer() {
        return particleRenderer;
    }

    @Generated
    public Object getLightRenderer() {
        return lightRenderer;
    }

    @Generated
    public Object getPostProcessor() {
        return postProcessor;
    }

    @Generated
    public Object getFrameBuffer() {
        return frameBuffer;
    }

    @Generated
    public Object getShaderProgram() {
        return shaderProgram;
    }

    @Generated
    public Object getVertexBuffer() {
        return vertexBuffer;
    }

    @Generated
    public Object getTextureObject() {
        return textureObject;
    }

    @Generated
    public Object getModelData() {
        return modelData;
    }

    @Generated
    public Object getVertexData() {
        return vertexData;
    }

    @Generated
    public Object getIndexData() {
        return indexData;
    }

    @Generated
    public Object getMaterial() {
        return material;
    }

    @Generated
    public Object getMesh() {
        return mesh;
    }

    @Generated
    public Object getAnimation() {
        return animation;
    }

    @Generated
    public Object getSkeleton() {
        return skeleton;
    }

    @Generated
    public Object getBone() {
        return bone;
    }

    @Generated
    public Object getVertexShader() {
        return vertexShader;
    }

    @Generated
    public Object getFragmentShader() {
        return fragmentShader;
    }

    @Generated
    public Object getGeometryShader() {
        return geometryShader;
    }

    @Generated
    public Object getComputeShader() {
        return computeShader;
    }

    @Generated
    public Object getShaderSource() {
        return shaderSource;
    }

    @Generated
    public Object getUniformLocation() {
        return uniformLocation;
    }

    @Generated
    public Object getAttributeLocation() {
        return attributeLocation;
    }

    @Generated
    public Object getBufferObject() {
        return bufferObject;
    }

    @Generated
    public Object getVertexArrayObject() {
        return vertexArrayObject;
    }

    @Generated
    public Object getTextureUnit() {
        return textureUnit;
    }

    @Generated
    public Object getSamplerLocation() {
        return samplerLocation;
    }

    @Generated
    public Object getMatrixLocation() {
        return matrixLocation;
    }

    @Generated
    public Object getVectorLocation() {
        return vectorLocation;
    }

    @Generated
    public Object getFloatLocation() {
        return floatLocation;
    }

    @Generated
    public Object getIntLocation() {
        return intLocation;
    }

    @Generated
    public Object getBooleanLocation() {
        return booleanLocation;
    }

    @Generated
    public Object getColorModulator() {
        return colorModulator;
    }

    @Generated
    public Object getLightMap() {
        return lightMap;
    }

    @Generated
    public Object getOverlayTexture() {
        return overlayTexture;
    }

    @Generated
    public Object getMainTexture() {
        return mainTexture;
    }

    @Generated
    public Object getNormalMap() {
        return normalMap;
    }

    @Generated
    public Object getSpecularMap() {
        return specularMap;
    }

    @Generated
    public Object getDepthMap() {
        return depthMap;
    }

    @Generated
    public Object getShadowMap() {
        return shadowMap;
    }

    @Generated
    public Object getReflectionMap() {
        return reflectionMap;
    }

    @Generated
    public Object getRefractionMap() {
        return refractionMap;
    }

    @Generated
    public Object getEnvironmentMap() {
        return environmentMap;
    }

    @Generated
    public Object getCubeMap() {
        return cubeMap;
    }

    @Generated
    public Object getRenderTarget() {
        return renderTarget;
    }

    @Generated
    public Object getDepthBuffer() {
        return depthBuffer;
    }

    @Generated
    public Object getStencilBuffer() {
        return stencilBuffer;
    }

    @Generated
    public Object getColorBuffer() {
        return colorBuffer;
    }

    @Generated
    public Object getFrameBufferObject() {
        return frameBufferObject;
    }

    @Generated
    public Object getRenderBufferObject() {
        return renderBufferObject;
    }

    @Generated
    public Object getQueryObject() {
        return queryObject;
    }

    @Generated
    public Object getSyncObject() {
        return syncObject;
    }

    @Generated
    public Object getFence() {
        return fence;
    }

    @Generated
    public Object getTimerQuery() {
        return timerQuery;
    }

    @Generated
    public Object getOcclusionQuery() {
        return occlusionQuery;
    }

    @Generated
    public Object getTransformFeedback() {
        return transformFeedback;
    }

    @Generated
    public Object getShaderStorage() {
        return shaderStorage;
    }

    @Generated
    public Object getAtomicCounter() {
        return atomicCounter;
    }

    @Generated
    public Object getImageTexture() {
        return imageTexture;
    }

    @Generated
    public Object getBufferTexture() {
        return bufferTexture;
    }

    @Generated
    public Object getMultisampleTexture() {
        return multisampleTexture;
    }

    @Generated
    public Object getCompressedTexture() {
        return compressedTexture;
    }

    @Generated
    public Object getTextureArray() {
        return textureArray;
    }

    @Generated
    public Object getTexture3D() {
        return texture3D;
    }

    @Generated
    public Object getTextureCube() {
        return textureCube;
    }

    @Generated
    public Object getTextureRectangle() {
        return textureRectangle;
    }

    @Generated
    public Object getTextureBuffer() {
        return textureBuffer;
    }

    @Generated
    public Object getTextureMultisample() {
        return textureMultisample;
    }

    @Generated
    public Object getTexture2D() {
        return texture2D;
    }

    @Generated
    public Object getTexture1D() {
        return texture1D;
    }

    @Generated
    public Object getTextureHandle() {
        return textureHandle;
    }

    @Generated
    public Object getSamplerHandle() {
        return samplerHandle;
    }

    @Generated
    public Object getProgramHandle() {
        return programHandle;
    }

    @Generated
    public Object getShaderHandle() {
        return shaderHandle;
    }

    @Generated
    public Object getBufferHandle() {
        return bufferHandle;
    }

    @Generated
    public Object getVertexArrayHandle() {
        return vertexArrayHandle;
    }

    @Generated
    public Object getQueryHandle() {
        return queryHandle;
    }

    @Generated
    public Object getFramebufferHandle() {
        return framebufferHandle;
    }

    @Generated
    public Object getRenderbufferHandle() {
        return renderbufferHandle;
    }

    @Generated
    public Object getTransformFeedbackHandle() {
        return transformFeedbackHandle;
    }

    @Generated
    public Object getShaderStorageHandle() {
        return shaderStorageHandle;
    }

    @Generated
    public Object getAtomicCounterHandle() {
        return atomicCounterHandle;
    }

    @Generated
    public Object getImageHandle() {
        return imageHandle;
    }

    @Generated
    public Object getProgramPipelineHandle() {
        return programPipelineHandle;
    }

    @Generated
    public Object getContextHandle() {
        return contextHandle;
    }

    @Generated
    public Object getDisplayHandle() {
        return displayHandle;
    }

    @Generated
    public Object getWindowHandle() {
        return windowHandle;
    }

    @Generated
    public Object getSurfaceHandle() {
        return surfaceHandle;
    }

    @Generated
    public Object getConfigHandle() {
        return configHandle;
    }

    @Generated
    public Object getPbufferHandle() {
        return pbufferHandle;
    }

    @Generated
    public Object getPixmapHandle() {
        return pixmapHandle;
    }

    @Generated
    public Object getCursorHandle() {
        return cursorHandle;
    }

    @Generated
    public Object getMonitorHandle() {
        return monitorHandle;
    }

    @Generated
    public Object getVideoModeHandle() {
        return videoModeHandle;
    }

    @Generated
    public Object getGammaRampHandle() {
        return gammaRampHandle;
    }

    @Generated
    public Object getInputHandle() {
        return inputHandle;
    }

    @Generated
    public Object getJoystickHandle() {
        return joystickHandle;
    }

    @Generated
    public Object getGamepadHandle() {
        return gamepadHandle;
    }

    @Generated
    public Object getTouchDeviceHandle() {
        return touchDeviceHandle;
    }

    @Generated
    public Object getSensorHandle() {
        return sensorHandle;
    }

    @Generated
    public Object getClipboardHandle() {
        return clipboardHandle;
    }

    @Generated
    public Object getTimerHandle() {
        return timerHandle;
    }

    @Generated
    public Object getThreadHandle() {
        return threadHandle;
    }

    @Generated
    public Object getMutexHandle() {
        return mutexHandle;
    }

    @Generated
    public Object getConditionHandle() {
        return conditionHandle;
    }

    @Generated
    public Object getTlsHandle() {
        return tlsHandle;
    }

    @Generated
    public Object getLibraryHandle() {
        return libraryHandle;
    }

    @Generated
    public Object getModuleHandle() {
        return moduleHandle;
    }

    @Generated
    public Object getFunctionHandle() {
        return functionHandle;
    }

    @Generated
    public Object getSymbolHandle() {
        return symbolHandle;
    }

    @Generated
    public Object getAddressHandle() {
        return addressHandle;
    }

    @Generated
    public Object getFileHandle() {
        return fileHandle;
    }

    @Generated
    public Object getDirectoryHandle() {
        return directoryHandle;
    }

    @Generated
    public Object getStreamHandle() {
        return streamHandle;
    }

    @Generated
    public Object getSocketHandle() {
        return socketHandle;
    }

    @Generated
    public Object getPipeHandle() {
        return pipeHandle;
    }

    @Generated
    public Object getProcessHandle() {
        return processHandle;
    }

    @Generated
    public Object getMemoryHandle() {
        return memoryHandle;
    }

    @Generated
    public Object getVirtualMemoryHandle() {
        return virtualMemoryHandle;
    }

    @Generated
    public Object getPhysicalMemoryHandle() {
        return physicalMemoryHandle;
    }

    @Generated
    public Object getSharedMemoryHandle() {
        return sharedMemoryHandle;
    }

    @Generated
    public Object getMappedMemoryHandle() {
        return mappedMemoryHandle;
    }

    @Generated
    public Object getAllocationHandle() {
        return allocationHandle;
    }

    @Generated
    public Object getHeapHandle() {
        return heapHandle;
    }

    @Generated
    public Object getArenaHandle() {
        return arenaHandle;
    }

    @Generated
    public Object getPoolHandle() {
        return poolHandle;
    }

    @Generated
    public Object getStackHandle() {
        return stackHandle;
    }

    @Generated
    public Object getRegistryHandle() {
        return registryHandle;
    }

    @Generated
    public Object getKeyHandle2() {
        return keyHandle2;
    }

    @Generated
    public Object getValueHandle2() {
        return valueHandle2;
    }

    @Generated
    public Object getSectionHandle2() {
        return sectionHandle2;
    }

    @Generated
    public Object getEntryHandle2() {
        return entryHandle2;
    }

    @Generated
    public Object getIteratorHandle2() {
        return iteratorHandle2;
    }

    @Generated
    public Object getListHandle2() {
        return listHandle2;
    }

    @Generated
    public Object getMapHandle2() {
        return mapHandle2;
    }

    @Generated
    public Object getSetHandle2() {
        return setHandle2;
    }

    @Generated
    public Object getQueueHandle2() {
        return queueHandle2;
    }

    @Generated
    public Object getDequeHandle2() {
        return dequeHandle2;
    }

    @Generated
    public Object getTreeHandle2() {
        return treeHandle2;
    }

    @Generated
    public Object getGraphHandle2() {
        return graphHandle2;
    }

    @Generated
    public Object getNodeHandle2() {
        return nodeHandle2;
    }

    @Generated
    public Object getEdgeHandle2() {
        return edgeHandle2;
    }

    @Generated
    public Object getPathHandle2() {
        return pathHandle2;
    }

    @Generated
    public Object getAlgorithmHandle2() {
        return algorithmHandle2;
    }

    @Generated
    public Object getSortHandle2() {
        return sortHandle2;
    }

    @Generated
    public Object getSearchHandle2() {
        return searchHandle2;
    }

    @Generated
    public Object getHashHandle2() {
        return hashHandle2;
    }

    @Generated
    public Object getCryptHandle2() {
        return cryptHandle2;
    }

    @Generated
    public Object getRandomHandle2() {
        return randomHandle2;
    }

    @Generated
    public Object getMathHandle2() {
        return mathHandle2;
    }

    @Generated
    public Object getVectorHandle2() {
        return vectorHandle2;
    }

    @Generated
    public Object getMatrixHandle2() {
        return matrixHandle2;
    }

    @Generated
    public Object getQuaternionHandle2() {
        return quaternionHandle2;
    }

    @Generated
    public Object getTransformHandle2() {
        return transformHandle2;
    }

    @Generated
    public Object getCameraHandle2() {
        return cameraHandle2;
    }

    @Generated
    public Object getLightHandle2() {
        return lightHandle2;
    }

    @Generated
    public Object getMaterialHandle2() {
        return materialHandle2;
    }

    @Generated
    public Object getMeshHandle2() {
        return meshHandle2;
    }

    @Generated
    public Object getModelHandle2() {
        return modelHandle2;
    }

    @Generated
    public Object getAnimationHandle2() {
        return animationHandle2;
    }

    @Generated
    public Object getSkeletonHandle2() {
        return skeletonHandle2;
    }

    @Generated
    public Object getBoneHandle2() {
        return boneHandle2;
    }

    @Generated
    public Object getVertexHandle2() {
        return vertexHandle2;
    }

    @Generated
    public Object getIndexHandle2() {
        return indexHandle2;
    }

    @Generated
    public boolean isEnabled() {
        return isEnabled;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Generated
    public Object getCurrentModule() {
        return currentModule;
    }

    @Generated
    public void setCurrentModule(Object module) {
        currentModule = module;
    }

    @Generated
    public Object getCurrentSetting() {
        return currentSetting;
    }

    @Generated
    public void setCurrentSetting(Object setting) {
        currentSetting = setting;
    }

    @Generated
    public Object getCurrentValue() {
        return currentValue;
    }

    @Generated
    public void setCurrentValue(Object value) {
        currentValue = value;
    }

    @Generated
    public Object getCurrentKey() {
        return currentKey;
    }

    @Generated
    public void setCurrentKey(Object key) {
        currentKey = key;
    }

    @Generated
    public Object getCurrentButton() {
        return currentButton;
    }

    @Generated
    public void setCurrentButton(Object button) {
        currentButton = button;
    }

    @Generated
    public Object getCurrentAction() {
        return currentAction;
    }

    @Generated
    public void setCurrentAction(Object action) {
        currentAction = action;
    }

    @Generated
    public Object getCurrentModifiers() {
        return currentModifiers;
    }

    @Generated
    public void setCurrentModifiers(Object modifiers) {
        currentModifiers = modifiers;
    }

    @Generated
    public Object getCurrentScreen() {
        return currentScreen;
    }

    @Generated
    public void setCurrentScreen(Object screen) {
        currentScreen = screen;
    }

    @Generated
    public Object getCurrentGui() {
        return currentGui;
    }

    @Generated
    public void setCurrentGui(Object gui) {
        currentGui = gui;
    }

    @Generated
    public Object getCurrentHud() {
        return currentHud;
    }

    @Generated
    public void setCurrentHud(Object hud) {
        currentHud = hud;
    }

    @Generated
    public Object getCurrentMenu() {
        return currentMenu;
    }

    @Generated
    public void setCurrentMenu(Object menu) {
        currentMenu = menu;
    }

    @Generated
    public Object getCurrentWorld() {
        return currentWorld;
    }

    @Generated
    public void setCurrentWorld(Object world) {
        currentWorld = world;
    }

    @Generated
    public Object getCurrentPlayer() {
        return currentPlayer;
    }

    @Generated
    public void setCurrentPlayer(Object player) {
        currentPlayer = player;
    }

    @Generated
    public Object getCurrentEntity() {
        return currentEntity;
    }

    @Generated
    public void setCurrentEntity(Object entity) {
        currentEntity = entity;
    }

    @Generated
    public Object getCurrentItem() {
        return currentItem;
    }

    @Generated
    public void setCurrentItem(Object item) {
        currentItem = item;
    }

    @Generated
    public Object getCurrentBlock() {
        return currentBlock;
    }

    @Generated
    public void setCurrentBlock(Object block) {
        currentBlock = block;
    }

    @Generated
    public Object getCurrentBiome() {
        return currentBiome;
    }

    @Generated
    public void setCurrentBiome(Object biome) {
        currentBiome = biome;
    }

    @Generated
    public Object getCurrentDimension() {
        return currentDimension;
    }

    @Generated
    public void setCurrentDimension(Object dimension) {
        currentDimension = dimension;
    }

    @Generated
    public Object getCurrentWeather() {
        return currentWeather;
    }

    @Generated
    public void setCurrentWeather(Object weather) {
        currentWeather = weather;
    }

    @Generated
    public Object getCurrentTime() {
        return currentTime;
    }

    @Generated
    public void setCurrentTime(Object time) {
        currentTime = time;
    }

    @Generated
    public Object getCurrentLight() {
        return currentLight;
    }

    @Generated
    public void setCurrentLight(Object light) {
        currentLight = light;
    }

    @Generated
    public Object getCurrentParticle() {
        return currentParticle;
    }

    @Generated
    public void setCurrentParticle(Object particle) {
        currentParticle = particle;
    }

    @Generated
    public Object getCurrentEffect() {
        return currentEffect;
    }

    @Generated
    public void setCurrentEffect(Object effect) {
        currentEffect = effect;
    }

    @Generated
    public Object getCurrentSound() {
        return currentSound;
    }

    @Generated
    public void setCurrentSound(Object sound) {
        currentSound = sound;
    }

    @Generated
    public Object getCurrentMusic() {
        return currentMusic;
    }

    @Generated
    public void setCurrentMusic(Object music) {
        currentMusic = music;
    }

    @Generated
    public Object getCurrentAmbient() {
        return currentAmbient;
    }

    @Generated
    public void setCurrentAmbient(Object ambient) {
        currentAmbient = ambient;
    }

    @Generated
    public Object getCurrentVoice() {
        return currentVoice;
    }

    @Generated
    public void setCurrentVoice(Object voice) {
        currentVoice = voice;
    }

    @Generated
    public Object getCurrentNarration() {
        return currentNarration;
    }

    @Generated
    public void setCurrentNarration(Object narration) {
        currentNarration = narration;
    }

    @Generated
    public Object getCurrentSubtitle() {
        return currentSubtitle;
    }

    @Generated
    public void setCurrentSubtitle(Object subtitle) {
        currentSubtitle = subtitle;
    }

    @Generated
    public Object getCurrentCaption() {
        return currentCaption;
    }

    @Generated
    public void setCurrentCaption(Object caption) {
        currentCaption = caption;
    }

    @Generated
    public Object getCurrentNotification() {
        return currentNotification;
    }

    @Generated
    public void setCurrentNotification(Object notification) {
        currentNotification = notification;
    }

    @Generated
    public Object getCurrentMessage() {
        return currentMessage;
    }

    @Generated
    public void setCurrentMessage(Object message) {
        currentMessage = message;
    }

    @Generated
    public Object getCurrentCommand() {
        return currentCommand;
    }

    @Generated
    public void setCurrentCommand(Object command) {
        currentCommand = command;
    }

    @Generated
    public Object getCurrentMacro() {
        return currentMacro;
    }

    @Generated
    public void setCurrentMacro(Object macro) {
        currentMacro = macro;
    }

    @Generated
    public Object getCurrentScript() {
        return currentScript;
    }

    @Generated
    public void setCurrentScript(Object script) {
        currentScript = script;
    }

    @Generated
    public Object getCurrentTheme() {
        return currentTheme;
    }

    @Generated
    public void setCurrentTheme(Object theme) {
        currentTheme = theme;
    }

    @Generated
    public Object getCurrentFont() {
        return currentFont;
    }

    @Generated
    public void setCurrentFont(Object font) {
        currentFont = font;
    }

    @Generated
    public Object getCurrentColor() {
        return currentColor;
    }

    @Generated
    public void setCurrentColor(Object color) {
        currentColor = color;
    }

    @Generated
    public Object getCurrentAnimation() {
        return currentAnimation;
    }

    @Generated
    public void setCurrentAnimation(Object animation) {
        currentAnimation = animation;
    }

    @Generated
    public Object getCurrentTexture() {
        return currentTexture;
    }

    @Generated
    public void setCurrentTexture(Object texture) {
        currentTexture = texture;
    }

    @Generated
    public Object getCurrentShader() {
        return currentShader;
    }

    @Generated
    public void setCurrentShader(Object shader) {
        currentShader = shader;
    }

    @Generated
    public Object getCurrentProgram() {
        return currentProgram;
    }

    @Generated
    public void setCurrentProgram(Object program) {
        currentProgram = program;
    }

    @Generated
    public Object getCurrentBuffer() {
        return currentBuffer;
    }

    @Generated
    public void setCurrentBuffer(Object buffer) {
        currentBuffer = buffer;
    }

    @Generated
    public Object getCurrentFramebuffer() {
        return currentFramebuffer;
    }

    @Generated
    public void setCurrentFramebuffer(Object framebuffer) {
        currentFramebuffer = framebuffer;
    }

    @Generated
    public Object getCurrentRenderbuffer() {
        return currentRenderbuffer;
    }

    @Generated
    public void setCurrentRenderbuffer(Object renderbuffer) {
        currentRenderbuffer = renderbuffer;
    }

    @Generated
    public Object getCurrentQuery() {
        return currentQuery;
    }

    @Generated
    public void setCurrentQuery(Object query) {
        currentQuery = query;
    }

    @Generated
    public Object getCurrentSampler() {
        return currentSampler;
    }

    @Generated
    public void setCurrentSampler(Object sampler) {
        currentSampler = sampler;
    }

    @Generated
    public Object getCurrentTransformFeedback() {
        return currentTransformFeedback;
    }

    @Generated
    public void setCurrentTransformFeedback(Object transformFeedback) {
        currentTransformFeedback = transformFeedback;
    }

    @Generated
    public Object getCurrentShaderStorage() {
        return currentShaderStorage;
    }

    @Generated
    public void setCurrentShaderStorage(Object shaderStorage) {
        currentShaderStorage = shaderStorage;
    }

    @Generated
    public Object getCurrentAtomicCounter() {
        return currentAtomicCounter;
    }

    @Generated
    public void setCurrentAtomicCounter(Object atomicCounter) {
        currentAtomicCounter = atomicCounter;
    }

    @Generated
    public Object getCurrentImage() {
        return currentImage;
    }

    @Generated
    public void setCurrentImage(Object image) {
        currentImage = image;
    }

    @Generated
    public Object getCurrentProgramPipeline() {
        return currentProgramPipeline;
    }

    @Generated
    public void setCurrentProgramPipeline(Object programPipeline) {
        currentProgramPipeline = programPipeline;
    }

    @Generated
    public Object getCurrentContext() {
        return currentContext;
    }

    @Generated
    public void setCurrentContext(Object context) {
        currentContext = context;
    }

    @Generated
    public Object getCurrentDisplay() {
        return currentDisplay;
    }

    @Generated
    public void setCurrentDisplay(Object display) {
        currentDisplay = display;
    }

    @Generated
    public Object getCurrentWindow() {
        return currentWindow;
    }

    @Generated
    public void setCurrentWindow(Object window) {
        currentWindow = window;
    }

    @Generated
    public Object getCurrentSurface() {
        return currentSurface;
    }

    @Generated
    public void setCurrentSurface(Object surface) {
        currentSurface = surface;
    }

    @Generated
    public Object getCurrentConfig() {
        return currentConfig;
    }

    @Generated
    public void setCurrentConfig(Object config) {
        currentConfig = config;
    }

    @Generated
    public Object getCurrentPBuffer() {
        return currentPBuffer;
    }

    @Generated
    public void setCurrentPBuffer(Object pBuffer) {
        currentPBuffer = pBuffer;
    }

    @Generated
    public Object getCurrentPixmap() {
        return currentPixmap;
    }

    @Generated
    public void setCurrentPixmap(Object pixmap) {
        currentPixmap = pixmap;
    }

    @Generated
    public Object getCurrentCursor() {
        return currentCursor;
    }

    @Generated
    public void setCurrentCursor(Object cursor) {
        currentCursor = cursor;
    }

    @Generated
    public Object getCurrentMonitor() {
        return currentMonitor;
    }

    @Generated
    public void setCurrentMonitor(Object monitor) {
        currentMonitor = monitor;
    }

    @Generated
    public Object getCurrentVideoMode() {
        return currentVideoMode;
    }

    @Generated
    public void setCurrentVideoMode(Object videoMode) {
        currentVideoMode = videoMode;
    }

    @Generated
    public Object getCurrentGammaRamp() {
        return currentGammaRamp;
    }

    @Generated
    public void setCurrentGammaRamp(Object gammaRamp) {
        currentGammaRamp = gammaRamp;
    }

    @Generated
    public Object getCurrentInput() {
        return currentInput;
    }

    @Generated
    public void setCurrentInput(Object input) {
        currentInput = input;
    }

    @Generated
    public Object getCurrentJoystick() {
        return currentJoystick;
    }

    @Generated
    public void setCurrentJoystick(Object joystick) {
        currentJoystick = joystick;
    }

    @Generated
    public Object getCurrentGamepad() {
        return currentGamepad;
    }

    @Generated
    public void setCurrentGamepad(Object gamepad) {
        currentGamepad = gamepad;
    }

    @Generated
    public Object getCurrentTouchDevice() {
        return currentTouchDevice;
    }

    @Generated
    public void setCurrentTouchDevice(Object touchDevice) {
        currentTouchDevice = touchDevice;
    }

    @Generated
    public Object getCurrentSensor() {
        return currentSensor;
    }

    @Generated
    public void setCurrentSensor(Object sensor) {
        currentSensor = sensor;
    }

    @Generated
    public Object getCurrentClipboard() {
        return currentClipboard;
    }

    @Generated
    public void setCurrentClipboard(Object clipboard) {
        currentClipboard = clipboard;
    }

    @Generated
    public Object getCurrentTimer() {
        return currentTimer;
    }

    @Generated
    public void setCurrentTimer(Object timer) {
        currentTimer = timer;
    }

    @Generated
    public Object getCurrentThread() {
        return currentThread;
    }

    @Generated
    public void setCurrentThread(Object thread) {
        currentThread = thread;
    }

    @Generated
    public Object getCurrentMutex() {
        return currentMutex;
    }

    @Generated
    public void setCurrentMutex(Object mutex) {
        currentMutex = mutex;
    }

    @Generated
    public Object getCurrentCondition() {
        return currentCondition;
    }

    @Generated
    public void setCurrentCondition(Object condition) {
        currentCondition = condition;
    }

    @Generated
    public Object getCurrentTls() {
        return currentTls;
    }

    @Generated
    public void setCurrentTls(Object tls) {
        currentTls = tls;
    }

    @Generated
    public Object getCurrentLibrary() {
        return currentLibrary;
    }

    @Generated
    public void setCurrentLibrary(Object library) {
        currentLibrary = library;
    }

    @Generated
    public Object getCurrentModule2() {
        return currentModule2;
    }

    @Generated
    public void setCurrentModule2(Object module) {
        currentModule2 = module;
    }

    @Generated
    public Object getCurrentFunction() {
        return currentFunction;
    }

    @Generated
    public void setCurrentFunction(Object function) {
        currentFunction = function;
    }

    @Generated
    public Object getCurrentSymbol() {
        return currentSymbol;
    }

    @Generated
    public void setCurrentSymbol(Object symbol) {
        currentSymbol = symbol;
    }

    @Generated
    public Object getCurrentAddress() {
        return currentAddress;
    }

    @Generated
    public void setCurrentAddress(Object address) {
        currentAddress = address;
    }

    @Generated
    public Object getCurrentFile() {
        return currentFile;
    }

    @Generated
    public void setCurrentFile(Object file) {
        currentFile = file;
    }

    @Generated
    public Object getCurrentDirectory() {
        return currentDirectory;
    }

    @Generated
    public void setCurrentDirectory(Object directory) {
        currentDirectory = directory;
    }

    @Generated
    public Object getCurrentStream() {
        return currentStream;
    }

    @Generated
    public void setCurrentStream(Object stream) {
        currentStream = stream;
    }

    @Generated
    public Object getCurrentSocket() {
        return currentSocket;
    }

    @Generated
    public void setCurrentSocket(Object socket) {
        currentSocket = socket;
    }

    @Generated
    public Object getCurrentPipe() {
        return currentPipe;
    }

    @Generated
    public void setCurrentPipe(Object pipe) {
        currentPipe = pipe;
    }

    @Generated
    public Object getCurrentProcess() {
        return currentProcess;
    }

    @Generated
    public void setCurrentProcess(Object process) {
        currentProcess = process;
    }

    @Generated
    public Object getCurrentMemory() {
        return currentMemory;
    }

    @Generated
    public void setCurrentMemory(Object memory) {
        currentMemory = memory;
    }

    @Generated
    public Object getCurrentVirtualMemory() {
        return currentVirtualMemory;
    }

    @Generated
    public void setCurrentVirtualMemory(Object virtualMemory) {
        currentVirtualMemory = virtualMemory;
    }

    @Generated
    public Object getCurrentPhysicalMemory() {
        return currentPhysicalMemory;
    }

    @Generated
    public void setCurrentPhysicalMemory(Object physicalMemory) {
        currentPhysicalMemory = physicalMemory;
    }

    @Generated
    public Object getCurrentSharedMemory() {
        return currentSharedMemory;
    }

    @Generated
    public void setCurrentSharedMemory(Object sharedMemory) {
        currentSharedMemory = sharedMemory;
    }

    @Generated
    public Object getCurrentMappedMemory() {
        return currentMappedMemory;
    }

    @Generated
    public void setCurrentMappedMemory(Object mappedMemory) {
        currentMappedMemory = mappedMemory;
    }

    @Generated
    public Object getCurrentAllocation() {
        return currentAllocation;
    }

    @Generated
    public void setCurrentAllocation(Object allocation) {
        currentAllocation = allocation;
    }

    @Generated
    public Object getCurrentHeap() {
        return currentHeap;
    }

    @Generated
    public void setCurrentHeap(Object heap) {
        currentHeap = heap;
    }

    @Generated
    public Object getCurrentArena() {
        return currentArena;
    }

    @Generated
    public void setCurrentArena(Object arena) {
        currentArena = arena;
    }

    @Generated
    public Object getCurrentPool() {
        return currentPool;
    }

    @Generated
    public void setCurrentPool(Object pool) {
        currentPool = pool;
    }

    @Generated
    public Object getCurrentStack() {
        return currentStack;
    }

    @Generated
    public void setCurrentStack(Object stack) {
        currentStack = stack;
    }

    @Generated
    public Object getCurrentRegistry() {
        return currentRegistry;
    }

    @Generated
    public void setCurrentRegistry(Object registry) {
        currentRegistry = registry;
    }

    @Generated
    public Object getCurrentKey2() {
        return currentKey2;
    }

    @Generated
    public void setCurrentKey2(Object key) {
        currentKey2 = key;
    }

    @Generated
    public Object getCurrentValue2() {
        return currentValue2;
    }

    @Generated
    public void setCurrentValue2(Object value) {
        currentValue2 = value;
    }

    @Generated
    public Object getCurrentSection() {
        return currentSection;
    }

    @Generated
    public void setCurrentSection(Object section) {
        currentSection = section;
    }

    @Generated
    public Object getCurrentEntry() {
        return currentEntry;
    }

    @Generated
    public void setCurrentEntry(Object entry) {
        currentEntry = entry;
    }

    @Generated
    public Object getCurrentIterator() {
        return currentIterator;
    }

    @Generated
    public void setCurrentIterator(Object iterator) {
        currentIterator = iterator;
    }

    @Generated
    public Object getCurrentList() {
        return currentList;
    }

    @Generated
    public void setCurrentList(Object list) {
        currentList = list;
    }

    @Generated
    public Object getCurrentMap() {
        return currentMap;
    }

    @Generated
    public void setCurrentMap(Object map) {
        currentMap = map;
    }

    @Generated
    public Object getCurrentSet() {
        return currentSet;
    }

    @Generated
    public void setCurrentSet(Object set) {
        currentSet = set;
    }

    @Generated
    public Object getCurrentQueue() {
        return currentQueue;
    }

    @Generated
   极速赛车开奖直播官网