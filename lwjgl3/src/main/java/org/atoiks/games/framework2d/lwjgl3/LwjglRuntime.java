package org.atoiks.games.framework2d.lwjgl3;

import org.atoiks.games.framework2d.IRuntime;
import org.atoiks.games.framework2d.FrameInfo;

import org.atoiks.games.framework2d.decoder.FontDecoder;
import org.atoiks.games.framework2d.decoder.TextureDecoder;

import org.atoiks.games.framework2d.lwjgl3.decoder.GLFontDecoder;
import org.atoiks.games.framework2d.lwjgl3.decoder.GLTextureDecoder;

public final class LwjglRuntime implements IRuntime {

    private static class SingletonHelper {

        private static final LwjglRuntime INST_RUNTIME = new LwjglRuntime();
    }

    private static volatile GLFontDecoder fontDecoder;
    private static volatile GLTextureDecoder textureDecoder;

    private LwjglRuntime() {
        // Singleton
    }

    public static LwjglRuntime getInstance() {
        return SingletonHelper.INST_RUNTIME;
    }

    @Override
    public Frame createFrame(FrameInfo info) {
        return new Frame(info);
    }

    @Override
    public FontDecoder getFontDecoder() {
        GLFontDecoder local = fontDecoder;
        if (local == null) {
            synchronized (this) {
                if (fontDecoder == null) {
                    fontDecoder = local = new GLFontDecoder();
                }
            }
        }
        return local;
    }

    @Override
    public TextureDecoder<?> getTextureDecoder() {
        GLTextureDecoder local = textureDecoder;
        if (local == null) {
            synchronized (this) {
                if (textureDecoder == null) {
                    textureDecoder = local = new GLTextureDecoder();
                }
            }
        }
        return local;
    }
}
