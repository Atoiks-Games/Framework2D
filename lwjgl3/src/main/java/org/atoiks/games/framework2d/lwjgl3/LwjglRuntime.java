package org.atoiks.games.framework2d.lwjgl3;

import org.atoiks.games.framework2d.IRuntime;
import org.atoiks.games.framework2d.FrameInfo;

import org.atoiks.games.framework2d.decoder.FontDecoder;
import org.atoiks.games.framework2d.decoder.TextureDecoder;

import org.atoiks.games.framework2d.lwjgl3.decoder.GLFontDecoder;
import org.atoiks.games.framework2d.lwjgl3.decoder.GLTextureDecoder;

public final class LwjglRuntime implements IRuntime {

    private static class SingletonHelper {

        private static final GLFontDecoder fontDecoder = new GLFontDecoder();
        private static final GLTextureDecoder textureDecoder = new GLTextureDecoder();
    }

    @Override
    public Frame createFrame(FrameInfo info) {
        return new Frame(info, this);
    }

    @Override
    public FontDecoder getFontDecoder() {
        return SingletonHelper.fontDecoder;
    }

    @Override
    public TextureDecoder<?> getTextureDecoder() {
        return SingletonHelper.textureDecoder;
    }
}
