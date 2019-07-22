package org.atoiks.games.framework2d.lwjgl3;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import org.atoiks.games.framework2d.Input;
import org.atoiks.games.framework2d.Scene;
import org.atoiks.games.framework2d.FrameInfo;
import org.atoiks.games.framework2d.SceneManager;
import org.atoiks.games.framework2d.AbstractFrame;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;

public final class Frame extends AbstractFrame {

    private static interface BiIntConsumer {

        public void accept(int a, int b);
    }

    private final GLGraphics graphics = new GLGraphics();

    private final long window;
    private final float secPerUpdate;
    private final float msPerUpdate;

    private boolean shouldCallResize = true;
    private Scene lastScene = null;

    private boolean fullScreenFlag = false;
    private int preFullScreenW = 0;
    private int preFullScreenH = 0;

    public Frame(final FrameInfo info, LwjglRuntime rt) {
        super(info, rt);

        // Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // see init()
        glfwWindowHint(GLFW_RESIZABLE, info.isResizable() ? GLFW_TRUE : GLFW_FALSE);

        this.window = glfwCreateWindow(info.getWidth(), info.getHeight(), info.getTitle(), NULL, NULL);
        if (this.window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetFramebufferSizeCallback(this.window, this::framebufferSizeCallback);
        glfwSetWindowSizeCallback(this.window, this::windowSizeCallback);

        this.centerWindow();

        this.setupMouseHandler();
        this.setupKeyboardHandler();

        glfwMakeContextCurrent(this.window);
        glfwSwapInterval(1);

        final float fps = info.getFps();
        this.secPerUpdate = 1.0f / fps;
        this.msPerUpdate = 1000.0f / fps;
        SceneManager.setFrameContext(this);
    }

    private void framebufferSizeCallback(long window, int width, int height) {
        glViewport(0, 0, width, height);
    }

    private void windowSizeCallback(long window, int width, int height) {
        // see this::loop and this::setupRenderingMatrix
        this.shouldCallResize = true;
    }

    private void setupMouseHandler() {
        final Mouse mouseHandler = new Mouse();
        Input.provideMouse(mouseHandler);
        glfwSetMouseButtonCallback(window, mouseHandler.mouseButtonCb);
        glfwSetCursorPosCallback(window, mouseHandler.cursorPosCb);
        glfwSetCursorEnterCallback(window, mouseHandler.cursorEnterCb);
        glfwSetScrollCallback(window, mouseHandler.scrollCb);
    }

    private void setupKeyboardHandler() {
        final Keyboard keyboardHandler = new Keyboard();
        Input.provideKeyboard(keyboardHandler);
        glfwSetKeyCallback(window, keyboardHandler.keyCb);
        glfwSetCharCallback(window, keyboardHandler.charCb);
    }

    private void centerWindow() {
        // Get the resolution of the primary monitor
        final GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center the window
        this.consumeWindowSize((w, h) -> glfwSetWindowPos(
                this.window,
                (vidmode.width() - w) / 2,
                (vidmode.height() - h) / 2));
    }

    @Override
    public void init() {
        super.init();
        glfwFocusWindow(this.window);

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    protected boolean shouldContinueRunning() {
        return !glfwWindowShouldClose(this.window);
    }

    @Override
    protected void resizeGame() {
        this.setupRenderingMatrix();

        // Resize current scene if necessary
        final Scene currentScene = SceneManager.getCurrentScene();
        if (this.shouldCallResize || lastScene != currentScene) {
            this.shouldCallResize = false;
            this.lastScene = currentScene;
            this.consumeWindowSize(currentScene::resize);
        }
    }

    @Override
    public void renderGame() {
        glLoadIdentity();
        SceneManager.renderCurrentScene(graphics);

        glfwSwapBuffers(this.window);
    }

    @Override
    public void postRender() {
        glfwPollEvents();
    }

    private void setupRenderingMatrix() {
        if (this.shouldCallResize) {
            // Full resize: touch the projection matrix and stuff
            this.consumeWindowSize((w, h) -> {
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                glOrtho(0, w, h, 0, -1, 1);

                glMatrixMode(GL_MODELVIEW);
            });
        }
    }

    private void consumeWindowSize(final BiIntConsumer consumer) {
        try (final MemoryStack stack = stackPush()) {
            final IntBuffer wptr = stack.mallocInt(1);
            final IntBuffer hptr = stack.mallocInt(1);

            glfwGetWindowSize(this.window, wptr, hptr);
            consumer.accept(wptr.get(0), hptr.get(0));
        }
    }

    @Override
    public void close() {
        super.close();

        glDisable(GL_TEXTURE_2D);

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(this.window);
        glfwDestroyWindow(this.window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    @Override
    public void setFullScreen(final boolean status) {
        if (status == this.fullScreenFlag) {
            return;
        }

        final long monitor = glfwGetPrimaryMonitor();

        if (status) {
            this.forceEnterFullScreen(monitor);
        } else {
            this.forceExitFullScreen(monitor);
        }

        this.fullScreenFlag = status;
    }

    private void forceEnterFullScreen(long monitor) {
        try (final MemoryStack stack = stackPush()) {
            final IntBuffer wptr = stack.mallocInt(1);
            final IntBuffer hptr = stack.mallocInt(1);

            glfwGetWindowSize(this.window, wptr, hptr);
            this.preFullScreenW = wptr.get(0);
            this.preFullScreenH = hptr.get(0);
        }

        final GLFWVidMode vidmode = glfwGetVideoMode(monitor);

        glfwSetWindowMonitor(
                this.window,
                monitor,
                0,
                0,
                vidmode.width(),
                vidmode.height(),
                0);
    }

    private void forceExitFullScreen(long monitor) {
        final GLFWVidMode vidmode = glfwGetVideoMode(monitor);

        glfwSetWindowMonitor(
                this.window,
                NULL,
                (vidmode.width() - this.preFullScreenW) / 2,
                (vidmode.height() - this.preFullScreenH) / 2,
                this.preFullScreenW,
                this.preFullScreenH,
                0);
    }

    @Override
    public void setSize(int w, int h) {
        glfwSetWindowSize(this.window, w, h);
    }

    @Override
    public int getWidth() {
        try (final MemoryStack stack = stackPush()) {
            final IntBuffer wptr = stack.mallocInt(1);
            final IntBuffer hptr = stack.mallocInt(1);
            glfwGetWindowSize(this.window, wptr, hptr);
            return wptr.get(0);
        }
    }

    @Override
    public int getHeight() {
        try (final MemoryStack stack = stackPush()) {
            final IntBuffer wptr = stack.mallocInt(1);
            final IntBuffer hptr = stack.mallocInt(1);
            glfwGetWindowSize(this.window, wptr, hptr);
            return hptr.get(0);
        }
    }

    @Override
    public void setVisible(boolean status) {
        if (status) {
            glfwShowWindow(this.window);
        } else {
            glfwHideWindow(this.window);
        }
    }

    @Override
    public boolean isVisible() {
        return glfwGetWindowAttrib(this.window, GLFW_VISIBLE) != 0;
    }

    @Override
    public void setTitle(String title) {
        glfwSetWindowTitle(this.window, title);
    }
}
