package org.atoiks.games.framework2d;

import java.io.Serializable;

public final class FrameInfo implements Serializable {

    private static final long serialVersionUID = -982134728L;

    private String titleName = "";
    private float fps = 30.0f;
    private int width = 800;
    private int height = 600;
    private boolean resizable = false;

    @Override
    public String toString() {
        return new StringBuilder()
                .append(titleName).append('@').append(fps)
                .append('[').append(width)
                .append('x').append(height).append(']')
                .append(resizable ? "resizable" : "fixed")
                .toString();
    }

    // ----- A bunch of getters
    public String getTitle() {
        return titleName;
    }

    public float getFps() {
        return fps;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResizable() {
        return resizable;
    }

    // ----- A bunch of setters (nothing interesting)

    public FrameInfo setTitle(String name) {
        this.titleName = name;
        return this;
    }

    public FrameInfo setFps(float fps) {
        this.fps = fps;
        return this;
    }

    public FrameInfo setWidth(int width) {
        this.width = width;
        return this;
    }

    public FrameInfo setHeight(int height) {
        this.height = height;
        return this;
    }

    public FrameInfo setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public FrameInfo setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }
}
