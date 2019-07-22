package org.atoiks.games.framework2d;

import java.awt.Color;

import org.atoiks.games.framework2d.resource.Texture;

public interface IGraphics {

    public void setColor(Color color);
    public void setColor(float r, float g, float b, float a);
    public Color getColor();

    public void setClearColor(Color color);
    public void setClearColor(float r, float g, float b, float a);
    public Color getClearColor();
    public void clearGraphics();

    public void scale(float x, float y);
    public void translate(float x, float y);
    public void rotate(float angle, float x, float y);

    public void drawTexture(Texture img, int x, int y);
    public void drawTexture(Texture img, int x, int y, Color bg);
    public void drawTexture(Texture img, int x1, int y1, int x2, int y2);
    public void drawTexture(Texture img, int x1, int y1, int x2, int y2, Color bg);

    public void drawLine(int x1, int y1, int x2, int y2);

    public void drawOval(int x1, int y1, int x2, int y2);
    public void fillOval(int x1, int y1, int x2, int y2);

    public default void drawCircle(int x, int y, int r) {
        drawOval(x - r, y - r, x + r, y + r);
    }
    public default void fillCircle(int x, int y, int r) {
        fillOval(x - r, y - r, x + r, y + r);
    }

    public void drawRect(int x1, int y1, int x2, int y2);
    public void fillRect(int x1, int y1, int x2, int y2);

    public void drawTexture(Texture img, float x, float y);
    public void drawTexture(Texture img, float x, float y, Color bg);
    public void drawTexture(Texture img, float x1, float y1, float x2, float y2);
    public void drawTexture(Texture img, float x1, float y1, float x2, float y2, Color bg);

    public void drawLine(float x1, float y1, float x2, float y2);
    public default void drawPoints(final float[] coords) {
        final int count = coords.length / 2;
        if (count == 0) return;

        for (int i = 0; i < count; ++i) {
            final int offset = 2 * i;
            final float k1 = coords[offset];
            final float k2 = coords[offset + 1];
            drawLine(k1, k2, k1, k2);
        }
    }

    public void drawOval(float x1, float y1, float x2, float y2);
    public void fillOval(float x1, float y1, float x2, float y2);

    public void drawRect(float x1, float y1, float x2, float y2);
    public void fillRect(float x1, float y1, float x2, float y2);

    public void drawPolygon(float[] coords);
    public void fillPolygon(float[] coords);
}
