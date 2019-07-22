package org.atoiks.games.framework2d.lwjgl3.resource;

import org.atoiks.games.framework2d.resource.Texture;

import org.atoiks.games.framework2d.IGraphics;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;

public final class GLTexture implements Texture {

    private final int width;
    private final int height;

    private final int handleId;

    public GLTexture(int textureHandle, int width, int height) {
        this.handleId = textureHandle;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void close() {
        glDeleteTextures(this.handleId);
    }

    public void bindTexture() {
        glBindTexture(GL_TEXTURE_2D, this.handleId);
    }
}
