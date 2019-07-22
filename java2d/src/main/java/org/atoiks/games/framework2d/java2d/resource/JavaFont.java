package org.atoiks.games.framework2d.java2d.resource;

import java.awt.Graphics2D;

import org.atoiks.games.framework2d.IGraphics;

import org.atoiks.games.framework2d.java2d.JavaGraphics;

import org.atoiks.games.framework2d.resource.Font;

public final class JavaFont implements Font {

    private final java.awt.Font font;

    public JavaFont(java.awt.Font font) {
        this.font = font;
    }

    @Override
    public Font deriveSize(float size) {
        return new JavaFont(this.font.deriveFont(size));
    }

    @Override
    public float getSize() {
        return this.font.getSize2D();
    }

    @Override
    public void renderText(IGraphics g, String text, float x, float y) {
        final Graphics2D g2d = ((JavaGraphics) g).getRawGraphics();
        g2d.setFont(this.font);
        g2d.drawString(text, x, y);
    }

    @Override
    public void close() {
        // Do nothing
    }
}
