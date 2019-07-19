package org.atoiks.games.framework2d.java2d;

import org.atoiks.games.framework2d.IRuntime;
import org.atoiks.games.framework2d.FrameInfo;

import org.atoiks.games.framework2d.decoder.TextureDecoder;

import org.atoiks.games.framework2d.java2d.decoder.JavaTextureDecoder;

public final class JavaRuntime implements IRuntime {

    private static class SingletonHelper {

        private static final JavaRuntime INST_RUNTIME = new JavaRuntime();
    }

    private static volatile JavaTextureDecoder textureDecoder;

    private JavaRuntime() {
        // Singleton
    }

    public static JavaRuntime getInstance() {
        return SingletonHelper.INST_RUNTIME;
    }

    @Override
    public Frame createFrame(FrameInfo info) {
        return new Frame(info);
    }

    @Override
    public TextureDecoder getTextureDecoder() {
        JavaTextureDecoder local = textureDecoder;
        if (local == null) {
            synchronized (this) {
                if (textureDecoder == null) {
                    textureDecoder = local = new JavaTextureDecoder();
                }
            }
        }
        return local;
    }
}
