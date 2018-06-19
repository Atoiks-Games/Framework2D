package org.atoiks.games.framework2d;

import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Path2D;

public interface IGraphics<T> {

    public T getRawGraphics();

    public void setFont(Font f);
    public Font getFont();

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

    public void drawImage(Image img, int x, int y);
    public void drawImage(Image img, int x, int y, Color bg);

    public void drawString(String str, int x, int y);
    public void drawLine(int x1, int y1, int x2, int y2);

    public void drawOval(int x1, int y1, int x2, int y2);
    public void fillOval(int x1, int y1, int x2, int y2);

    public void drawRect(int x1, int y1, int x2, int y2);
    public void fillRect(int x1, int y1, int x2, int y2);

    public void drawImage(Image img, float x, float y);
    public void drawImage(Image img, float x, float y, Color bg);

    public void drawString(String str, float x, float y);
    public void drawLine(float x1, float y1, float x2, float y2);

    public void drawOval(float x1, float y1, float x2, float y2);
    public void fillOval(float x1, float y1, float x2, float y2);

    public void drawRect(float x1, float y1, float x2, float y2);
    public void fillRect(float x1, float y1, float x2, float y2);

    public void drawPath2D(Path2D path);
    public void fillPath2D(Path2D path);
}