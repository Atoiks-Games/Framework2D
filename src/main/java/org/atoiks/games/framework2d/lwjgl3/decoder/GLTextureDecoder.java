package org.atoiks.games.framework2d.lwjgl3.decoder;

import java.io.InputStream;
import java.io.IOException;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;

import java.util.concurrent.Callable;

import org.lwjgl.BufferUtils;

import org.lwjgl.stb.STBTTFontinfo;

import org.lwjgl.system.MemoryStack;

import org.atoiks.games.framework2d.decoder.TextureDecoder;
import org.atoiks.games.framework2d.decoder.DecodeException;

import org.atoiks.games.framework2d.lwjgl3.LwjglRuntime;

import org.atoiks.games.framework2d.lwjgl3.resource.GLTexture;

import static org.lwjgl.stb.STBImage.*;

import static org.lwjgl.opengl.GL11.*;

public final class GLTextureDecoder implements TextureDecoder<GLTexture> {

    private static interface BufferTransformer<T> {

        public T transform(ByteBuffer buffer, int w, int h, int comp);
    }

    @Override
    public GLTexture decode(InputStream is) throws DecodeException {
        return loadAndTransformBuffer(is, this::loadAndFreeBuffer);
    }

    @Override
    public Callable<GLTexture> delayDecode(InputStream is) throws DecodeException {
        return loadAndTransformBuffer(is, this::delayLoadAndFreeBuffer);
    }

    private <T> T loadAndTransformBuffer(InputStream is, BufferTransformer<T> func) throws DecodeException {
        final ByteBuffer buf;
        try {
            buf = Utils.readResourceToBuffer(is, 8 * 1024);
        } catch (IOException ex) {
            throw new DecodeException(ex);
        }

        try (final MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer w = stack.mallocInt(1);
            final IntBuffer h = stack.mallocInt(1);
            final IntBuffer comp = stack.mallocInt(1);

            if (!stbi_info_from_memory(buf, w, h, comp)) {
                throw new DecodeException("Failed to read image information: " + stbi_failure_reason());
            }

            final ByteBuffer image = stbi_load_from_memory(buf, w, h, comp, 0);
            if (image == null) {
                throw new DecodeException("Failed to load image: " + stbi_failure_reason());
            }

            final int width = w.get(0);
            final int height = h.get(0);
            final int channels = comp.get(0);

            return func.transform(image, width, height, channels);
        } catch (DecodeException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw new DecodeException(ex);
        }
    }

    private Callable<GLTexture> delayLoadAndFreeBuffer(final ByteBuffer buffer, final int w, final int h, final int comp) {
        return () -> this.loadAndFreeBuffer(buffer, w, h, comp);
    }

    private GLTexture loadAndFreeBuffer(ByteBuffer buffer, int w, int h, int comp) {
        final GLTexture texture = fromBuffer(buffer, w, h, comp);
        stbi_image_free(buffer);
        return texture;
    }

    public static GLTexture fromBuffer(ByteBuffer buffer, int w, int h, int comp) {
        final int handle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, handle);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        switch (comp) {
            case 3:
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
                break;
            case 4:
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
                break;
            default:
                throw new DecodeException("Unrecognized component count of " + comp);
        }

        glBindTexture(GL_TEXTURE_2D, 0);

        return new GLTexture(handle, w, h);
    }
}
