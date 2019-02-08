package org.atoiks.games.framework2d.java2d;

import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import org.atoiks.games.framework2d.*;

/* package */ final class JavaGraphics implements IGraphics<Graphics2D> {

    /* package */ final IFrame parent;

    /* package */ Graphics2D g;

    public JavaGraphics(final IFrame parent) {
        this.parent = parent;
    }

    @Override
    public Graphics2D getRawGraphics() {
        return g;
    }

    @Override
    public void setFont(Font f) {
        g.setFont(f);
    }

    @Override
    public Font getFont() {
        return g.getFont();
    }

    @Override
    public void setColor(Color c) {
        g.setColor(c);
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        setColor(new Color(r, g, b, a));
    }

    @Override
    public Color getColor() {
        return g.getColor();
    }

    @Override
    public void setClearColor(Color c) {
        g.setBackground(c);
    }

    @Override
    public void setClearColor(float r, float g, float b, float a) {
        setClearColor(new Color(r, g, b, a));
    }

    @Override
    public Color getClearColor() {
        return g.getBackground();
    }

    @Override
    public void clearGraphics() {
        g.clearRect(0, 0, parent.getWidth(), parent.getHeight());
    }

    @Override
    public void clearRegion(int x1, int y1, int x2, int y2) {
        g.clearRect(x1, y1, x2, y2);
    }

    @Override
    public void scale(float x, float y) {
        g.scale(x, y);
    }

    @Override
    public void translate(float x, float y) {
        g.translate(x, y);
    }

    @Override
    public void rotate(float angle, float x, float y) {
        g.rotate(angle, x, y);
    }

    @Override
    public void drawImage(Image img, int x, int y) {
        g.drawImage(img, x, y, null);
    }

    @Override
    public void drawImage(Image img, int x, int y, Color bg) {
        g.drawImage(img, x, y, bg, null);
    }

    @Override
    public void drawImage(Image img, int x1, int y1, int x2, int y2) {
        g.drawImage(img, x1, y1, x2 - x1, y2 - y1, null);
    }

    @Override
    public void drawImage(Image img, int x1, int y1, int x2, int y2, Color bg) {
        g.drawImage(img, x1, y1, x2 - x1, y2 - y1, bg, null);
    }

    @Override
    public void drawString(String str, int x, int y) {
        g.drawString(str, x, y);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawOval(int x1, int y1, int x2, int y2) {
        g.drawOval(x1, y1, (x2 - x1), (y2 - y1));
    }

    @Override
    public void fillOval(int x1, int y1, int x2, int y2) {
        g.fillOval(x1, y1, (x2 - x1), (y2 - y1));
    }

    @Override
    public void drawRect(int x1, int y1, int x2, int y2) {
        g.drawRect(x1, y1, (x2 - x1), (y2 - y1));
    }

    @Override
    public void fillRect(int x1, int y1, int x2, int y2) {
        g.fillRect(x1, y1, (x2 - x1), (y2 - y1));
    }

    @Override
    public void drawImage(Image img, float x, float y) {
        g.drawImage(img, (int) x, (int) y, null);
    }

    @Override
    public void drawImage(Image img, float x, float y, Color bg) {
        g.drawImage(img, (int) x, (int) y, bg, null);
    }

    @Override
    public void drawImage(Image img, float x1, float y1, float x2, float y2) {
        g.drawImage(img, (int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1), null);
    }

    @Override
    public void drawImage(Image img, float x1, float y1, float x2, float y2, Color bg) {
        g.drawImage(img, (int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1), bg, null);
    }

    @Override
    public void drawString(String str, float x, float y) {
        g.drawString(str, x, y);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    @Override
    public void drawOval(float x1, float y1, float x2, float y2) {
        g.drawOval((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1));
    }

    @Override
    public void fillOval(float x1, float y1, float x2, float y2) {
        g.fillOval((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1));
    }

    @Override
    public void drawRect(float x1, float y1, float x2, float y2) {
        g.drawRect((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1));
    }

    @Override
    public void fillRect(float x1, float y1, float x2, float y2) {
        g.fillRect((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1));
    }

    @Override
    public void drawPolygon(float[] coords) {
        g.draw(makePolygon(coords));
    }

    @Override
    public void fillPolygon(float[] coords) {
        g.fill(makePolygon(coords));
    }

    private Path2D makePolygon(float[] coords) {
        final int count = coords.length / 2;
        final Path2D.Float path = new Path2D.Float();

        if (count == 0) return path;

        path.moveTo(coords[0], coords[1]);
        for (int i = 1; i < count; ++i) {
            final int k = 2 * i;
            path.lineTo(coords[k], coords[k + 1]);
        }
        // Connect back to starting point
        path.closePath();
        return path;
    }
}
