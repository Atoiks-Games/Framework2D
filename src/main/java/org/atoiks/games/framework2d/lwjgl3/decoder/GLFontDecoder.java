package org.atoiks.games.framework2d.lwjgl3.decoder;

import java.io.InputStream;
import java.io.IOException;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;

import java.util.concurrent.Callable;

import org.lwjgl.stb.STBTTFontinfo;

import org.lwjgl.system.MemoryStack;

import org.atoiks.games.framework2d.decoder.FontDecoder;
import org.atoiks.games.framework2d.decoder.DecodeException;

import org.atoiks.games.framework2d.lwjgl3.resource.GLFont;

import static org.lwjgl.stb.STBTruetype.*;

import static org.lwjgl.opengl.GL11.*;

public final class GLFontDecoder implements FontDecoder<GLFont> {

    private static interface BufferTransformer<T> {

        public T transform(ByteBuffer buffer, STBTTFontinfo info);
    }

    @Override
    public GLFont decode(InputStream is) throws DecodeException {
        return loadAndTransformBuffer(is, GLFontDecoder::fromBuffer);
    }

    @Override
    public Callable<GLFont> delayDecode(InputStream is) throws DecodeException {
        return loadAndTransformBuffer(is, this::delayFromBuffer);
    }

    private <T> T loadAndTransformBuffer(InputStream is, BufferTransformer<T> func) throws DecodeException {
        final ByteBuffer buf;
        try {
            buf = Utils.readResourceToBuffer(is, 8 * 1024);
        } catch (IOException ex) {
            throw new DecodeException(ex);
        }

        final STBTTFontinfo info = STBTTFontinfo.create();

        if (!stbtt_InitFont(info, buf)) {
            throw new DecodeException("Failed to read font information");
        }

        return func.transform(buf, info);
    }

    private Callable<GLFont> delayFromBuffer(final ByteBuffer buf, final STBTTFontinfo info) {
        return () -> fromBuffer(buf, info);
    }

    public static GLFont fromBuffer(ByteBuffer buffer, STBTTFontinfo info) {
        return new GLFont(buffer, info);
    }
}
