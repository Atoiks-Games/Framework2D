package org.atoiks.games.framework2d;

import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics2D;

/* package */ class SwingGraphics implements IGraphics<Graphics2D> {

    /* package */ Graphics2D g;
    /* package */ int width, height;

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
        g.clearRect(0, 0, width, height);
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
    public void drawImage(Image img, float x, float y) {
        g.drawImage(img, (int) x, (int) y, null);
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
}