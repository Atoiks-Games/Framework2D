package org.atoiks.games.framework2d.lwjgl3;

import java.awt.Color;

import org.atoiks.games.framework2d.IGraphics;

import org.atoiks.games.framework2d.resource.Texture;

import org.atoiks.games.framework2d.lwjgl3.resource.GLTexture;

import static org.lwjgl.opengl.GL11.*;

/* package */ final class GLGraphics implements IGraphics {

    // For now draw with old-outdated immediate mode

    private static final int CIRCLE_SUBDIV = 64;

    private final float[] bgColor = new float[4];
    private final float[] fgColor = new float[4];

    @Override
    public void setColor(Color color) {
        glColor4fv(color.getRGBComponents(this.fgColor));
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        this.fgColor[0] = r;
        this.fgColor[1] = g;
        this.fgColor[2] = b;
        this.fgColor[3] = a;
        glColor4fv(this.fgColor);
    }

    @Override
    public Color getColor() {
        return new Color(
                this.fgColor[0],
                this.fgColor[1],
                this.fgColor[2],
                this.fgColor[3]);
    }

    @Override
    public void setClearColor(Color color) {
        color.getRGBComponents(this.bgColor);
        glClearColor(
                this.bgColor[0],
                this.bgColor[1],
                this.bgColor[2],
                this.bgColor[3]);
    }

    @Override
    public void setClearColor(float r, float g, float b, float a) {
        glClearColor(
                (this.bgColor[0] = r),
                (this.bgColor[1] = g),
                (this.bgColor[2] = b),
                (this.bgColor[3] = a));
    }

    @Override
    public Color getClearColor() {
        return new Color(
                this.bgColor[0],
                this.bgColor[1],
                this.bgColor[2],
                this.bgColor[3]);
    }

    @Override
    public void clearGraphics() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void scale(float x, float y) {
        glScalef(x, y, 1.0f);
    }

    @Override
    public void translate(float x, float y) {
        glTranslatef(x, y, 0.0f);
    }

    @Override
    public void rotate(final float radians, float x, float y) {
        // glRotatef (for some reason) takes degrees, not radians!
        glRotatef(radians * (float) (180.0 / Math.PI), x, y, 1.0f);
    }

    @Override
    public void drawTexture(final Texture img, final int x, final int y) {
        final GLTexture tex = (GLTexture) img;
        this.drawGLTexture(tex, x, y, x + tex.getWidth(), y + tex.getHeight());
    }

    @Override
    public void drawTexture(final Texture img, final int x, final int y, Color c) {
        glColor4fv(c.getRGBComponents(new float[4]));
        final GLTexture tex = (GLTexture) img;
        this.drawGLTexture(tex, x, y, x + tex.getWidth(), y + tex.getHeight());
        glColor4fv(this.fgColor);
    }

    @Override
    public void drawTexture(final Texture img, final int x1, final int y1, final int x2, final int y2) {
        this.drawGLTexture((GLTexture) img, x1, y1, x2, y2);
    }

    @Override
    public void drawTexture(final Texture img, final int x1, final int y1, final int x2, final int y2, Color c) {
        glColor4fv(c.getRGBComponents(new float[4]));
        this.drawGLTexture((GLTexture) img, x1, y1, x2, y2);
        glColor4fv(this.fgColor);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        glBegin(GL_LINES);
        glVertex2i(x1, y1);
        glVertex2i(x2, y2);
        glEnd();
    }

    @Override
    public void drawOval(int x1, int y1, int x2, int y2) {
        final float mx = (x1 + x2) / 2.0f;
        final float my = (y1 + y2) / 2.0f;
        final float a = x2 - mx;
        final float b = y2 - my;
        drawEllipses(mx, my, a, b);
    }

    @Override
    public void fillOval(int x1, int y1, int x2, int y2) {
        final float mx = (x1 + x2) / 2.0f;
        final float my = (y1 + y2) / 2.0f;
        final float a = x2 - mx;
        final float b = y2 - my;
        fillEllipses(mx, my, a, b);
    }

    @Override
    public void drawRect(int x1, int y1, int x2, int y2) {
        glBegin(GL_LINE_LOOP);
        glVertex2i(x1, y1);
        glVertex2i(x1, y2);
        glVertex2i(x2, y2);
        glVertex2i(x2, y1);
        glEnd();
    }

    @Override
    public void fillRect(int x1, int y1, int x2, int y2) {
        glBegin(GL_QUADS);
        glVertex2i(x1, y1);
        glVertex2i(x2, y1);
        glVertex2i(x2, y2);
        glVertex2i(x1, y2);
        glEnd();
    }

    @Override
    public void drawTexture(final Texture img, final float x, final float y) {
        final GLTexture tex = (GLTexture) img;
        this.drawGLTexture(tex, x, y, x + tex.getWidth(), y + tex.getHeight());
    }

    @Override
    public void drawTexture(final Texture img, final float x, final float y, Color c) {
        glColor4fv(c.getRGBComponents(new float[4]));
        final GLTexture tex = (GLTexture) img;
        this.drawGLTexture(tex, x, y, x + tex.getWidth(), y + tex.getHeight());
        glColor4fv(this.fgColor);
    }

    @Override
    public void drawTexture(final Texture img, final float x1, final float y1, final float x2, final float y2) {
        this.drawGLTexture((GLTexture) img, x1, y1, x2, y2);
    }

    @Override
    public void drawTexture(final Texture img, final float x1, final float y1, final float x2, final float y2, Color c) {
        glColor4fv(c.getRGBComponents(new float[4]));
        this.drawGLTexture((GLTexture) img, x1, y1, x2, y2);
        glColor4fv(this.fgColor);
    }

    @Override
    public void drawLine(final float x1, final float y1, final float x2, final float y2) {
        glBegin(GL_LINES);
        glVertex2f(x1, y1);
        glVertex2f(x2, y2);
        glEnd();
    }

    @Override
    public void drawOval(final float x1, final float y1, final float x2, final float y2) {
        final float mx = (x1 + x2) / 2.0f;
        final float my = (y1 + y2) / 2.0f;
        final float a = x2 - mx;
        final float b = y2 - my;
        drawEllipses(mx, my, a, b);
    }

    @Override
    public void fillOval(final float x1, final float y1, final float x2, final float y2) {
        final float mx = (x1 + x2) / 2.0f;
        final float my = (y1 + y2) / 2.0f;
        final float a = x2 - mx;
        final float b = y2 - my;
        fillEllipses(mx, my, a, b);
    }

    @Override
    public void drawRect(final float x1, final float y1, final float x2, final float y2) {
        glBegin(GL_LINE_LOOP);
        glVertex2f(x1, y1);
        glVertex2f(x1, y2);
        glVertex2f(x2, y2);
        glVertex2f(x2, y1);
        glEnd();
    }

    @Override
    public void fillRect(final float x1, final float y1, final float x2, final float y2) {
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
    }

    @Override
    public void drawPolygon(final float[] coords) {
        glBegin(GL_LINE_LOOP);
        final int limit = coords.length;
        for (int i = 0; i < limit; i += 2) {
            glVertex2f(coords[i], coords[i + 1]);
        }
        glEnd();
    }

    @Override
    public void fillPolygon(final float[] coords) {
        glBegin(GL_TRIANGLE_FAN);
        final int limit = coords.length;
        for (int i = 0; i < limit; i += 2) {
            glVertex2f(coords[i], coords[i + 1]);
        }
        glEnd();
    }

    private void drawGLTexture(GLTexture texture, float x1, float y1, float x2, float y2) {
        texture.bindTexture();

        glBegin(GL_QUADS);

        glTexCoord2f(0.0f, 0.0f);
        glVertex2f(x1, y1);

        glTexCoord2f(1.0f, 0.0f);
        glVertex2f(x2, y1);

        glTexCoord2f(1.0f, 1.0f);
        glVertex2f(x2, y2);

        glTexCoord2f(0.0f, 1.0f);
        glVertex2f(x1, y2);

        glEnd();

        // MUST UNBIND! Otherwise some strange stuff be happening!
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void drawEllipses(final float mx, final float my, final float a, final float b) {
        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < CIRCLE_SUBDIV; ++i) {
            final float x = mx + a * (float) Math.cos(i * Math.PI / (CIRCLE_SUBDIV / 2));
            final float y = my + b * (float) Math.sin(i * Math.PI / (CIRCLE_SUBDIV / 2));
            glVertex2f(x, y);
        }
        glEnd();
    }

    private void fillEllipses(final float mx, final float my, final float a, final float b) {
        glBegin(GL_TRIANGLE_FAN);
        for (int i = 0; i < CIRCLE_SUBDIV; ++i) {
            final float x = mx + a * (float) Math.cos(i * Math.PI / (CIRCLE_SUBDIV / 2));
            final float y = my + b * (float) Math.sin(i * Math.PI / (CIRCLE_SUBDIV / 2));
            glVertex2f(x, y);
        }
        glEnd();
    }
}
