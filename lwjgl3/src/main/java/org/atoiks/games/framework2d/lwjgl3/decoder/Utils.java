package org.atoiks.games.framework2d.lwjgl3.decoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public final class Utils {

    private Utils() {
    }

    public static ByteBuffer readResourceToBuffer(final InputStream is, final int bufSize) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] buffer = new byte[bufSize];
        while (is.read(buffer) != -1) {
            baos.write(buffer);
        }
        final byte[] bytes = baos.toByteArray();
        final ByteBuffer buf = BufferUtils.createByteBuffer(bytes.length);
        buf.put(bytes);
        buf.flip();
        return buf;
    }
}
