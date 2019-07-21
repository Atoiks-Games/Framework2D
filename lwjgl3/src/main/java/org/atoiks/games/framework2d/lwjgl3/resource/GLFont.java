package org.atoiks.games.framework2d.lwjgl3.resource;

import java.util.Map;
import java.util.HashMap;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTAlignedQuad;

import org.lwjgl.BufferUtils;

import org.lwjgl.system.MemoryStack;

import org.atoiks.games.framework2d.IGraphics;

import org.atoiks.games.framework2d.resource.Font;

import static org.lwjgl.stb.STBTruetype.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

public final class GLFont implements Font {

    private static final int BITMAP_W = 512;
    private static final int BITMAP_H = 512;

    private static class FontData {

        private static final float BASE_HEIGHT = 16.0f;

        // reference counted!
        private int refCount;

        private final int ascent;
        private final int descent;
        private final int lineGap;

        private final STBTTFontinfo info;

        private final  int handle;
        private final STBTTBakedChar.Buffer cdata;

        public FontData(final ByteBuffer buf, final STBTTFontinfo info) {
            this.info = info;

            try (MemoryStack stack = MemoryStack.stackPush()) {
                final IntBuffer pAscent  = stack.mallocInt(1);
                final IntBuffer pDescent = stack.mallocInt(1);
                final IntBuffer pLineGap = stack.mallocInt(1);

                stbtt_GetFontVMetrics(info, pAscent, pDescent, pLineGap);

                this.ascent = pAscent.get(0);
                this.descent = pDescent.get(0);
                this.lineGap = pLineGap.get(0);
            }

            final int texID = (this.handle = glGenTextures());

            // Only bake chars 32 to 127
            this.cdata = STBTTBakedChar.malloc(96);

            final ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
            stbtt_BakeFontBitmap(buf, BASE_HEIGHT, bitmap, BITMAP_W, BITMAP_H, 32, cdata);

            glBindTexture(GL_TEXTURE_2D, texID);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            glBindTexture(GL_TEXTURE_2D, 0);
        }

        public FontData newRef() {
            ++refCount;
            return this;
        }

        public void deleteRef() {
            if (--refCount < 0) {
                cdata.free();
            }
        }

        public void renderText(final String text, final float dx, final float dy, final float fontHeight) {
            glBindTexture(GL_TEXTURE_2D, handle);

            final float scale = fontHeight / BASE_HEIGHT;

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer pCodePoint = stack.mallocInt(1);

                FloatBuffer x = stack.floats(0.0f);
                FloatBuffer y = stack.floats(0.0f);

                STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

                int lineStart = 0;

                float lineY = 0.0f;

                glBegin(GL_QUADS);
                for (int i = 0, to = text.length(); i < to; ) {
                    i += getCP(text, to, i, pCodePoint);

                    int cp = pCodePoint.get(0);
                    if (cp == '\n') {
                        y.put(0, lineY = y.get(0) + (ascent - descent + lineGap) * scale);
                        x.put(0, 0.0f);

                        lineStart = i;
                        continue;
                    } else if (cp < 32 || 128 <= cp) {
                        continue;
                    }

                    final float cpX = x.get(0);
                    stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, cp - 32, x, y, q, true);
                    x.put(0, scale(cpX, x.get(0), scale));

                    // Kerning
                    if (i < to) {
                        getCP(text, to, i, pCodePoint);
                        x.put(0, x.get(0) + stbtt_GetCodepointKernAdvance(info, cp, pCodePoint.get(0)) * scale);
                    }

                    final float x0 = scale(cpX, q.x0(), scale) + dx;
                    final float x1 = scale(cpX, q.x1(), scale) + dx;
                    final float y0 = scale(lineY, q.y0(), scale) + dy;
                    final float y1 = scale(lineY, q.y1(), scale) + dy;

                    glTexCoord2f(q.s0(), q.t0());
                    glVertex2f(x0, y0);

                    glTexCoord2f(q.s1(), q.t0());
                    glVertex2f(x1, y0);

                    glTexCoord2f(q.s1(), q.t1());
                    glVertex2f(x1, y1);

                    glTexCoord2f(q.s0(), q.t1());
                    glVertex2f(x0, y1);
                }
                glEnd();
            }

            // Unbind texture
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        private static int getCP(String text, int to, int i, IntBuffer cpOut) {
            char c1 = text.charAt(i);
            if (Character.isHighSurrogate(c1) && i + 1 < to) {
                char c2 = text.charAt(i + 1);
                if (Character.isLowSurrogate(c2)) {
                    cpOut.put(0, Character.toCodePoint(c1, c2));
                    return 2;
                }
            }
            cpOut.put(0, c1);
            return 1;
        }

        private static float scale(float center, float offset, float factor) {
            return (offset - center) * factor + center;
        }
    }

    private FontData data;
    private float fontHeight;

    public GLFont(ByteBuffer buf, STBTTFontinfo info) {
        this.data = new FontData(buf, info);
        this.fontHeight = 16;
    }

    private GLFont(FontData data, float size) {
        this.data = data.newRef();
        this.fontHeight = size;
    }

    @Override
    public float getSize() {
        return this.fontHeight;
    }

    @Override
    public void close() {
        data.deleteRef();
    }

    @Override
    public Font deriveSize(float size) {
        return new GLFont(data, size);
    }

    @Override
    public void renderText(final IGraphics g, final String text, final float x, final float y) {
        data.renderText(text, x, y, this.fontHeight);
    }
}
