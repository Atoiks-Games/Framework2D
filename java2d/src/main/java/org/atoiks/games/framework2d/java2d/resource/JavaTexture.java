package org.atoiks.games.framework2d.java2d.resource;

import java.awt.image.BufferedImage;

import org.atoiks.games.framework2d.resource.Texture;

public final class JavaTexture implements Texture {

    public final BufferedImage image;

    public JavaTexture(BufferedImage image) {
        this.image = image;
    }

    @Override
    public int getWidth() {
        return this.image.getWidth(null);
    }

    @Override
    public int getHeight() {
        return this.image.getHeight(null);
    }

    @Override
    public void close() {
        this.image.flush();
    }
}
